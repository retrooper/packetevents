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

package io.github.retrooper.packetevents.utils.netty.channel;

import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAllocatorAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.pipeline.ChannelPipelineAbstract;
import io.github.retrooper.packetevents.utils.nms.MinecraftReflection;

public interface ChannelHandlerContextAbstract {
    static ChannelHandlerContextAbstract generate(Object rawChannelHandlerContext) {
        if (MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return new ChannelHandlerContextModern(rawChannelHandlerContext);
        } else {
            return new ChannelHandlerContextLegacy(rawChannelHandlerContext);
        }
    }

    Object rawChannelHandlerContext();

    ChannelAbstract channel();

    String name();

    ChannelHandlerAbstract handler();

    boolean isRemoved();

    ChannelHandlerContextAbstract fireChannelRegistered();

    ChannelHandlerContextAbstract fireChannelUnregistered();

    ChannelHandlerContextAbstract fireChannelActive();

    ChannelHandlerContextAbstract fireChannelInactive();

    ChannelHandlerContextAbstract fireExceptionCaught(Throwable var1);

    ChannelHandlerContextAbstract fireUserEventTriggered(Object var1);

    ChannelHandlerContextAbstract fireChannelRead(Object var1);

    ChannelHandlerContextAbstract fireChannelReadComplete();

    ChannelHandlerContextAbstract fireChannelWritabilityChanged();

    ChannelHandlerContextAbstract read();

    ChannelHandlerContextAbstract flush();

    ChannelPipelineAbstract pipeline();

    ByteBufAllocatorAbstract alloc();

    //TODO Support ChannelFuture for write and writeandflush
    void write(Object msg);
    //void write(Object msg, ChannelPromiseAbstract promise);

    void writeAndFlush(Object msg);
    //void writeAndFlush(Object msg, ChannelPromiseAbstract promise);
}
