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

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProtocolLibListener {
    public enum ProtocolLibPresent {
        UNKNOWN, NOT_PRESENT, PRESENT
    }

    private static ProtocolLibPresent plibPresent = ProtocolLibPresent.UNKNOWN;


    public static void load() {
        if (ProtocolLibUtils.isAvailable()) {
            plibPresent = ProtocolLibPresent.PRESENT;
        }
    }

    public static ProtocolLibPresent isProtocolLibPresent() {
        return plibPresent;
    }

    public static void registerProtocolLibListener(PacketListener listener, List<Method> methods) {
        if (plibPresent == ProtocolLibPresent.PRESENT) {
            List<Method> receiveListener = new ArrayList<Method>();
            List<Method> sendListener = new ArrayList<Method>();

            List<Boolean> receiveListenerIsCancelled = new ArrayList<Boolean>();
            List<Boolean> sendListenerIsCancelled = new ArrayList<Boolean>();

            int highestPriorityMethodReceiveListenerIndex = 0;
            int highestPriorityMethodSendListenerIndex = 0;

            //PACKET RECEIVE EVENT
            for (int i = 0; i < methods.size(); i++) {
                Method m = methods.get(i);

                PacketHandler annotation = m.getAnnotation(PacketHandler.class);

                PacketHandler competitorAnnotation = methods.get(highestPriorityMethodReceiveListenerIndex).getAnnotation(PacketHandler.class);

                if (competitorAnnotation.priority() >= annotation.priority()) {
                    highestPriorityMethodReceiveListenerIndex = i;
                }

                if (m.getParameterTypes()[0].equals(PacketReceiveEvent.class)) {
                    receiveListener.add(m);

                }
            }

            final int finalHighestPriorityMethodReceiveListenerIndex = highestPriorityMethodReceiveListenerIndex;
            ProtocolLibrary.getProtocolManager().addPacketListener(
                    new PacketAdapter(PacketEvents.getPlugins().get(0)) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            System.out.println(event.getSource().getClass().getSimpleName());
                            PacketReceiveEvent receiveEvent =
                                    new PacketReceiveEvent(event.getPlayer(), event.getSource());

                            for (Method receive : receiveListener) {
                                try {
                                    receive.invoke(listener, receiveEvent);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }

                            receiveListenerIsCancelled.add(receiveEvent.isCancelled());


                            if (receiveListener.size() == receiveListenerIsCancelled.size()) {
                                event.setCancelled(receiveListenerIsCancelled.get(finalHighestPriorityMethodReceiveListenerIndex));
                            }
                        }
                    });

            //PACKET SEND EVENT
            for (int i = 0; i < methods.size(); i++) {
                Method m = methods.get(i);

                PacketHandler annotation = m.getAnnotation(PacketHandler.class);

                PacketHandler competitorAnnotation = methods.get(highestPriorityMethodSendListenerIndex).getAnnotation(PacketHandler.class);

                if (competitorAnnotation.priority() >= annotation.priority()) {
                    highestPriorityMethodSendListenerIndex = i;
                }

                if (m.getParameterTypes()[0].equals(PacketSendEvent.class)) {
                    sendListener.add(m);
                }
            }

            final int finalHighestPriorityMethodSendListenerIndex = highestPriorityMethodSendListenerIndex;
            ProtocolLibrary.getProtocolManager().addPacketListener(
                    new PacketAdapter(PacketEvents.getPlugins().get(0)) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            System.out.println(event.getSource().getClass().getSimpleName());
                            PacketSendEvent sendEvent =
                                    new PacketSendEvent(event.getPlayer(), event.getSource());

                            for (Method send : sendListener) {
                                try {
                                    send.invoke(listener, sendEvent);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }

                            sendListenerIsCancelled.add(sendEvent.isCancelled());


                            if (sendListener.size() == sendListenerIsCancelled.size()) {
                                event.setCancelled(sendListenerIsCancelled.get(finalHighestPriorityMethodSendListenerIndex));
                            }
                        }
                    });

        }
    }


}