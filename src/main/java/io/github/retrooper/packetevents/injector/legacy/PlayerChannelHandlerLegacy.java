/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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
            packet = PacketEvents.get().getInternalPacketProcessor().read(player, ctx.channel(), packet);
            if (packet != null) {
                super.channelRead(ctx, packet);
                PacketEvents.get().getInternalPacketProcessor().postRead(player, ctx.channel(), packet);
            }
        } catch (Throwable t) {
            PacketEvents.get().getPlugin().getLogger().log(Level.SEVERE, "Unable to handle an incoming packet ", t.getMessage());
        }
    }

    @Override
    public void write(final ChannelHandlerContext ctx, Object packet, final ChannelPromise promise) throws Exception {
        try {
            packet = PacketEvents.get().getInternalPacketProcessor().write(player, ctx.channel(), packet);
            if (packet != null) {
                super.write(ctx, packet, promise);
                PacketEvents.get().getInternalPacketProcessor().postWrite(player, ctx.channel(), packet);
            }
        } catch (Throwable t) {
            PacketEvents.get().getPlugin().getLogger().log(Level.SEVERE, "Unable to handle an outgoing packet ", t.getMessage());
        }
    }
}
