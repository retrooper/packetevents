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

package io.github.retrooper.packetevents.handlers.legacy.early;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketCompressor;
import io.github.retrooper.packetevents.handlers.legacy.PacketDecoderLegacy;
import io.github.retrooper.packetevents.handlers.legacy.PacketEncoderLegacy;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;

public class CompressionManagerLegacy {
    public static boolean refactorHandlers(ChannelHandlerContext ctx, ByteBuf buf, ByteBuf decompressed) {
        try {
            buf.clear().writeBytes(decompressed);
        } finally {
            decompressed.release();
        }
        PacketEncoderLegacy encoder = (PacketEncoderLegacy) ctx.pipeline().get(PacketEvents.ENCODER_NAME);
        PacketDecoderLegacy decoder = (PacketDecoderLegacy) ctx.pipeline().get(PacketEvents.DECODER_NAME);
        ctx.pipeline().remove(encoder);
        ctx.pipeline().remove(decoder);
        ctx.pipeline().addAfter("compress", PacketEvents.ENCODER_NAME, encoder);
        ctx.pipeline().addAfter("decompress", PacketEvents.DECODER_NAME, decoder);
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
