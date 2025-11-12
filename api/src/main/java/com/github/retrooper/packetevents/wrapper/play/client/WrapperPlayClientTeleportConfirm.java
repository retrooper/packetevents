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

public class WrapperPlayClientTeleportConfirm extends PacketWrapper<WrapperPlayClientTeleportConfirm> {
    private int teleportID;

    public WrapperPlayClientTeleportConfirm(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientTeleportConfirm(int teleportID) {
        super(PacketType.Play.Client.TELEPORT_CONFIRM);
        this.teleportID = teleportID;
    }

    @Override
    public void read() {
        teleportID = readVarInt();
    }

    @Override
    public void write() {
        writeVarInt(teleportID);
    }

    @Override
    public void copy(WrapperPlayClientTeleportConfirm wrapper) {
        teleportID = wrapper.teleportID;
    }

    public int getTeleportId() {
        return teleportID;
    }

    public void setTeleportId(int teleportID) {
        this.teleportID = teleportID;
    }
}
