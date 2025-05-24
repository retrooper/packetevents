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
import com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
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
    /**
     * Changed with 1.21.2
     * <p>
     * In versions before 1.21.2, the {@link EntityPositionData#getDeltaMovement()} will always be zero.
     */
    private EntityPositionData values;
    /**
     * Added with 1.21.2
     */
    private RelativeFlag relativeFlags;
    private boolean onGround;

    public WrapperPlayServerEntityTeleport(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityTeleport(int entityID, Location location, boolean onGround) {
        this(entityID, location.getPosition(), location.getYaw(), location.getPitch(), onGround);
    }

    public WrapperPlayServerEntityTeleport(int entityID, Vector3d position, float yaw, float pitch, boolean onGround) {
        this(entityID, position, Vector3d.zero(), yaw, pitch, RelativeFlag.NONE, onGround);
    }

    public WrapperPlayServerEntityTeleport(
            int entityID, Vector3d position, Vector3d deltaMovement,
            float yaw, float pitch, RelativeFlag relativeFlags, boolean onGround
    ) {
        this(entityID, new EntityPositionData(position, deltaMovement, yaw, pitch), relativeFlags, onGround);
    }

    public WrapperPlayServerEntityTeleport(
            int entityID, EntityPositionData values, RelativeFlag relativeFlags, boolean onGround
    ) {
        super(PacketType.Play.Server.ENTITY_TELEPORT);
        this.entityID = entityID;
        this.values = values;
        this.relativeFlags = relativeFlags;
        this.onGround = onGround;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.entityID = this.readVarInt();
            this.values = EntityPositionData.read(this);
            this.relativeFlags = new RelativeFlag(this.readInt());
        } else {
            this.entityID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) ? this.readVarInt() : this.readInt();
            Vector3d position = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9) ? Vector3d.read(this) :
                    new Vector3d(this.readInt() / 32d, this.readInt() / 32d, this.readInt() / 32d);
            float yaw = this.readByte() / ROTATION_FACTOR;
            float pitch = this.readByte() / ROTATION_FACTOR;
            this.values = new EntityPositionData(position, Vector3d.zero(), yaw, pitch);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.onGround = this.readBoolean();
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeVarInt(this.entityID);
            EntityPositionData.write(this, this.values);
            this.writeInt(this.relativeFlags.getFullMask());
        } else {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
                this.writeVarInt(this.entityID);
            } else {
                this.writeInt(this.entityID);
            }
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                Vector3d.write(this, this.values.getPosition());
            } else {
                Vector3d pos = this.values.getPosition();
                this.writeInt(MathUtil.floor(pos.x * 32d));
                this.writeInt(MathUtil.floor(pos.y * 32d));
                this.writeInt(MathUtil.floor(pos.z * 32d));
            }
            this.writeByte((int) (this.values.getYaw() * ROTATION_FACTOR));
            this.writeByte((int) (this.values.getPitch() * ROTATION_FACTOR));
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.writeBoolean(this.onGround);
        }
    }

    @Override
    public void copy(WrapperPlayServerEntityTeleport wrapper) {
        this.entityID = wrapper.entityID;
        this.values = wrapper.values;
        this.relativeFlags = wrapper.relativeFlags;
        this.onGround = wrapper.onGround;
    }

    public int getEntityId() {
        return this.entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public EntityPositionData getValues() {
        return this.values;
    }

    public void setValues(EntityPositionData values) {
        this.values = values;
    }

    public Vector3d getPosition() {
        return this.values.getPosition();
    }

    public void setPosition(Vector3d position) {
        this.values.setPosition(position);
    }

    public Vector3d getDeltaMovement() {
        return this.values.getDeltaMovement();
    }

    public void setDeltaMovement(Vector3d deltaMovement) {
        this.values.setDeltaMovement(deltaMovement);
    }

    public float getYaw() {
        return this.values.getYaw();
    }

    public void setYaw(float yaw) {
        this.values.setYaw(yaw);
    }

    public float getPitch() {
        return this.values.getPitch();
    }

    public void setPitch(float pitch) {
        this.values.setPitch(pitch);
    }

    public RelativeFlag getRelativeFlags() {
        return this.relativeFlags;
    }

    public void setRelativeFlags(RelativeFlag relativeFlags) {
        this.relativeFlags = relativeFlags;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
