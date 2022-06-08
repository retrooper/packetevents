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

package io.github.retrooper.packetevents.injector.connection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import com.github.retrooper.packetevents.util.reflection.ClassUtil;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;
import java.util.NoSuchElementException;


public class ServerConnectionInitializer {

    private static void destroyHandlers(Channel channel) {
        channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.DECODER_NAME);
        if (decoder != null && ClassUtil.getClassSimpleName(decoder.getClass()).equals("PacketEventsDecoder")) {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
        } else if (ViaVersionUtil.isAvailable()) {
            decoder = channel.pipeline().get("decoder");
            if (ViaVersionUtil.getBukkitDecodeHandlerClass().equals(decoder.getClass())) {
                ReflectionObject reflectMCDecoder = new ReflectionObject(decoder);
                ByteToMessageDecoder injectedDecoder = reflectMCDecoder.readObject(0, ByteToMessageDecoder.class);
                if (injectedDecoder == null) {
                    return;
                }
                //We are the father decoder
                if (injectedDecoder instanceof PacketEventsDecoder) {
                    //Since we are the father, we can just hop out of Via's handler.
                    reflectMCDecoder.write(ByteToMessageDecoder.class, 0, injectedDecoder);
                } else if (ClassUtil.getClassSimpleName(injectedDecoder.getClass()).equals("PacketEventsDecoder")) {
                    //Some other packetevents instance already injected. Let us find our child decoder somewhere in here.
                    ReflectionObject reflectInjectedDecoder = new ReflectionObject(injectedDecoder);
                    List<Object> decoders = reflectInjectedDecoder.readList(0);
                    decoders.removeIf(o -> o instanceof PacketEventsDecoder);
                }
            }
        }
    }

    public static void initChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        if (!(channel instanceof EpollSocketChannel) && !(channel instanceof NioSocketChannel)) {
            return;
        }

        User user = new User(channel, connectionState, null, new UserProfile(null, null));

        synchronized (channel) {
            UserConnectEvent connectEvent = new UserConnectEvent(user);
            PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
            if (connectEvent.isCancelled()) {
                channel.unsafe().closeForcibly();
                return;
            }

            channel.closeFuture().addListener((ChannelFutureListener) future -> PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
            ProtocolManager.USERS.put(channel, user);
        }

        try {
            PacketEventsDecoder decoder = new PacketEventsDecoder(user);
            channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, decoder);
        } catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(channel);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }

        PacketEventsEncoder encoder = new PacketEventsEncoder(user);
        ChannelHandler vanillaEncoder = channel.pipeline().get("encoder");

        if (ViaVersionUtil.isAvailable() && ViaVersionUtil.getBukkitEncodeHandlerClass().equals(vanillaEncoder.getClass())) {
            //Read the minecraft encoder stored in ViaVersion's encoder.
            encoder.vanillaEncoder = new ReflectionObject(vanillaEncoder)
                    .read(0, MessageToByteEncoder.class);
        } else {
            encoder.vanillaEncoder = (MessageToByteEncoder<?>) vanillaEncoder;
        }

        channel.pipeline().addAfter("encoder", PacketEvents.ENCODER_NAME, encoder);
    }

    public static void destroyChannel(Object ch) {
        Channel channel = (Channel) ch;
        if (!(channel instanceof EpollSocketChannel) &&
                !(channel instanceof NioSocketChannel)) {
            return;
        }

        synchronized (channel) {
            User user = ProtocolManager.USERS.get(channel);
            if (user == null) {
                return;
            }

            UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
            PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);

            destroyHandlers(channel);
            ProtocolManager.USERS.remove(channel);
        }
    }
}
