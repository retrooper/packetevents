/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.vector.positionpath.LinearPositionPath;
import com.github.retrooper.packetevents.protocol.vector.positionpath.PositionPath;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mojang name: ClientboundEntityPositionSyncPacket
 *
 * @versions 1.21.2+
 */
public class WrapperPlayServerEntityPositionSync extends PacketWrapper<WrapperPlayServerEntityPositionSync> {

    private int id;
    /**
     * @versions 1.21.2-26.2
     */
    private EntityPositionData values;
    /**
     * @versions 26.3+
     */
    private PositionPath position;
    /**
     * @versions 26.3+
     */
    private float yRot;
    /**
     * @versions 26.3+
     */
    private float xRot;
    private boolean onGround;

    public WrapperPlayServerEntityPositionSync(PacketSendEvent event) {
        super(event);
    }

    /**
     * @versions 1.21.2-26.2
     */
    @ApiStatus.Obsolete
    public WrapperPlayServerEntityPositionSync(int id, EntityPositionData values, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_POSITION_SYNC);
        this.id = id;
        this.values = values;
        this.onGround = onGround;
        // minimal backward compat
        this.position = new LinearPositionPath(values.getPosition());
        this.yRot = values.getYaw();
        this.xRot = values.getPitch();
    }

    /**
     * @versions 26.3+
     */
    public WrapperPlayServerEntityPositionSync(int id, PositionPath position, float yRot, float xRot, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_POSITION_SYNC);
        this.id = id;
        this.position = position;
        this.yRot = yRot;
        this.xRot = xRot;
        this.onGround = onGround;
    }

    @Override
    public void read() {
        this.id = this.readVarInt();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3)) {
            this.position = PositionPath.read(this);
            this.yRot = this.readFloat();
            this.xRot = this.readFloat();
        } else {
            this.setValues(EntityPositionData.read(this));
        }
        this.onGround = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeVarInt(this.id);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_3)) {
            PositionPath.write(this, this.getPosition());
            this.writeFloat(this.yRot);
            this.writeFloat(this.xRot);
        } else {
            EntityPositionData.write(this, this.getValues());
        }
        this.writeBoolean(this.onGround);
    }

    @Override
    public void copy(WrapperPlayServerEntityPositionSync wrapper) {
        this.id = wrapper.id;
        this.values = wrapper.values;
        this.position = wrapper.position;
        this.yRot = wrapper.yRot;
        this.xRot = wrapper.xRot;
        this.onGround = wrapper.onGround;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @versions 1.21.2-26.2
     */
    public EntityPositionData getValues() {
        if (this.values == null) {
            this.values = new EntityPositionData(
                    this.getPosition().getEndPosition(),
                    Vector3d.zero(),
                    this.getYRot(), this.getXRot()
            );
        }
        return this.values;
    }

    /**
     * @versions 1.21.2-26.2
     */
    public void setValues(EntityPositionData values) {
        this.values = values;
        this.yRot = values.getYaw();
        this.xRot = values.getPitch();
    }

    /**
     * @versions 26.3+
     */
    public PositionPath getPosition() {
        if (this.position == null) {
            this.position = new LinearPositionPath(this.getValues().getPosition());
        }
        return this.position;
    }

    /**
     * @versions 26.3+
     */
    public void setPosition(PositionPath position) {
        this.position = position;
    }

    /**
     * @versions 26.3+
     */
    public float getYRot() {
        return this.yRot;
    }

    /**
     * @versions 26.3+
     */
    public void setYRot(float yRot) {
        this.yRot = yRot;
    }

    /**
     * @versions 26.3+
     */
    public float getXRot() {
        return this.xRot;
    }

    /**
     * @versions 26.3+
     */
    public void setXRot(float xRot) {
        this.xRot = xRot;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
