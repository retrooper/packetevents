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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockChange extends PacketWrapper<WrapperPlayServerBlockChange> {
    private Vector3i blockPosition;
    private int blockID;
    public WrapperPlayServerBlockChange(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockChange(Vector3i blockPosition, int blockID) {
        super(PacketType.Play.Server.BLOCK_CHANGE);
        this.blockPosition = blockPosition;
        this.blockID = blockID;
    }

    @Override
    public void readData() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            blockPosition = new Vector3i(readInt(), readUnsignedByte(), readInt());
            int block = readVarInt();
            int blockData = readUnsignedByte();
        }
        else {
            blockPosition = readBlockPosition()
            int block = readVarInt();
        }
    }

    @Override
    public void readData(WrapperPlayServerBlockChange wrapper) {
        super.readData(wrapper);
    }

    @Override
    public void writeData() {
        super.writeData();
    }
}
