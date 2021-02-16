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
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class PlayerChannelHandler8 extends ChannelDuplexHandler {
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
        packet = PacketEvents.get().packetProcessorInternal.read(player, ctx.channel(), packet);
        if (packet != null) {
            super.channelRead(ctx, packet);
            PacketEvents.get().packetProcessorInternal.postRead(player, ctx.channel(), packet);
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
        packet = PacketEvents.get().packetProcessorInternal.write(player, ctx.channel(), packet);
        if (packet != null) {
            super.write(ctx, packet, promise);
            PacketEvents.get().packetProcessorInternal.postWrite(player, ctx.channel(), packet);
        }
    }
}
