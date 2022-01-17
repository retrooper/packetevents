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

package com.github.retrooper.packetevents.netty.channel;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAllocatorAbstract;
import com.github.retrooper.packetevents.netty.channel.pipeline.ChannelPipelineAbstract;

public interface ChannelHandlerContextAbstract {
    Object rawChannelHandlerContext();

    ChannelAbstract channel();

    String name();

    ChannelHandlerAbstract handler();

    boolean isRemoved();

    ChannelHandlerContextAbstract fireChannelRegistered();

    ChannelHandlerContextAbstract fireChannelUnregistered();

    ChannelHandlerContextAbstract fireChannelActive();

    ChannelHandlerContextAbstract fireChannelInactive();

    ChannelHandlerContextAbstract fireExceptionCaught(Throwable throwable);

    ChannelHandlerContextAbstract fireUserEventTriggered(Object event);

    ChannelHandlerContextAbstract fireChannelRead0(Object msg);

    default ChannelHandlerContextAbstract fireChannelRead(Object msg) {
        if (msg instanceof ByteBufAbstract) {
            msg = ((ByteBufAbstract) msg).rawByteBuf();
        }
        return fireChannelRead0(msg);
    }

    ChannelHandlerContextAbstract fireChannelReadComplete();

    ChannelHandlerContextAbstract fireChannelWritabilityChanged();

    ChannelHandlerContextAbstract read();

    ChannelHandlerContextAbstract flush();

    ChannelPipelineAbstract pipeline();

    ByteBufAllocatorAbstract alloc();

    //TODO Support ChannelFuture for write and writeandflush
    void write0(Object msg);
    default void write(Object msg) {
        if (msg instanceof ByteBufAbstract) {
            msg = ((ByteBufAbstract) msg).rawByteBuf();
        }
        write0(msg);
    }
    //TODO void write(Object msg, ChannelPromiseAbstract promise);

    void writeAndFlush0(Object msg);
    default void writeAndFlush(Object msg) {
        if (msg instanceof ByteBufAbstract) {
            msg = ((ByteBufAbstract) msg).rawByteBuf();
        }
        writeAndFlush0(msg);
    }

    //TODO void writeAndFlush(Object msg, ChannelPromiseAbstract promise);
}
