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

package io.github.retrooper.packetevents.impl.netty;

import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHandler;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import io.github.retrooper.packetevents.impl.netty.buffer.ByteBufHandlerImpl;
import io.github.retrooper.packetevents.impl.netty.buffer.ByteBufImpl;
import io.github.retrooper.packetevents.impl.netty.channel.ChannelHandlerContextImpl;
import io.github.retrooper.packetevents.impl.netty.channel.ChannelImpl;

public class NettyManagerImpl implements NettyManager {
    private static final ByteBufHandler BYTE_BUF_HANDLER = new ByteBufHandlerImpl();

    @Override
    public ByteBufHandler getByteBufHandler() {
        return BYTE_BUF_HANDLER;
    }

    @Override
    public ByteBufAbstract wrappedBuffer(byte[] bytes) {
        return ByteBufUtil.wrappedBuffer(bytes);
    }

    @Override
    public ByteBufAbstract copiedBuffer(byte[] bytes) {
        return ByteBufUtil.copiedBuffer(bytes);
    }

    @Override
    public ByteBufAbstract buffer() {
        return ByteBufUtil.buffer();
    }

    @Override
    public ByteBufAbstract buffer(int initialCapacity) {
        return ByteBufUtil.buffer(initialCapacity);
    }

    @Override
    public ByteBufAbstract buffer(int initialCapacity, int maxCapacity) {
        return ByteBufUtil.buffer(initialCapacity, maxCapacity);
    }

    @Override
    public ByteBufAbstract directBuffer() {
        return ByteBufUtil.directBuffer();
    }

    @Override
    public ByteBufAbstract directBuffer(int initialCapacity) {
        return ByteBufUtil.directBuffer(initialCapacity);
    }

    @Override
    public ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity) {
        return ByteBufUtil.directBuffer(initialCapacity, maxCapacity);
    }

    @Override
    public ByteBufAbstract compositeBuffer() {
        return ByteBufUtil.compositeBuffer();
    }

    @Override
    public ByteBufAbstract compositeBuffer(int maxNumComponents) {
        return ByteBufUtil.compositeBuffer(maxNumComponents);
    }

    @Override
    public ByteBufAbstract wrapByteBuf(Object byteBuf) {
        return new ByteBufImpl(byteBuf);
    }

    @Override
    public ChannelAbstract wrapChannel0(Object channel) {
        return new ChannelImpl(channel);
    }

    @Override
    public ChannelHandlerContextAbstract wrapChannelHandlerContext(Object ctx) {
        return new ChannelHandlerContextImpl(ctx);
    }
}
