/*
 *
 *  * This file is part of packetevents - https://github.com/retrooper/packetevents
 *  * Copyright (C) 2016-2021 retrooper and contributors
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.spawnentity;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.math.MathUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;

import java.util.Optional;
import java.util.UUID;

//TODO Finish this wrapper
class WrappedPacketOutSpawnEntity extends WrappedPacketEntityAbstraction {
    private static final float rotationFactor = 256.0F / 360.0F;
    private UUID uuid;
    private Vector3d position;
    private float pitch, yaw;

    public WrappedPacketOutSpawnEntity(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntity a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntity a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutSpawnEntity a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntity a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutSpawnEntity a7;
    }

    public Optional<UUID> getUUID() {
        if (version.isNewerThan(ServerVersion.v_1_8_8)) {
            if (packet != null) {
                return Optional.of(readObject(0, UUID.class));
            } else {
                return Optional.of(uuid);
            }
        } else {
            return Optional.empty();
        }
    }

    public void setUUID(UUID uuid) {
        if (version.isNewerThan(ServerVersion.v_1_8_8)) {
            if (packet != null) {
                writeObject(0, uuid);
            } else {
                this.uuid = uuid;
            }
        }
    }

    public Vector3d getPosition() {
        if (packet != null) {
            double x;
            double y;
            double z;
            if (version.isOlderThan(ServerVersion.v_1_9)) {
                x = readInt(1) / 32.0;
                y = readInt(2) / 32.0;
                z = readInt(3) / 32.0;
            } else {
                x = readDouble(0);
                y = readDouble(1);
                z = readDouble(2);
            }
            return new Vector3d(x, y, z);
        } else {
            return position;
        }
    }

    public void setPosition(Vector3d position) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_9)) {
                writeInt(1, MathUtils.floor(position.x * 32.0));
                writeInt(2, MathUtils.floor(position.y * 32.0));
                writeInt(3, MathUtils.floor(position.z * 32.0));
            } else {
                writeDouble(0, position.x);
                writeDouble(1, position.y);
                writeDouble(2, position.z);
            }
        } else {
            this.position = position;
        }
    }

    public float getPitch() {
        if (packet != null) {
            int factoredPitch = readInt(4);
            return factoredPitch / rotationFactor;
        } else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            writeInt(4, MathUtils.floor(pitch * rotationFactor));
        } else {
            this.pitch = pitch;
        }
    }

    public float getYaw() {
        if (packet != null) {
            int factoredYaw = readInt(5);
            return factoredYaw / rotationFactor;
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            writeInt(5, MathUtils.floor(yaw * rotationFactor));
        } else {
            this.yaw = yaw;
        }
    }
}
