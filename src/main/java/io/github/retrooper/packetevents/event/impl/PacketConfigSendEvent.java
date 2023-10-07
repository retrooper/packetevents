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
import io.github.retrooper.packetevents.event.eventtypes.PostTaskEvent;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code PacketConfigSendEvent} event is fired whenever the a LOGIN packet is being planned to be sent to the client.
 * Use the {@link #getSocketAddress()} to identify who sends the packet.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Configuration">https://wiki.vg/Protocol#Configuration</a>
 * @since 1.8
 */
public class PacketConfigSendEvent extends CancellableNMSPacketEvent implements PostTaskEvent {
    private Runnable postTask;
    public PacketConfigSendEvent(Object channel, NMSPacket packet) {
        super(channel, packet);
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
        if (listener.serverSidedLoginAllowance == null || listener.serverSidedLoginAllowance.contains(getPacketId())) {
            listener.onPacketConfigSend(this);
        }
    }
}
