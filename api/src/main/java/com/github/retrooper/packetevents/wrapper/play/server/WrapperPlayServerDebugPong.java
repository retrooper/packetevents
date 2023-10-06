/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDebugPong extends PacketWrapper<WrapperPlayServerDebugPong> {

    private long timestamp;

    public WrapperPlayServerDebugPong(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDebugPong(long timestamp) {
        super(PacketType.Play.Server.DEBUG_PONG);
        this.timestamp = timestamp;
    }

    @Override
    public void read() {
        this.timestamp = this.readLong();
    }

    @Override
    public void write() {
        this.writeLong(this.timestamp);
    }

    @Override
    public void copy(WrapperPlayServerDebugPong wrapper) {
        this.timestamp = wrapper.timestamp;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
