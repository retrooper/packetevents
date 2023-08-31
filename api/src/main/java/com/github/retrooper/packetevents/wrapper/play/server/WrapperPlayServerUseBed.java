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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUseBed extends PacketWrapper<WrapperPlayServerUseBed> {
    private int entityId;
    private Vector3i position;

    public WrapperPlayServerUseBed(PacketSendEvent event) {
        super(event);
    }

    /**
     * This packet is for 1.12 and older only, please use entity metadata for newer versions
     *
     * @param entityId The entity of the sleeping player, does not have to be the player you send this to
     * @param position The block position of the head of the bed
     */
    public WrapperPlayServerUseBed(int entityId, Vector3i position) {
        super(PacketType.Play.Server.USE_BED);
        this.entityId = entityId;
        this.position = position;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            entityId = readVarInt();
            position = readBlockPosition();
        } else {
            entityId = readInt();
            int x = readInt();
            int y = readUnsignedByte();
            int z = readInt();
            position = new Vector3i(x, y, z);
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            writeVarInt(entityId);
            writeBlockPosition(position);
        } else {
            writeInt(entityId);
            writeInt(position.getX());
            writeByte(position.getY());
            writeInt(position.getZ());
        }
    }

    @Override
    public void copy(WrapperPlayServerUseBed wrapper) {
        wrapper.entityId = entityId;
        wrapper.position = position;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }
}
