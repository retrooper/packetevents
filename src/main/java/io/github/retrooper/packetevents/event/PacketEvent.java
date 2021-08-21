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

package io.github.retrooper.packetevents.event;


import io.github.retrooper.packetevents.event.type.CallableEvent;

/**
 * An event in both of PacketEvents' event systems.
 *
 * @author retrooper
 * @since 1.2.6
 */
public abstract class PacketEvent implements CallableEvent {
    private long timestamp = System.currentTimeMillis();

    /**
     * Timestamp of when the PacketEvent was created.
     * Basically timestamp of the packet.
     *
     * @return Packet timestamp in milliseconds.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for the timestamp.
     *
     * @param timestamp Packet timestamp in milliseconds.
     */
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public void callPacketEventExternal(final PacketListenerAbstract listener) {
        listener.onPacketEventExternal(this);
    }

    public boolean isInbuilt() {
        return false;
    }
}