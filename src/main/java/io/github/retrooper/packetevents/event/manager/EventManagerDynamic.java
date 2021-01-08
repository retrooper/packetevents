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

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;

import java.util.*;

class EventManagerDynamic {
    /**
     * All listeners ordered by their priority.
     * The most low priority ones are at the begging of the list and the most high priority ones are at the end.
     */
    private final List<PacketListenerDynamic> listeners = new ArrayList<>();

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
        for (PacketListenerDynamic listener : listeners) {
            highestReachedPriority = listener.getPriority().getPriorityValue();
            event.callPacketEvent(listener);
            event.call(listener);
            if (event instanceof CancellableEvent) {
                CancellableEvent ce = (CancellableEvent) event;
                cancel = ce.isCancelled();
            }
        }
        if (event instanceof CancellableEvent) {
            CancellableEvent ce = (CancellableEvent) event;
            ce.setCancelled(cancel);
        }

        PEEventManager.EVENT_MANAGER_LEGACY.callEvent(event, highestReachedPriority);
    }

    /**
     * Register the dynamic packet event listener.
     *
     * @param listener {@link PacketListenerDynamic}
     */
    public void registerListener(final PacketListenerDynamic listener) {
        final byte priorityValue = listener.getPriority().getPriorityValue();
        for (int i = 0; i < listeners.size(); i++) {
            PacketListenerDynamic other = listeners.get(i);
            byte otherPriorityValue = other.getPriority().getPriorityValue();
            if (i + 1 == listeners.size()) {
                listeners.add(listener);
                break;
            } else if (otherPriorityValue == priorityValue) {
                listeners.add(i, listener);
                break;
            } else if (otherPriorityValue > priorityValue) {
                listeners.add(i - 1, listener);
                break;
            }
        }
    }

    /**
     * Register multiple dynamic packet event listeners with one method.
     *
     * @param listeners {@link PacketListenerDynamic}
     */
    public void registerListeners(PacketListenerDynamic... listeners) {
        for (PacketListenerDynamic listener : listeners) {
            registerListener(listener);
        }
    }

    /**
     * Unregister the dynamic packet event listener.
     *
     * @param listener {@link PacketListenerDynamic}
     */
    public void unregisterListener(PacketListenerDynamic listener) {
        listeners.remove(listener);
    }

    /**
     * Unregister multiple dynamic packet event listeners with one method.
     *
     * @param listeners {@link PacketListenerDynamic}
     */
    public void unregisterListeners(PacketListenerDynamic... listeners) {
        for (PacketListenerDynamic listener : listeners) {
            unregisterListener(listener);
        }
    }

    /**
     * Unregister all dynamic packet event listeners.
     */
    public void unregisterAllListeners() {
        listeners.clear();
    }
}
