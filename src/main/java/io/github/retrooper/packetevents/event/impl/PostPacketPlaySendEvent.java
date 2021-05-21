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

package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.NMSPacketEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Player;

/**
 * The {@code PostPacketPlaySendEvent} event is fired after all PacketEvents listeners finished processing
 * the {@code PacketPlaySendEvent}. This event won't be called if the PacketPlaySendEvent event was cancelled.
 * You cannot cancel this event.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Clientbound_4">https://wiki.vg/Protocol#Clientbound_4</a>
 * @since 1.7
 */
public class PostPacketPlaySendEvent extends NMSPacketEvent implements PlayerEvent {
    private final Player player;

    public PostPacketPlaySendEvent(final Player player, final Object channel, final NMSPacket packet) {
        super(channel, packet);
        this.player = player;
    }

    /**
     * This method returns the bukkit player object of the packet sender.
     *
     * @return Packet receiver.
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        if (listener.serverSidedPlayAllowance == null || listener.serverSidedPlayAllowance.contains(getPacketId())) {
            listener.onPostPacketPlaySend(this);
        }
    }
}
