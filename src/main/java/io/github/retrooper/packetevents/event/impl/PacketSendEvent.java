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

package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.ProtocolPacketEvent;
import io.github.retrooper.packetevents.protocol.PacketSide;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import org.bukkit.entity.Player;

public class PacketSendEvent extends ProtocolPacketEvent {
   public PacketSendEvent(Object channel, Player player, ByteBufAbstract byteBuf) {
        super(PacketSide.SERVER, channel, player, byteBuf, true);
    }

    public PacketSendEvent(Object channel, Player player, Object rawByteBuf) {
        super(PacketSide.SERVER, channel, player, rawByteBuf, true);
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketSend(this);
    }
}
