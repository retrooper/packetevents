/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.handler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.NMSUtils;
import net.minecraft.util.io.netty.channel.*;
import org.bukkit.entity.Player;

import java.util.concurrent.Future;

final class NettyPacketHandler_7 {

    public static void injectPlayer(final Player player) {
        final ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                Object packet = NettyPacketHandler.read(player, msg);
                if (packet == null) {
                    return;
                }
                super.channelRead(ctx, msg);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                Object packet = NettyPacketHandler.write(player, msg);
                if (packet == null) {
                    return;
                }
                super.write(ctx, msg, promise);
            }
        };
        final ChannelPipeline pipeline = ((Channel) NMSUtils.getChannel(player)).pipeline();
        pipeline.addBefore("packet_handler", PacketEvents.getHandlerName(player.getName()), channelDuplexHandler);
    }

    public static void uninjectPlayer(final Player player) {
        final Channel channel = (Channel) NMSUtils.getChannel(player);
        channel.pipeline().remove(PacketEvents.getHandlerName(player.getName()));
    }

    public static Future<?> uninjectPlayerAsync(final Player player) {
        final Channel channel = (Channel) NMSUtils.getChannel(player);
        return channel.eventLoop().submit(() -> {
            channel.pipeline().remove(PacketEvents.getHandlerName(player.getName()));
        });
    }
}