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

package com.github.retrooper.packetevents.manager.server;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.util.PacketTransformationUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface ServerManager {
    /**
     * Get the server version.
     *
     * @return Get Server Version
     */
    ServerVersion getVersion();

    /**
     * Get the operating system of the local machine
     *
     * @return Get Operating System
     */
    default SystemOS getOS() {
        return SystemOS.getOS();
    }

    void receivePacket(ChannelAbstract channel, ByteBufAbstract byteBuf);

    default void receivePacket(Object player, ByteBufAbstract byteBuf) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        receivePacket(channel, byteBuf);
    }

    default void receivePacket(Object player, PacketWrapper<?> wrapper) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        receivePacket(channel, wrapper);
    }

    default void receivePacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        for (PacketWrapper<?> packet : wrappers) {
            packet.prepareForSend();
            receivePacket(channel, packet.buffer);
        }
    }
}
