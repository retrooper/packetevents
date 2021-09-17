/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.handlers.compression.CompressionManager;
import io.github.retrooper.packetevents.handlers.compression.CustomPacketDecompressor;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionUtil;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextAbstract;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.util.List;

@ChannelHandler.Sharable
public class PacketDecoderModern extends MessageToMessageDecoder<ByteBuf> {
    public volatile Player player;
    public ConnectionState connectionState = ConnectionState.HANDSHAKING;
    private boolean handledCompression;
    private boolean skipDoubleTransform;
    public boolean handleViaVersion = false;
    public Object viaUserConnectionObj;

    public void handle(ChannelHandlerContextAbstract ctx, ByteBufAbstract byteBuf, List<Object> output) throws Exception {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            output.add(byteBuf.retain().rawByteBuf());
        }
        boolean needsCompress = handleCompressionOrder(ctx, byteBuf);
        int firstReaderIndex = byteBuf.readerIndex();
        PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(ctx.channel(), player, byteBuf);
        int readerIndex = byteBuf.readerIndex();
        PacketEvents.get().getEventManager().callEvent(packetReceiveEvent, () -> {
            byteBuf.readerIndex(readerIndex);
        });
        if (!packetReceiveEvent.isCancelled()) {
            //A wrapper has been used
            if (packetReceiveEvent.getCurrentPacketWrapper() != null) {
                //Rewrite the bytebuf with the content of the wrapper (modify the packet)
                packetReceiveEvent.getByteBuf().clear();
                packetReceiveEvent.getCurrentPacketWrapper().writeVarInt(packetReceiveEvent.getPacketID());
                packetReceiveEvent.getCurrentPacketWrapper().writeData();
            }
            byteBuf.readerIndex(firstReaderIndex);
            if (needsCompress) {
                CompressionManager.recompress(ctx, byteBuf);
                skipDoubleTransform = true;
            }
            output.add(byteBuf.retain().rawByteBuf());
        }
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(byteBuf);
        if (handleViaVersion) {
            if (!ViaVersionUtil.checkServerboundPacketUserConnection(viaUserConnectionObj)) {
                byteBuf.clear();
                throw ViaVersionUtil.throwCancelEncoderException(null);
            }
            if (ViaVersionUtil.isUserConnectionActive(viaUserConnectionObj)) {
                //Transform the packet
                //int preID = PacketWrapperUtil.readVarInt(ByteBufAbstract.generate(byteBuf.copy()));
                ByteBuf updated = ctx.alloc().buffer().writeBytes(byteBuf);
                ViaVersionUtil.transformPacket(viaUserConnectionObj, updated, true);
                transformedBuf.clear().writeBytes(updated);
                //int postID = PacketWrapperUtil.readVarInt((ByteBufAbstract.generate(updated).copy()));
                /*
                if (postID == 0x05) {
                    System.out.println("PRE ID: " + preID + ", POST ID: " + postID);
                    PacketWrapper<?> packetWrapper = new PacketWrapper<>(ClientVersion.UNKNOWN, PacketEvents.get().getServerManager().getVersion(),
                            ByteBufAbstract.generate(updated), postID);
                    String locale = packetWrapper.readString(16);
                    byte vd = packetWrapper.readByte();
                    int chatModeIndex = packetWrapper.readVarInt();
                    boolean chatColors = packetWrapper.readBoolean();
                    short displayedSkinPartsMask = packetWrapper.readUnsignedByte();
                    int mainHandIndex = packetWrapper.readVarInt();
                    boolean dtf = packetWrapper.readBoolean();
                    System.out.println("LOCALE: " + locale + ", vd: " + vd + ", chat mode index: " + chatModeIndex + ", chat colors: " + chatColors +
                            ", displayed skin parts mask: " + displayedSkinPartsMask + ", main hand inex: " + mainHandIndex + ", dtf: " + dtf);
                    System.out.println("REMAINING BYTES: " + updated.readableBytes());
                    //Client settings
                    System.exit(0);
                }*/
            }
        }
        try {
            handle(ChannelHandlerContextAbstract.generate(ctx), ByteBufAbstract.generate(transformedBuf), out);
        } finally {
            transformedBuf.release();
        }
    }

    private boolean handleCompressionOrder(ChannelHandlerContextAbstract ctx, ByteBufAbstract buf) {
        if (handledCompression) return false;

        int decoderIndex = ctx.pipeline().names().indexOf("decompress");
        if (decoderIndex == -1) return false;
        handledCompression = true;
        if (decoderIndex > ctx.pipeline().names().indexOf(PacketEvents.get().decoderName)) {
            // Need to decompress this packet due to bad order
            ByteBufAbstract decompressed = CustomPacketDecompressor.decompress(ctx, buf);
            return CompressionManager.refactorHandlers(ctx, buf, decompressed);
        }
        return false;
    }
}
