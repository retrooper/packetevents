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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;

public class EventManager {
    private final Map<PacketListenerPriority, Set<PacketListenerCommon>> listenersMap =
            Collections.synchronizedMap(new EnumMap<>(PacketListenerPriority.class));

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

    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
        for (PacketListenerPriority priority : PacketListenerPriority.values()) {
            Set<PacketListenerCommon> listeners = listenersMap.get(priority);
            if (listeners != null) {
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
            }
        }
        // For performance reasons, we don't want to re-encode the packet if it's not needed.
        if (event instanceof ProtocolPacketEvent && !((ProtocolPacketEvent<?>) event).needsReEncode()) {
            ((ProtocolPacketEvent<?>) event).setLastUsedWrapper(null);
        }

    }

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
        Set<PacketListenerCommon> listenerSet = listenersMap.get(listener.getPriority());
        if (listenerSet == null) {
            listenerSet = new CopyOnWriteArraySet<>();
        }
        listenerSet.add(listener);
        listenersMap.put(listener.getPriority(), listenerSet);
        return listener;
    }

    /**
     * Register multiple dynamic packet event listeners with one method.
     *
     * @param listeners {@link PacketListenerCommon}
     */
    public PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners) {
        for (PacketListenerCommon listener : listeners) {
            registerListener(listener);
        }
        return listeners;
    }

    public void unregisterListener(PacketListenerCommon listener) {
        Set<PacketListenerCommon> listenerSet = listenersMap.get(listener.getPriority());
        if (listenerSet == null) return;
        listenerSet.remove(listener);
    }

    public void unregisterListeners(PacketListenerCommon... listeners) {
        for (PacketListenerCommon listener : listeners) {
            unregisterListener(listener);
        }
    }


    /**
     * Unregister all dynamic packet event listeners.
     */
    public void unregisterAllListeners() {
        listenersMap.clear();
    }
}
