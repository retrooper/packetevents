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

package io.github.retrooper.packetevents.handlers.compression;

import io.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.util.netty.channel.ChannelHandlerAbstract;
import com.github.retrooper.packetevents.util.netty.channel.ChannelHandlerContextAbstract;

public class CompressionManager {
    public static boolean refactorHandlers(ChannelHandlerContextAbstract ctx, ByteBufAbstract buf, ByteBufAbstract decompressed) {
        try {
            buf.clear().writeBytes(decompressed);
        } finally {
            decompressed.release();
        }
        ChannelHandlerAbstract encoder = ctx.pipeline().get(PacketEvents.get().encoderName);
        ChannelHandlerAbstract decoder = ctx.pipeline().get(PacketEvents.get().decoderName);
        ctx.pipeline().remove(encoder);
        ctx.pipeline().remove(decoder);
        ctx.pipeline().addAfter("compress", PacketEvents.get().encoderName, encoder);
        ctx.pipeline().addAfter("decompress", PacketEvents.get().decoderName, decoder);
        return true;
    }

    public static void recompress(ChannelHandlerContextAbstract ctx, ByteBufAbstract buf) {
        ByteBufAbstract compressed = CustomPacketCompressor.compress(ctx, buf);
        try {
            buf.clear().writeBytes(compressed);
        } finally {
            compressed.release();
        }
    }
}
