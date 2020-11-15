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
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.annotation.PacketHandler;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.exceptions.PacketEventsMethodAccessException;
import io.github.retrooper.packetevents.exceptions.PacketEventsMethodInvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EventManagerLegacy {
    private final Map<Byte, Map<PacketListener, List<Method>>> map = new HashMap<>();

    public void callEvent(PacketEvent event) {
        boolean isCancelled = false;
        for (byte i = PacketEventPriority.LOWEST; i < PacketEventPriority.MONITOR; i++) {
            if (map.get(i) != null) {
                for (PacketListener listener : map.get(i).keySet()) {
                    if (map.get(i).get(listener) != null) {
                        for (Method method : map.get(i).get(listener)) {
                            try {
                                method.invoke(listener, event);
                            } catch (IllegalAccessException ex) {
                                throw new PacketEventsMethodAccessException(method, listener);
                            } catch (InvocationTargetException ex) {
                                throw new PacketEventsMethodInvokeException(method, listener);
                            }
                            if (event instanceof CancellableEvent) {
                                CancellableEvent ce = (CancellableEvent) event;
                                isCancelled = ce.isCancelled();
                            }
                        }
                    }
                }
            }
        }
        if (event instanceof CancellableEvent) {
            CancellableEvent ce = (CancellableEvent) event;
            ce.setCancelled(isCancelled);
        }
    }

    public void registerListener(PacketListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            if (method.isAnnotationPresent(PacketHandler.class)
                    && method.getParameterTypes().length == 1 &&
                    (PacketEvent.class.equals(method.getParameterTypes()[0])
                            || PacketEvent.class.isAssignableFrom(method.getParameterTypes()[0]))) {
                PacketHandler annotation = method.getAnnotation(PacketHandler.class);
                Map<PacketListener, List<Method>> insideMap = map.get(annotation.priority());
                if (insideMap == null) {
                    map.put(annotation.priority(), new HashMap<>());
                    insideMap = map.get(annotation.priority());
                }
                insideMap.get(listener).add(method);
            }
        }
    }

    public void registerListeners(PacketListener... listeners) {
        for (PacketListener listener : listeners) {
            registerListener(listener);
        }
    }

    public void unregisterListener(PacketListener listener) {
        for (Map<PacketListener, List<Method>> insideMap : map.values()) {
            insideMap.remove(listener);
        }
    }

    public void unregisterListeners(PacketListener... listeners) {
        for (PacketListener listener : listeners) {
            unregisterListener(listener);
        }
    }

    public void unregisterAllListeners() {
        map.clear();
    }
}
