/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.injector.early.channelinitializer.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.Method;

public class PEChannelInitializer8 extends ChannelInitializer<SocketChannel> {
    private final ChannelInitializer<?> oldChannelInitializer;
    private Method initChannelMethod;
    public PEChannelInitializer8(ChannelInitializer<?> oldChannelInitializer) {
        this.oldChannelInitializer = oldChannelInitializer;
        load();
    }

    private void load() {
        initChannelMethod = Reflection.getMethod(oldChannelInitializer.getClass(), "initChannel", 0);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        initChannelMethod.invoke(oldChannelInitializer, socketChannel);
        PlayerChannelHandler8 channelHandler = new PlayerChannelHandler8();
        socketChannel.pipeline().addBefore("packet_handler", PacketEvents.handlerName, channelHandler);
    }
}
