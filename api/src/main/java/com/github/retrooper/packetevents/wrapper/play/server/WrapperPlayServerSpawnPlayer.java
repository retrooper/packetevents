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
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerSpawnPlayer extends PacketWrapper<WrapperPlayServerSpawnPlayer> {
    private static final float ROTATION_DIVISOR = 256.0F / 360.0F;
    private int entityID;
    private UUID uuid;
    private Vector3d position;
    private float yaw;
    private float pitch;

    @Deprecated
    private ItemType item;
    private List<EntityData> entityMetadata;

    public WrapperPlayServerSpawnPlayer(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnPlayer(int entityId, UUID uuid, Location location, EntityMetadataProvider metadata) {
        this(entityId, uuid, location.getPosition(), location.getYaw(), location.getPitch(), metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
    }

    public WrapperPlayServerSpawnPlayer(int entityId, UUID uuid, Location location, List<EntityData> entityMetadata) {
        this(entityId, uuid, location.getPosition(), location.getYaw(), location.getPitch(), entityMetadata);
    }

    public WrapperPlayServerSpawnPlayer(int entityId, UUID uuid, Location location, EntityData... entityMetadata) {
        this(entityId, uuid, location.getPosition(), location.getYaw(), location.getPitch(), Arrays.asList(entityMetadata));
    }

    public WrapperPlayServerSpawnPlayer(int entityID, UUID uuid, Vector3d position, float yaw, float pitch, List<EntityData> entityMetadata) {
        super(PacketType.Play.Server.SPAWN_PLAYER);
        this.entityID = entityID;
        this.uuid = uuid;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.entityMetadata = entityMetadata;
        this.item = ItemTypes.AIR;
    }

    public WrapperPlayServerSpawnPlayer(int entityID, UUID uuid, Vector3d position, float yaw, float pitch, EntityMetadataProvider metadata) {
        this(entityID, uuid, position, yaw, pitch, metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
    }

    @Override
    public void read() {
        entityID = readVarInt();
        uuid = readUUID();
        boolean v1_9 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
        if (v1_9) {
            position = new Vector3d(readDouble(), readDouble(), readDouble());
        } else {
            position = new Vector3d(readInt() / 32.0, readInt() / 32.0, readInt() / 32.0);
        }
        yaw = readByte() / ROTATION_DIVISOR;
        pitch = readByte() / ROTATION_DIVISOR;
        if (!v1_9) {
            item = ItemTypes.getById(serverVersion.toClientVersion(), readShort());
        } else {
            item = ItemTypes.AIR;
        }
        if (serverVersion.isOlderThan(ServerVersion.V_1_15)) {
            entityMetadata = readEntityMetadata();
        } else {
            entityMetadata = new ArrayList<>();
        }
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        writeUUID(uuid);
        boolean v1_9 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
        if (v1_9) {
            writeDouble(position.getX());
            writeDouble(position.getY());
            writeDouble(position.getZ());
        } else {
            writeInt(MathUtil.floor(position.getX() * 32.0));
            writeInt(MathUtil.floor(position.getY() * 32.0));
            writeInt(MathUtil.floor(position.getZ() * 32.0));
        }
        writeByte((byte) (yaw * ROTATION_DIVISOR));
        writeByte((byte) (pitch * ROTATION_DIVISOR));
        if (!v1_9) {
            writeShort(item.getId(serverVersion.toClientVersion()));
        }
        if (serverVersion.isOlderThan(ServerVersion.V_1_15)) {
            writeEntityMetadata(entityMetadata);
        }
    }

    @Override
    public void copy(WrapperPlayServerSpawnPlayer wrapper) {
        entityID = wrapper.entityID;
        uuid = wrapper.uuid;
        position = wrapper.position;
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Deprecated
    public List<EntityData> getEntityMetadata() {
        return entityMetadata;
    }

    @Deprecated
    public void setEntityMetadata(List<EntityData> entityMetadata) {
        this.entityMetadata = entityMetadata;
    }

    @Deprecated
    public void setEntityMetadata(EntityMetadataProvider metadata) {
        this.entityMetadata = metadata.entityData(serverVersion.toClientVersion());
    }

    @Deprecated
    public ItemType getItem() {
        return item;
    }

    @Deprecated
    public void setItem(ItemType item) {
        this.item = item;
    }
}