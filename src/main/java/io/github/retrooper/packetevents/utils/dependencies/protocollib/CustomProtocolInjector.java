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

package io.github.retrooper.packetevents.utils.dependencies.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.error.ErrorReporter;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.NetworkMarker;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.ListenerInvoker;
import com.comphenix.protocol.injector.netty.*;
import com.comphenix.protocol.injector.player.PlayerInjectionHandler;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Set;

public class CustomProtocolInjector extends ProtocolInjector {
    public CustomProtocolInjector(Plugin plugin, ListenerInvoker invoker, ErrorReporter reporter) {
        super(plugin, invoker, reporter);
    }

    @Override
    public PlayerInjectionHandler getPlayerInjector() {
        ReflectionObject reflect = new ReflectionObject(this, getClass().getSuperclass());
        PacketTypeSet sendingFilters = reflect.read(0, PacketTypeSet.class);
        PacketTypeSet mainThreadFilters = reflect.read(2, PacketTypeSet.class);
        InjectionFactory injectionFactory = reflect.read(0, InjectionFactory.class);
        return new AbstractPlayerHandler(sendingFilters) {
            private final ChannelListener listener = CustomProtocolInjector.this;

            @Override
            public int getProtocolVersion(Player player) {
                return injectionFactory.fromPlayer(player, listener).getProtocolVersion();
            }

            @Override
            public void updatePlayer(Player player) {
                safeInjection(player);
            }

            @Override
            public void injectPlayer(Player player, ConflictStrategy strategy) {
                safeInjection(player);
            }

            private void safeInjection(Player player) {
                Channel channel = getChannel(player);
                boolean skip = false;

                if (channel != null) {
                    if (ClassUtil.getClassSimpleName(channel.pipeline().get("decoder").getClass()).equals("CustomBukkitDecodeHandler")) {
                        skip = true;
                    }
                }
                if (!skip) {
                    injectionFactory.fromPlayer(player, listener).inject();
                }
            }

            @Override
            public boolean uninjectPlayer(InetSocketAddress address) {
                // Ignore this too
                return true;
            }

            @Override
            public void addPacketHandler(PacketType type, Set<ListenerOptions> options) {
                if (!type.isAsyncForced() && (options == null || !options.contains(ListenerOptions.ASYNC)))
                    mainThreadFilters.addType(type);
                super.addPacketHandler(type, options);
            }

            @Override
            public void removePacketHandler(PacketType type) {
                mainThreadFilters.removeType(type);
                super.removePacketHandler(type);
            }

            @Override
            public boolean uninjectPlayer(Player player) {
                // Just let Netty clean this up
                return true;
            }

            @Override
            public void sendServerPacket(Player receiver, PacketContainer packet, NetworkMarker marker, boolean filters) throws InvocationTargetException {
                injectionFactory.fromPlayer(receiver, listener).sendServerPacket(packet.getHandle(), marker, filters);
            }

            @Override
            public boolean hasMainThreadListener(PacketType type) {
                return mainThreadFilters.contains(type);
            }

            @Override
            public void recieveClientPacket(Player player, Object mcPacket) throws IllegalAccessException, InvocationTargetException {
                injectionFactory.fromPlayer(player, listener).recieveClientPacket(mcPacket);
            }

            @Override
            public PacketEvent handlePacketRecieved(PacketContainer packet, InputStream input, byte[] buffered) {
                // Ignore this
                return null;
            }

            @Override
            public void handleDisconnect(Player player) {
                injectionFactory.fromPlayer(player, listener).close();
            }

            @Override
            public Channel getChannel(Player player) {
                Injector injector = injectionFactory.fromPlayer(player, listener);
                if (injector instanceof ChannelInjector) {
                    return ((ChannelInjector) injector).getChannel();
                }

                return null;
            }
        };
    }
}
