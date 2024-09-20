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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.github.retrooper.packetevents.injector.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.PromiseCombiner;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

// Thanks to ViaVersion for the compression method.
@ChannelHandler.Sharable
public class PacketEventsEncoder extends ChannelOutboundHandlerAdapter {

    private static final Recycler<OutList> OUT_LIST_RECYCLER = new Recycler<OutList>() {
        @Override
        protected OutList newObject(Handle<OutList> handle) {
            return new OutList(handle);
        }
    };

    public ProxiedPlayer player;
    public User user;
    public boolean handledCompression;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer, ChannelPromise promise) throws Exception {
        boolean doCompression = handleCompressionOrder(ctx, buffer);
        int firstReaderIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player,
                buffer, false);
        int readerIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> buffer.readerIndex(readerIndex));
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().write();
            } else {
                buffer.readerIndex(firstReaderIndex);
            }
            if (doCompression) {
                this.recompress(ctx, buffer, promise);
            } else {
                ctx.write(buffer, promise);
            }
        } else {
            ReferenceCountUtil.release(packetSendEvent.getByteBuf());
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            super.write(ctx, msg, promise);
            return;
        }
        ByteBuf buf = (ByteBuf) msg;
        if (!buf.isReadable()) {
            buf.release();
        } else {
            this.read(ctx, buf, promise);
        }
    }

    private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        ChannelPipeline pipe = ctx.pipeline();
        if (handledCompression) {
            return false;
        }
        int encoderIndex = pipe.names().indexOf("compress");
        if (encoderIndex == -1) {
            return false;
        }
        if (encoderIndex > pipe.names().indexOf(PacketEvents.ENCODER_NAME)) {
            // Need to decompress this packet due to bad order
            ChannelHandler decompressor = pipe.get("decompress");
            try {
                ByteBuf decompressed = (ByteBuf) CustomPipelineUtil.callPacketDecodeByteBuf(decompressor, ctx, buffer).get(0);
                if (buffer != decompressed) {
                    try {
                        buffer.clear().writeBytes(decompressed);
                    } finally {
                        decompressed.release();
                    }
                }
                //Relocate handlers
                PacketEventsDecoder decoder = (PacketEventsDecoder) pipe.remove(PacketEvents.DECODER_NAME);
                PacketEventsEncoder encoder = (PacketEventsEncoder) pipe.remove(PacketEvents.ENCODER_NAME);
                pipe.addAfter("decompress", PacketEvents.DECODER_NAME, decoder);
                pipe.addAfter("compress", PacketEvents.ENCODER_NAME, encoder);
                //System.out.println("Pipe: " + ChannelHelper.pipelineHandlerNamesAsString(ctx.channel()));
                handledCompression = true;
                return true;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void recompress(ChannelHandlerContext ctx, ByteBuf buffer, ChannelPromise promise) {
        OutList outWrapper = OUT_LIST_RECYCLER.get();
        List<Object> out = outWrapper.list;
        try {
            ChannelHandler compressor = ctx.pipeline().get("compress");
            CustomPipelineUtil.callPacketEncodeByteBuf(compressor, ctx, buffer, out);

            int len = out.size();
            if (len == 1) {
                // should be the only case which
                // happens on vanilla bungeecord
                ctx.write(out.get(0), promise);
            } else {
                // copied from MessageToMessageEncoder#writePromiseCombiner
                PromiseCombiner combiner = new PromiseCombiner(ctx.executor());
                for (int i = 0; i < len; i++) {
                    combiner.add(ctx.write(out.get(i)));
                }
                combiner.finish(promise);
            }
        } catch (InvocationTargetException exception) {
            throw new EncoderException("Error while recompressing bytebuf " + buffer.readableBytes(), exception);
        } finally {
            outWrapper.handle.recycle(outWrapper);
        }
    }

    private static final class OutList {

        // the default bungee compressor only produces one output bytebuf
        private final List<Object> list = new ArrayList<>(1);
        private final Recycler.Handle<OutList> handle;

        public OutList(Recycler.Handle<OutList> handle) {
            this.handle = handle;
        }
    }
}
