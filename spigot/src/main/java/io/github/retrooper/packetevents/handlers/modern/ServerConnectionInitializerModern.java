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
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.NoSuchElementException;


public class ServerConnectionInitializerModern {
    //TODO Only inject Epoll & NioSocketChannels(check v1.8 packetevents)
    public static void postInitChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        User user = new User(channel, connectionState, null, new UserProfile(null, null));
        ProtocolManager.USERS.put(channel, user);
        try {
            channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, new PacketDecoderModern(user));
        }
        catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(channel);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
        PacketEncoderModern encoder = new PacketEncoderModern(user);
        ChannelHandler vanillaEncoder = channel.pipeline().get(PacketEvents.ENCODER_NAME);
        encoder.wrappedEncoder = (MessageToByteEncoder<?>) vanillaEncoder;
        //TODO Test new encoder stuff on multiple packetevents instances, and add same code to legacy initializer
        if (ViaVersionUtil.isAvailable()
                && ViaVersionUtil.getBukkitEncodeHandlerClass().equals(vanillaEncoder.getClass())) {
            //Read the minecraft encoder stored in ViaVersion's encoder.
            encoder.vanillaEncoder = new ReflectionObject(encoder.wrappedEncoder)
                    .read(0, MessageToByteEncoder.class);
        } else if (ClassUtil.getClassSimpleName(encoder.wrappedEncoder.getClass()).equals("PacketEncoderModern")) {
            ReflectionObject reflectEncoder = new ReflectionObject(encoder.wrappedEncoder);
            List<MessageToByteEncoder<?>> encoders = reflectEncoder.readList(0);
            encoders.add(encoder);
        } else {
            encoder.vanillaEncoder = encoder.wrappedEncoder;
        }
        //Replace "encoder" with "encoder"
        channel.pipeline().replace(PacketEvents.ENCODER_NAME, PacketEvents.ENCODER_NAME, encoder);
    }

    public static void postDestroyChannel(Object ch) {
        Channel channel = (Channel) ch;
        //TODO Confirm this decoder part still works, as the encoder part works.
        ChannelHandler viaDecoder = channel.pipeline().get("decoder");
        if (ViaVersionUtil.isAvailable() && ViaVersionUtil.getBukkitDecodeHandlerClass().equals(viaDecoder.getClass())) {
            ReflectionObject reflectViaDecoder = new ReflectionObject(viaDecoder);
            ByteToMessageDecoder decoder = reflectViaDecoder.readObject(0, ByteToMessageDecoder.class);
            if (decoder instanceof PacketDecoderModern) {
                PacketDecoderModern decoderModern = (PacketDecoderModern) decoder;
                //No decoders injected into our decoder
                if (decoderModern.decoders.isEmpty()) {
                    reflectViaDecoder.write(ByteToMessageDecoder.class, 0, decoderModern.mcDecoder);
                }
                //Some decoders injected into our decoder, lets do some cleaning up
                else {
                    //This decoder will now be injected into ViaVersion
                    ByteToMessageDecoder newDecoderModern = decoderModern.decoders.get(0);
                    ReflectionObject reflectNewDecoderModern = new ReflectionObject(newDecoderModern);
                    decoderModern.decoders.remove(0);

                    //Write custom decoders
                    reflectNewDecoderModern.writeList(0, decoderModern.decoders);

                    //Write mc decoder
                    reflectNewDecoderModern.write(ByteToMessageDecoder.class, 0, decoderModern.mcDecoder);

                    //TODO Write user, and check whats left
                    reflectNewDecoderModern.write(User.class, 0, decoderModern.user);

                    //Write player
                    reflectNewDecoderModern.write(Player.class, 0, decoderModern.player);

                    //Write handledCompression
                    reflectNewDecoderModern.write(boolean.class, 0, decoderModern.handledCompression);

                    //Write skipDoubleTransform
                    reflectNewDecoderModern.write(boolean.class, 1, decoderModern.skipDoubleTransform);
                }
            } else if (ClassUtil.getClassSimpleName(decoder.getClass()).equals("PacketDecoderModern")) {
                //Possibly another instance of packetevents has already injected into ViaVersion.
                //Let us try to remove our own decoder without breaking the other instance.
                ReflectionObject reflectDecoder = new ReflectionObject(decoder);
                List<Object> decoders = reflectDecoder.readList(0);
                int targetIndex = -1;
                for (int i = 0; i < decoders.size(); i++) {
                    if (decoders.get(i) instanceof PacketDecoderModern) {
                        targetIndex = i;
                    }
                }
                if (targetIndex != -1) {
                    decoders.remove(targetIndex);
                    reflectDecoder.writeList(0, decoders);
                }
            } else {
                //ViaVersion is present, we didn't inject into ViaVersion yet, because we haven't needed to.
                channel.pipeline().remove(PacketEvents.DECODER_NAME);
            }
        } else {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
        }


        ChannelHandler encoder = channel.pipeline().get(PacketEvents.ENCODER_NAME);
        if (encoder instanceof PacketEncoderModern) {
            PacketEncoderModern encoderModern = (PacketEncoderModern) encoder;
            channel.pipeline().replace(PacketEvents.ENCODER_NAME, PacketEvents.ENCODER_NAME, encoderModern.wrappedEncoder);
        } else if (ClassUtil.getClassSimpleName(encoder.getClass()).equals("PacketEncoderModern")) {
            //Possibly another packetevents instance
            ReflectionObject reflectEncoder = new ReflectionObject(encoder);
            List<Object> encoders = reflectEncoder.readList(0);
            encoders.removeIf(e -> e instanceof PacketEncoderModern);
        }
    }
}
