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

package io.github.retrooper.packetevents.injector.modern.late;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.injector.LateInjector;
import io.github.retrooper.packetevents.injector.modern.PlayerChannelHandlerModern;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

public class LateChannelInjectorModern implements LateInjector {
    @Override
    public void inject() {

    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Player player) {
        PlayerChannelHandlerModern playerChannelHandlerModern = new PlayerChannelHandlerModern();
        playerChannelHandlerModern.player = player;
        Channel channel = (Channel) PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel != null) {
            channel.eventLoop().execute(() -> {
                channel.pipeline().addBefore("packet_handler", PacketEvents.get().getHandlerName(), playerChannelHandlerModern);
            });
        }
    }

    @Override
    public void ejectPlayer(Player player) {
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        if (channel != null) {
            try {
                ((Channel) channel).pipeline().remove(PacketEvents.get().getHandlerName());
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        Channel channel = (Channel) PacketEvents.get().getPlayerUtils().getChannel(player);
        return channel != null && channel.pipeline().get(PacketEvents.get().getHandlerName()) != null;
    }

    @Override
    public void writePacket(Object ch, Object rawNMSPacket) {
        Channel channel = (Channel) ch;
        //Don't write packets to fake channels
        if (ClassUtil.getClassSimpleName(channel.getClass()).equals("FakeChannel")
                || ClassUtil.getClassSimpleName(channel.getClass()).equals("SpoofedChannel")) {
            return;
        }
        channel.write(rawNMSPacket);
    }

    @Override
    public void flushPackets(Object ch) {
        Channel channel = (Channel) ch;
        //Don't flush packets for fake channels
        if (ClassUtil.getClassSimpleName(channel.getClass()).equals("FakeChannel")
                || ClassUtil.getClassSimpleName(channel.getClass()).equals("SpoofedChannel")) {
            return;
        }
        channel.flush();
    }

    @Override
    public void sendPacket(Object rawChannel, Object packet) {
        Channel channel = (Channel) rawChannel;
        //Don't send packets to fake channels
        if (ClassUtil.getClassSimpleName(channel.getClass()).equals("FakeChannel")
                || ClassUtil.getClassSimpleName(channel.getClass()).equals("SpoofedChannel")) {
            return;
        }
        channel.pipeline().writeAndFlush(packet);
    }
}