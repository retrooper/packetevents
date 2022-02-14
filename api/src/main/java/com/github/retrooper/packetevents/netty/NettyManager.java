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

package com.github.retrooper.packetevents.netty;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHandler;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;

import java.util.HashMap;
import java.util.Map;

public interface NettyManager {
    Map<Object, ChannelAbstract> CHANNEL_MAP = new HashMap<>();

    ByteBufHandler getByteBufHandler();

    ByteBufAbstract wrappedBuffer(byte[] bytes);

    ByteBufAbstract copiedBuffer(byte[] bytes);

    ByteBufAbstract buffer();

    ByteBufAbstract buffer(int initialCapacity);

    ByteBufAbstract buffer(int initialCapacity, int maxCapacity);

    ByteBufAbstract directBuffer();

    ByteBufAbstract directBuffer(int initialCapacity);

    ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity);

    ByteBufAbstract compositeBuffer();

    ByteBufAbstract compositeBuffer(int maxNumComponents);

    ByteBufAbstract wrapByteBuf(Object byteBuf);

    default ChannelAbstract wrapChannel(Object channel) {
        ChannelAbstract result = CHANNEL_MAP.get(channel);
        if (result == null) {
            result = wrapChannel0(channel);
            CHANNEL_MAP.put(channel, result);
        }
        return result;
    }
    ChannelAbstract wrapChannel0(Object channel);

    ChannelHandlerContextAbstract wrapChannelHandlerContext(Object ctx);

    default byte[] asByteArray(ByteBufAbstract byteBuf) {
        int len = byteBuf.readableBytes();
        byte[] bytes = new byte[len];
        byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        return bytes;
    }
}
