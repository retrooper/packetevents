/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.annotation.PacketHandler;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Deprecated
class EventManagerLegacy {
    /**
     * Map storing all all legacy packet event listeners.
     * The key is an individual listener, the values are the key's event methods.
     */

    private final Map<PacketListener, HashSet<Method>> staticRegisteredMethods = new HashMap<>();

    /**
     * Call a PacketEvent with the legacy event manager.
     * This method processes the event on all legacy packet event listeners.
     * This method is actually called by {@link EventManagerModern#callEvent(PacketEvent)} as the
     * if you mix the two event systems, the dynamic listeners are always processed first and pass
     * their highest reached event priority that this legacy one has to beat/reach to be able to cancel
     * the event.
     * This downside of this system is the event listeners aren't executed in any particular order, just
     * in the order they were registered.
     * This system is also slower than the other event system.
     *
     * @param event         {@link PacketEvent}
     * @param eventPriority Priority the legacy listeners should beat to decide cancellation of the event.
     */
    @Deprecated
    public void callEvent(final PacketEvent event, byte eventPriority) {
        boolean isCancelled = false;
        if (event instanceof CancellableEvent) {
            isCancelled = ((CancellableEvent) event).isCancelled();
        }
        //STATIC LISTENERS
        for (final PacketListener listener : staticRegisteredMethods.keySet()) {
            HashSet<Method> methods = staticRegisteredMethods.get(listener);

            for (Method method : methods) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType.equals(PacketEvent.class)
                        || parameterType.isInstance(event)) {

                    PacketHandler annotation = method.getAnnotation(PacketHandler.class);
                    try {
                        method.invoke(listener, event);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                    if (event instanceof CancellableEvent) {
                        CancellableEvent ce = (CancellableEvent) event;
                        if (annotation.priority() >= eventPriority) {
                            eventPriority = annotation.priority();
                            isCancelled = ce.isCancelled();
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

    /**
     * Register a legacy packet event listener.
     * Not recommended to use the deprecated event listener.
     *
     * @param listener {@link PacketListener}
     */
    @Deprecated
    public void registerListener(final PacketListener listener) {
        final HashSet<Method> methods = new HashSet<>();
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

    /**
     * Register multiple legacy packet event listeners with one method.
     * Not recommended to use the deprecated event listener.
     *
     * @param listeners {@link PacketListener}
     */
    @Deprecated
    public void registerListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            registerListener(listener);
        }
    }

    /**
     * Unregister a legacy packet event listener.
     * Not recommended to use the deprecated event listener.
     *
     * @param listener {@link PacketListener}
     */
    @Deprecated
    public void unregisterListener(final PacketListener listener) {
        staticRegisteredMethods.remove(listener);
    }

    /**
     * Unregister multiple legacy packet event listeners with one method.
     * Not recommended to use the deprecated event listener.
     *
     * @param listeners {@link PacketListener}
     */
    @Deprecated
    public void unregisterListeners(final PacketListener... listeners) {
        for (final PacketListener listener : listeners) {
            unregisterListener(listener);
        }
    }

    /**
     * Unregister all legacy packet event listeners.
     */
    @Deprecated
    public void unregisterAllListeners() {
        staticRegisteredMethods.clear();
    }
}