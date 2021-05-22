/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.event.manager;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

class EventManagerModern {
    private final Map<Byte, HashSet<PacketListenerAbstract>> listenersMap = new ConcurrentHashMap<>();

    /**
     * Call the PacketEvent.
     * This method processes the event on all the registered dynamic packet event listeners.
     * The {@link PacketEventPriority#LOWEST} prioritized listeners will be processing first,
     * the {@link PacketEventPriority#MONITOR} will be processing last and can
     * be the final decider whether the event has been cancelled or not.
     * This call event also calls the legacy event manager call event.
     *
     * @param event {@link PacketEvent}
     * @see EventManagerLegacy#callEvent(PacketEvent, byte)
     */
    public void callEvent(final PacketEvent event) {
        boolean cancel = false;
        if (event instanceof CancellableEvent) {
            cancel = ((CancellableEvent) event).isCancelled();
        }
        byte highestReachedPriority = PacketEventPriority.LOWEST.getPriorityValue();
        for (byte priority = PacketEventPriority.LOWEST.getPriorityValue(); priority <= PacketEventPriority.MONITOR.getPriorityValue(); priority++) {
            HashSet<PacketListenerAbstract> listeners = listenersMap.get(priority);
            if (listeners != null) {
                for (PacketListenerAbstract listener : listeners) {
                    try {
                        event.call(listener);
                    } catch (Exception ex) {
                        PacketEvents.get().getPlugin().getLogger()
                                .log(Level.SEVERE, "PacketEvents found an exception while calling a packet listener.", ex);
                    }
                    if (event instanceof CancellableEvent) {
                        CancellableEvent ce = (CancellableEvent) event;
                        cancel = ce.isCancelled();
                        highestReachedPriority = priority;
                    }
                }
                if (event instanceof CancellableEvent) {
                    CancellableEvent ce = (CancellableEvent) event;
                    ce.setCancelled(cancel);
                }
            }
        }
        PEEventManager.EVENT_MANAGER_LEGACY.callEvent(event, highestReachedPriority);
    }

    /**
     * Register the dynamic packet event listener.
     *
     * @param listener {@link PacketListenerAbstract}
     */
    public synchronized void registerListener(final PacketListenerAbstract listener) {
        byte priority = listener.getPriority().getPriorityValue();
        HashSet<PacketListenerAbstract> listenerSet = listenersMap.get(priority);
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
        }
        listenerSet.add(listener);
        listenersMap.put(priority, listenerSet);
    }

    /**
     * Register multiple dynamic packet event listeners with one method.
     *
     * @param listeners {@link PacketListenerAbstract}
     */
    public synchronized void registerListeners(PacketListenerAbstract... listeners) {
        for (PacketListenerAbstract listener : listeners) {
            registerListener(listener);
        }
    }

    /**
     * Unregister all dynamic packet event listeners.
     */
    public synchronized void unregisterAllListeners() {
        listenersMap.clear();
    }
}