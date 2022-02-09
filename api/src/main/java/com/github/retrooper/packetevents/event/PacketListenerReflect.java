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

package com.github.retrooper.packetevents.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public interface PacketListenerReflect {
    default PacketListenerAbstract asAbstract(PacketListenerPriority priority, boolean readOnly) {
        Map<Byte, List<Method>> methods = new HashMap<>();
        for (byte i = 0; i < 6; i++) {
            methods.put(i, new ArrayList<>());
        }
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(PacketHandler.class)
                    && method.getParameterTypes().length == 1) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType.equals(PlayerInjectEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 0);
                    eventMethods.add(method);
                    methods.put((byte) 0, eventMethods);
                } else if (parameterType.equals(PostPlayerInjectEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 1);
                    eventMethods.add(method);
                    methods.put((byte) 1, eventMethods);
                } else if (parameterType.equals(PlayerEjectEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 2);
                    eventMethods.add(method);
                    methods.put((byte) 2, eventMethods);
                } else if (parameterType.equals(PacketReceiveEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 3);
                    eventMethods.add(method);
                    methods.put((byte) 3, eventMethods);
                } else if (parameterType.equals(PacketSendEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 4);
                    eventMethods.add(method);
                    methods.put((byte) 4, eventMethods);
                } else if (parameterType.equals(PacketEvent.class)) {
                    //Add to all
                    for (byte i = 0; i < 6; i++) {
                        List<Method> eventMethods = methods.get(i);
                        eventMethods.add(method);
                        methods.put(i, eventMethods);
                    }
                } else {
                    List<Method> eventMethods = methods.get((byte) 5);
                    eventMethods.add(method);
                    methods.put((byte) 5, eventMethods);
                }
            }
        }
        return new PacketListenerAbstract(priority, methods, readOnly) {
            private void callEventByIndex(byte index, PacketEvent event) {
                if (methods != null) {
                    List<Method> targets = methods.get(index);
                    for (Method target : targets) {
                        try {
                            target.invoke(PacketListenerReflect.this, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onPlayerInject(PlayerInjectEvent event) {
                callEventByIndex((byte) 0, event);
            }

            @Override
            public void onPostPlayerInject(PostPlayerInjectEvent event) {
                callEventByIndex((byte) 1, event);
            }

            @Override
            public void onPlayerEject(PlayerEjectEvent event) {
                callEventByIndex((byte) 2, event);
            }

            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                callEventByIndex((byte) 3, event);
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                callEventByIndex((byte) 4, event);
            }

            @Override
            public void onPacketEventExternal(PacketEvent event) {
                callEventByIndex((byte) 5, event);
            }
        };
    }
}
