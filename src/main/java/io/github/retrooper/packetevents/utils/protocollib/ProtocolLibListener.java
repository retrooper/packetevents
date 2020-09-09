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

package io.github.retrooper.packetevents.utils.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.PacketEventPriority;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ProtocolLibListener {

    public static void registerProtocolLibListener(PacketListener listener, List<Method> methods) {
        if (ProtocolLibUtils.isAvailable()) {
            for (Method m : methods) {
                if (m.getParameterTypes()[0].equals(PacketReceiveEvent.class)) {
                    PacketHandler annot = m.getAnnotation(PacketHandler.class);
                    ListenerPriority priority;
                    switch (annot.priority()) {
                        case PacketEventPriority.LOWEST:
                            priority = ListenerPriority.LOWEST;
                            break;
                        case PacketEventPriority.LOW:
                            priority = ListenerPriority.LOW;
                            break;
                        case PacketEventPriority.HIGH:
                            priority = ListenerPriority.HIGH;
                            break;
                        case PacketEventPriority.HIGHEST:
                            priority = ListenerPriority.HIGHEST;
                            break;
                        case PacketEventPriority.MONITOR:
                            priority = ListenerPriority.MONITOR;
                            break;
                        default:
                            priority = ListenerPriority.NORMAL;
                            break;
                    }
                    ProtocolLibrary.getProtocolManager().
                            addPacketListener(new PacketAdapter(PacketEvents.getPlugins().get(0),
                                    priority, PacketType.Play.Client.getInstance().values()) {
                                @Override
                                public void onPacketReceiving(PacketEvent event) {
                                    event.setReadOnly(false);
                                    PacketReceiveEvent receiveEvent = new PacketReceiveEvent(event.getPlayer(), event.getPacket().getHandle());
                                    try {
                                        m.invoke(listener, receiveEvent);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                    event.setCancelled(receiveEvent.isCancelled());
                                }
                            });
                } else if (m.getParameterTypes()[0].equals(PacketSendEvent.class)) {
                    PacketHandler annot = m.getAnnotation(PacketHandler.class);
                    ListenerPriority priority;
                    switch (annot.priority()) {
                        case PacketEventPriority.LOWEST:
                            priority = ListenerPriority.LOWEST;
                            break;
                        case PacketEventPriority.LOW:
                            priority = ListenerPriority.LOW;
                            break;
                        case PacketEventPriority.HIGH:
                            priority = ListenerPriority.HIGH;
                            break;
                        case PacketEventPriority.HIGHEST:
                            priority = ListenerPriority.HIGHEST;
                            break;
                        case PacketEventPriority.MONITOR:
                            priority = ListenerPriority.MONITOR;
                            break;
                        default:
                            priority = ListenerPriority.NORMAL;
                            break;
                    }
                    ProtocolLibrary.getProtocolManager().
                            addPacketListener(new PacketAdapter(PacketEvents.getPlugins().get(0),
                                    priority, PacketType.Play.Server.getInstance().values()) {
                                @Override
                                public void onPacketSending(PacketEvent event) {
                                    event.setReadOnly(false);
                                    PacketSendEvent sendEvent = new PacketSendEvent(event.getPlayer(), event.getPacket().getHandle());
                                    try {
                                        m.invoke(listener, sendEvent);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                    event.setCancelled(sendEvent.isCancelled());
                                }
                            });
                }
            }
        }
    }


}