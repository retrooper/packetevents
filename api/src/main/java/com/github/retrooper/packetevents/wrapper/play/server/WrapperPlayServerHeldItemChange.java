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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerHeldItemChange extends PacketWrapper<WrapperPlayServerHeldItemChange> {
    private int slot;

    public WrapperPlayServerHeldItemChange(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerHeldItemChange(int slot) {
        super(PacketType.Play.Server.HELD_ITEM_CHANGE);
        this.slot = slot;
    }

    @Override
    public void read() {
        this.slot = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)
                ? this.readVarInt() : this.readByte();
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
            this.writeVarInt(this.slot);
        } else {
            this.writeByte(this.slot);
        }
    }

    @Override
    public void copy(WrapperPlayServerHeldItemChange wrapper) {
        this.slot = wrapper.slot;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
