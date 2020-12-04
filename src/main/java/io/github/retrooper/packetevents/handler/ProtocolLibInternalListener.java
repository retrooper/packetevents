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

package io.github.retrooper.packetevents.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.impl.PacketLoginEvent;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.event.impl.PacketStatusEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtocolLibInternalListener {
    private static final Map<PacketListenerDynamic, PacketAdapter> listenerCache = new HashMap<>();
    private static List<PacketType> supportedPacketTypes = null;

    public static void registerInternalListener(PacketListenerDynamic listener) {
        ListenerPriority priority = getListenerPriority(listener.getPriority());
        if (supportedPacketTypes == null) {
            supportedPacketTypes = new ArrayList<>();
            Iterable<PacketType> types = PacketType.values();
            for (PacketType type : types) {
                if (type.isSupported()) {
                    supportedPacketTypes.add(type);
                }
            }
        }
        PacketAdapter packetAdapter = new PacketAdapter(PacketEvents.get().getPlugins().get(0),
                priority, supportedPacketTypes) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                InetSocketAddress address = player.getAddress();
                Object nmsPacket = event.getPacket().getHandle();
                event.setReadOnly(false);
                io.github.retrooper.packetevents.event.PacketEvent peEvent = null;
                if (event.getPacketType().getProtocol().equals(PacketType.Protocol.PLAY)) {
                    peEvent = new PacketReceiveEvent(player, nmsPacket);
                } else if (event.getPacketType().getProtocol().equals(PacketType.Protocol.LOGIN)) {
                    peEvent = new PacketLoginEvent(address, nmsPacket);
                } else if (event.getPacketType().getProtocol().equals(PacketType.Protocol.STATUS)) {
                    peEvent = new PacketStatusEvent(address, nmsPacket);
                }
                if (peEvent != null) {
                    peEvent.call(listener);
                    CancellableEvent ce = (CancellableEvent)peEvent;
                    event.setCancelled(ce.isCancelled());
                }

            }

            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                InetSocketAddress address = player.getAddress();
                Object nmsPacket = event.getPacket().getHandle();
                event.setReadOnly(false);
                io.github.retrooper.packetevents.event.PacketEvent peEvent = null;
                if (event.getPacketType().getProtocol().equals(PacketType.Protocol.PLAY)) {
                    peEvent = new PacketSendEvent(player, nmsPacket);
                } else if (event.getPacketType().getProtocol().equals(PacketType.Protocol.LOGIN)) {
                    peEvent = new PacketLoginEvent(address, nmsPacket);
                } else if (event.getPacketType().getProtocol().equals(PacketType.Protocol.STATUS)) {
                    peEvent = new PacketStatusEvent(address, nmsPacket);
                }
                if (peEvent != null) {
                    peEvent.call(listener);
                    CancellableEvent ce = (CancellableEvent)peEvent;
                    event.setCancelled(ce.isCancelled());
                }
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(packetAdapter);
        listenerCache.put(listener, packetAdapter);
    }

    public static void unregisterInternalListener(PacketListenerDynamic listener) {
        PacketAdapter packetAdapter = listenerCache.get(listener);
        if (packetAdapter != null) {
            ProtocolLibrary.getProtocolManager().removePacketListener(packetAdapter);
            listenerCache.remove(listener);
        }
    }

    public static void unregisterAllListeners() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(PacketEvents.get().getPlugins().get(0));
    }

    private static ListenerPriority getListenerPriority(final byte priority) {
        switch (priority) {
            case PacketEventPriority
                    .LOWEST:
                return ListenerPriority.LOWEST;
            case PacketEventPriority.LOW:
                return ListenerPriority.LOW;
            case PacketEventPriority.HIGH:
                return ListenerPriority.HIGH;
            case PacketEventPriority.HIGHEST:
                return ListenerPriority.HIGHEST;
            case PacketEventPriority.MONITOR:
                return ListenerPriority.MONITOR;
            default:
                return ListenerPriority.NORMAL;
        }
    }
}
