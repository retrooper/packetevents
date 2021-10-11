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

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;


public class ServerConnectionInitializerModern {
    public static void postInitChannel(Object ch) {
        Channel channel = (Channel) ch;
        channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, new PacketDecoderModern());
        channel.pipeline().addBefore("encoder", PacketEvents.ENCODER_NAME, new PacketEncoderModern());
    }

    public static void postDestroyChannel(Object ch) {
        Channel channel = (Channel) ch;
        ChannelHandler mcDecoder = channel.pipeline().get("decoder");
        if (ViaVersionUtil.getBukkitDecodeHandlerClass().isInstance(mcDecoder)) {
            ReflectionObject reflectMCDecoder = new ReflectionObject(mcDecoder);
            PacketDecoderModern decoder = (PacketDecoderModern) reflectMCDecoder.readObject(0, ByteToMessageDecoder.class);
            reflectMCDecoder.write(ByteToMessageDecoder.class, 0, decoder.mcDecoder);
        } else {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
            channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        }
    }
}
