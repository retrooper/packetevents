/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class PreChannelInitializer extends ChannelInboundHandlerAdapter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(io.netty.channel.ChannelInitializer.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        try {
            ServerConnectionInitializer.initChannel(ctx.channel(), ConnectionState.HANDSHAKING);
        } catch (Throwable t) {
            exceptionCaught(ctx, t);
        } finally {
            ChannelPipeline pipeline = ctx.pipeline();
            if (pipeline.context(this) != null) {
                pipeline.remove(this);
            }
        }
        ctx.pipeline().fireChannelRegistered();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
        PreChannelInitializer.logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), t);
        ctx.close();
    }
}
