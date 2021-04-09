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

package io.github.retrooper.packetevents.injector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PlayerChannelHandlerLegacy extends ChannelDuplexHandler {
    /**
     * Associated player.
     * This is null until the player is injected.
     */
    public volatile Player player;

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object packet) throws Exception {
        try {
            packet = PacketEvents.get().packetProcessorInternal.read(player, ctx.channel(), packet);
            if (packet != null) {
                super.channelRead(ctx, packet);
                PacketEvents.get().packetProcessorInternal.postRead(player, ctx.channel(), packet);
            }
        }
        catch (Throwable t) {
            PacketEvents.get().getPlugin().getLogger().log(Level.SEVERE, "Unable to handle an incoming packet ", t.getMessage());
        }
    }

    @Override
    public void write(final ChannelHandlerContext ctx, Object packet, final ChannelPromise promise) throws Exception {
        try {
            packet = PacketEvents.get().packetProcessorInternal.write(player, ctx.channel(), packet);
            if (packet != null) {
                super.write(ctx, packet, promise);
                PacketEvents.get().packetProcessorInternal.postWrite(player, ctx.channel(), packet);
            }
        }
        catch (Throwable t) {
            PacketEvents.get().getPlugin().getLogger().log(Level.SEVERE, "Unable to handle an outgoing packet ", t.getMessage());
        }
    }
}
