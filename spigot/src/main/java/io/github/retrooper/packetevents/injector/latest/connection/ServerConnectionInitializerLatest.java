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

package io.github.retrooper.packetevents.injector.latest.connection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.reflection.ClassUtil;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.injector.latest.handlers.PacketDecoderLatest;
import io.github.retrooper.packetevents.injector.latest.handlers.PacketEncoderLatest;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.NoSuchElementException;


public class ServerConnectionInitializerLatest {
    public static void initChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        if (!(channel instanceof EpollSocketChannel) &&
                !(channel instanceof NioSocketChannel)) {
            return;
        }
        if (channel.pipeline().get(PacketEvents.DECODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
            //TODO This is a reload, so do we call userdisconnectevent, will this call connectevent for the second time now?
        }
        if (channel.pipeline().get(PacketEvents.ENCODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        }
        User user = new User(channel, connectionState, null, new UserProfile(null, null));
        UserConnectEvent connectEvent = new UserConnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
        if (connectEvent.isCancelled()) {
            channel.unsafe().closeForcibly();
            return;
        }
        ProtocolManager.USERS.put(channel, user);
        try {
            channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, new PacketDecoderLatest(user));
        } catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(channel);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
        PacketEncoderLatest encoder = new PacketEncoderLatest(user);
        ChannelHandler vanillaEncoder = channel.pipeline().get("encoder");
        if (ViaVersionUtil.isAvailable()
                && ViaVersionUtil.getBukkitEncodeHandlerClass().equals(vanillaEncoder.getClass())) {
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
        User user = ProtocolManager.USERS.get(channel);
        if (user == null) {
            return;
        }
        UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);

        channel.pipeline().remove(PacketEvents.DECODER_NAME);
        channel.pipeline().remove(PacketEvents.ENCODER_NAME);

        ProtocolManager.USERS.remove(channel);
    }
}
