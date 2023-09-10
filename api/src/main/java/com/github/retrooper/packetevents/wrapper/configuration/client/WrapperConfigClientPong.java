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

package com.github.retrooper.packetevents.wrapper.configuration.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigClientPong extends PacketWrapper<WrapperConfigClientPong> {

    private int id;

    public WrapperConfigClientPong(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperConfigClientPong(int id) {
        super(PacketType.Configuration.Client.PONG);
        this.id = id;
    }

    @Override
    public void read() {
        this.id = this.readInt();
    }

    @Override
    public void write() {
        this.writeInt(this.id);
    }

    @Override
    public void copy(WrapperConfigClientPong wrapper) {
        this.id = wrapper.id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
