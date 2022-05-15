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

package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.netty.channel.Channel;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PaperChannelInjector extends SpigotChannelInjector {
    private static final Class<?> CHANNEL_INITIALIZE_LISTENER_HOLDER_CLASS;
    private static Class<?> CHANNEL_INITIALIZE_LISTENER_CLASS;
    private static Method ADD_LISTENER_METHOD;
    private static Method HAS_LISTENER_METHOD;
    private static Method REMOVE_LISTENER_METHOD;

    static {
        CHANNEL_INITIALIZE_LISTENER_HOLDER_CLASS = Reflection.getClassByNameWithoutException("io.papermc.paper.network.ChannelInitializeListenerHolder");
        if (CHANNEL_INITIALIZE_LISTENER_HOLDER_CLASS != null) {
            CHANNEL_INITIALIZE_LISTENER_CLASS = Reflection.getClassByNameWithoutException("io.papermc.paper.network.ChannelInitializeListener");
            ADD_LISTENER_METHOD = Reflection.getMethod(CHANNEL_INITIALIZE_LISTENER_HOLDER_CLASS, "addListener", null, Key.class, CHANNEL_INITIALIZE_LISTENER_CLASS);
            HAS_LISTENER_METHOD = Reflection.getMethod(CHANNEL_INITIALIZE_LISTENER_HOLDER_CLASS, "hasListener", null, Key.class);
            REMOVE_LISTENER_METHOD = Reflection.getMethod(CHANNEL_INITIALIZE_LISTENER_HOLDER_CLASS, "removeListener", null, Key.class);
        }
    }

    public static boolean canBeUsed() {
        //ProtocolSupport breaks it as their handlers would be added afterwards.
        return CHANNEL_INITIALIZE_LISTENER_HOLDER_CLASS != null
                && Bukkit.getPluginManager().getPlugin("ProtocolSupport") == null;
    }

    @Override
    public boolean isServerBound() {
        return true;
    }

    @Override
    public void inject() {
        Object channelInitializer = Proxy.newProxyInstance(PaperChannelInjector.class.getClassLoader(),
                new Class[]{CHANNEL_INITIALIZE_LISTENER_CLASS},
                (proxy, m, args) -> {
                    if (m.getName().equals("afterInitChannel")) {
                        Channel channel = (Channel) args[0];
                        ServerConnectionInitializer.initChannel(channel, ConnectionState.HANDSHAKING);
                        return null;
                    }
                    return m.invoke(proxy, args);
                });
        try {
            Key key = Key.key(PacketEvents.IDENTIFIER, "injector");
            ADD_LISTENER_METHOD.invoke(null, key, channelInitializer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uninject() {
        Key key = Key.key(PacketEvents.IDENTIFIER, "injector");
        try {
            REMOVE_LISTENER_METHOD.invoke(null, key);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
