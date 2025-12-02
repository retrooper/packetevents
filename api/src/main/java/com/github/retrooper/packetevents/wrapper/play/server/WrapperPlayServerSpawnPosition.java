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
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

/**
 * Mojang name: ClientboundSetDefaultSpawnPositionPacket
 */
@NullMarked
public class WrapperPlayServerSpawnPosition extends PacketWrapper<WrapperPlayServerSpawnPosition> {

    /**
     * @versions 1.21.9+
     */
    private ResourceLocation dimension;
    private Vector3i position;
    private float yaw;
    /**
     * @versions 1.21.9+
     */
    private float pitch;

    public WrapperPlayServerSpawnPosition(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnPosition(Vector3i position) {
        this(position, 0f);
    }

    public WrapperPlayServerSpawnPosition(Vector3i position, float yaw) {
        this(WorldBlockPosition.OVERWORLD_DIMENSION, position, yaw, 0f);
    }
    public WrapperPlayServerSpawnPosition(ResourceLocation dimension,Vector3i position, float yaw, float pitch) {
        super(PacketType.Play.Server.SPAWN_POSITION);
        this.dimension = dimension;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.dimension = ResourceLocation.read(this);
        }
        this.position = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)
                ? this.readBlockPosition() : new Vector3i(this.readInt(), this.readInt(), this.readInt());

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.yaw = this.readFloat();
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.pitch = this.readFloat();
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            ResourceLocation.write(this, this.dimension);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.writeBlockPosition(this.position);
        } else {
            this.writeInt(this.position.x);
            this.writeInt(this.position.y);
            this.writeInt(this.position.z);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.writeFloat(this.yaw);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            this.writeFloat(this.pitch);
        }
    }

    @Override
    public void copy(WrapperPlayServerSpawnPosition wrapper) {
        this.dimension = wrapper.dimension;
        this.position = wrapper.position;
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
    }

    /**
     * @versions 1.21.9+
     */
    public ResourceLocation getDimension() {
        return this.dimension;
    }

    /**
     * @versions 1.21.9+
     */
    public void setDimension(ResourceLocation dimension) {
        this.dimension = dimension;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    /**
     * @deprecated renamed to {@link #getYaw()}
     */
    @Deprecated
    public Optional<Float> getAngle() {
        return Optional.ofNullable(yaw);
    }

    /**
     * @deprecated renamed to {@link #setYaw(float)}
     */
    @Deprecated
    public void setAngle(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * @versions 1.21.9+
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * @versions 1.21.9+
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
