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
import io.github.retrooper.packetevents.event.eventtypes.CancellableNMSPacketEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.event.eventtypes.PostTaskEvent;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@code PacketPlaySendEvent} event is fired whenever the a PLAY packet is about to be sent to a client.
 * Cancelling this event will result in preventing minecraft from sending the packet.
 * The player won't receive the packet if you cancel it.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Play">https://wiki.vg/Protocol#Play</a>
 * @since 1.2.6
 */
public final class PacketPlaySendEvent extends CancellableNMSPacketEvent implements PlayerEvent, PostTaskEvent {
    private final Player player;
    private Runnable postTask;

    public PacketPlaySendEvent(final Player player, final Object channel, final NMSPacket packet) {
        super(channel, packet);
        this.player = player;
    }

    /**
     * This method returns the bukkit player object of the packet receiver.
     * The player object might be null during early packets.
     *
     * @return Packet receiver.
     */
    @NotNull
    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isPostTaskAvailable() {
        return postTask != null;
    }

    @Override
    public Runnable getPostTask() {
        return postTask;
    }

    @Override
    public void setPostTask(@NotNull Runnable postTask) {
        this.postTask = postTask;
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        if (listener.serverSidedPlayAllowance == null || listener.serverSidedPlayAllowance.contains(getPacketId())) {
            listener.onPacketPlaySend(this);
        }
    }
}

