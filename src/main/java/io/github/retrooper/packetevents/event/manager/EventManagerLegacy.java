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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EventManagerLegacy {
    private final Map<PacketListener, List<Method>> staticRegisteredMethods = new HashMap<>();

    public void callEvent(final PacketEvent e) {
        boolean isCancelled = false;
        byte eventPriority = PacketEventPriority.LOWEST;
        //STATIC LISTENERS
        for (final PacketListener listener : staticRegisteredMethods.keySet()) {
            List<Method> methods = staticRegisteredMethods.get(listener);

            for (Method method : methods) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType.equals(PacketEvent.class)
                        || parameterType.isInstance(e)) {

                    PacketHandler annotation = method.getAnnotation(PacketHandler.class);
                    try {
                        method.invoke(listener, e);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                    if (e instanceof CancellableEvent) {
                        CancellableEvent ce = (CancellableEvent) e;
                        if (annotation.priority() >= eventPriority) {
                            eventPriority = annotation.priority();
                            isCancelled = ce.isCancelled();
                        }
                    }
                }
            }
        }
        if (e instanceof CancellableEvent) {
            CancellableEvent ce = (CancellableEvent) e;
            ce.setCancelled(isCancelled);
        }
    }

    public void registerListener(final PacketListener listener) {
        final List<Method> methods = new ArrayList<>();
        for (final Method m : listener.getClass().getDeclaredMethods()) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            if (m.isAnnotationPresent(PacketHandler.class)
                    && m.getParameterTypes().length == 1) {
                methods.add(m);
            }
        }

        if (!methods.isEmpty()) {
            staticRegisteredMethods.put(listener, methods);
        }
    }

    public void registerListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            registerListener(listener);
        }
    }

    public void unregisterListener(final PacketListener e) {
        staticRegisteredMethods.remove(e);
    }

    public void unregisterListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            unregisterListener(listener);
        }
    }

    public void unregisterAllListeners() {
        staticRegisteredMethods.clear();
    }
}
