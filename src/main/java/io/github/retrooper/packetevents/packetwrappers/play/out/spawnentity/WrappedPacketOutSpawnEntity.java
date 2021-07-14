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

//TODO Make this sendable
public class WrappedPacketOutSpawnEntity extends WrappedPacketEntityAbstraction {
    private static boolean v_1_9, v_1_17;
    private static final float rotationFactor = 256.0F / 360.0F;
    private UUID uuid;
    private Vector3d position, velocity;
    private float pitch, yaw;

    public WrappedPacketOutSpawnEntity(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_9 = version.isNewerThanOrEquals(ServerVersion.v_1_9);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }

    public Optional<UUID> getUUID() {
        if (v_1_9) {
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
        if (v_1_9) {
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
            if (v_1_9) {
                x = readDouble(v_1_17 ? 1 : 0);
                y = readDouble(v_1_17 ? 2 : 1);
                z = readDouble(v_1_17 ? 3 : 2);
            }
            else {
                x = readInt(1) / 32.0;
                y = readInt(2) / 32.0;
                z = readInt(3) / 32.0;
            }
            return new Vector3d(x, y, z);
        } else {
            return position;
        }
    }

    public void setPosition(Vector3d position) {
        if (packet != null) {
            if (v_1_9) {
                writeDouble(v_1_17 ? 1 : 0, position.x);
                writeDouble(v_1_17 ? 2 : 1, position.y);
                writeDouble(v_1_17 ? 3 : 2, position.z);
            }
            else {
                writeInt(1, MathUtils.floor(position.x * 32.0));
                writeInt(2, MathUtils.floor(position.y * 32.0));
                writeInt(3, MathUtils.floor(position.z * 32.0));
            }
        } else {
            this.position = position;
        }
    }

    public Vector3d getVelocity() {
        if (packet != null) {
            double velX;
            double velY;
            double velZ;
            if (v_1_9) {
                velX = readInt(1) / 8000.0;
                velY = readInt(2) / 8000.0;
                velZ = readInt(3) / 8000.0;
            }
            else {
                velX = readInt(4) / 8000.0;
                velY = readInt(5) / 8000.0;
                velZ = readInt(6) / 8000.0;
            }
            return new Vector3d(velX, velY, velZ);
        } else {
            return velocity;
        }
    }

    public void setVelocity(Vector3d velocity) {
        if (packet != null) {
            int velX = (int) (velocity.x * 8000.0);
            int velY = (int) (velocity.y * 8000.0);
            int velZ = (int) (velocity.z * 8000.0);
            if (v_1_9) {
                writeInt(1, velX);
                writeInt(2, velY);
                writeInt(3, velZ);
            }
            else {
                writeInt(4, velX);
                writeInt(5, velY);
                writeInt(6, velZ);
            }
        } else {
            this.velocity = velocity;
        }
    }

    public float getPitch() {
        if (packet != null) {
            int factoredPitch;
            if (!v_1_9) {
                factoredPitch = readInt(7);
            } else {
                factoredPitch = readInt(4);
            }
            return factoredPitch / rotationFactor;
        } else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            if (!v_1_9) {
                writeInt(7, MathUtils.floor(pitch * rotationFactor));
            } else {
                writeInt(4, MathUtils.floor(pitch * rotationFactor));
            }
        } else {
            this.pitch = pitch;
        }
    }

    public float getYaw() {
        if (packet != null) {
            int factoredYaw;
            if (!v_1_9) {
                factoredYaw = readInt(8);
            } else {
                factoredYaw = readInt(5);
            }
            return factoredYaw / rotationFactor;
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            if (!v_1_9) {
                writeInt(8, MathUtils.floor(yaw * rotationFactor));
            } else {
                writeInt(5, MathUtils.floor(yaw * rotationFactor));
            }
        } else {
            this.yaw = yaw;
        }
    }
}
