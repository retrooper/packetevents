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

package io.github.retrooper.packetevents.packetwrappers.play.out.spawnentityliving;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.math.MathUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.UUID;

public class WrappedPacketOutSpawnEntityLiving extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static final byte[] byteBufAllocation = new byte[48];
    private static final float rotationFactor = 256.0F / 360.0F;
    private static final double velocityFactor = 8000.0;
    private static boolean v_1_9, v_1_17;
    private static Constructor<?> packetConstructor;

    private Vector3d position, velocity;
    private EntityType entityType;
    private UUID uuid;
    private float yaw, pitch, headPitch;

    public WrappedPacketOutSpawnEntityLiving(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutSpawnEntityLiving(Entity entity, UUID uuid, EntityType entityType, Vector3d position, Vector3d velocity, float yaw, float pitch, float headPitch) {
        setEntity(entity);
        this.uuid = uuid;
        this.entityType = entityType;
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headPitch = headPitch;
    }

    public WrappedPacketOutSpawnEntityLiving(int entityID, UUID uuid, EntityType entityType, Vector3d position, Vector3d velocity, float yaw, float pitch, float headPitch) {
        setEntityId(entityID);
        this.uuid = uuid;
        this.entityType = entityType;
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headPitch = headPitch;
    }

    @Override
    protected void load() {
        try {
            v_1_9 = version.isNewerThanOrEquals(ServerVersion.v_1_9);
            v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.SPAWN_ENTITY_LIVING.getConstructor(NMSUtils.packetDataSerializerClass);
            } else {
                packetConstructor = PacketTypeClasses.Play.Server.SPAWN_ENTITY_LIVING.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
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

    public EntityType getEntityType() {
        if (packet != null) {
            int entityTypeID = readInt(1);
            return EntityType.fromId(entityTypeID);
        } else {
            return entityType;
        }
    }

    public void setEntityType(EntityType entityType) {
        if (packet != null) {
            writeInt(1, entityType.getTypeId());
        } else {
            this.entityType = entityType;
        }
    }

    public Vector3d getPosition() {
        if (packet != null) {
            double x;
            double y;
            double z;
            //On 1.8.x and 1.7.10 they are factored by 32
            if (!v_1_9) {
                x = readInt(2) / 32.0;
                y = readInt(3) / 32.0;
                z = readInt(4) / 32.0;
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
            //On 1.8.x and 1.7.10 they must be factored by 32
            if (!v_1_9) {
                writeInt(2, MathUtils.floor(position.x * 32.0));
                writeInt(3, MathUtils.floor(position.y * 32.0));
                writeInt(4, MathUtils.floor(position.z * 32.0));
            } else {
                writeDouble(0, position.x);
                writeDouble(1, position.y);
                writeDouble(2, position.z);
            }
        } else {
            this.position = position;
        }
    }

    public Vector3d getVelocity() {
        if (packet != null) {
            int factoredVelX;
            int factoredVelY;
            int factoredVelZ;
            if (!v_1_9) {
                factoredVelX = readInt(5);
                factoredVelY = readInt(6);
                factoredVelZ = readInt(7);
            } else {
                factoredVelX = readInt(2);
                factoredVelY = readInt(3);
                factoredVelZ = readInt(4);
            }
            return new Vector3d(factoredVelX / velocityFactor, factoredVelY / velocityFactor, factoredVelZ / velocityFactor);
        } else {
            return this.velocity;
        }
    }

    public void setVelocity(Vector3d velocity) {
        if (packet != null) {
            int factoredVelX = (int) (velocity.x * velocityFactor);
            int factoredVelY = (int) (velocity.y * velocityFactor);
            int factoredVelZ = (int) (velocity.z * velocityFactor);
            if (!v_1_9) {
                writeInt(5, factoredVelX);
                writeInt(6, factoredVelY);
                writeInt(7, factoredVelZ);
            } else {
                writeInt(2, factoredVelX);
                writeInt(3, factoredVelY);
                writeInt(4, factoredVelZ);
            }
        } else {
            this.velocity = velocity;
        }
    }

    public float getYaw() {
        if (packet != null) {
            byte factoredYaw = readByte(0);
            return factoredYaw / rotationFactor;
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            writeByte(0, (byte) ((int) (yaw * rotationFactor)));
        } else {
            this.yaw = yaw;
        }
    }

    public float getPitch() {
        if (packet != null) {
            byte factoredPitch = readByte(1);
            return factoredPitch / rotationFactor;
        } else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            writeByte(1, (byte) ((int) (pitch * rotationFactor)));
        } else {
            this.pitch = pitch;
        }
    }

    public float getHeadPitch() {
        if (packet != null) {
            byte factoredHeadPitch = readByte(2);
            return factoredHeadPitch / rotationFactor;
        } else {
            return this.headPitch;
        }
    }

    public void setHeadPitch(float headPitch) {
        if (packet != null) {
            writeByte(2, (byte) ((int) (headPitch * rotationFactor)));
        } else {
            this.headPitch = headPitch;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(byteBufAllocation);
            Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            packetInstance = packetConstructor.newInstance(packetDataSerializer);
        } else {
            packetInstance = packetConstructor.newInstance();
        }
        WrappedPacketOutSpawnEntityLiving wrappedPacketOutSpawnEntityLiving = new WrappedPacketOutSpawnEntityLiving(new NMSPacket(packetInstance));
        wrappedPacketOutSpawnEntityLiving.setEntityId(getEntityId());
        if (v_1_9) {
            wrappedPacketOutSpawnEntityLiving.setUUID(getUUID().get());
        }
        wrappedPacketOutSpawnEntityLiving.setEntityType(getEntityType());
        wrappedPacketOutSpawnEntityLiving.setPosition(getPosition());
        wrappedPacketOutSpawnEntityLiving.setVelocity(getVelocity());
        wrappedPacketOutSpawnEntityLiving.setYaw(getYaw());
        wrappedPacketOutSpawnEntityLiving.setPitch(getPitch());
        wrappedPacketOutSpawnEntityLiving.setHeadPitch(getHeadPitch());
        return packetInstance;
    }
}
