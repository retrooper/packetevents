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

package com.github.retrooper.packetevents.wrapper.configuration.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerKeepAlive extends PacketWrapper<WrapperConfigServerKeepAlive> {

    private long id;

    public WrapperConfigServerKeepAlive(PacketSendEvent event) {
        super(event);
    }

    public WrapperConfigServerKeepAlive(long id) {
        super(PacketType.Configuration.Server.KEEP_ALIVE);
        this.id = id;
    }

    @Override
    public void read() {
        this.id = this.readLong();
    }

    @Override
    public void write() {
        this.writeLong(this.id);
    }

    @Override
    public void copy(WrapperConfigServerKeepAlive wrapper) {
        this.id = wrapper.id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
