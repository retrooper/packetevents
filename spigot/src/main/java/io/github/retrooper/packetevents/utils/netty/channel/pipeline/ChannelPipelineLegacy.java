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

package io.github.retrooper.packetevents.utils.netty.channel.pipeline;

import com.github.retrooper.packetevents.netty.channel.ChannelHandlerAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import com.github.retrooper.packetevents.netty.channel.pipeline.ChannelPipelineAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextLegacy;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerLegacy;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelPipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelPipelineLegacy implements ChannelPipelineAbstract {
    private final ChannelPipeline pipeline;

    public ChannelPipelineLegacy(Object rawChannelPipeline) {
        this.pipeline = (ChannelPipeline) rawChannelPipeline;
    }

    @Override
    public Object rawChannelPipeline() {
        return pipeline;
    }

    @Override
    public List<String> names() {
        return pipeline.names();
    }

    @Override
    public ChannelHandlerAbstract get(String handlerName) {
        return new ChannelHandlerLegacy(pipeline.get(handlerName));
    }

    @Override
    public ChannelPipelineAbstract addFirst(String handlerName, ChannelHandlerAbstract handler) {
        return new ChannelPipelineLegacy(pipeline.addFirst(handlerName, (ChannelHandler) handler.rawChannelHandler()));
    }

    @Override
    public ChannelPipelineAbstract addLast(String handlerName, ChannelHandlerAbstract handler) {
        return new ChannelPipelineLegacy(pipeline.addLast(handlerName, (ChannelHandler) handler.rawChannelHandler()));
    }

    @Override
    public ChannelPipelineAbstract addBefore(String targetHandlerName, String handlerName, ChannelHandlerAbstract handler) {
        return new ChannelPipelineLegacy(pipeline.addBefore(targetHandlerName, handlerName, (ChannelHandler) handler.rawChannelHandler()));
    }

    @Override
    public ChannelPipelineAbstract addAfter(String targetHandlerName, String handlerName, ChannelHandlerAbstract handler) {
        return new ChannelPipelineLegacy(pipeline.addAfter(targetHandlerName, handlerName, (ChannelHandler) handler.rawChannelHandler()));
    }

    @Override
    public ChannelPipelineAbstract remove(ChannelHandlerAbstract handler) {
        return new ChannelPipelineLegacy(pipeline.remove((ChannelHandler) handler.rawChannelHandler()));
    }

    @Override
    public ChannelHandlerAbstract remove(String handlerName) {
        return new ChannelHandlerLegacy(pipeline.remove(handlerName));
    }

    @Override
    public ChannelHandlerAbstract removeFirst() {
        return new ChannelHandlerLegacy(pipeline.removeFirst());
    }

    @Override
    public ChannelHandlerAbstract removeLast() {
        return new ChannelHandlerLegacy(pipeline.removeLast());
    }

    @Override
    public ChannelHandlerAbstract replace(String previousHandlerName, String handlerName, ChannelHandlerAbstract handler) {
        return new ChannelHandlerLegacy(pipeline.replace(previousHandlerName, handlerName, (ChannelHandler) handler.rawChannelHandler()));
    }

    @Override
    public ChannelPipelineAbstract fireChannelRegistered() {
        return new ChannelPipelineLegacy(pipeline.fireChannelRegistered());
    }

    @Override
    public ChannelPipelineAbstract fireChannelUnregistered() {
        return new ChannelPipelineLegacy(pipeline.fireChannelUnregistered());
    }

    @Override
    public ChannelPipelineAbstract fireChannelActive() {
        return new ChannelPipelineLegacy(pipeline.fireChannelActive());
    }

    @Override
    public ChannelPipelineAbstract fireChannelInactive() {
        return new ChannelPipelineLegacy(pipeline.fireChannelInactive());
    }

    @Override
    public ChannelPipelineAbstract fireExceptionCaught(Throwable throwable) {
        return new ChannelPipelineLegacy(pipeline.fireExceptionCaught(throwable));
    }

    @Override
    public ChannelPipelineAbstract fireUserEventTriggered(Object event) {
        return new ChannelPipelineLegacy(pipeline.fireUserEventTriggered(event));
    }

    @Override
    public ChannelPipelineAbstract fireChannelRead0(Object msg) {
        return new ChannelPipelineLegacy(pipeline.fireChannelRead(msg));
    }

    @Override
    public ChannelPipelineAbstract fireChannelReadComplete() {
        return new ChannelPipelineLegacy(pipeline.fireChannelReadComplete());
    }

    @Override
    public ChannelPipelineAbstract fireChannelWritabilityChanged() {
        return new ChannelPipelineLegacy(pipeline.fireChannelWritabilityChanged());
    }

    @Override
    public ChannelPipelineAbstract flush() {
        return new ChannelPipelineLegacy(pipeline.flush());
    }

    @Override
    public ChannelHandlerContextAbstract context(String handlerName) {
        return new ChannelHandlerContextLegacy(pipeline.context(handlerName));
    }

    @Override
    public ChannelHandlerContextAbstract context(ChannelHandlerAbstract handler) {
        return new ChannelHandlerContextLegacy(pipeline.context((ChannelHandler)handler.rawChannelHandler()));
    }

    @Override
    public Map<String, ChannelHandlerAbstract> toMap() {
        Map<String, ChannelHandler> internalMap = pipeline.toMap();
        Map<String, ChannelHandlerAbstract> wrapperMap = new HashMap<>();
        for (String name : internalMap.keySet()) {
            ChannelHandler rawHandler = internalMap.get(name);
            wrapperMap.put(name, new ChannelHandlerLegacy(rawHandler));
        }
        return wrapperMap;
    }
}
