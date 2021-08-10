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
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import io.github.retrooper.packetevents.packettype.PacketState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class PacketDecoderModern extends ByteToMessageDecoder {
    private static Method DECODE_METHOD;
    public final ByteToMessageDecoder previousDecoder;
    public volatile Player player;
    public PacketState packetState;

    private void load() {
        if (DECODE_METHOD == null) {
            try {
                DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
                DECODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public PacketDecoderModern(ByteToMessageDecoder previousDecoder) {
        load();
        this.previousDecoder = previousDecoder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(ctx.channel(), player, byteBuf.copy());
        PacketEvents.get().getEventManager().callEvent(packetDecodeEvent);

        if (packetDecodeEvent.isCancelled()) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }
        try {
            DECODE_METHOD.invoke(previousDecoder, ctx, byteBuf, list);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            } else if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
        }
        //TODO COMPLETE PACKETDECODERLEGACY
    }
}
