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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.TileEntityType;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityType;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockEntityData extends PacketWrapper<WrapperPlayServerBlockEntityData> {

    private Vector3i position;
    private BlockEntityType type;
    private NBTCompound nbt;

    public WrapperPlayServerBlockEntityData(PacketSendEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperPlayServerBlockEntityData(Vector3i position, TileEntityType type, NBTCompound nbt) {
        this(position, type.getId(), nbt);
    }

    @Deprecated
    public WrapperPlayServerBlockEntityData(Vector3i position, int type, NBTCompound nbt) {
        this(position, BlockEntityTypes.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), type), nbt);
    }

    public WrapperPlayServerBlockEntityData(Vector3i position, BlockEntityType type, NBTCompound nbt) {
        super(PacketType.Play.Server.BLOCK_ENTITY_DATA);
        this.position = position;
        this.type = type;
        this.nbt = nbt;
    }

    @Override
    public void read() {
        this.position = this.readBlockPosition();
        int typeId = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)
                ? this.readVarInt() : this.readUnsignedByte();
        this.type = BlockEntityTypes.getById(this.serverVersion.toClientVersion(), typeId);
        this.nbt = this.readNBT();
    }

    @Override
    public void write() {
        this.writeBlockPosition(this.position);
        int typeId = this.type.getId(this.serverVersion.toClientVersion());
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.writeVarInt(typeId);
        } else {
            this.writeByte(typeId);
        }
        this.writeNBT(this.nbt);
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

    @Deprecated
    public int getType() {
        return this.type.getId(this.serverVersion.toClientVersion());
    }

    public BlockEntityType getBlockEntityType() {
        return this.type;
    }

    @Deprecated
    public TileEntityType getAsTileType() {
        return TileEntityType.getById(this.getType());
    }

    @Deprecated
    public void setType(int type) {
        this.setType(BlockEntityTypes.getById(this.serverVersion.toClientVersion(), type));
    }

    public void setType(BlockEntityType blockEntityType) {
        this.type = blockEntityType;
    }

    @Deprecated
    public void setType(TileEntityType type) {
        this.setType(type.getId());
    }

    public NBTCompound getNBT() {
        return nbt;
    }

    public void setNBT(NBTCompound nbt) {
        this.nbt = nbt;
    }

}
