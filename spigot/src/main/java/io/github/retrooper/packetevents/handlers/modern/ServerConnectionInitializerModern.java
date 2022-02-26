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

package io.github.retrooper.packetevents.handlers.modern;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.reflection.ClassUtil;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.NoSuchElementException;


public class ServerConnectionInitializerModern {
    private static Constructor<?> BUKKIT_ENCODE_HANDLER_CONSTRUCTOR;
    private static Constructor<?> VANILLA_ENCODE_HANDLER_CONSTRUCTOR;

    public static void initChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        if (!(channel instanceof EpollSocketChannel) &&
                !(channel instanceof NioSocketChannel)) {
            return;
        }
        User user = new User(channel, connectionState, null, new UserProfile(null, null));
        ProtocolManager.USERS.put(channel, user);
        try {
            channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, new PacketDecoderModern(user));
        } catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(channel);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
        PacketEncoderModern encoder = new PacketEncoderModern(user);
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
        ChannelHandler viaDecoder = channel.pipeline().get("decoder");
        if (ViaVersionUtil.isAvailable() && ViaVersionUtil.getBukkitDecodeHandlerClass().equals(viaDecoder.getClass())) {
            ReflectionObject reflectViaDecoder = new ReflectionObject(viaDecoder);
            ByteToMessageDecoder decoder = reflectViaDecoder.readObject(0, ByteToMessageDecoder.class);
            //We are the father decoder.(but child of ViaVersion's decoder)
            if (decoder instanceof PacketDecoderModern) {
                PacketDecoderModern decoderModern = (PacketDecoderModern) decoder;
                //No decoders injected into our decoder.
                //We can just let Via wrap the vanilla decoder again.
                if (decoderModern.decoders.isEmpty()) {
                    reflectViaDecoder.write(ByteToMessageDecoder.class, 0, decoderModern.mcDecoder);
                }
                //Some decoders injected into our decoder, lets do some cleaning up
                else {
                    //Elect a new father decoder. They will now manage the rest of the packetevents instances.
                    ByteToMessageDecoder newDecoderModern = decoderModern.decoders.get(0);
                    //Copy our decoder's data into there.
                    ReflectionObject reflectNewDecoderModern = new ReflectionObject(newDecoderModern);
                    decoderModern.decoders.remove(0);
                    reflectNewDecoderModern.writeList(0, decoderModern.decoders);
                    reflectNewDecoderModern.write(ByteToMessageDecoder.class, 0, decoderModern.mcDecoder);
                    reflectNewDecoderModern.write(User.class, 0, decoderModern.user);
                    reflectNewDecoderModern.write(Player.class, 0, decoderModern.player);
                    reflectNewDecoderModern.write(boolean.class, 0, decoderModern.handledCompression);
                    reflectNewDecoderModern.write(boolean.class, 1, decoderModern.skipDoubleTransform);
                    //Force via to now wrap this new father decoder.
                    reflectViaDecoder.write(ByteToMessageDecoder.class, 0, newDecoderModern);
                }
            } else if (ClassUtil.getClassSimpleName(decoder.getClass()).equals("PacketDecoderModern")) {
                //Possibly another instance of packetevents has already injected into ViaVersion.
                //Let us try to remove our own decoder without breaking the other instance.
                ReflectionObject reflectDecoder = new ReflectionObject(decoder);
                List<Object> decoders = reflectDecoder.readList(0);
                decoders.removeIf(d -> d instanceof PacketDecoderModern);
            } else {
                //ViaVersion is present, we didn't inject into ViaVersion yet, because we haven't needed to.
                channel.pipeline().remove(PacketEvents.DECODER_NAME);
            }
        } else {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
        }

        //Easily cleanup the encoder
        channel.pipeline().remove(PacketEvents.ENCODER_NAME);
    }
}
