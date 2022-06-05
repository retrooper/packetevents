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

package io.github.retrooper.packetevents.injector.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PacketEventsDecoder extends ByteToMessageDecoder {
    public User user;
    public volatile Player player;

    public ByteToMessageDecoder mcDecoder = null;
    public List<ByteToMessageDecoder> decoders = new ArrayList<>();
    public boolean handledCompression;
    public boolean skipDoubleTransform;

    public PacketEventsDecoder(User user) {
        this.user = user;
    }

    public PacketEventsDecoder(PacketEventsDecoder decoder) {
        user = decoder.user;
        player = decoder.player;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            out.add(input.retain());
        }
        ByteBuf outputBuffer = ctx.alloc().buffer().writeBytes(input);
        try {
            boolean doRecompression =
                    handleCompression(ctx, outputBuffer);
            PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, player, outputBuffer, true);
            if (outputBuffer.isReadable()) {
                if (doRecompression) {
                    ByteBuf temp = ctx.alloc().buffer();
                    compress(ctx, outputBuffer, temp);
                    try {
                        outputBuffer.clear().writeBytes(temp);
                    } finally {
                        temp.release();
                    }
                    skipDoubleTransform = true;
                }
                out.add(outputBuffer.retain());
            }
        } finally {
            outputBuffer.release();
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.isReadable()) {
            read(ctx, buffer, out);
            for (ByteToMessageDecoder decoder : decoders) {
                //Only support one output object
                if (!out.isEmpty()) {
                    Object input = out.get(0);
                    out.clear();
                    out.addAll(CustomPipelineUtil.callDecode(decoder, ctx, input));
                    ByteBufHelper.release(input); // Decode doesn't free, so we must do it
                }
            }
            if (mcDecoder != null && !out.isEmpty()) {
                //Call minecraft decoder to convert the ByteBuf to an NMS object for the next handlers
                try {
                    Object input = out.get(0);
                    out.clear();
                    out.addAll(CustomPipelineUtil.callDecode(mcDecoder, ctx, input));
                    ByteBufHelper.release(input); // Decode doesn't free, so we must do it
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //Check if the minecraft server will already print our exception for us.
        if (ExceptionUtil.isException(cause, PacketProcessException.class) && !SpigotReflectionUtil.isMinecraftServerInstanceDebugging()
                && (user == null || user.getConnectionState() != ConnectionState.HANDSHAKING)) {
            cause.printStackTrace();
        }
    }

    private void compress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) throws InvocationTargetException {
        ChannelHandler compressor = ctx.pipeline().get("compress");
        if (compressor != null) {
            CustomPipelineUtil.callEncode(compressor, ctx, input, output);
        }
    }

    private void decompress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) throws InvocationTargetException {
        ChannelHandler decompressor = ctx.pipeline().get("decompress");
        if (decompressor != null) {
            ByteBuf temp = (ByteBuf) CustomPipelineUtil.callDecode(decompressor, ctx, input).get(0);
            try {
                output.clear().writeBytes(temp);
            } finally {
                temp.release();
            }
        }
    }

    private boolean handleCompression(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        if (handledCompression) return false;
        int decompressIndex = ctx.pipeline().names().indexOf("decompress");
        if (decompressIndex == -1) return false;
        handledCompression = true;
        int peDecoderIndex = ctx.pipeline().names().indexOf(PacketEvents.DECODER_NAME);
        if (peDecoderIndex == -1) return false;
        if (decompressIndex > peDecoderIndex) {
            //We are ahead of the decompression handler (they are added dynamically) so let us relocate.
            //But first we need to compress the data and re-compress it after we do all our processing to avoid issues.
            decompress(ctx, buffer, buffer);
            //Let us relocate and no longer deal with compression.
            ChannelHandler encoder = ctx.pipeline().remove(PacketEvents.ENCODER_NAME);
            ctx.pipeline().addAfter("compress", PacketEvents.ENCODER_NAME, encoder);
            PacketEventsDecoder decoder = (PacketEventsDecoder) ctx.pipeline().remove(PacketEvents.DECODER_NAME);
            ctx.pipeline().addAfter("decompress", PacketEvents.DECODER_NAME, new PacketEventsDecoder(decoder));
            return true;
        }
        return false;
    }
}
