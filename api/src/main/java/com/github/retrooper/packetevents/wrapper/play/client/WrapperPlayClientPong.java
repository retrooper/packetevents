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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;

/**
 * A response to the ping packet.
 * The vanilla server doesn't seem to send the Ping packet.
 * Most likely added as a replacement to the removed Window Confirmation packet.
 *
 * @see WrapperPlayServerPing
 */
public class WrapperPlayClientPong extends PacketWrapper<WrapperPlayClientPong> {
    private int id;

    public WrapperPlayClientPong(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPong(int id) {
        super(PacketType.Play.Client.PONG);
        this.id = id;
    }

    @Override
    public void read() {
        this.id = readInt();
    }

    @Override
    public void write() {
        writeInt(id);
    }

    @Override
    public void copy(WrapperPlayClientPong wrapper) {
        this.id = wrapper.id;
    }

    /**
     * ID of the last sent Ping packet.
     *
     * @return ID
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
