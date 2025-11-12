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

package com.github.retrooper.packetevents.netty.channel;

import java.net.SocketAddress;
import java.util.List;

public interface ChannelOperator {
    SocketAddress remoteAddress(Object channel);

    SocketAddress localAddress(Object channel);

    boolean isOpen(Object channel);

    Object close(Object channel);

    Object write(Object channel, Object buffer);

    Object flush(Object channel);

    Object writeAndFlush(Object channel, Object buffer);

    Object fireChannelRead(Object channel, Object buffer);

    Object writeInContext(Object channel, String ctx, Object buffer);

    Object flushInContext(Object channel, String ctx);

    Object writeAndFlushInContext(Object channel, String ctx, Object buffer);

    Object fireChannelReadInContext(Object channel, String ctx, Object buffer);

    List<String> pipelineHandlerNames(Object channel);

    Object getPipelineHandler(Object channel, String name);

    Object getPipelineContext(Object channel, String name);

    Object getPipeline(Object channel);

    void runInEventLoop(Object channel, Runnable runnable);

    Object pooledByteBuf(Object channel);
}
