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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateEntityNBT extends PacketWrapper<WrapperPlayServerUpdateEntityNBT> {
    private int entityId;
    private NBTCompound nbtCompound;

    public WrapperPlayServerUpdateEntityNBT(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateEntityNBT(int entityId, NBTCompound nbtCompound) {
        super(PacketType.Play.Server.UPDATE_ENTITY_NBT);
        this.entityId = entityId;
        this.nbtCompound = nbtCompound;
    }

    @Override
    public void read() {
        this.entityId = readVarInt();
        this.nbtCompound = readNBT();
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        writeNBT(nbtCompound);
    }

    @Override
    public void copy(WrapperPlayServerUpdateEntityNBT wrapper) {
        this.entityId = wrapper.entityId;
        this.nbtCompound = wrapper.nbtCompound;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public NBTCompound getNbtCompound() {
        return nbtCompound;
    }

    public void setNbtCompound(NBTCompound nbtCompound) {
        this.nbtCompound = nbtCompound;
    }
}
