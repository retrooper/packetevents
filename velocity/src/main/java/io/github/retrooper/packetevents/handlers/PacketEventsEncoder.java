/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.config.ProxyConfig;
import com.velocitypowered.natives.compression.VelocityCompressor;
import com.velocitypowered.natives.util.MoreByteBufUtils;
import com.velocitypowered.natives.util.Natives;
import io.github.retrooper.packetevents.injector.VelocityPipelineInjector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class PacketEventsEncoder extends MessageToByteEncoder<ByteBuf> {
    public Player player;
    public User user;
    public int compressionThreshold;
    public VelocityCompressor compressor;
    public boolean compressPackets;
    public boolean suppressRemoval;

    public PacketEventsEncoder(User user) {
        this.user = user;
        // TODO: find ProxyConfig instance in more appropriate way?
        VelocityPipelineInjector injector = (VelocityPipelineInjector) PacketEvents.getAPI().getInjector();
        ProxyConfig config = injector.getServer().getConfiguration();

        this.compressionThreshold = config.getCompressionThreshold();
        if (this.compressionThreshold != -1) {
            this.compressor = Natives.compress.get().create(config.getCompressionLevel());
        }
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        int firstReaderIndex = buffer.readerIndex();

        boolean rewriteSize = false;
        if (this.user.getEncoderState() == ConnectionState.STATUS) {
            int frameSize = ByteBufHelper.readVarInt(buffer);
            rewriteSize = frameSize > 0 && frameSize == buffer.readableBytes();
            if (!rewriteSize) {
                buffer.readerIndex(firstReaderIndex);
            }
        }

        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player, buffer,
                false);
        int readerIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> buffer.readerIndex(readerIndex));
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                if (rewriteSize) {
                    buffer.writeMedium(0);
                }

                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().write();

                if (rewriteSize) {
                    int frameSize = buffer.readableBytes() - 3;
                    buffer.setMedium(0, (frameSize & 0x7F | 0x80) << 16 | ((frameSize >>> 7) & 0x7F | 0x80) << 8 | (frameSize >>> 14));
                }
            }
            buffer.readerIndex(firstReaderIndex);
        } else {
            ByteBufHelper.clear(packetSendEvent.getByteBuf());
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (!msg.isReadable()) return;

        // TODO: simplify this
        // FastPrepareAPI is injected, completly re-encode packets.
        if (ctx.pipeline().context("fastprepare-encoder") != null) {
            // As FastPrepareAPI can send multiple packets at a single 'encode', we should process them all
            while (msg.isReadable()) {
                int size = ByteBufHelper.readVarInt(msg);

                // Must be a compressed payload
                if (this.compressPackets && this.user.getEncoderState() == ConnectionState.LOGIN) {
                    // TODO: decompress this packet
                    size -= 1;
                    msg.readByte(); // skip 'uncompressed' byte
                }

                ByteBuf transformed = ctx.alloc().buffer(size).writeBytes(msg, size);
                try {
                    read(ctx, transformed);
                    if (!transformed.isReadable()) {
                        continue;
                    }

                    if (this.compressPackets && this.compressor != null) {
                        int uncompressed = transformed.readableBytes();
                        if (uncompressed < this.compressionThreshold) {
                            ByteBufHelper.writeVarInt(out, uncompressed + 1);
                            ByteBufHelper.writeVarInt(out, 0);
                            out.writeBytes(transformed);
                        } else {
                            int frameIndex = out.writerIndex();
                            out.writeMedium(0);
                            ByteBufHelper.writeVarInt(out, uncompressed);
                            ByteBuf compatible = MoreByteBufUtils.ensureCompatible(ctx.alloc(), this.compressor, transformed);

                            try {
                                this.compressor.deflate(compatible, out);
                            } finally {
                                compatible.release();
                            }

                            int frameSize = (out.writerIndex() - frameIndex) - 3;
                            out.setMedium(frameIndex, (frameSize & 0x7F | 0x80) << 16 | ((frameSize >>> 7) & 0x7F | 0x80) << 8 | (frameSize >>> 14));
                        }
                    } else {
                        ByteBufHelper.writeVarInt(out, transformed.readableBytes());
                        out.writeBytes(transformed);
                    }
                } finally {
                    transformed.release();
                }
            }
            return;
        }

        ByteBuf transformed = ctx.alloc().buffer().writeBytes(msg);
        try {
            read(ctx, transformed);
            out.writeBytes(transformed);
        } finally {
            transformed.release();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (this.compressor != null && !this.suppressRemoval) {
            this.compressor.close();
            this.compressor = null;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

