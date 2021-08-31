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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityteleport;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public class WrappedPacketOutEntityTeleport extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static final float rotationMultiplier = 256.0F / 360.0F;
    private static boolean v_1_17;
    private static boolean legacyVersionMode;
    private static boolean ultraLegacyVersionMode;
    private static Constructor<?> constructor;
    private Vector3d position;
    private float yaw, pitch;
    private boolean onGround;

    public WrappedPacketOutEntityTeleport(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityTeleport(int entityID, Location loc, boolean onGround) {
        this(entityID, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
    }

    public WrappedPacketOutEntityTeleport(Entity entity, Location loc, boolean onGround) {
        this(entity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
    }

    public WrappedPacketOutEntityTeleport(int entityID, Vector3d position, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public WrappedPacketOutEntityTeleport(Entity entity, Vector3d position, float yaw, float pitch, boolean onGround) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public WrappedPacketOutEntityTeleport(int entityID, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public WrappedPacketOutEntityTeleport(Entity entity, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.position = new Vector3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    private static int floor(double value) {
        int i = (int) value;
        return value < (double) i ? i - 1 : i;
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        Class<?> packetClass = PacketTypeClasses.Play.Server.ENTITY_TELEPORT;
        try {
            constructor = packetClass.getConstructor(int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class, boolean.class);
            ultraLegacyVersionMode = true;
            legacyVersionMode = true;
        } catch (NoSuchMethodException e) {
            try {
                constructor = packetClass.getConstructor(int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class);
                ultraLegacyVersionMode = false;
                legacyVersionMode = true;
            } catch (NoSuchMethodException e2) {
                try {
                    if (v_1_17) {
                        constructor = packetClass.getConstructor(NMSUtils.packetDataSerializerClass);
                    } else {
                        constructor = packetClass.getConstructor();
                    }
                    ultraLegacyVersionMode = false;
                    legacyVersionMode = false;
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                }
            }

        }
    }

    public Vector3d getPosition() {
        if (packet != null) {
            double x, y, z;
            if (legacyVersionMode) {
                x = readInt(1) / 32.0D;
                y = readInt(2) / 32.0D;
                z = readInt(3) / 32.0D;
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
            if (legacyVersionMode) {
                writeInt(1, floor(position.x * 32.0D));
                writeInt(2, floor(position.y * 32.0D));
                writeInt(3, floor(position.z * 32.0D));
            } else {
                writeDouble(0, position.x);
                writeDouble(1, position.y);
                writeDouble(2, position.z);
            }
        } else {
            this.position = position;
        }
    }

    public float getYaw() {
        if (packet != null) {
            return (readByte(0) / rotationMultiplier);
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            writeByte(0, (byte) (yaw * rotationMultiplier));
        } else {
            this.yaw = yaw;
        }
    }

    public float getPitch() {
        if (packet != null) {
            return (readByte(1) / rotationMultiplier);
        } else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            writeByte(1, (byte) (pitch * rotationMultiplier));
        } else {
            this.pitch = pitch;
        }
    }

    public boolean isOnGround() {
        if (packet != null) {
            return readBoolean(0);
        } else {
            return onGround;
        }
    }

    public void setOnGround(boolean onGround) {
        if (packet != null) {
            writeBoolean(0, onGround);
        } else {
            this.onGround = onGround;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Vector3d pos = getPosition();
        if (ultraLegacyVersionMode) {
            //1.7.10
            return constructor.newInstance(entityID, floor(pos.x * 32.0D), floor(pos.y * 32.0D), floor(pos.z * 32.0D),
                    (byte) ((int) getYaw() * rotationMultiplier), (byte) (int) (getPitch() * rotationMultiplier), false, false);
        } else if (legacyVersionMode) {
            //1.8.x
            return constructor.newInstance(entityID, floor(pos.x * 32.0D), floor(pos.y * 32.0D), floor(pos.z * 32.0D),
                    (byte) ((int) getYaw() * rotationMultiplier), (byte) (int) (getPitch() * rotationMultiplier), false);
        } else {
            //newer versions
            Object instance;
            if (v_1_17) {
                Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(new byte[]{
                        0, 0, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 0, 0});
                //4 bytes per bytebuf field
                Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
                instance = constructor.newInstance(packetDataSerializer);
            } else {
                instance = constructor.newInstance();
            }
            WrappedPacketOutEntityTeleport instanceWrapper = new WrappedPacketOutEntityTeleport(new NMSPacket(instance));
            instanceWrapper.setEntityId(getEntityId());
            instanceWrapper.setPosition(pos);

            instanceWrapper.setPitch(getPitch());
            instanceWrapper.setYaw(getYaw());
            if (isOnGround()) {
                //default value is false, so we can save a reflection call
                instanceWrapper.setOnGround(true);
            }
            return instance;
        }
    }
}
