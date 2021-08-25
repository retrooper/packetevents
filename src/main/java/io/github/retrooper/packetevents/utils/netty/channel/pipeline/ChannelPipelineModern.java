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

import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerAbstract;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;

import java.util.List;

public class ChannelPipelineModern implements ChannelPipelineAbstract {
    private final ChannelPipeline pipeline;
    public ChannelPipelineModern(Object rawChannelPipeline) {
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
        return ChannelHandlerAbstract.generate(pipeline.get(handlerName));
    }

    @Override
    public ChannelPipelineAbstract addFirst(String handlerName, Object handler) {
        return new ChannelPipelineModern(pipeline.addFirst(handlerName, (ChannelHandler) handler));
    }

    @Override
    public ChannelPipelineAbstract addLast(String handlerName, Object handler) {
        return new ChannelPipelineModern(pipeline.addLast(handlerName, (ChannelHandler) handler));
    }

    @Override
    public ChannelPipelineAbstract addBefore(String targetHandlerName, String handlerName, Object handler) {
        return new ChannelPipelineModern(pipeline.addBefore(targetHandlerName, handlerName, (ChannelHandler) handler));
    }

    @Override
    public ChannelPipelineAbstract addAfter(String targetHandlerName, String handlerName, Object handler) {
        return new ChannelPipelineModern(pipeline.addAfter(targetHandlerName, handlerName, (ChannelHandler) handler));
    }

    @Override
    public ChannelPipelineAbstract remove(Object handler) {
        return new ChannelPipelineModern(pipeline.remove((ChannelHandler) handler));
    }

    @Override
    public Object remove(String handlerName) {
        return new ChannelPipelineModern(pipeline.remove(handlerName));
    }

    @Override
    public Object removeFirst() {
        return new ChannelPipelineModern(pipeline.removeFirst());
    }

    @Override
    public Object removeLast() {
        return new ChannelPipelineModern(pipeline.removeLast());
    }

    @Override
    public Object replace(String previousHandlerName, String handlerName, Object handler) {
        return new ChannelPipelineModern(pipeline.replace(previousHandlerName, handlerName, (ChannelHandler) handler));
    }

    @Override
    public ChannelPipelineAbstract fireChannelRegistered() {
        return new ChannelPipelineModern(pipeline.fireChannelRegistered());
    }

    @Override
    public ChannelPipelineAbstract fireChannelUnregistered() {
        return new ChannelPipelineModern(pipeline.fireChannelUnregistered());
    }

    @Override
    public ChannelPipelineAbstract fireChannelActive() {
        return new ChannelPipelineModern(pipeline.fireChannelActive());
    }

    @Override
    public ChannelPipelineAbstract fireChannelInactive() {
        return new ChannelPipelineModern(pipeline.fireChannelInactive());
    }

    @Override
    public ChannelPipelineAbstract fireExceptionCaught(Throwable throwable) {
        return new ChannelPipelineModern(pipeline.fireExceptionCaught(throwable));
    }

    @Override
    public ChannelPipelineAbstract fireUserEventTriggered(Object event) {
        return new ChannelPipelineModern(pipeline.fireUserEventTriggered(event));
    }

    @Override
    public ChannelPipelineAbstract fireChannelRead(Object msg) {
        return new ChannelPipelineModern(pipeline.fireChannelRead(msg));
    }

    @Override
    public ChannelPipelineAbstract fireChannelReadComplete() {
        return new ChannelPipelineModern(pipeline.fireChannelReadComplete());
    }

    @Override
    public ChannelPipelineAbstract fireChannelWritabilityChanged() {
        return new ChannelPipelineModern(pipeline.fireChannelWritabilityChanged());
    }

    @Override
    public ChannelPipelineAbstract flush() {
        return new ChannelPipelineModern(pipeline.flush());
    }
}
