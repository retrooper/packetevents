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

package com.github.retrooper.packetevents.netty.channel.pipeline;


import com.github.retrooper.packetevents.netty.channel.ChannelHandlerAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;

import java.util.Arrays;
import java.util.List;

public interface ChannelPipelineAbstract {
    Object rawChannelPipeline();

    List<String> names();

    //namesArray is not a feature in netty, something I came up with to make my life easier
    default String[] namesArray() {
        return names().toArray(new String[0]);
    }

    //namesToString is not a feature in netty, something I came up with to make my life easier
    default String namesToString() {
        return Arrays.toString(namesArray());
    }

    ChannelHandlerAbstract get(String handlerName);

    ChannelPipelineAbstract addFirst(String handlerName, ChannelHandlerAbstract handler);

    ChannelPipelineAbstract addLast(String handlerName, ChannelHandlerAbstract handler);

    ChannelPipelineAbstract addBefore(String targetHandlerName, String handlerName, ChannelHandlerAbstract handler);

    ChannelPipelineAbstract addAfter(String targetHandlerName, String handlerName, ChannelHandlerAbstract handler);

    ChannelPipelineAbstract remove(ChannelHandlerAbstract handler);

    ChannelHandlerAbstract remove(String handlerName);

    ChannelHandlerAbstract removeFirst();

    ChannelHandlerAbstract removeLast();

    ChannelHandlerAbstract replace(String previousHandlerName, String handlerName, ChannelHandlerAbstract handler);

    ChannelPipelineAbstract fireChannelRegistered();

    ChannelPipelineAbstract fireChannelUnregistered();

    ChannelPipelineAbstract fireChannelActive();

    ChannelPipelineAbstract fireChannelInactive();

    ChannelPipelineAbstract fireExceptionCaught(Throwable throwable);

    ChannelPipelineAbstract fireUserEventTriggered(Object event);

    ChannelPipelineAbstract fireChannelRead(Object msg);

    ChannelPipelineAbstract fireChannelReadComplete();

    ChannelPipelineAbstract fireChannelWritabilityChanged();

    ChannelPipelineAbstract flush();

    ChannelHandlerContextAbstract context(String handlerName);
}
