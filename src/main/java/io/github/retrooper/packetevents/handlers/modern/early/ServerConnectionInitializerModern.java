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

package io.github.retrooper.packetevents.handlers.modern.early;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.CustomBukkitDecodeHandler;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ServerConnectionInitializerModern {
    public static void postInitChannel(Channel channel) {
        channel.pipeline().addAfter("splitter", PacketEvents.get().decoderName, new PacketDecoderModern());
        channel.pipeline().addBefore("encoder", PacketEvents.get().encoderName, new PacketEncoderModern());
    }

    public static void postDestroyChannel(Channel channel) {
        ChannelHandler mcDecoder = channel.pipeline().get("decoder");
        if (mcDecoder instanceof CustomBukkitDecodeHandler) {
            //Convert back to the original ViaVersion decoder
            CustomBukkitDecodeHandler customBukkitDecodeHandler = (CustomBukkitDecodeHandler) mcDecoder;
            channel.pipeline().replace("decoder", "decoder", customBukkitDecodeHandler.oldBukkitDecodeHandler);
            ChannelHandler protocolLibDecoder = channel.pipeline().get("protocol_lib_decoder");
            if (protocolLibDecoder != null) {
                //Reflect the ProtocolLib decoder
                ReflectionObject reflectProtocolLibDecoder = new ReflectionObject(protocolLibDecoder);
                //Correct the vanillaDecoder variable in the ProtocolLib decoder
                reflectProtocolLibDecoder.write(ByteToMessageDecoder.class, 0, customBukkitDecodeHandler.oldBukkitDecodeHandler);
                //Correct the decodeBuffer variable in ProtocolLib decoder
                Method minecraftDecodeMethod = Reflection.getMethod(customBukkitDecodeHandler.oldBukkitDecodeHandler.getClass(), "decode", 0);
                try {
                    Field decodeBufferField = protocolLibDecoder.getClass().getDeclaredField("decodeBuffer");
                    decodeBufferField.setAccessible(true);
                    decodeBufferField.set(protocolLibDecoder, minecraftDecodeMethod);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            channel.pipeline().remove(PacketEvents.get().decoderName);
            channel.pipeline().remove(PacketEvents.get().encoderName);
        }
    }
}
