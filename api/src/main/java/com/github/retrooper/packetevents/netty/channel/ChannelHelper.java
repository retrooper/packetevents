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

import com.github.retrooper.packetevents.PacketEvents;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

public class ChannelHelper {
    public static SocketAddress remoteAddress(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().remoteAddress(channel);
    }

    public static SocketAddress localAddress(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().localAddress(channel);
    }

    public static boolean isOpen(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().isOpen(channel);
    }

    public static Object close(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().close(channel);
    }

    public static Object write(Object channel, Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().write(channel, buffer);
    }

    public static Object flush(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().flush(channel);
    }

    public static Object writeAndFlush(Object channel, Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().writeAndFlush(channel, buffer);
    }

    public static Object fireChannelRead(Object channel, Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().fireChannelRead(channel, buffer);
    }

    public static Object writeInContext(Object channel, String ctx, Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().writeInContext(channel, ctx, buffer);
    }

    public static Object flushInContext(Object channel, String ctx) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().flushInContext(channel, ctx);
    }

    public static Object writeAndFlushInContext(Object channel, String ctx, Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().writeAndFlushInContext(channel, ctx, buffer);
    }

    public static Object fireChannelReadInContext(Object channel, String ctx, Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().fireChannelReadInContext(channel, ctx, buffer);
    }

    public static List<String> pipelineHandlerNames(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().pipelineHandlerNames(channel);
    }

    public static String pipelineHandlerNamesAsString(Object channel) {
        return Arrays.toString(pipelineHandlerNames(channel).toArray(new String[0]));
    }

    public static Object getPipeline(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().getPipeline(channel);
    }

    public static Object getPipelineHandler(Object channel, String name) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().getPipelineHandler(channel, name);
    }

    public static Object getPipelineContext(Object channel, String handlerName) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().getPipelineContext(channel, handlerName);
    }

    public static Object pooledByteBuf(Object channel) {
        return PacketEvents.getAPI().getNettyManager().getChannelOperator().pooledByteBuf(channel);
    }

    public static void runInEventLoop(Object channel, Runnable runnable) {
        PacketEvents.getAPI().getNettyManager().getChannelOperator().runInEventLoop(channel, runnable);
    }
}
