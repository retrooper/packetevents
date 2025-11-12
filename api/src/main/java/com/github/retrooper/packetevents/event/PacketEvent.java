/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.event;


import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.TimeStampMode;

/**
 * An event in both of PacketEvents' event systems.
 *
 * @author retrooper
 * @since 1.2.6
 */
public abstract class PacketEvent implements CallableEvent {
    private final long timestamp;

    public PacketEvent() {
        TimeStampMode timeStampMode = PacketEvents.getAPI().getSettings()
                .getTimeStampMode();
        switch (timeStampMode) {
            case MILLIS:
                timestamp = System.currentTimeMillis();
                break;
            case NANO:
                timestamp = System.nanoTime();
                break;
                //AKA NONE:
            default:
                timestamp = 0L;
        }
    }

    /**
     * Timestamp of when the PacketEvent was created.
     * Basically timestamp of the packet.
     *
     * @return Packet timestamp in milliseconds.
     */
    public long getTimestamp() {
        return timestamp;
    }

    public void callPacketEventExternal(PacketListenerCommon listener) {
        listener.onPacketEventExternal(this);
    }
}