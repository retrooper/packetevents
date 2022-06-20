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

package io.github.retrooper.packetevents.packetwrappers.play.out.entity;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
public class WrappedPacketOutEntity extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_8, v_1_15, v_1_17;
    //Byte = 1.7.10->1.8.8, Int = 1.9->1.15.x, Short = 1.16.x
    private static byte mode; //byte = 0, int = 1, short = 2
    private static double dXYZDivisor;
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private static int yawByteIndex = 0;
    private static int pitchByteIndex = 1;
    private static Constructor<?> entityPacketConstructor, entityRelMovePacketConstructor, entityLookConstructor, entityRelMoveLookConstructor;
    private double deltaX, deltaY, deltaZ;
    private float pitch, yaw;
    private boolean onGround, rotating, moving;

    public WrappedPacketOutEntity(NMSPacket packet) {
        super(packet);

    }

    public WrappedPacketOutEntity(int entityID, double deltaX, double deltaY, double deltaZ,
                                  float yaw, float pitch, boolean onGround, boolean rotating, boolean moving) {
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.rotating = rotating;
        this.moving = moving;
    }

    public WrappedPacketOutEntity(int entityID, double deltaX, double deltaY, double deltaZ,
                                  float yaw, float pitch, boolean onGround, boolean rotating) {
        this(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, rotating, false);
    }

    public WrappedPacketOutEntity(int entityID, double deltaX, double deltaY, double deltaZ,
                                  float yaw, float pitch, boolean onGround) {
        this(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, false, false);
    }

    public WrappedPacketOutEntity(Entity entity, double deltaX, double deltaY, double deltaZ,
                                  float yaw, float pitch, boolean onGround, boolean isLook) {
        this(entity.getEntityId(), deltaX, deltaY, deltaZ, yaw, pitch, onGround, isLook);
    }

    public WrappedPacketOutEntity(Entity entity, double deltaX, double deltaY, double deltaZ,
                                  float yaw, float pitch, boolean onGround) {
        this(entity.getEntityId(), deltaX, deltaY, deltaZ, yaw, pitch, onGround, false);
    }

    @Override
    protected void load() {
        v_1_8 = version.isNewerThanOrEquals(ServerVersion.v_1_8);
        v_1_15 = version.isNewerThanOrEquals(ServerVersion.v_1_15);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        Class<?> packetClass = PacketTypeClasses.Play.Server.ENTITY;

        Field dxField = Reflection.getField(packetClass, v_1_17 ? 2 : 1);
        if (Objects.requireNonNull(dxField).equals(Reflection.getField(packetClass, byte.class, 0))) {
            mode = 0;
            yawByteIndex = 3;
            pitchByteIndex = 4;
        } else if (dxField.equals(Reflection.getField(packetClass, int.class, 1))) {
            mode = 1;
        } else if (dxField.equals(Reflection.getField(packetClass, short.class, 0))
        //1.19 support
                || dxField.equals(Reflection.getField(packetClass, short.class, 1))) {
            mode = 2;
        }
        if (mode == 0) {
            dXYZDivisor = 32.0;
        } else {
            dXYZDivisor = 4096.0;
        }
        try {
            if (v_1_17) {
                entityPacketConstructor = packetClass.getDeclaredConstructor(int.class, short.class, short.class, short.class,
                        byte.class, byte.class, boolean.class, boolean.class, boolean.class);
                entityPacketConstructor.setAccessible(true);
            } else {
                entityPacketConstructor = packetClass.getConstructor(int.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public float getPitch() {
        if (packet != null) {
            return readByte(pitchByteIndex) / ROTATION_FACTOR;
        } else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            writeByte(pitchByteIndex, (byte) (pitch * ROTATION_FACTOR));
        } else {
            this.pitch = pitch;
        }
    }

    public float getYaw() {
        if (packet != null) {
            return readByte(yawByteIndex) / ROTATION_FACTOR;
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            writeByte(yawByteIndex, (byte) (yaw * ROTATION_FACTOR));
        }
    }

    public double getDeltaX() {
        if (packet != null) {
            switch (mode) {
                case 0:
                    return readByte(0) / dXYZDivisor;
                case 1:
                    return readInt(1) / dXYZDivisor;
                case 2:
                    return readShort(0) / dXYZDivisor;
                default:
                    return -1;
            }
        } else {
            return deltaX;
        }
    }

    public void setDeltaX(double deltaX) {
        if (packet != null) {
            int dx = (int) (deltaX * dXYZDivisor);
            switch (mode) {
                case 0:
                    writeByte(0, (byte) dx);
                    break;
                case 1:
                    writeInt(1, dx);
                    break;
                case 2:
                    writeShort(0, (short) dx);
                    break;
            }
        } else {
            this.deltaX = deltaX;
        }
    }

    public double getDeltaY() {
        if (packet != null) {
            switch (mode) {
                case 0:
                    return readByte(1) / dXYZDivisor;
                case 1:
                    return readInt(2) / dXYZDivisor;
                case 2:
                    return readShort(1) / dXYZDivisor;
                default:
                    return -1;
            }
        } else {
            return deltaY;
        }
    }

    public void setDeltaY(double deltaY) {
        if (packet != null) {
            int dy = (int) (deltaY * dXYZDivisor);
            switch (mode) {
                case 0:
                    writeByte(1, (byte) dy);
                    break;
                case 1:
                    writeInt(2, dy);
                    break;
                case 2:
                    writeShort(1, (short) dy);
                    break;
            }
        } else {
            this.deltaY = deltaY;
        }
    }

    public double getDeltaZ() {
        if (packet != null) {
            switch (mode) {
                case 0:
                    return readByte(2) / dXYZDivisor;
                case 1:
                    return readInt(3) / dXYZDivisor;
                case 2:
                    return readShort(2) / dXYZDivisor;
                default:
                    return -1;
            }
        } else {
            return deltaZ;
        }
    }

    public void setDeltaZ(double deltaZ) {
        if (packet != null) {
            int dz = (int) (deltaZ * dXYZDivisor);
            switch (mode) {
                case 0:
                    writeByte(2, (byte) dz);
                    break;
                case 1:
                    writeInt(3, dz);
                    break;
                case 2:
                    writeShort(2, (short) dz);
                    break;
            }
        } else {
            this.deltaZ = deltaZ;
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

    public Optional<Boolean> isRotating() {
        if (!v_1_8) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readBoolean(1));
        } else {
            return Optional.of(rotating);
        }
    }

    public void setRotating(boolean rotating) {
        if (v_1_8) {
            if (packet != null) {
                writeBoolean(1, rotating);
            } else {
                this.rotating = rotating;
            }
        }
    }

    public Optional<Boolean> isMoving() {
        if (!v_1_15) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readBoolean(2));
        } else {
            return Optional.of(moving);
        }
    }

    public void setMoving(boolean moving) {
        if (v_1_15) {
            if (packet != null) {
                writeBoolean(2, moving);
            } else {
                this.moving = moving;
            }
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        if (v_1_17) {
            byte angleYaw = (byte) (getYaw() * ROTATION_FACTOR);
            byte anglePitch = (byte) (getPitch() * ROTATION_FACTOR);
            return entityPacketConstructor.newInstance(getEntityId(),
                    (short) (getDeltaX() * dXYZDivisor), (short) (getDeltaY() * dXYZDivisor),
                    (short) (getDeltaZ() * dXYZDivisor),
                    angleYaw, anglePitch, isOnGround(), isRotating().get(), isMoving().get());
        } else {
            Object packetInstance = entityPacketConstructor.newInstance(getEntityId());
            WrappedPacketOutEntity wrapper = new WrappedPacketOutEntity(new NMSPacket(packetInstance));
            wrapper.setDeltaX(getDeltaX());
            wrapper.setDeltaY(getDeltaY());
            wrapper.setDeltaZ(getDeltaZ());
            wrapper.setYaw(getYaw());
            wrapper.setPitch(getPitch());
            wrapper.setOnGround(isOnGround());
            if (v_1_8) {
                wrapper.setRotating(isRotating().get());
                /*if (v_1_16) {
                    wrapper.setMoving(isMoving().get());
                }*/
            }
            return packetInstance;
        }
    }

    public static class WrappedPacketOutEntityLook extends WrappedPacketOutEntity {
        public WrappedPacketOutEntityLook(NMSPacket packet) {
            super(packet);
        }

        public WrappedPacketOutEntityLook(int entityID, float yaw, float pitch, boolean onGround) {
            super(entityID, 0, 0, 0, yaw, pitch, onGround, true);
        }

        @Override
        protected void load() {
            super.load();
            try {
                if (v_1_17) {
                    entityLookConstructor = PacketTypeClasses.Play.Server.ENTITY_LOOK.getConstructor(int.class, byte.class, byte.class, boolean.class);
                }
                else {
                    entityLookConstructor = PacketTypeClasses.Play.Server.ENTITY_LOOK.getConstructor();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object asNMSPacket() throws Exception {
            Object packetInstance;
            if (v_1_17) {
                byte angleYaw = (byte) (getYaw() * ROTATION_FACTOR);
                byte anglePitch = (byte) (getPitch() * ROTATION_FACTOR);
                packetInstance = entityLookConstructor.newInstance(getEntityId(), angleYaw, anglePitch, isOnGround());
            }
            else {
                packetInstance = entityLookConstructor.newInstance();
                WrappedPacketOutEntityLook wrapper = new WrappedPacketOutEntityLook(new NMSPacket(packetInstance));
                wrapper.setEntityId(getEntityId());
                wrapper.setYaw(getYaw());
                wrapper.setPitch(getPitch());
                wrapper.setOnGround(isOnGround());
                if (v_1_8) {
                    wrapper.setRotating(true);
                }
            }
            return packetInstance;
        }
    }

    public static class WrappedPacketOutRelEntityMove extends WrappedPacketOutEntity {
        public WrappedPacketOutRelEntityMove(NMSPacket packet) {
            super(packet);
        }

        public WrappedPacketOutRelEntityMove(int entityID, double deltaX, double deltaY, double deltaZ, boolean onGround) {
            super(entityID, deltaX, deltaY, deltaZ, 0, 0, onGround, false);
        }

        @Override
        protected void load() {
            super.load();
            try {
                if (v_1_17) {
                    entityRelMovePacketConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE.getConstructor(int.class, short.class, short.class, short.class, boolean.class);
                }
                else {
                    entityRelMovePacketConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE.getConstructor();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object asNMSPacket() throws Exception {
            Object packetInstance;
            if (v_1_17) {
                short dx = (short) (getDeltaX() * dXYZDivisor);
                short dy = (short) (getDeltaY() * dXYZDivisor);
                short dz = (short) (getDeltaZ() * dXYZDivisor);
                packetInstance = entityRelMovePacketConstructor.newInstance(getEntityId(), dx, dy, dz, isOnGround());
            }
            else {
                packetInstance = entityRelMovePacketConstructor.newInstance();

                WrappedPacketOutRelEntityMove wrapper = new WrappedPacketOutRelEntityMove(new NMSPacket(packetInstance));
                wrapper.setEntityId(getEntityId());
                wrapper.setDeltaX(getDeltaX());
                wrapper.setDeltaY(getDeltaY());
                wrapper.setDeltaZ(getDeltaZ());
                wrapper.setOnGround(isOnGround());
                if (v_1_15) {
                    wrapper.setMoving(true);
                }
            }
            return packetInstance;
        }
    }

    public static class WrappedPacketOutRelEntityMoveLook extends WrappedPacketOutEntity {
        public WrappedPacketOutRelEntityMoveLook(NMSPacket packet) {
            super(packet);
        }

        public WrappedPacketOutRelEntityMoveLook(int entityID, double deltaX, double deltaY, double deltaZ,
                                                 float yaw, float pitch, boolean onGround) {
            super(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, false);
        }

        @Override
        protected void load() {
            super.load();
            try {
                if (v_1_17) {
                    entityRelMoveLookConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK.getConstructor(int.class, short.class, short.class, short.class, byte.class, byte.class, boolean.class);
                }
                else {
                    entityRelMoveLookConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK.getConstructor();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object asNMSPacket() throws Exception {
            Object packetInstance;
            if (v_1_17) {
                short dx = (short) (getDeltaX() * dXYZDivisor);
                short dy = (short) (getDeltaY() * dXYZDivisor);
                short dz = (short) (getDeltaZ() * dXYZDivisor);
                byte angleYaw = (byte) (getYaw() * ROTATION_FACTOR);
                byte anglePitch = (byte) (getPitch() * ROTATION_FACTOR);
                packetInstance = entityRelMoveLookConstructor.newInstance(getEntityId(), dx, dy, dz, angleYaw, anglePitch, isOnGround());
            }
            else {
                packetInstance = entityRelMoveLookConstructor.newInstance();
                WrappedPacketOutRelEntityMoveLook wrapper = new WrappedPacketOutRelEntityMoveLook(new NMSPacket(packetInstance));
                wrapper.setEntityId(getEntityId());
                wrapper.setDeltaX(getDeltaX());
                wrapper.setDeltaY(getDeltaY());
                wrapper.setDeltaZ(getDeltaZ());
                wrapper.setYaw(getYaw());
                wrapper.setPitch(getPitch());
                wrapper.setOnGround(isOnGround());
                if (v_1_8) {
                    wrapper.setRotating(true);
                    if (v_1_15) {
                        wrapper.setMoving(true);
                    }
                }
            }
            return packetInstance;
        }
    }
}