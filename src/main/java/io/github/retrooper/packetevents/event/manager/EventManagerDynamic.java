/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

class EventManagerDynamic {
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