/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.entity;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class WrappedPacketOutEntity extends WrappedPacketEntityAbstraction implements SendableWrapper {
    //Byte = 1.7.10->1.8.8, Int = 1.9->1.15.x, Short = 1.16.x
    private static byte mode; //byte = 0, int = 1, short = 2
    private static double dXYZDivisor;
    private static int yawByteIndex;
    private static int pitchByteIndex = 1;
    private static Constructor<?> entityPacketConstructor, entityRelMovePacketConstructor, entityLookConstructor, entityRelMoveLookConstructor;
    private double deltaX, deltaY, deltaZ;
    private byte pitch, yaw;
    private boolean onGround, isLook;

    public WrappedPacketOutEntity(NMSPacket packet) {
        super(packet);

    }

    public WrappedPacketOutEntity(int entityID, double deltaX, double deltaY, double deltaZ,
                                  byte yaw, byte pitch, boolean onGround, boolean isLook) {
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.isLook = isLook;
    }

    public WrappedPacketOutEntity(int entityID, double deltaX, double deltaY, double deltaZ,
                                  byte yaw, byte pitch, boolean onGround) {
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.onGround = onGround;
        this.isLook = false;
    }

    public WrappedPacketOutEntity(Entity entity, double deltaX, double deltaY, double deltaZ,
                                  byte yaw, byte pitch, boolean onGround, boolean isLook) {
        this(entity.getEntityId(), deltaX, deltaY, deltaZ, yaw, pitch, onGround, isLook);
    }

    public WrappedPacketOutEntity(Entity entity, double deltaX, double deltaY, double deltaZ,
                                  byte yaw, byte pitch, boolean onGround) {
        this(entity.getEntityId(), deltaX, deltaY, deltaZ, yaw, pitch, onGround, false);
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.ENTITY;

        Field dxField = Reflection.getField(packetClass, 1);
        if (Objects.requireNonNull(dxField).equals(Reflection.getField(packetClass, byte.class, 0))) {
            mode = 0;
            yawByteIndex = 3;
            pitchByteIndex = 4;
        } else if (dxField.equals(Reflection.getField(packetClass, int.class, 1))) {
            mode = 1;
        } else if (dxField.equals(Reflection.getField(packetClass, short.class, 0))) {
            mode = 2;
        }

        if (mode == 0) {
            dXYZDivisor = 32.0;
        } else {
            dXYZDivisor = 4096.0;
        }
        try {
            entityPacketConstructor = packetClass.getConstructor(int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public byte getPitch() {
        if (packet != null) {
            return readByte(pitchByteIndex);
        } else {
            return pitch;
        }
    }

    public void setPitch(byte pitch) {
        if (packet != null) {
            writeByte(pitchByteIndex, pitch);
        } else {
            this.pitch = pitch;
        }
    }

    public byte getYaw() {
        if (packet != null) {
            return readByte(yawByteIndex);
        } else {
            return yaw;
        }
    }

    public void setYaw(byte yaw) {
        if (packet != null) {
            writeByte(yawByteIndex, yaw);
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

    public boolean isLook() {
        if (packet != null) {
            return readBoolean(1);
        } else {
            return isLook;
        }
    }

    public void setLook(boolean isLook) {
        if (packet != null) {
            writeBoolean(1, isLook);
        } else {
            this.isLook = isLook;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            Object packetInstance = entityPacketConstructor.newInstance(getEntityId());
            WrappedPacketOutEntity wrapper = new WrappedPacketOutEntity(new NMSPacket(packetInstance));
            wrapper.setDeltaX(getDeltaX());
            wrapper.setDeltaY(getDeltaY());
            wrapper.setDeltaZ(getDeltaZ());
            wrapper.setYaw(getYaw());
            wrapper.setPitch(getPitch());
            wrapper.setOnGround(isOnGround());
            wrapper.setLook(isLook());
            return packetInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class WrappedPacketOutEntityLook extends WrappedPacketOutEntity {
        public WrappedPacketOutEntityLook(NMSPacket packet) {
            super(packet);
        }

        public WrappedPacketOutEntityLook(int entityID, byte yaw, byte pitch, boolean onGround) {
            super(entityID, 0, 0, 0, yaw, pitch, onGround, true);
        }

        @Override
        protected void load() {
            super.load();
            try {
                entityLookConstructor = PacketTypeClasses.Play.Server.ENTITY_LOOK.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object asNMSPacket() {
            try {
                Object packetInstance = entityLookConstructor.newInstance();
                WrappedPacketOutEntityLook wrapper = new WrappedPacketOutEntityLook(new NMSPacket(packetInstance));
                wrapper.setEntityId(getEntityId());
                wrapper.setYaw(getYaw());
                wrapper.setPitch(getPitch());
                wrapper.setOnGround(isOnGround());
                wrapper.setLook(true);
                return packetInstance;
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class WrappedPacketOutRelEntityMove extends WrappedPacketOutEntity {
        public WrappedPacketOutRelEntityMove(NMSPacket packet) {
            super(packet);
        }

        public WrappedPacketOutRelEntityMove(int entityID, double deltaX, double deltaY, double deltaZ, boolean onGround) {
            super(entityID, deltaX, deltaY, deltaZ, (byte) 0, (byte) 0, onGround, false);
        }

        @Override
        protected void load() {
            super.load();
            try {
                entityRelMovePacketConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object asNMSPacket() {
            try {
                Object packetInstance = entityRelMovePacketConstructor.newInstance();
                WrappedPacketOutRelEntityMove wrapper = new WrappedPacketOutRelEntityMove(new NMSPacket(packetInstance));
                wrapper.setEntityId(getEntityId());
                wrapper.setDeltaX(getDeltaX());
                wrapper.setDeltaY(getDeltaY());
                wrapper.setDeltaZ(getDeltaZ());
                wrapper.setOnGround(isOnGround());
                wrapper.setLook(false);
                return packetInstance;
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class WrappedPacketOutRelEntityMoveLook extends WrappedPacketOutEntity {
        public WrappedPacketOutRelEntityMoveLook(NMSPacket packet) {
            super(packet);
        }

        public WrappedPacketOutRelEntityMoveLook(int entityID, double deltaX, double deltaY, double deltaZ, byte yaw, byte pitch, boolean onGround) {
            super(entityID, deltaX, deltaY, deltaZ, yaw, pitch, onGround, false);
        }

        @Override
        protected void load() {
            super.load();
            try {
                entityRelMoveLookConstructor = PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object asNMSPacket() {
            try {
                Object packetInstance = entityRelMoveLookConstructor.newInstance();
                WrappedPacketOutRelEntityMoveLook wrapper = new WrappedPacketOutRelEntityMoveLook(new NMSPacket(packetInstance));
                wrapper.setEntityId(getEntityId());
                wrapper.setDeltaX(getDeltaX());
                wrapper.setDeltaY(getDeltaY());
                wrapper.setDeltaZ(getDeltaZ());
                wrapper.setYaw(getYaw());
                wrapper.setPitch(getPitch());
                wrapper.setOnGround(isOnGround());
                wrapper.setLook(true);
                return packetInstance;
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}