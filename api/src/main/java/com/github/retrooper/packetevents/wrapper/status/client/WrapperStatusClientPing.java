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

package com.github.retrooper.packetevents.wrapper.status.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.data.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusClientPing extends PacketWrapper<WrapperStatusClientPing> {
    private long time;

    public WrapperStatusClientPing(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperStatusClientPing(long time) {
        super(PacketType.Status.Client.PING.getID(), ClientVersion.UNKNOWN);
        this.time = time;
    }

    @Override
    public void readData() {
        this.time = readLong();
    }

    @Override
    public void readData(WrapperStatusClientPing wrapper) {
        this.time = wrapper.time;
    }

    @Override
    public void writeData() {
        writeLong(time);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
