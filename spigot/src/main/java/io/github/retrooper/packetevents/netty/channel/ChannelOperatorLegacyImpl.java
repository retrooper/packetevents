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

package io.github.retrooper.packetevents.netty.channel;

import com.github.retrooper.packetevents.netty.channel.ChannelOperator;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.handler.timeout.ReadTimeoutException;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChannelOperatorLegacyImpl implements ChannelOperator {
    private static Set<Class<? extends Throwable>> IGNORED_HANDLER_EXCEPTIONS = null;

    @Override
    public Set<Class<? extends Throwable>> getIgnoredHandlerExceptions() {
        if (IGNORED_HANDLER_EXCEPTIONS == null) {
            IGNORED_HANDLER_EXCEPTIONS = new HashSet<>();
            IGNORED_HANDLER_EXCEPTIONS.add(ClosedChannelException.class);
            IGNORED_HANDLER_EXCEPTIONS.add(ReadTimeoutException.class);
            IGNORED_HANDLER_EXCEPTIONS.add(IOException.class);
            //TODO NativeIoException, was thing a thing in 1.7? Only simiarly named class i found was IOExceptionWithCause
        }
        return IGNORED_HANDLER_EXCEPTIONS;
    }

    @Override
    public SocketAddress remoteAddress(Object channel) {
        return ((Channel) channel).remoteAddress();
    }

    @Override
    public SocketAddress localAddress(Object channel) {
        return ((Channel) channel).localAddress();
    }

    @Override
    public boolean isOpen(Object channel) {
        return ((Channel) channel).isOpen();
    }

    @Override
    public Object close(Object channel) {
        return ((Channel) channel).close();
    }

    @Override
    public Object write(Object channel, Object buffer) {
        return ((Channel) channel).write(buffer);
    }

    @Override
    public Object flush(Object channel) {
        return ((Channel) channel).flush();
    }

    @Override
    public Object writeAndFlush(Object channel, Object buffer) {
        return ((Channel) channel).writeAndFlush(buffer);
    }

    @Override
    public Object fireChannelRead(Object channel, Object buffer) {
        return ((Channel) channel).pipeline().fireChannelRead(buffer);
    }

    @Override
    public Object writeInContext(Object channel, String ctx, Object buffer) {
        return ((Channel) channel).pipeline().context(ctx).write(buffer);
    }

    @Override
    public Object flushInContext(Object channel, String ctx) {
        return ((Channel) channel).pipeline().context(ctx).flush();
    }

    @Override
    public Object writeAndFlushInContext(Object channel, String ctx, Object buffer) {
        return ((Channel) channel).pipeline().context(ctx).writeAndFlush(buffer);
    }

    @Override
    public Object fireChannelReadInContext(Object channel, String ctx, Object buffer) {
        return ((Channel) channel).pipeline().context(ctx).fireChannelRead(buffer);
    }

    @Override
    public List<String> pipelineHandlerNames(Object channel) {
        return ((Channel) channel).pipeline().names();
    }

    @Override
    public Object getPipelineHandler(Object channel, String name) {
        return ((Channel)channel).pipeline().get(name);
    }

    @Override
    public Object getPipelineContext(Object channel, String name) {
        return ((Channel)channel).pipeline().context(name);
    }

    @Override
    public void runInEventLoop(Object channel, Runnable runnable) {
        ((Channel)channel).eventLoop().execute(runnable);
    }
}
