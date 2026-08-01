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

package io.github.retrooper.packetevents.impl.netty.channel;

import com.github.retrooper.packetevents.netty.channel.ChannelOperator;
import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.List;

public class ChannelOperatorImpl implements ChannelOperator {
    @Override
    public SocketAddress remoteAddress(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).remoteAddress();
        }
        // OnThePixel (Minestom): raw NIO SocketChannel, not a Netty Channel.
        if (channel instanceof java.nio.channels.SocketChannel) {
            try {
                return ((java.nio.channels.SocketChannel) channel).getRemoteAddress();
            } catch (java.io.IOException ignored) {
                return null;
            }
        }
        return null;
    }

    @Override
    public SocketAddress localAddress(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).localAddress();
        }
        // OnThePixel (Minestom): raw NIO SocketChannel, not a Netty Channel.
        if (channel instanceof java.nio.channels.SocketChannel) {
            try {
                return ((java.nio.channels.SocketChannel) channel).getLocalAddress();
            } catch (java.io.IOException ignored) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean isOpen(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).isOpen();
        }
        // OnThePixel (Minestom): the "channel" is a raw NIO java.nio.channels.SocketChannel, not a
        // Netty Channel. Grim's shouldCheck() calls ChannelHelper.isOpen() on the tick thread; the
        // blind Netty cast threw ClassCastException and stopped every player from being checked.
        if (channel instanceof java.nio.channels.Channel) {
            return ((java.nio.channels.Channel) channel).isOpen();
        }
        return false;
    }

    @Override
    public Object close(Object channel) {
        if (channel instanceof Channel) {
            return ((Channel) channel).close();
        }
        // OnThePixel (Minestom): the "channel" is a raw NIO java.nio.channels.SocketChannel, not a Netty
        // Channel. Closing it via the Netty cast threw ClassCastException on the tick thread. Close it
        // directly so no closeConnection() path can ever take down the server tick.
        if (channel instanceof java.nio.channels.Channel) {
            try {
                ((java.nio.channels.Channel) channel).close();
            } catch (java.io.IOException ignored) {
            }
        }
        return null;
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
        return ((Channel) channel).pipeline().get(name);
    }

    @Override
    public Object getPipelineContext(Object channel, String name) {
        return ((Channel) channel).pipeline().context(name);
    }

    @Override
    public Object getPipeline(Object channel) {
        return ((Channel) channel).pipeline();
    }

    @Override
    public void runInEventLoop(Object channel, Runnable runnable) {
        if (channel instanceof Channel) {
            ((Channel) channel).eventLoop().execute(runnable);
            return;
        }
        // OnThePixel (Minestom): a raw NIO SocketChannel has no Netty event loop. Grim uses this via
        // runSafely() to get channel-thread affinity; with no event loop to hand off to, run inline on
        // the calling thread (the Minestom packet/tick thread that already drives this player's flow).
        runnable.run();
    }

    @Override
    public Object pooledByteBuf(Object channel) {
        return ((Channel) channel).alloc().buffer();
    }
}
