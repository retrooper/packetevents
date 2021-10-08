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
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerSpawnLivingEntity extends PacketWrapper<WrapperPlayServerSpawnLivingEntity> {
    private static final double POSITION_FACTOR = 32.0;
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private static final double VELOCITY_FACTOR = 8000.0;
    private int entityID;
    private Optional<UUID> entityUUID;
    private int entityTypeID;
    private Vector3d position;
    private float yaw;
    private float pitch;
    private float headPitch;
    private Vector3d velocity;

    public WrapperPlayServerSpawnLivingEntity(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnLivingEntity(int entityID, Optional<UUID> entityUUID, int entityTypeID, Vector3d position, float yaw, float pitch, float headPitch, Vector3d velocity) {
        super(PacketType.Play.Server.SPAWN_LIVING_ENTITY);
        this.entityID = entityID;
        this.entityUUID = entityUUID;
        this.entityTypeID = entityTypeID;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headPitch = headPitch;
        this.velocity = velocity;
    }

    @Override
    public void readData() {
        this.entityID = readVarInt();
        if (serverVersion.isOlderThan(ServerVersion.v_1_9)) {
            this.entityUUID = Optional.empty();
            this.entityTypeID = readByte() & 255;
            this.position = new Vector3d(readInt() / POSITION_FACTOR, readInt() / POSITION_FACTOR, readInt() / POSITION_FACTOR);
        } else {
            this.entityUUID = Optional.of(readUUID());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_11)) {
                this.entityTypeID = readVarInt();
            } else {
                this.entityTypeID = readByte() & 255;
            }
            this.position = new Vector3d(readDouble(), readDouble(), readDouble());
        }
        this.yaw = readByte() / ROTATION_FACTOR;
        this.pitch = readByte() / ROTATION_FACTOR;
        this.headPitch = readByte() / ROTATION_FACTOR;
        double velX = readShort() / VELOCITY_FACTOR;
        double velY = readShort() / VELOCITY_FACTOR;
        double velZ = readShort() / VELOCITY_FACTOR;
        this.velocity = new Vector3d(velX, velY, velZ);
    }

    @Override
    public void readData(WrapperPlayServerSpawnLivingEntity wrapper) {
        this.entityID = wrapper.entityID;
        this.entityUUID = wrapper.entityUUID;
        this.entityTypeID = wrapper.entityTypeID;
        this.position = wrapper.position;
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
        this.headPitch = wrapper.headPitch;
        this.velocity = wrapper.velocity;
    }


    @Override
    public void writeData() {
        writeVarInt(entityID);
        if (serverVersion.isOlderThan(ServerVersion.v_1_9)) {
            writeByte(entityTypeID & 255);
            writeInt(MathUtil.floor(position.x * POSITION_FACTOR));
            writeInt(MathUtil.floor(position.y * POSITION_FACTOR));
            writeInt(MathUtil.floor(position.z * POSITION_FACTOR));
        } else {
            writeUUID(entityUUID.orElse(new UUID(0L, 0L)));
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_11)) {
                writeVarInt(entityTypeID);
            } else {
                writeByte(entityTypeID & 255);
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
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public Optional<UUID> getEntityUUID() {
        return entityUUID;
    }

    public void setEntityUUID(Optional<UUID> entityUUID) {
        this.entityUUID = entityUUID;
    }

    public int getEntityTypeID() {
        return entityTypeID;
    }

    public void setEntityTypeID(int entityTypeID) {
        this.entityTypeID = entityTypeID;
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
}
