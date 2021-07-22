/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.injector.legacy.late;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.LateInjector;
import io.github.retrooper.packetevents.injector.legacy.PlayerChannelHandlerLegacy;
import net.minecraft.util.io.netty.channel.Channel;
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
        PlayerChannelHandlerLegacy playerChannelHandlerLegacy = new PlayerChannelHandlerLegacy();
        playerChannelHandlerLegacy.player = player;
        Channel channel = (Channel) PacketEvents.get().getPlayerUtils().getChannel(player);
        channel.pipeline().addBefore("packet_handler", PacketEvents.get().getHandlerName(), playerChannelHandlerLegacy);
    }

    @Override
    public void ejectPlayer(Player player) {
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel != null) {
            try {
                ((Channel) channel).pipeline().remove(PacketEvents.get().getHandlerName());
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        Channel channel = (Channel) PacketEvents.get().getPlayerUtils().getChannel(player);
        return channel.pipeline().get(PacketEvents.get().getHandlerName()) != null;
    }

    @Override
    public void sendPacket(Object rawChannel, Object packet) {
        Channel channel = (Channel) rawChannel;
        channel.pipeline().writeAndFlush(packet);
    }

    @Override
    public void sendPacketWithoutFlush(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        channel.write(rawNMSPacket);
    }
}