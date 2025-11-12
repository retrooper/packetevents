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

package com.github.retrooper.packetevents.wrapper.status.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusServerPong extends PacketWrapper<WrapperStatusServerPong> {
    private long time;

    public WrapperStatusServerPong(PacketSendEvent event) {
        super(event);
    }

    public WrapperStatusServerPong(long time) {
        super(PacketType.Status.Server.PONG);
        this.time = time;
    }

    @Override
    public void read() {
        this.time = readLong();
    }

    @Override
    public void write() {
        writeLong(time);
    }

    @Override
    public void copy(WrapperStatusServerPong wrapper) {
        this.time = wrapper.time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
