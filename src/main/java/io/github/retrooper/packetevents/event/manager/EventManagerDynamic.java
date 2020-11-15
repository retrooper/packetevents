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
import io.github.retrooper.packetevents.event.annotation.PacketHandler;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.exceptions.PacketEventsMethodInvokeException;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EventManagerDynamic {
    private final Map<Byte, List<PacketListenerDynamic>> map = new HashMap<>();

    public void callEvent(PacketEvent event) {
        boolean isCancelled = false;
        for (byte i = PacketEventPriority.LOWEST; i <= PacketEventPriority.MONITOR; i++) {
            if (map.get(i) != null) {
                for (PacketListenerDynamic listener : map.get(i)) {
                    try {
                        listener.onPacketEvent(event);
                        if (event instanceof PacketStatusEvent) {
                            listener.onPacketStatus((PacketStatusEvent) event);
                        } else if (event instanceof PacketLoginEvent) {
                            listener.onPacketLogin((PacketLoginEvent) event);
                        } else if (event instanceof PacketReceiveEvent) {
                            listener.onPacketReceive((PacketReceiveEvent) event);
                        } else if (event instanceof PacketSendEvent) {
                            listener.onPacketSend((PacketSendEvent) event);
                        } else if (event instanceof PostPacketReceiveEvent) {
                            listener.onPostPacketReceive((PostPacketReceiveEvent) event);
                        } else if (event instanceof PostPacketSendEvent) {
                            listener.onPostPacketSend((PostPacketSendEvent) event);
                        } else if (event instanceof PlayerInjectEvent) {
                            listener.onPlayerInject((PlayerInjectEvent) event);
                        } else if (event instanceof PlayerEjectEvent) {
                            listener.onPlayerEject((PlayerEjectEvent) event);
                        }

                    }
                    catch(Exception ex) {
                        throw new PacketEventsMethodInvokeException("PacketEvents " +
                                "failed to call an event method in the " +
                                "dynamic packet listener. Event type: "
                                + ClassUtil.getClassSimpleName(event.getClass()));
                    }

                    if(event instanceof CancellableEvent) {
                        CancellableEvent ce = (CancellableEvent)event;
                        isCancelled = ce.isCancelled();
                    }
                }
            }
        }
        if (event instanceof CancellableEvent) {
            CancellableEvent ce = (CancellableEvent) event;
            ce.setCancelled(isCancelled);
        }
    }

    public void registerListener(PacketListenerDynamic listener) {
        List<PacketListenerDynamic> listeners = map.get(listener.getPriority());
        if(listeners == null) {
            map.put(listener.getPriority(), new ArrayList<>());
            listeners = map.get(listener.getPriority());
        }
        listeners.add(listener);
    }

    public void registerListeners(PacketListenerDynamic... listeners) {
        for (PacketListenerDynamic listener : listeners) {
            registerListener(listener);
        }
    }

    public void unregisterListener(PacketListenerDynamic listener) {
        List<PacketListenerDynamic> listeners = map.get(listener.getPriority());
        if(listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    public void unregisterListeners(PacketListenerDynamic... listeners) {
        for (PacketListenerDynamic listener : listeners) {
            unregisterListener(listener);
        }
    }

    public void unregisterAllListeners() {
        map.clear();
    }
}
