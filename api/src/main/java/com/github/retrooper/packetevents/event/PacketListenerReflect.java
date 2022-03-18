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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public interface PacketListenerReflect {
    default PacketListenerAbstract asAbstract(PacketListenerPriority priority) {
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
                } else if (parameterType.equals(UserConnectEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 1);
                    eventMethods.add(method);
                } else if (parameterType.equals(UserLoginEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 2);
                    eventMethods.add(method);
                } else if (parameterType.equals(UserDisconnectEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 3);
                    eventMethods.add(method);
                } else if (parameterType.equals(PacketReceiveEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 4);
                    eventMethods.add(method);
                } else if (parameterType.equals(PacketSendEvent.class)) {
                    List<Method> eventMethods = methods.get((byte) 5);
                    eventMethods.add(method);
                } else if (parameterType.equals(PacketEvent.class)) {
                    //Add to all
                    for (byte i = 0; i < 7; i++) {
                        List<Method> eventMethods = methods.get(i);
                        eventMethods.add(method);
                        methods.put(i, eventMethods);
                    }
                } else {
                    List<Method> eventMethods = methods.get((byte) 6);
                    eventMethods.add(method);
                }
            }
        }
        return new PacketListenerAbstract(priority, methods) {
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
            public void onUserConnect(UserConnectEvent event) {
                callEventByIndex((byte) 1, event);
            }

            @Override
            public void onUserLogin(UserLoginEvent event) {
                callEventByIndex((byte) 2, event);
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                callEventByIndex((byte) 3, event);
            }

            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                callEventByIndex((byte) 4, event);
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                callEventByIndex((byte) 5, event);
            }

            @Override
            public void onPacketEventExternal(PacketEvent event) {
                callEventByIndex((byte) 6, event);
            }
        };
    }
}
