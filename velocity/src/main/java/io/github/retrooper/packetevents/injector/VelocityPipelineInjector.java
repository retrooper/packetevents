/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.retrooper.packetevents.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.handlers.PacketEventsEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class VelocityPipelineInjector implements ChannelInjector {
    private static Class<?> CONNECTION_MANAGER_CLASS, SERVER_INITIALIZER_HOLDER_CLASS, BACKEND_INITIALIZER_HOLDER_CLASS;
    private static Method SET_SERVER_INTIIALIZER, SET_BACKEND_INITIALIZER;
    private final ProxyServer server;

    public VelocityPipelineInjector(ProxyServer server) {
        this.server = server;
    }

    @Override
    public boolean isServerBound() {
        return true;
    }

    @Override
    public void inject() {
        if (CONNECTION_MANAGER_CLASS == null) {
            CONNECTION_MANAGER_CLASS = Reflection.getClassByNameWithoutException("com.velocitypowered.proxy.network.ConnectionManager");
            SERVER_INITIALIZER_HOLDER_CLASS = Reflection.getClassByNameWithoutException("com.velocitypowered.proxy.network.ServerChannelInitializerHolder");
            BACKEND_INITIALIZER_HOLDER_CLASS = Reflection.getClassByNameWithoutException("com.velocitypowered.proxy.network.BackendChannelInitializerHolder");
            SET_SERVER_INTIIALIZER = Reflection.getMethod(SERVER_INITIALIZER_HOLDER_CLASS, 0, ChannelInitializer.class);
            SET_BACKEND_INITIALIZER = Reflection.getMethod(BACKEND_INITIALIZER_HOLDER_CLASS, 0, ChannelInitializer.class);
        }
        ReflectionObject reflectServer = new ReflectionObject(server);
        Object connectionManager = reflectServer.readObject(0, CONNECTION_MANAGER_CLASS);
        ReflectionObject reflectConnectionManager = new ReflectionObject(connectionManager);
        Object proxyInitializerHolder = reflectConnectionManager.readObject(0, SERVER_INITIALIZER_HOLDER_CLASS);
        ChannelInitializer<Channel> wrappedProxyInitializer = ((Supplier<ChannelInitializer<Channel>>) proxyInitializerHolder).get();
        VelocityChannelInitializer initializer = new VelocityChannelInitializer(wrappedProxyInitializer);
        try {
            SET_SERVER_INTIIALIZER.invoke(proxyInitializerHolder, initializer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //TODO Do we also register in backend initializer?
       /* Object backendInitializerHolder = reflectConnectionManager.readObject(0, BACKEND_INITIALIZER_HOLDER_CLASS);
        ChannelInitializer<Channel> wrappedBackendInitializer = ((Supplier<ChannelInitializer<Channel>>) backendInitializerHolder).get();
        try {
            SET_BACKEND_INITIALIZER.invoke(backendInitializerHolder, new VelocityChannelInitializer(wrappedBackendInitializer));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void uninject() {

    }

    @Override
    public void updateUser(Object channel, User user) {
        PacketEventsDecoder decoder = (PacketEventsDecoder) ((Channel) channel).pipeline().get(PacketEvents.DECODER_NAME);
        decoder.user = user;

        PacketEventsEncoder encoder = (PacketEventsEncoder) ((Channel) channel).pipeline().get(PacketEvents.ENCODER_NAME);
        encoder.user = user;
    }

    @Override
    public void setPlayer(Object ch, Object p) {
        Channel channel = (Channel) ch;
        Player player = (Player) p;
        PacketEventsDecoder decoder = (PacketEventsDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        decoder.player = player;
        decoder.user.getProfile().setUUID(player.getUniqueId());
        decoder.user.getProfile().setName(player.getUsername());
        PacketEventsEncoder encoder = (PacketEventsEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
        encoder.player = player;
    }

    @Override
    public boolean isProxy() {
        return true;
    }
}
