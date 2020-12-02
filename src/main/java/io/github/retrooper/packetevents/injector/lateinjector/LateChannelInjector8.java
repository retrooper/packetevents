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

package io.github.retrooper.packetevents.injector.lateinjector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.ChannelInjector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class LateChannelInjector8 implements ChannelInjector {
    private final Plugin plugin;
    public LateChannelInjector8(final Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public void injectPlayerSync(Player player) {
        final ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                Object packet = PacketEvents.get().packetManager.read(player, ctx.channel(), msg);
                if (packet == null) {
                    return;
                }
                super.channelRead(ctx, msg);
                PacketEvents.get().packetManager.postRead(player, packet);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                Object packet = PacketEvents.get().packetManager.write(player, ctx.channel(), msg);
                if (packet == null) {
                    return;
                }
                super.write(ctx, msg, promise);
                PacketEvents.get().packetManager.postWrite(player, packet);
            }
        };
        final Channel channel = (Channel) PacketEvents.get().packetManager.getChannel(player.getName());
        channel.pipeline().addBefore("packet_handler", getNettyHandlerName(plugin), channelDuplexHandler);
    }

    @Override
    public void ejectPlayerSync(Player player) {
        final Channel channel = (Channel) PacketEvents.get().packetManager.getChannel(player.getName());
        channel.pipeline().remove(getNettyHandlerName(plugin));
    }

    @Override
    public void injectPlayerAsync(Player player) {
        PacketEvents.get().packetHandlingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                injectPlayerSync(player);
            }
        });
    }

    @Override
    public void ejectPlayerAsync(Player player) {
        PacketEvents.get().packetHandlingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                final Channel channel = (Channel) PacketEvents.get().packetManager.getChannel(player.getName());
                channel.pipeline().remove(getNettyHandlerName(plugin));
            }
        });
    }

    public void sendPacket(Object rawChannel, Object packet) {
        Channel channel = (Channel) rawChannel;
        channel.pipeline().writeAndFlush(packet);
    }
}