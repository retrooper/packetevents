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
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerSpawnEntity extends PacketWrapper<WrapperPlayServerSpawnEntity> {
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private static final double VELOCITY_FACTOR = 8000.0;
    private int entityID;
    private Optional<UUID> uuid;
    private EntityType entityType;
    private Vector3d position;
    private float pitch;
    private float yaw;
    private int data;
    private Optional<Vector3d> velocity;

    public WrapperPlayServerSpawnEntity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnEntity(int entityID, Optional<UUID> uuid, EntityType entityType, Vector3d position, float pitch, float yaw, int data, Optional<Vector3d> velocity) {
        super(PacketType.Play.Server.SPAWN_ENTITY);
        this.entityID = entityID;
        this.uuid = uuid;
        this.entityType = entityType;
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.data = data;
        this.velocity = velocity;
    }

    @Override
    public void read() {
        entityID = readVarInt();
        boolean v1_9 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
        if (v1_9) {
            uuid = Optional.of(readUUID());
        } else {
            uuid = Optional.empty();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            entityType = EntityTypes.getById(serverVersion.toClientVersion(), readVarInt());
        } else {
            int id = readByte();
            entityType = EntityTypes.getByLegacyId(serverVersion.toClientVersion(), id);
            if (entityType == null) // Should not happen but anyway
                entityType = EntityTypes.getById(serverVersion.toClientVersion(), id);
        }
        double x;
        double y;
        double z;
        if (v1_9) {
            x = readDouble();
            y = readDouble();
            z = readDouble();
        } else {
            x = readInt() / 32.0;
            y = readInt() / 32.0;
            z = readInt() / 32.0;
        }
        position = new Vector3d(x, y, z);
        yaw = readByte() / ROTATION_FACTOR;
        pitch = readByte() / ROTATION_FACTOR;
        data = readInt();

        //On 1.8 check if data > 0 before reading, or it won't be in the packet
        if (v1_9 || data > 0) {
            double velX = readShort() / VELOCITY_FACTOR;
            double velY = readShort() / VELOCITY_FACTOR;
            double velZ = readShort() / VELOCITY_FACTOR;
            velocity = Optional.of(new Vector3d(velX, velY, velZ));
        } else {
            velocity = Optional.empty();
        }
    }

    @Override
    public void copy(WrapperPlayServerSpawnEntity wrapper) {
        entityID = wrapper.entityID;
        uuid = wrapper.uuid;
        entityType = wrapper.entityType;
        position = wrapper.position;
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
        data = wrapper.data;
        velocity = wrapper.velocity;
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        boolean v1_9 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
        if (v1_9) {
            writeUUID(uuid.orElse(new UUID(0L, 0L)));
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            writeVarInt(entityType.getId(serverVersion.toClientVersion()));
        } else {
            if (entityType.getLegacyId(serverVersion.toClientVersion()) != -1) { // Will always be true if they use correct EntityTypes for this packet
                writeByte(entityType.getLegacyId(serverVersion.toClientVersion()));
            } else {
                writeByte(entityType.getId(serverVersion.toClientVersion()));
            }
        }

        if (v1_9) {
            writeDouble(position.x);
            writeDouble(position.y);
            writeDouble(position.z);
        } else {
            writeInt(MathUtil.floor(position.x * 32.0));
            writeInt(MathUtil.floor(position.y * 32.0));
            writeInt(MathUtil.floor(position.z * 32.0));
        }
        writeByte(MathUtil.floor(yaw * ROTATION_FACTOR));
        writeByte(MathUtil.floor(pitch * ROTATION_FACTOR));
        writeInt(data);

        //On 1.8 check if data > 0 before reading, or it won't be in the packet
        if (v1_9 || data > 0) {
            Vector3d vel = velocity.orElse(new Vector3d(-1, -1, -1));
            int velX = (int) (vel.x * VELOCITY_FACTOR);
            int velY = (int) (vel.y * VELOCITY_FACTOR);
            int velZ = (int) (vel.z * VELOCITY_FACTOR);
            writeShort(velX);
            writeShort(velY);
            writeShort(velZ);
        }
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Optional<UUID> getUUID() {
        return uuid;
    }

    public void setUUID(Optional<UUID> uuid) {
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
        return velocity;
    }

    public void setVelocity(Optional<Vector3d> velocity) {
        this.velocity = velocity;
    }
}
