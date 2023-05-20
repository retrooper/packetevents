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

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.TileEntityType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockEntityData extends PacketWrapper<WrapperPlayServerBlockEntityData> {

    private Vector3i position;
    private int type;
    private NBTCompound nbt;

    public WrapperPlayServerBlockEntityData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockEntityData(Vector3i position, int type, NBTCompound nbt) {
        super(PacketType.Play.Server.BLOCK_ENTITY_DATA);
        this.position = position;
        this.type = type;
        this.nbt = nbt;
    }

    public WrapperPlayServerBlockEntityData(Vector3i position, TileEntityType type, NBTCompound nbt) {
        this(position, type.getId(), nbt);
    }

    @Override
    public void read() {
        this.position = readBlockPosition();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.type = readVarInt();
        } else {
            this.type = readUnsignedByte();
        }
        this.nbt = readNBT();
    }

    @Override
    public void write() {
        writeBlockPosition(this.position);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            writeVarInt(this.type);
        } else {
            writeByte(this.type);
        }
        writeNBT(this.nbt);
    }

    @Override
    public void copy(WrapperPlayServerBlockEntityData wrapper) {
        this.position = wrapper.position;
        this.type = wrapper.type;
        this.nbt = wrapper.nbt;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public TileEntityType getAsTileType() {
        return TileEntityType.getById(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(TileEntityType type) {
        this.type = type.getId();
    }

    public NBTCompound getNBT() {
        return nbt;
    }

    public void setNBT(NBTCompound nbt) {
        this.nbt = nbt;
    }

}
