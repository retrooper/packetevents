/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2022 ViaVersion and contributors
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
//Thanks to ViaVersion for this bungeecord injector
package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handlers.PacketDecoder;
import io.github.retrooper.packetevents.handlers.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BungeePipelineInjector implements ChannelInjector {
    private static final Field LISTENERS_FIELD;

    static {
        Field tempField = null;
        try {
            tempField = ProxyServer.getInstance().getClass().getDeclaredField("listeners");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        LISTENERS_FIELD = tempField;
        LISTENERS_FIELD.setAccessible(true);
    }

    private final List<ChannelFuture> injectedFutures = new ArrayList<>();

    @Override
    public @Nullable ConnectionState getConnectionState(ChannelAbstract ch) {
        Channel channel = (Channel) ch.rawChannel();
        PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        return decoder.user.getConnectionState();
    }

    @Override
    public void changeConnectionState(ChannelAbstract ch, @Nullable ConnectionState packetState) {
        Channel channel = (Channel) ch.rawChannel();
        PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        decoder.user.setConnectionState(packetState);
    }

    public void injectChannel(Channel channel) {
        channel.pipeline().addFirst(PacketEvents.CONNECTION_NAME,
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Channel channel = (Channel) msg;
                        channel.pipeline().addLast(PacketEvents.SERVER_CHANNEL_HANDLER_NAME, new PreChannelInitializer());
                        super.channelRead(ctx, msg);
                    }
                });

       /*
        List<Object> networkManagers = SpigotReflectionUtil.getNetworkManagers();
        synchronized (networkManagers) {
            for (Object networkManager : networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel.isOpen()) {
                    if (channel.localAddress().equals(future.channel().localAddress())) {
                        channel.close();
                    }
                }
            }
        }*/
        //injectedFutures.add(future);
    }

    @Override
    public void inject() {
        try {
            Set<Channel> listeners = (Set<Channel>) LISTENERS_FIELD.get(ProxyServer.getInstance());
            Set<Channel> wrapper = new SetWrapper<>(listeners, channel -> {
                injectChannel(channel);
            });
            LISTENERS_FIELD.set(ProxyServer.getInstance(), wrapper);

            for (Channel channel : listeners) {
                injectChannel(channel);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Object p, @Nullable ConnectionState connectionState) {
        ProxiedPlayer player = (ProxiedPlayer) p;
        Channel channel = (Channel) PacketEvents.getAPI().getPlayerManager().getChannel(player).rawChannel();
        PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        decoder.player = player;
        decoder.user.getProfile().setUUID(player.getUniqueId());
        decoder.user.getProfile().setName(player.getName());
        if (connectionState != null) {
            decoder.user.setConnectionState(connectionState);
        }
        PacketEncoder encoder = (PacketEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
        encoder.player = player;
    }

    @Override
    public void updateUser(ChannelAbstract ch, User user) {
        Channel channel = (Channel) ch.rawChannel();
        PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        decoder.user = user;
        PacketEncoder encoder = (PacketEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
        encoder.user = user;
    }

    @Override
    public void ejectPlayer(Object player) {
        Channel channel = (Channel) PacketEvents.getAPI().getPlayerManager().getChannel(player).rawChannel();
        if (channel != null) {
            ServerConnectionInitializer.destroyChannel(channel);
        }
    }

    @Override
    public boolean hasInjected(Object player) {
        Channel channel = (Channel) PacketEvents.getAPI().getPlayerManager().getChannel(player).rawChannel();
        PacketDecoder decoder = (PacketDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        PacketEncoder encoder = (PacketEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
        return decoder != null && encoder != null
                && decoder.player != null && encoder.player != null;
    }
}
