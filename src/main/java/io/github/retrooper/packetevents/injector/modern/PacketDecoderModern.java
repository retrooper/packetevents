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
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufModern;
import io.github.retrooper.packetevents.utils.wrapper.PacketWrapperUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.bukkit.entity.Player;

import java.util.Arrays;

@ChannelHandler.Sharable
public class PacketDecoderModern extends ChannelInboundHandlerAdapter {
    public volatile Player player;
    public ConnectionState connectionState = ConnectionState.HANDSHAKING;
    public boolean decompressHandlerPresent = false;

    public PacketDecoderModern() {

    }
    /*
        int firstReaderIndex = byteBuf.readerIndex();
        PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(ctx.channel(), player, byteBuf);
        int readerIndex = byteBuf.readerIndex();
        PacketEvents.get().getEventManager().callEvent(packetReceiveEvent, () -> {
        byteBuf.readerIndex(readerIndex);
        });
        byteBuf.readerIndex(firstReaderIndex);

           if (packetReceiveEvent.isCancelled()) {
               byteBuf.skipBytes(byteBuf.readableBytes());
               return;
           }*/


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        //TODO Work on this, this is just some random testing, please note these links
        //TODO https://wiki.vg/Protocol#Without_compression https://wiki.vg/Protocol#With_compression
        String before = Arrays.toString(ctx.channel().pipeline().names().stream().toArray());
        if (!decompressHandlerPresent && ctx.channel().pipeline().get("decompress") != null) {
            System.out.println("DECODER CHANNEL PIPELINE NAMES BEFORE: " + before);
            ctx.channel().pipeline().remove(PacketEvents.get().decoderName);
            decompressHandlerPresent = true;
            ctx.channel().pipeline().addAfter("decompress", PacketEvents.get().decoderName, this);
            System.out.println("DECODER EDITED CHANNEL PIPELINE NAMES: " + Arrays.toString(ctx.channel().pipeline().names().stream().toArray()));
        }
        int firstReaderIndex = byteBuf.readerIndex();
        if (decompressHandlerPresent) {
            int a = PacketWrapperUtils.readVarInt(new ByteBufModern(byteBuf));
            int b = PacketWrapperUtils.readVarInt(new ByteBufModern(byteBuf));
            //System.out.println("A: " + a +  "B: " + b);
        }
        else {
            PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(ctx.channel(), player, byteBuf, !decompressHandlerPresent);
            int readerIndex = byteBuf.readerIndex();
            PacketEvents.get().getEventManager().callEvent(packetReceiveEvent, () -> {
                byteBuf.readerIndex(readerIndex);
            });
        }
        byteBuf.readerIndex(firstReaderIndex);
      /*  int begin = byteBuf.readerIndex();
        int id = PacketWrapperUtils.readVarInt(new ByteBufModern(byteBuf));
        System.out.println("ID: " + id);
        byteBuf.readerIndex(begin);*/
        ctx.fireChannelRead(byteBuf);
    }
}
