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
import com.github.retrooper.packetevents.protocol.vector.vecdelta.LinearVecDelta;
import com.github.retrooper.packetevents.protocol.vector.vecdelta.VecDelta;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mojang name: ClientboundMoveEntityPacket$PosRot
 */
public class WrapperPlayServerEntityRelativeMoveAndRotation extends PacketWrapper<WrapperPlayServerEntityRelativeMoveAndRotation> {

    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private static final double MODERN_DELTA_DIVISOR = 4096.0;
    private static final double LEGACY_DELTA_DIVISOR = 32.0;

    private int entityID;
    /**
     * @versions 26.3+
     */
    private VecDelta delta;
    /**
     * @versions -26.2
     */
    private double deltaX;
    /**
     * @versions -26.2
     */
    private double deltaY;
    /**
     * @versions -26.2
     */
    private double deltaZ;
    private float yaw;
    private float pitch;
    private boolean onGround;

    public WrapperPlayServerEntityRelativeMoveAndRotation(PacketSendEvent event) {
        super(event);
    }

    /**
     * @versions -26.2
     */
    @ApiStatus.Obsolete
    public WrapperPlayServerEntityRelativeMoveAndRotation(int entityID, double deltaX, double deltaY, double deltaZ,
                                                          float yaw, float pitch, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION);
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        // minimal backward compat
        this.delta = new LinearVecDelta(deltaX, deltaY, deltaZ);
    }

    /**
     * @versions 26.3+
     */
    public WrapperPlayServerEntityRelativeMoveAndRotation(int entityID, VecDelta delta, float yaw, float pitch, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION);
        this.entityID = entityID;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        setDelta(delta);
    }

    @Override
    public void read() {
        entityID = readVarInt();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3)) {
            int properties = this.readVarInt();
            this.delta = VecDelta.read(this, properties >>> 1);
            this.onGround = (properties & 0b1) == 0b1;
            // minimal backward compat
            Vector3d totalDelta = this.delta.apply(Vector3d.zero());
            this.deltaX = totalDelta.x;
            this.deltaY = totalDelta.y;
            this.deltaZ = totalDelta.z;
        } else {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                this.deltaX = this.readShort() / MODERN_DELTA_DIVISOR;
                this.deltaY = this.readShort() / MODERN_DELTA_DIVISOR;
                this.deltaZ = this.readShort() / MODERN_DELTA_DIVISOR;
            } else {
                this.deltaX = this.readByte() / LEGACY_DELTA_DIVISOR;
                this.deltaY = this.readByte() / LEGACY_DELTA_DIVISOR;
                this.deltaZ = this.readByte() / LEGACY_DELTA_DIVISOR;
            }
        }
        yaw = readByte() / ROTATION_FACTOR;
        pitch = readByte() / ROTATION_FACTOR;
        if (this.serverVersion.isOlderThan(ServerVersion.V_26_3)) {
            this.onGround = this.readBoolean();
        }
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3)) {
            VecDelta delta = this.getDelta();
            this.writeVarInt((delta.getStepCount() << 1) | (this.onGround ? 1 : 0)); // properties
            VecDelta.write(this, delta);
        } else {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                this.writeShort((short) (this.deltaX * MODERN_DELTA_DIVISOR));
                this.writeShort((short) (this.deltaY * MODERN_DELTA_DIVISOR));
                this.writeShort((short) (this.deltaZ * MODERN_DELTA_DIVISOR));
            } else {
                this.writeByte((byte) (this.deltaX * LEGACY_DELTA_DIVISOR));
                this.writeByte((byte) (this.deltaY * LEGACY_DELTA_DIVISOR));
                this.writeByte((byte) (this.deltaZ * LEGACY_DELTA_DIVISOR));
            }
        }
        writeByte((int) (yaw * ROTATION_FACTOR));
        writeByte((int) (pitch * ROTATION_FACTOR));
        if (this.serverVersion.isOlderThan(ServerVersion.V_26_3)) {
            writeBoolean(onGround);
        }
    }

    @Override
    public void copy(WrapperPlayServerEntityRelativeMoveAndRotation wrapper) {
        entityID = wrapper.entityID;
        delta = wrapper.delta;
        deltaX = wrapper.deltaX;
        deltaY = wrapper.deltaY;
        deltaZ = wrapper.deltaZ;
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

    /**
     * @versions 26.3+
     */
    public VecDelta getDelta() {
        if (this.delta == null){
            this.delta = new LinearVecDelta(this.deltaX, this.deltaY, this.deltaZ);
        }
        return this.delta;
    }

    /**
     * @versions 26.3+
     */
    public void setDelta(VecDelta delta) {
        this.delta = delta;
        Vector3d total = delta.apply(Vector3d.zero());
        this.deltaX = total.x;
        this.deltaY = total.y;
        this.deltaZ = total.z;
    }

    /**
     * @versions -26.2
     */
    public double getDeltaX() {
        return deltaX;
    }

    /**
     * @versions -26.2
     */
    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    /**
     * @versions -26.2
     */
    public double getDeltaY() {
        return deltaY;
    }

    /**
     * @versions -26.2
     */
    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    /**
     * @versions -26.2
     */
    public double getDeltaZ() {
        return deltaZ;
    }

    /**
     * @versions -26.2
     */
    public void setDeltaZ(double deltaZ) {
        this.deltaZ = deltaZ;
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
