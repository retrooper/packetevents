/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;

/**
 * Class for event managing. Implements both, internal and API methods.
 *
 * @apiNote It is highly recommended to register the event listeners at server start-up,
 * without frequent modifications during runtime, since this class needs to recalculate all of
 * its event listeners during any modifications. In other words, it assumes that reads (events fired)
 * greatly outnumber writes (listeners modifications). If your case requires frequent listener modifications,
 * open a pull request on GitHub and describe your case.
 *
 * @author retrooper
 * @author ShadowOfHeaven (optimization)
 *
 */

public class EventManager {

    //Using a ConcurrentHashMap is faster and more secure here, compared to Collections.synchronizedMap(new EnumMap<>(PacketListenerPriority.class))
    //This is mainly due to:
    //1. On each modification Collections.synchronizedMap synchronizes the whole Map object, while ConcurrentHashMap only it's internal, currently modified Node
    //2. ConcurrentHashMap won't fail in a multi-thread environment, while Collections.synchronizedMap is said to have a lot of potential problems,
    //being a generalized method for synchronization
    private final Map<PacketListenerPriority, Set<PacketListenerCommon>> listenersMap = new ConcurrentHashMap<>();
    //Since reads greatly outnumber writes, create an array for the best possible iteration time
    //Updated as a whole on writes, no index modifications are allowed
    private volatile PacketListenerCommon[] listeners = new PacketListenerCommon[0];


    /**
     * Call the PacketEvent.
     * This method processes the event on all the registered dynamic packet event listeners.
     * The {@link PacketListenerPriority#LOWEST} prioritized listeners will be processing first,
     * the {@link PacketListenerPriority#MONITOR} will be processing last and can
     * be the final decider whether the event has been cancelled or not.
     *
     * @param event {@link PacketEvent}
     */
    public void callEvent(PacketEvent event) {
        callEvent(event, null);
    }


    /**
     * Call the PacketEvent.
     * This method processes the event on all the registered dynamic packet event listeners.
     * The {@link PacketListenerPriority#LOWEST} prioritized listeners will be processing first,
     * the {@link PacketListenerPriority#MONITOR} will be processing last and can
     * be the final decider whether the event has been cancelled or not.
     *
     * @param event                  {@link PacketEvent}
     * @param postCallListenerAction The action to be ran after all the listeners have finished processing
     */
    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
        for (PacketListenerCommon listener : listeners) {
            try {
                event.call(listener);
            } catch (Exception t) {
                // ignore handshake exceptions
                if (t.getClass() != InvalidHandshakeException.class) {
                    PacketEvents.getAPI().getLogger().log(Level.WARNING, "PacketEvents caught an unhandled exception while calling your listener.", t);
                }
            }
            if (postCallListenerAction != null) {
                postCallListenerAction.run();
            }
        }
        // For performance reasons, we don't want to re-encode the packet if it's not needed.
        if (event instanceof ProtocolPacketEvent && !((ProtocolPacketEvent) event).needsReEncode()) {
            ((ProtocolPacketEvent) event).setLastUsedWrapper(null);
        }
    }

    /**
     * Register the dynamic packet event listener.
     *
     * @param listener {@link PacketListenerCommon}
     * @param priority {@link PacketListenerPriority}
     */
    public PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority) {
        PacketListenerCommon packetListenerAbstract = listener.asAbstract(priority);
        return registerListener(packetListenerAbstract);
    }

    /**
     * Register the dynamic packet event listener.
     *
     * @param listener {@link PacketListenerCommon}
     */
    public PacketListenerCommon registerListener(PacketListenerCommon listener) {
        this.registerListenerNoRecalculation(listener);
        this.recalculateListeners();
        return listener;
    }

    /**
     * Register multiple dynamic packet event listeners with one method.
     *
     * @param listeners {@link PacketListenerCommon}
     */
    public PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners) {
        for (PacketListenerCommon listener : listeners) {
            this.registerListenerNoRecalculation(listener);
        }
        this.recalculateListeners();
        return listeners;
    }

    public void unregisterListener(PacketListenerCommon listener) {
        if (this.unregisterListenerNoRecalculation(listener)) this.recalculateListeners();
    }

    public void unregisterListeners(PacketListenerCommon... listeners) {
        boolean modified = false;
        for (PacketListenerCommon listener : listeners) {
            modified |= this.unregisterListenerNoRecalculation(listener);//OR - at least one of these needs to be true to return true, meaning the boolean will permanently stay true if once set so
        }
        if (modified) this.recalculateListeners();
    }


    /**
     * Unregister all dynamic packet event listeners.
     */
    public void unregisterAllListeners() {
        this.listenersMap.clear();
        synchronized (this) {//like booky10 said, the synchronization is necessary here
            this.listeners = new PacketListenerCommon[0];
        }
    }

    //Needs to be synchronized in order to avoid race conditions (for example where the 'listeners' variable
    //is overridden by its non-up-to-date value, simply because it finished a bit later than the most recent update)
    private void recalculateListeners() {
        synchronized (this) {
            List<PacketListenerCommon> list = new ArrayList<>();
            //adds from LOWEST to MONITOR, so in the correct order
            for (PacketListenerPriority priority : PacketListenerPriority.values()) {
                Set<PacketListenerCommon> set = this.listenersMap.get(priority);
                if (set != null) list.addAll(set);
            }
            this.listeners = list.toArray(new PacketListenerCommon[0]);
        }
    }

    //Internal registration methods, specifically separated for lesser overhead when registering an array of Listeners

    private void registerListenerNoRecalculation(PacketListenerCommon listener) {
        Set<PacketListenerCommon> listenerSet = this.listenersMap.computeIfAbsent(listener.getPriority(), p -> new CopyOnWriteArraySet<>());
        listenerSet.add(listener);
    }

    //Returns true if the listener was removed, so a modification occurred
    private boolean unregisterListenerNoRecalculation(PacketListenerCommon listener) {
        Set<PacketListenerCommon> listenerSet = this.listenersMap.get(listener.getPriority());
        return listenerSet != null && listenerSet.remove(listener);
    }
}
