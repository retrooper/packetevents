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

package io.github.retrooper.packetevents.utils.netty;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationHandler;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHandler;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.github.retrooper.packetevents.utils.netty.buffer.*;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextLegacy;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextModern;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelLegacy;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelModern;

public class NettyManagerImpl implements NettyManager {
    private static ByteBufHandler BYTE_BUF_HANDLER;
private static ByteBufAllocationHandler BYTE_BUF_ALLOCATION_HANDLER;
    @Override
    public ByteBufHandler getByteBufHandler() {
        if (BYTE_BUF_HANDLER == null) {
            if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
                BYTE_BUF_HANDLER = new ByteBufHandlerModernImpl();
            }
            else {
                BYTE_BUF_HANDLER = new ByteBufHandlerLegacyImpl();
            }
        }
        return BYTE_BUF_HANDLER;
    }

    @Override
    public ByteBufAllocationHandler getByteBufAllocationHandler() {
        if (BYTE_BUF_ALLOCATION_HANDLER == null) {
            if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
                BYTE_BUF_ALLOCATION_HANDLER = new ByteBufAllocationHandlerModernImpl();
            }
            else {
                BYTE_BUF_ALLOCATION_HANDLER = new ByteBufAllocationHandlerLegacyImpl();
            }
        }
        return BYTE_BUF_ALLOCATION_HANDLER;
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
        if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return new ByteBufModern(byteBuf);
        }
        else {
            return new ByteBufLegacy(byteBuf);
        }
    }

    @Override
    public ChannelAbstract wrapChannel0(Object channel) {
        if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return new ChannelModern(channel);
        }
        else {
            return new ChannelLegacy(channel);
        }
    }

    @Override
    public ChannelHandlerContextAbstract wrapChannelHandlerContext(Object ctx) {
        if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return new ChannelHandlerContextModern(ctx);
        }
        else {
            return new ChannelHandlerContextLegacy(ctx);
        }
    }
}
