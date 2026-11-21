/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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
import com.github.retrooper.packetevents.util.LogManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * Class for event managing. Implements both, internal and API methods.
 *
 * @author retrooper
 * @author ShadowOfHeaven (optimization)
 * @apiNote It is highly recommended to register the event listeners at server start-up,
 * without frequent modifications during runtime, since this class needs to recalculate all of
 * its event listeners during any modifications. In other words, it assumes that reads (events fired)
 * greatly outnumber writes (listeners modifications). If your case requires frequent listener modifications,
 * open a pull request on GitHub and describe your case.
 */
@NullMarked
@ApiStatus.NonExtendable
public class EventContainer {

    //Using a ConcurrentHashMap is faster and more secure here, compared to Collections.synchronizedMap(new EnumMap<>(PacketListenerPriority.class))
    //This is mainly due to:
    //1. On each modification Collections.synchronizedMap synchronizes the whole Map object, while ConcurrentHashMap only it's internal, currently modified Node
    //2. ConcurrentHashMap won't fail in a multi-thread environment, while Collections.synchronizedMap is said to have a lot of potential problems,
    //being a generalized method for synchronization
    private final Map<PacketListenerPriority, Set<PacketListenerCommon>> listenersMap = new ConcurrentHashMap<>();
    //Since reads greatly outnumber writes, create an array for the best possible iteration time
    //Updated as a whole on writes, no index modifications are allowed
    private volatile PacketListenerCommon[] listeners = new PacketListenerCommon[0];

    // how many listener exceptions can be logged in a row, and how long it takes until one more can be
    private static final int LOG_BURST = 10;
    private static final long LOG_NANOS = TimeUnit.SECONDS.toNanos(1);
    private final Object logLock = new Object();
    private int logsLeft = LOG_BURST;
    private long lastRegain;
    private int notLogged;

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
     * @param postCallListenerAction The action to be run after all the listeners have finished processing
     */
    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
        for (PacketListenerCommon listener : listeners) {
            try {
                event.call(listener);
            } catch (Exception t) {
                // ignore handshake exceptions
                if (t.getClass() != InvalidHandshakeException.class && (t.getCause() == null || t.getCause().getClass() != InvalidHandshakeException.class)) {
                    this.logException(t, System.nanoTime());
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

    // a client can make a listener throw with every packet it sends, so after LOG_BURST
    // exceptions in a row only one a second is logged, the rest are counted
    void logException(Exception t, long now) {
        LogManager log = PacketEvents.getAPI().getLogManager();
        String message = "PacketEvents caught an unhandled exception while calling your listener.";
        if (!log.isDebug()) {
            synchronized (this.logLock) {
                if (this.logsLeft < LOG_BURST) {
                    long regained = (now - this.lastRegain) / LOG_NANOS;
                    if (regained > 0) {
                        this.logsLeft = (int) Math.min(LOG_BURST, this.logsLeft + regained);
                        this.lastRegain += regained * LOG_NANOS;
                    }
                }
                if (this.logsLeft == LOG_BURST) {
                    this.lastRegain = now;
                }
                if (this.logsLeft == 0) {
                    this.notLogged++;
                    return;
                }
                this.logsLeft--;
                if (this.notLogged > 0) {
                    message += " (" + this.notLogged + " more were not logged)";
                    this.notLogged = 0;
                }
            }
        }
        log.warn(message, t);
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

    public boolean isEmpty() {
        return this.listenersMap.isEmpty();
    }

    @ApiStatus.Internal
    void recalculateListeners() {
        synchronized (this) {
            List<PacketListenerCommon> list = new ArrayList<>();
            //adds from LOWEST to MONITOR, so in the correct order
            for (PacketListenerPriority priority : PacketListenerPriority.values()) {
                Set<PacketListenerCommon> set = this.listenersMap.get(priority);
                if (set != null) {
                    list.addAll(set);
                }
            }
            this.listeners = list.toArray(new PacketListenerCommon[0]);
        }
    }

    @ApiStatus.Internal
    void registerListenerNoRecalculation(PacketListenerCommon listener) {
        this.handleNewListener(listener);
        Set<PacketListenerCommon> listenerSet = this.listenersMap.computeIfAbsent(listener.getPriority(), p -> new CopyOnWriteArraySet<>());
        listenerSet.add(listener);
    }

    /**
     * @return whether listener was removed
     */
    @ApiStatus.Internal
    boolean unregisterListenerNoRecalculation(PacketListenerCommon listener) {
        Set<PacketListenerCommon> listenerSet = this.listenersMap.get(listener.getPriority());
        return listenerSet != null && listenerSet.remove(listener);
    }

    @ApiStatus.OverrideOnly
    protected void handleNewListener(PacketListenerCommon listener) {
    }
}
