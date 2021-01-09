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
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import net.minecraft.util.io.netty.channel.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 1.7.10 Spigot Early channel injector.
 * This is an early injector  for 1.7.10 spigot servers.
 * The netty import on spigot changed since 1.8.
 * So we needed to make a new class using the older import location to support 1.7.10.
 * This is why we also commonly use a java {@link Object} class when it comes to netty channels
 * in some classes to maintain support for both netty imports.
 *
 * @author retrooper
 * @since 1.8
 */
public class EarlyChannelInjector7 implements EarlyInjector {
    /**
     * Netty minecraft server channels.
     */
    private final List<Channel> serverChannels = new ArrayList<>();

    /**
     * First channel initializer.
     * Used to inject incoming channels.
     */
    private ChannelInitializer<Channel> firstChannelInitializer;

    /**
     * Second channel initializer.
     */
    private ChannelInitializer<Channel> secondChannelInitializer;

    /**
     * Channel handler.
     */
    private ChannelInboundHandlerAdapter channelHandler;

    /**
     * Network managers.
     */
    private List<Object> networkMarkers;


    /**
     * Access minecraft's network managers to synchronize against as they interfere with our channel injection.
     * Initiate the channel initializers and the channel handler.
     * Access minecraft's server channels and add our own.
     */
    @Override
    public void prepare() {
        networkMarkers = NMSUtils.getNetworkMarkers();
        firstChannelInitializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(final Channel channel) {
                if (networkMarkers != null) {
                    synchronized (networkMarkers) {
                        channel.eventLoop().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    injectChannel(channel);
                                } catch (Exception ex) {
                                    //Failed
                                    channel.disconnect();
                                }
                            }
                        });
                    }
                } else {
                    channel.eventLoop().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                injectChannel(channel);
                            } catch (Exception ex) {
                                //Failed
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

        //Wrapper for the minecraft server connection.
        WrappedPacket serverConnectionWrapper = new WrappedPacket(new NMSPacket(NMSUtils.getMinecraftServerConnection()));
        boolean searching = true;
        for (int i = 0; searching; i++) {
            try {
                //Get the list.
                List<Object> serverChannelList = (List<Object>) serverConnectionWrapper.readObject(i, List.class);
                for (Object serverChannel : serverChannelList) {
                    //Is this the server channel list?
                    if (serverChannel instanceof ChannelFuture) {
                        //Yes it is...
                        Channel channel = ((ChannelFuture) serverChannel).channel();
                        serverChannels.add(channel);
                        //Add our channel handler to all the server channel pipelines.
                        channel.pipeline().addFirst(channelHandler);
                        searching = false;
                    }
                }
            } catch (Exception ex) {
                break;
            }
        }
    }

    /**
     * Remove our channel handler from all server channel pipelines from the current thread.
     */
    @Override
    public void cleanup() {
        for (Channel channel : serverChannels) {
            try {
                channel.pipeline().remove(channelHandler);
            } catch (NoSuchElementException ignored) {

            }
        }
    }

    /**
     * Inject a netty channel to listen to packets.
     * If already injected, get the channel interceptor.
     *
     * @param ch Netty channel.
     * @return {@link PlayerChannelInterceptor}
     */
    @Override
    public PlayerChannelInterceptor injectChannel(Object ch) {
        Channel channel = (Channel) ch;
        String handlerName = PacketEvents.get().getHandlerName();
        PlayerChannelInterceptor interceptor = (PlayerChannelInterceptor) channel.pipeline().get(handlerName);
        if (interceptor == null) {
            interceptor = new PlayerChannelInterceptor();
            if (channel.pipeline().get("packet_handler") != null) {
                channel.pipeline().addBefore("packet_handler", handlerName, interceptor);
            } else {
                throw new IllegalStateException("Failed to inject an incoming channel due to \"packet_handler\" not being added to the pipeline! Let them rejoin!");
            }
        }
        return interceptor;
    }

    /**
     * Eject a netty channel.
     *
     * @param ch Netty channel.
     */
    @Override
    public void ejectChannel(Object ch) {
        Channel channel = (Channel) ch;
        String handlerName = PacketEvents.get().getHandlerName();
        if (channel.pipeline().get(handlerName) != null) {
            channel.pipeline().remove(handlerName);
        }
    }

    /**
     * Inject a player on the current thread.
     *
     * @param player Target player.
     */
    @Override
    public void injectPlayerSync(Player player) {
        Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player);
        injectChannel(channel).player = player;
    }

    /**
     * Eject a player on the current thread.
     *
     * @param player Target player.
     */
    @Override
    public void ejectPlayerSync(Player player) {
        Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player);
        ejectChannel(channel);
        PacketEvents.get().getPlayerUtils().clientVersionsMap.remove(player.getAddress());
        PacketEvents.get().getPlayerUtils().tempClientVersionMap.remove(player.getAddress());
    }

    /**
     * Inject a player on our custom inject and eject (fixed) thread pool (=asynchronously).
     *
     * @param player Target player.
     */
    @Override
    public void injectPlayerAsync(Player player) {
        PacketEvents.get().injectAndEjectExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player);
                injectChannel(channel).player = player;
            }
        });
    }

    /**
     * Eject a player on our custom inject and eject (fixed) thread pool (=asynchronously).
     *
     * @param player Target player.
     */
    @Override
    public void ejectPlayerAsync(Player player) {
        PacketEvents.get().injectAndEjectExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Channel channel = (Channel) PacketEvents.get().packetHandlerInternal.getChannel(player);
                ejectChannel(channel);
                PacketEvents.get().packetHandlerInternal.keepAliveMap.remove(player.getUniqueId());
                PacketEvents.get().packetHandlerInternal.channelMap.remove(player.getName());
                PacketEvents.get().getPlayerUtils().clientVersionsMap.remove(player.getAddress());
                PacketEvents.get().getPlayerUtils().tempClientVersionMap.remove(player.getAddress());
            }
        });
    }

    /**
     * Send a raw NMS packet to a netty channel.
     *
     * @param ch     Netty channel.
     * @param packet Raw NMS Packet.
     */
    @Override
    public void sendPacket(Object ch, Object packet) {
        Channel channel = (Channel) ch;
        channel.writeAndFlush(packet);
    }

    /**
     * Player channel interceptor.
     *
     * @author retrooper
     * @since 1.8
     */
    public static class PlayerChannelInterceptor extends ChannelDuplexHandler {
        /**
         * Associated player.
         * This is null until you inject the player.
         * (We inject the PLAYER by PlayerLoginEvent or PlayerJoinEvent depending on your settings)
         */
        public volatile Player player;

        /**
         * Incoming packet interception.
         *
         * @param ctx    Netty channel handler context.
         * @param packet Raw NMS Packet.
         * @throws Exception Possible exception.
         */
        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object packet) throws Exception {
            packet = PacketEvents.get().packetHandlerInternal.read(player, ctx.channel(), packet);
            if (packet != null) {
                super.channelRead(ctx, packet);
                PacketEvents.get().packetHandlerInternal.postRead(player, ctx.channel(), packet);
            }
        }

        /**
         * Outgoing packet interception.
         *
         * @param ctx     Netty channel handler context.
         * @param packet  Raw NMS Packet
         * @param promise Netty channel promise.
         * @throws Exception Possible exception.
         */
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
