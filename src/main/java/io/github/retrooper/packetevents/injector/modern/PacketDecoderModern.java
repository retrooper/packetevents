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

package io.github.retrooper.packetevents.injector.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ChannelHandler.Sharable
public class PacketDecoderModern extends MessageToMessageDecoder<ByteBuf> {
    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;
    private static Method MTM_DECODE;
    public volatile Player player;
    public ConnectionState connectionState = ConnectionState.HANDSHAKING;
    private boolean handledCompression;
    private boolean skipDoubleTransform;

    protected static void load() {
        if (DECODE_METHOD == null) {
            try {
                DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
                DECODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                ENCODE_METHOD = MessageToByteEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
                ENCODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                MTM_DECODE = MessageToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, Object.class, List.class);
                MTM_DECODE.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            out.add(byteBuf.retain());
            return;
        }
        ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(byteBuf);
        try {
            boolean needsCompress = handleCompressionOrder(ctx, transformedBuf);
            int firstReaderIndex = transformedBuf.readerIndex();
            PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(ctx.channel(), player, transformedBuf);
            int readerIndex = transformedBuf.readerIndex();
            PacketEvents.get().getEventManager().callEvent(packetReceiveEvent, () -> {
                transformedBuf.readerIndex(readerIndex);
            });
            transformedBuf.readerIndex(firstReaderIndex);
            if (needsCompress) {
                recompress(ctx, transformedBuf);
                skipDoubleTransform = true;
            }
            out.add(transformedBuf.retain());
        } finally {
            transformedBuf.release();
        }
    }

    protected static void callEncode(MessageToByteEncoder<?> encoder, ChannelHandlerContext ctx, Object msg, ByteBuf output) {
        load();
        try {
            ENCODE_METHOD.invoke(encoder, ctx, msg, output);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    protected static ByteBuf callDecode(ByteToMessageDecoder decoder, ChannelHandlerContext ctx, Object msg) {
        load();
        List<Object> output = new ArrayList<>();
        try {
            DECODE_METHOD.invoke(decoder, ctx, msg, output);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (ByteBuf) output.get(0);
    }

    protected static ByteBuf callDecode(MessageToMessageDecoder<?> decoder, ChannelHandlerContext ctx, Object msg) {
        load();
        List<Object> output = new ArrayList<>();
        try {
            MTM_DECODE.invoke(decoder, ctx, msg, output);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (ByteBuf) output.get(0);
    }

    protected static boolean refactorHandlers(ChannelHandlerContext ctx, ByteBuf buf, ByteBuf decompressed) {
        try {
            buf.clear().writeBytes(decompressed);
        } finally {
            decompressed.release();
        }
        ChannelHandler encoder = ctx.pipeline().get(PacketEvents.get().encoderName);
        ChannelHandler decoder = ctx.pipeline().get(PacketEvents.get().decoderName);
        ctx.pipeline().remove(encoder);
        ctx.pipeline().remove(decoder);
        ctx.pipeline().addAfter("compress", PacketEvents.get().encoderName, encoder);
        ctx.pipeline().addAfter("decompress", PacketEvents.get().decoderName, decoder);
        System.out.println("REFACTORING, PIPELINE NAMES: " + Arrays.toString(ctx.channel().pipeline().names().toArray(new String[0])));
        return true;
    }

    private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buf) {
        if (handledCompression) return false;

        int decoderIndex = ctx.pipeline().names().indexOf("decompress");
        if (decoderIndex == -1) return false;
        handledCompression = true;
        if (decoderIndex > ctx.pipeline().names().indexOf(PacketEvents.get().decoderName)) {
            // Need to decompress this packet due to bad order
            ByteBuf decompressed = callDecode((MessageToMessageDecoder<?>) ctx.pipeline().get("decompress"), ctx, buf);
            return refactorHandlers(ctx, buf, decompressed);
        }
        return false;
    }

    private void recompress(ChannelHandlerContext ctx, ByteBuf buf) {
        ByteBuf compressed = ctx.alloc().buffer();
        try {
            callEncode((MessageToByteEncoder<?>) ctx.pipeline().get("compress"), ctx, buf, compressed);
            buf.clear().writeBytes(compressed);
        } finally {
            compressed.release();
        }
    }
}
