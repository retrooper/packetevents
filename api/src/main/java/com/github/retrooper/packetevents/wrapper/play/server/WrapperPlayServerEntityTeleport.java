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

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This is not for teleporting players, but for teleporting other entities the player can see - such as mobs, animals, etc.
 * If you want to teleport a player, use {@link WrapperPlayServerPlayerPositionAndLook} instead.
 */
public class WrapperPlayServerEntityTeleport extends PacketWrapper<WrapperPlayServerEntityTeleport> {
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;

    private int entityID;
    private Vector3d position;
    private float yaw, pitch;
    private boolean onGround;

    public WrapperPlayServerEntityTeleport(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityTeleport(int entityID, Location location, boolean onGround) {
        this(entityID, location.getPosition(), location.getYaw(), location.getPitch(), onGround);
    }

    public WrapperPlayServerEntityTeleport(int entityID, Vector3d position, float yaw, float pitch, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_TELEPORT);
        this.entityID = entityID;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            entityID = readInt();
        } else {
            entityID = readVarInt();
        }
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            position = new Vector3d((readInt() / 32.0), (readInt() / 32.0), (readInt() / 32.0));
        } else {
            position = new Vector3d(readDouble(), readDouble(), readDouble());
        }
        yaw = readByte() / ROTATION_FACTOR;
        pitch = readByte() / ROTATION_FACTOR;
        if (serverVersion != ServerVersion.V_1_7_10) {
            onGround = readBoolean();
        } else {
            onGround = false;
        }
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
        } else {
            writeVarInt(entityID);
        }
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            writeInt(MathUtil.floor(position.x * 32.0));
            writeInt(MathUtil.floor(position.y * 32.0));
            writeInt(MathUtil.floor(position.z * 32.0));
        } else {
            writeDouble(position.x);
            writeDouble(position.y);
            writeDouble(position.z);
        }
        writeByte((int) (yaw * ROTATION_FACTOR));
        writeByte((int) (pitch * ROTATION_FACTOR));
        if (serverVersion != ServerVersion.V_1_7_10) {
            writeBoolean(onGround);
        }
    }

    @Override
    public void copy(WrapperPlayServerEntityTeleport wrapper) {
        entityID = wrapper.entityID;
        position = wrapper.position;
        yaw = wrapper.yaw;
        pitch = wrapper.pitch;
        onGround = wrapper.onGround;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
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

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
