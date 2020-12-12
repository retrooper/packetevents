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

package io.github.retrooper.packetevents.injector.earlyinjector;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.ChannelInjector;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.netty.channel.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EarlyChannelInjector8 implements ChannelInjector {
    private final Plugin plugin;
    private final List<Channel> serverChannels = new ArrayList<>();
    private ChannelInitializer<Channel> firstChannelInitializer;
    private ChannelInitializer<Channel> secondChannelInitializer;
    private ChannelInboundHandlerAdapter channelHandler;
    private List<Object> networkMarkers;

    public EarlyChannelInjector8(final Plugin plugin) {
        this.plugin = plugin;
    }

    public void startup() {
        networkMarkers = NMSUtils.getNetworkMarkers();
        firstChannelInitializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(final Channel channel) {
                if(networkMarkers != null) {
                synchronized (networkMarkers) {
                    channel.eventLoop().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                injectChannel(channel);
                            } catch (Exception ex) {
                                channel.disconnect();
                            }
                        }
                    });
                }
                }
                else {
                    channel.eventLoop().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                injectChannel(channel);
                            } catch (Exception ex) {
                                channel.disconnect();
                            }
                        }
                    });
                }
            }
        };
        secondChannelInitializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(firstChannelInitializer);
            }
        };

        channelHandler = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                Channel channel = (Channel) msg;
                channel.pipeline().addFirst(secondChannelInitializer);
                ctx.fireChannelRead(msg);
            }
        };

        for (int i = 0; true; i++) {
            WrappedPacket serverConnectionWrapper = new WrappedPacket(NMSUtils.getMinecraftServerConnection());
            try {
                List<Object> serverChannelList = (List<Object>) serverConnectionWrapper.readObject(i, List.class);
                for (Object serverChannel : serverChannelList) {
                    if (serverChannel instanceof ChannelFuture) {
                        Channel channel = ((ChannelFuture) serverChannel).channel();

                        serverChannels.add(channel);
                        channel.pipeline().addFirst(channelHandler);
                        break;
                    }
                }
            } catch (Exception ex) {
                break;
            }
        }
    }

    public void close() {
        for (Channel channel : serverChannels) {
            try {
                channel.pipeline().remove(channelHandler);
            } catch (NoSuchElementException ignored) {

            }
        }
    }

    public PlayerChannelInterceptor injectChannel(Object ch) {
        Channel channel = (Channel) ch;
        String handlerName = getNettyHandlerName(plugin);
        PlayerChannelInterceptor interceptor = (PlayerChannelInterceptor) channel.pipeline().get(handlerName);
        if (interceptor == null) {
            interceptor = new PlayerChannelInterceptor();
            channel.pipeline().addBefore("packet_handler", handlerName, interceptor);
        }
        return interceptor;
    }

    public void ejectChannel(Object ch) {
        Channel channel = (Channel) ch;
        String handlerName = getNettyHandlerName(plugin);
        if (channel.pipeline().get(handlerName) != null) {
            channel.pipeline().remove(handlerName);
        }
    }

    @Override
    public void injectPlayerSync(Player player) {
        Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player.getName());
        injectChannel(channel).player = player;
    }

    @Override
    public void ejectPlayerSync(Player player) {
        Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player.getName());
        ejectChannel(channel);
        PacketEvents.get().getPlayerUtils().clientVersionsMap.remove(player.getAddress());
    }

    @Override
    public void injectPlayerAsync(Player player) {
        PacketEvents.get().packetHandlingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player.getName());
                injectChannel(channel).player = player;
            }
        });
    }

    @Override
    public void ejectPlayerAsync(Player player) {
        PacketEvents.get().packetHandlingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player.getName());
                ejectChannel(channel);
                PacketEvents.get().packetHandlerInternal.keepAliveMap.remove(player.getUniqueId());
                PacketEvents.get().packetHandlerInternal.channelTimePassed.remove(channel);
                PacketEvents.get().packetHandlerInternal.channelMap.remove(player.getName());
                PacketEvents.get().getPlayerUtils().clientVersionsMap.remove(player.getAddress());
            }
        });
    }

    @Override
    public void sendPacket(Object ch, Object packet) {
        Channel channel = (Channel) ch;
        channel.writeAndFlush(packet);
    }

    public static class PlayerChannelInterceptor extends ChannelDuplexHandler {
        public volatile Player player;

        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object packet) throws Exception {
            packet = PacketEvents.get().packetHandlerInternal.read(player, ctx.channel(), packet);
            if (packet != null) {
                super.channelRead(ctx, packet);
                PacketEvents.get().packetHandlerInternal.postRead(player, ctx.channel(), packet);
            }
        }

        @Override
        public void write(final ChannelHandlerContext ctx, Object packet, final ChannelPromise promise) throws Exception {
            packet = PacketEvents.get().packetHandlerInternal.write(player, ctx.channel(), packet);
            if (packet != null) {
                super.write(ctx, packet, promise);
                PacketEvents.get().packetHandlerInternal.postWrite(player, ctx.channel(), packet);
            }
        }
    }
}
