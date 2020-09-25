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
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.PacketEventPriority;
import io.github.retrooper.packetevents.event.CancellableEvent;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.utils.protocollib.ProtocolLibListener;
import io.github.retrooper.packetevents.utils.protocollib.ProtocolLibUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class EventManager {
    private final ConcurrentHashMap<PacketListener, ConcurrentLinkedQueue<Method>> registeredMethods = new ConcurrentHashMap<>();

    public void callEvent(final PacketEvent e) {
        for (final PacketListener listener : registeredMethods.keySet()) {
            ConcurrentLinkedQueue<Method> methods = registeredMethods.get(listener);
            final boolean[] isCancelled = {false};
            final byte[] eventPriority = {PacketEventPriority.LOWEST};
            for (Method method : methods) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType.equals(PacketEvent.class) || parameterType.isInstance(e)) {
                    PacketHandler annotation = method.getAnnotation(PacketHandler.class);
                    Runnable invokeMethod = () -> {
                        try {
                            method.invoke(listener, e);
                        } catch (IllegalAccessException | InvocationTargetException ignored) {
                        }
                        if (e instanceof CancellableEvent) {
                            CancellableEvent ce = (CancellableEvent) e;
                            if (annotation.priority() >= eventPriority[0]) {
                                eventPriority[0] = annotation.priority();
                                isCancelled[0] = ce.isCancelled();
                            }
                        }
                    };
                    switch (annotation.synchronization()) {
                        case FORCE_ASYNC:
                            Bukkit.getScheduler().runTaskAsynchronously(PacketEvents.getPlugin(), invokeMethod);
                            break;
                        case FORCE_SYNC:
                            Bukkit.getScheduler().runTask(PacketEvents.getPlugin(), invokeMethod);
                            break;
                        default:
                            try {
                                method.invoke(listener, e);
                            } catch (IllegalAccessException | InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                            if (e instanceof CancellableEvent) {
                                CancellableEvent ce = (CancellableEvent) e;
                                if (annotation.priority() >= eventPriority[0]) {
                                    eventPriority[0] = annotation.priority();
                                    isCancelled[0] = ce.isCancelled();
                                }
                            }
                            break;
                    }
                    if (e instanceof CancellableEvent) {
                        CancellableEvent cancellableEvent = (CancellableEvent) e;
                        cancellableEvent.setCancelled(isCancelled[0]);
                    }
                }
            }
        }
    }

    public void registerListener(final PacketListener listener) {
        final ConcurrentLinkedQueue<Method> methods = new ConcurrentLinkedQueue<>();
        for (final Method m : listener.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(PacketHandler.class)
                    && m.getParameterTypes().length == 1) {
                methods.add(m);
            }
        }

        if (!methods.isEmpty()) {
            if (ProtocolLibUtils.isAvailable()
                    && PacketEvents.getAPI().getSettings().isUseProtocolLibIfAvailable()) {
                ProtocolLibListener.registerProtocolLibListener(listener, methods);
            } else {
                registeredMethods.put(listener, methods);
            }
        }
    }

    public void registerListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            registerListener(listener);
        }
    }

    public void unregisterListener(final PacketListener e) {
        registeredMethods.remove(e);
    }

    public void unregisterListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            unregisterListener(listener);
        }
    }

    public void unregisterAllListeners() {
        registeredMethods.clear();
    }

    public boolean isRegistered(final PacketListener listener) {
        return registeredMethods.containsKey(listener);
    }
}
