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

import com.retrooper.packetevents.util.netty.buffer.ByteBufAbstract;
import com.retrooper.packetevents.util.netty.buffer.ByteBufAllocatorAbstract;
import com.retrooper.packetevents.util.netty.channel.pipeline.ChannelPipelineAbstract;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;

public class ChannelHandlerContextLegacy implements ChannelHandlerContextAbstract {
    private final ChannelHandlerContext ctx;

    public ChannelHandlerContextLegacy(Object rawChannelHandlerContext) {
        this.ctx = (ChannelHandlerContext) rawChannelHandlerContext;
    }

    @Override
    public Object rawChannelHandlerContext() {
        return ctx;
    }

    @Override
    public ChannelAbstract channel() {
        return ChannelAbstract.generate(ctx.channel());
    }

    @Override
    public String name() {
        return ctx.name();
    }

    @Override
    public ChannelHandlerAbstract handler() {
        return ChannelHandlerAbstract.generate(ctx.handler());
    }

    @Override
    public boolean isRemoved() {
        return ctx.isRemoved();
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelRegistered() {
        return new ChannelHandlerContextLegacy(ctx.fireChannelRegistered());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelUnregistered() {
        return new ChannelHandlerContextLegacy(ctx.fireChannelUnregistered());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelActive() {
        return new ChannelHandlerContextLegacy(ctx.fireChannelActive());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelInactive() {
        return new ChannelHandlerContextLegacy(ctx.fireChannelInactive());
    }

    @Override
    public ChannelHandlerContextAbstract fireExceptionCaught(Throwable throwable) {
        return new ChannelHandlerContextLegacy(ctx.fireExceptionCaught(throwable));
    }

    @Override
    public ChannelHandlerContextAbstract fireUserEventTriggered(Object event) {
        return new ChannelHandlerContextLegacy(ctx.fireUserEventTriggered(event));
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelRead(Object msg) {
        return new ChannelHandlerContextLegacy(ctx.fireChannelRead(msg));
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelReadComplete() {
        return new ChannelHandlerContextLegacy(ctx.fireChannelReadComplete());
    }

    @Override
    public ChannelHandlerContextAbstract fireChannelWritabilityChanged() {
        return new ChannelHandlerContextLegacy(ctx.fireChannelWritabilityChanged());
    }

    @Override
    public ChannelHandlerContextAbstract read() {
        return new ChannelHandlerContextLegacy(ctx.read());
    }

    @Override
    public ChannelHandlerContextAbstract flush() {
        return new ChannelHandlerContextLegacy(ctx.flush());
    }

    @Override
    public ChannelPipelineAbstract pipeline() {
        return ChannelPipelineAbstract.generate(ctx.pipeline());
    }

    @Override
    public ByteBufAllocatorAbstract alloc() {
        return ByteBufAllocatorAbstract.generate(ctx.alloc());
    }

    @Override
    public void write(Object msg) {
        if (msg instanceof ByteBufAbstract) {
            msg = ((ByteBufAbstract) msg).rawByteBuf();
        }
        ctx.write(msg);
    }

    @Override
    public void writeAndFlush(Object msg) {
        if (msg instanceof ByteBufAbstract) {
            msg = ((ByteBufAbstract) msg).rawByteBuf();
        }
        ctx.writeAndFlush(msg);
    }
}
