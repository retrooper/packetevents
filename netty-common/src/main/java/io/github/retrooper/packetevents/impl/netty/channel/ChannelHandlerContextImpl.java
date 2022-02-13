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

package io.github.retrooper.packetevents.impl.netty.channel;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAllocatorAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import com.github.retrooper.packetevents.netty.channel.pipeline.ChannelPipelineAbstract;
import io.github.retrooper.packetevents.impl.netty.buffer.ByteBufAllocatorImpl;
import io.github.retrooper.packetevents.impl.netty.channel.pipeline.ChannelPipelineImpl;
import io.netty.channel.ChannelHandlerContext;

public class ChannelHandlerContextImpl implements ChannelHandlerContextAbstract {
    private final ChannelHandlerContext ctx;

    public ChannelHandlerContextImpl(Object rawChannelHandlerContext) {
        this.ctx = (ChannelHandlerContext) rawChannelHandlerContext;
    }

    @Override
    public Object rawChannelHandlerContext() {
        return ctx;
    }

    @Override
    public ChannelAbstract channel() {
        return new ChannelImpl(ctx.channel());
    }

    @Override
    public String name() {
        return ctx.name();
    }

    @Override
    public ChannelHandlerAbstract handler() {
        return new ChannelHandlerImpl(ctx.handler());
    }

    @Override
    public boolean isRemoved() {
        return ctx.isRemoved();
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelRegistered() {
        return new ChannelHandlerContextImpl(ctx.fireChannelRegistered());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelUnregistered() {
        return new ChannelHandlerContextImpl(ctx.fireChannelUnregistered());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelActive() {
        return new ChannelHandlerContextImpl(ctx.fireChannelActive());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelInactive() {
        return new ChannelHandlerContextImpl(ctx.fireChannelInactive());
    }

    @Override
    public ChannelHandlerContextAbstract fireExceptionCaught(Throwable throwable) {
        return new ChannelHandlerContextImpl(ctx.fireExceptionCaught(throwable));
    }

    @Override
    public ChannelHandlerContextAbstract fireUserEventTriggered(Object event) {
        return new ChannelHandlerContextImpl(ctx.fireUserEventTriggered(event));
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelRead0(Object msg) {
        return new ChannelHandlerContextImpl(ctx.fireChannelRead(msg));
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelReadComplete() {
        return new ChannelHandlerContextImpl(ctx.fireChannelReadComplete());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelWritabilityChanged() {
        return new ChannelHandlerContextImpl(ctx.fireChannelWritabilityChanged());
    }

    @Override
    public ChannelHandlerContextAbstract read() {
        return new ChannelHandlerContextImpl(ctx.read());
    }

    @Override
    public ChannelHandlerContextAbstract flush() {
        return new ChannelHandlerContextImpl(ctx.flush());
    }

    @Override
    public ChannelPipelineAbstract pipeline() {
        return new ChannelPipelineImpl(ctx.pipeline());
    }

    @Override
    public ByteBufAllocatorAbstract alloc() {
        return new ByteBufAllocatorImpl(ctx.alloc());
    }

    @Override
    public void write0(Object msg) {
        ctx.write(msg);
    }

    @Override
    public void writeAndFlush0(Object msg) {
        ctx.writeAndFlush(msg);
    }
}
