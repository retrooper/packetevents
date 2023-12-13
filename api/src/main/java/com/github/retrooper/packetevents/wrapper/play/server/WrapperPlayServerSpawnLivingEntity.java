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
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerSpawnLivingEntity extends PacketWrapper<WrapperPlayServerSpawnLivingEntity> {
    private static final double POSITION_FACTOR = 32.0;
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private static final double VELOCITY_FACTOR = 8000.0;
    private int entityID;
    private UUID entityUUID;
    private EntityType entityType;
    private Vector3d position;
    private float yaw;
    private float pitch;
    private float headPitch;
    private Vector3d velocity;
    private List<EntityData> entityMetadata;

    public WrapperPlayServerSpawnLivingEntity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnLivingEntity(int entityID, UUID entityUUID, EntityType entityType, Vector3d position, float yaw, float pitch, float headPitch, Vector3d velocity, List<EntityData> entityMetadata) {
        super(PacketType.Play.Server.SPAWN_LIVING_ENTITY);
        this.entityID = entityID;
        this.entityUUID = entityUUID;
        this.entityType = entityType;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headPitch = headPitch;
        this.velocity = velocity;
        this.entityMetadata = entityMetadata;
    }

    public WrapperPlayServerSpawnLivingEntity(int entityID, UUID entityUUID, EntityType entityType, Vector3d position, float yaw, float pitch, float headPitch, Vector3d velocity, EntityMetadataProvider metadata) {
        this(entityID, entityUUID, entityType, position, yaw, pitch, headPitch, velocity, metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
    }

    public WrapperPlayServerSpawnLivingEntity(int entityId, UUID entityUUID,
                                              EntityType entityType, Location location, float headPitch,
                                              Vector3d velocity,
                                              List<EntityData> entityMetadata) {
        this(entityId, entityUUID, entityType, location.getPosition(), location.getYaw(), location.getPitch(),
                headPitch, velocity, entityMetadata);
    }

    public WrapperPlayServerSpawnLivingEntity(int entityId, UUID entityUUID,
                                              EntityType entityType, Location location, float headPitch,
                                              Vector3d velocity,
                                              EntityMetadataProvider metadata) {
        this(entityId, entityUUID, entityType, location.getPosition(), location.getYaw(), location.getPitch(),
                headPitch, velocity, metadata.entityData(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
    }

    @Override
    public void read() {
        this.entityID = readVarInt();
        if (serverVersion.isOlderThan(ServerVersion.V_1_9)) {
            this.entityUUID = new UUID(0L, 0L);
            int entityTypeID = readByte() & 255;
            entityType = EntityTypes.getById(serverVersion.toClientVersion(), entityTypeID);
            this.position = new Vector3d(readInt() / POSITION_FACTOR, readInt() / POSITION_FACTOR, readInt() / POSITION_FACTOR);
        } else {
            this.entityUUID = readUUID();
            int entityTypeID;
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
                entityTypeID = readVarInt();
            } else {
                entityTypeID = readUnsignedByte();
            }
            entityType = EntityTypes.getById(serverVersion.toClientVersion(), entityTypeID);
            this.position = new Vector3d(readDouble(), readDouble(), readDouble());
        }
        this.yaw = readByte() / ROTATION_FACTOR;
        this.pitch = readByte() / ROTATION_FACTOR;
        this.headPitch = readByte() / ROTATION_FACTOR;
        double velX = readShort() / VELOCITY_FACTOR;
        double velY = readShort() / VELOCITY_FACTOR;
        double velZ = readShort() / VELOCITY_FACTOR;
        this.velocity = new Vector3d(velX, velY, velZ);
        if (serverVersion.isOlderThan(ServerVersion.V_1_15)) {
            this.entityMetadata = readEntityMetadata();
        } else {
            this.entityMetadata = new ArrayList<>();
        }
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        if (serverVersion.isOlderThan(ServerVersion.V_1_9)) {
            writeByte(entityType.getId(serverVersion.toClientVersion()) & 255);
            writeInt(MathUtil.floor(position.x * POSITION_FACTOR));
            writeInt(MathUtil.floor(position.y * POSITION_FACTOR));
            writeInt(MathUtil.floor(position.z * POSITION_FACTOR));
        } else {
            writeUUID(entityUUID);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
                writeVarInt(entityType.getId(serverVersion.toClientVersion()));
            } else {
                //TODO Confirm if necessary
                writeByte(entityType.getId(serverVersion.toClientVersion()) & 255);
            }
            writeDouble(position.x);
            writeDouble(position.y);
            writeDouble(position.z);
        }
        writeByte((int) (yaw * ROTATION_FACTOR));
        writeByte((int) (pitch * ROTATION_FACTOR));
        writeByte((int) (headPitch * ROTATION_FACTOR));
        writeShort((int) (velocity.x * VELOCITY_FACTOR));
        writeShort((int) (velocity.y * VELOCITY_FACTOR));
        writeShort((int) (velocity.z * VELOCITY_FACTOR));
        if (serverVersion.isOlderThan(ServerVersion.V_1_15)) {
            writeEntityMetadata(entityMetadata);
        }
    }

    @Override
    public void copy(WrapperPlayServerSpawnLivingEntity wrapper) {
        this.entityID = wrapper.entityID;
        this.entityUUID = wrapper.entityUUID;
        this.entityType = wrapper.entityType;
        this.position = wrapper.position;
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
        this.headPitch = wrapper.headPitch;
        this.velocity = wrapper.velocity;
        this.entityMetadata = wrapper.entityMetadata;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public UUID getEntityUUID() {
        return entityUUID;
    }

    public void setEntityUUID(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
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

    public float getHeadPitch() {
        return headPitch;
    }

    public void setHeadPitch(float headPitch) {
        this.headPitch = headPitch;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
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
