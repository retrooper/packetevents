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

package io.github.retrooper.packetevents.injector.lateinjector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.injector.lateinjector.LateInjector;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class LateChannelInjectorLegacy implements LateInjector {
    @Override
    public void inject() {

    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                packet = PacketEvents.get().packetProcessorInternal.read(player, ctx.channel(), packet);
                if (packet != null) {
                    super.channelRead(ctx, packet);
                    PacketEvents.get().packetProcessorInternal.postRead(player, ctx.channel(), packet);
                }
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
                packet = PacketEvents.get().packetProcessorInternal.write(player, ctx.channel(), packet);
                if (packet != null) {
                    super.write(ctx, packet, promise);
                    PacketEvents.get().packetProcessorInternal.postWrite(player, ctx.channel(), packet);
                }
            }
        };
        Channel channel = (Channel) PacketEvents.get().packetProcessorInternal.getChannel(player);
        channel.pipeline().addBefore("packet_handler", PacketEvents.handlerName, channelDuplexHandler);
    }

    @Override
    public void ejectPlayer(Player player) {
        Channel channel = (Channel) PacketEvents.get().packetProcessorInternal.getChannel(player);
        if (channel.pipeline().get(PacketEvents.handlerName) != null) {
            channel.pipeline().remove(PacketEvents.handlerName);
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        Channel channel = (Channel) PacketEvents.get().packetProcessorInternal.getChannel(player);
        return channel.pipeline().get(PacketEvents.handlerName) != null;
    }

    @Override
    public void sendPacket(Object rawChannel, Object packet) {
        Channel channel = (Channel) rawChannel;
        channel.pipeline().writeAndFlush(packet);
    }
}