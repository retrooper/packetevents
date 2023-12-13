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
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityMetadataProvider;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class WrapperPlayServerEntityMetadata extends PacketWrapper<WrapperPlayServerEntityMetadata> {
    private int entityID;
    private List<EntityData> entityMetadata;

    public WrapperPlayServerEntityMetadata(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityMetadata(int entityID, List<EntityData> entityMetadata) {
        super(PacketType.Play.Server.ENTITY_METADATA);
        this.entityID = entityID;
        this.entityMetadata = entityMetadata;
    }

    public WrapperPlayServerEntityMetadata(int entityID, EntityMetadataProvider metadata) {
        this(entityID, metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
    }

    @Override
    public void read() {
        entityID = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) ? readVarInt() : readInt();
        entityMetadata = readEntityMetadata();
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            writeVarInt(entityID);
        } else {
            writeInt(entityID);
        }
        writeEntityMetadata(entityMetadata);
    }

    @Override
    public void copy(WrapperPlayServerEntityMetadata wrapper) {
        entityID = wrapper.entityID;
        entityMetadata = wrapper.entityMetadata;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public List<EntityData> getEntityMetadata() {
        return entityMetadata;
    }

    public void setEntityMetadata(List<EntityData> entityMetadata) {
        this.entityMetadata = entityMetadata;
    }

    public void setEntityMetadata(EntityMetadataProvider metadata) {
        this.entityMetadata = metadata.entityData(serverVersion.toClientVersion());
    }
}
