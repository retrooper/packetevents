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
import com.github.retrooper.packetevents.manager.server.MultiVersion;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerSpawnEntity extends PacketWrapper<WrapperPlayServerSpawnEntity> {
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private static final double VELOCITY_FACTOR = 8000.0;
    private int entityId;
    private @Nullable UUID uuid;
    private EntityType entityType;
    private Vector3d position;
    private float pitch;
    private float yaw;
    private float headYaw;
    private int data;
    private @Nullable Vector3d velocity;

    public WrapperPlayServerSpawnEntity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnEntity(int entityId, EntityType entityType, Vector3d position, float pitch,
                                        float yaw, float headYaw, int data) {
        this(entityId, null, entityType, position, pitch, yaw, headYaw, data, null);
    }

    public WrapperPlayServerSpawnEntity(int entityId, @Nullable UUID uuid, EntityType entityType, Vector3d position, float pitch,
                                        float yaw, float headYaw, int data, @Nullable Vector3d velocity) {
        super(PacketType.Play.Server.SPAWN_ENTITY);
        this.entityId = entityId;
        this.uuid = uuid;
        this.entityType = entityType;
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.headYaw = headYaw;
        this.data = data;
        this.velocity = velocity;
    }

    @Override
    public void read() {
        boolean v1_9 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);

        entityId = readVarInt();
        if (v1_9) {
            uuid = readUUID();
        }

        entityType = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_14, packetWrapper -> EntityTypes.getById(serverVersion.toClientVersion(), packetWrapper.readVarInt()), packetWrapper -> EntityTypes.getById(serverVersion.toClientVersion(), packetWrapper.readByte()));

        position = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_9, packetWrapper -> {
            double x = packetWrapper.readDouble();
            double y = packetWrapper.readDouble();
            double z = packetWrapper.readDouble();
            return new Vector3d(x, y, z);
        }, packetWrapper -> {
            double x = packetWrapper.readDouble() / 32.0;
            double y = packetWrapper.readDouble() / 32.0;
            double z = packetWrapper.readDouble() / 32.0;
            return new Vector3d(x, y, z);
        });

        readMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_15, packetWrapper -> {
            pitch = packetWrapper.readByte() / ROTATION_FACTOR;
            yaw = packetWrapper.readByte() / ROTATION_FACTOR;
        }, packetWrapper -> {
            yaw = packetWrapper.readByte() / ROTATION_FACTOR;
            pitch = packetWrapper.readByte() / ROTATION_FACTOR;
        });

        readMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_19, packetWrapper -> {
            headYaw = packetWrapper.readByte() / ROTATION_FACTOR;
            data = packetWrapper.readVarInt();
        }, packetWrapper -> data = packetWrapper.readInt());

        // On 1.8 check if data > 0 before reading, or it won't be in the packet
        if (v1_9 || data > 0) {
            double velX = readShort() / VELOCITY_FACTOR;
            double velY = readShort() / VELOCITY_FACTOR;
            double velZ = readShort() / VELOCITY_FACTOR;
            velocity = new Vector3d(velX, velY, velZ);
        }
    }

    @Override
    public void write() {
        boolean v1_9 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);

        writeVarInt(entityId);
        if (v1_9) {
            writeUUID(uuid);
        }

        writeMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_14, entityType,
                (packetWrapper, fieldType) -> packetWrapper.writeVarInt(fieldType.getId(serverVersion.toClientVersion())),
                (packetWrapper, type) -> {
                    // Will always be true if they use correct EntityTypes for this packet
                    if (type.getLegacyId(serverVersion.toClientVersion()) != -1) {
                        packetWrapper.writeByte(type.getLegacyId(serverVersion.toClientVersion()));
                    } else {
                        packetWrapper.writeByte(type.getId(serverVersion.toClientVersion()));
                    }
                });

        writeMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_9, packetWrapper -> {
            packetWrapper.writeDouble(position.x);
            packetWrapper.writeDouble(position.y);
            packetWrapper.writeDouble(position.z);
        }, packetWrapper -> {
            packetWrapper.writeInt(MathUtil.floor(position.x * 32.0));
            packetWrapper.writeInt(MathUtil.floor(position.y * 32.0));
            packetWrapper.writeInt(MathUtil.floor(position.z * 32.0));
        });

        writeMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_15, packetWrapper -> {
            packetWrapper.writeByte(MathUtil.floor(pitch * ROTATION_FACTOR));
            packetWrapper.writeByte(MathUtil.floor(yaw * ROTATION_FACTOR));
        }, packetWrapper -> {
            packetWrapper.writeByte(MathUtil.floor(yaw * ROTATION_FACTOR));
            packetWrapper.writeByte(MathUtil.floor(pitch * ROTATION_FACTOR));
        });

        writeMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_19, packetWrapper -> {
            packetWrapper.writeByte(MathUtil.floor(headYaw * ROTATION_FACTOR));
            packetWrapper.writeVarInt(data);
        }, packetWrapper -> packetWrapper.writeInt(data));

        // On 1.8 check if data > 0 before reading, or it won't be in the packet
        if (v1_9 || data > 0) {
            Vector3d vel = velocity == null ? new Vector3d(-1, -1, -1) : velocity;
            int velX = (int) (vel.x * VELOCITY_FACTOR);
            int velY = (int) (vel.y * VELOCITY_FACTOR);
            int velZ = (int) (vel.z * VELOCITY_FACTOR);
            writeShort(velX);
            writeShort(velY);
            writeShort(velZ);
        }
    }

    @Override
    public void copy(WrapperPlayServerSpawnEntity wrapper) {
        entityId = wrapper.entityId;
        uuid = wrapper.uuid;
        entityType = wrapper.entityType;
        position = wrapper.position;
        pitch = wrapper.pitch;
        yaw = wrapper.yaw;
        headYaw = wrapper.headYaw;
        data = wrapper.data;
        velocity = wrapper.velocity;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Optional<UUID> getUUID() {
        return Optional.ofNullable(uuid);
    }

    public void setUUID(@Nullable UUID uuid) {
        this.uuid = uuid;
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

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Optional<Vector3d> getVelocity() {
        return Optional.ofNullable(velocity);
    }

    public void setVelocity(@Nullable Vector3d velocity) {
        this.velocity = velocity;
    }
}
