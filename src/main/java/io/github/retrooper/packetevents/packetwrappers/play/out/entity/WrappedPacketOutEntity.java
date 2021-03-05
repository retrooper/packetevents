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
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class WrappedPacketOutEntity extends WrappedPacket implements SendableWrapper {
    //Byte = 1.7.10->1.8.8, Int = 1.9->1.15.x, Short = 1.16.x
    private static byte mode; //byte = 0, int = 1, short = 2
    private static double dXYZDivisor;
    private static int yawByteIndex;
    private static int pitchByteIndex = 1;
    private static Constructor<?> entityPacketConstructor;
    private Entity entity;

    private int entityID = -1;
    private double deltaX, deltaY, deltaZ;
    private byte pitch, yaw;
    private boolean onGround;

    public WrappedPacketOutEntity(NMSPacket packet) {
        super(packet);

    }

    public WrappedPacketOutEntity(int entityID, double deltaX, double deltaY, double deltaZ,
                                  byte pitch, byte yaw, boolean onGround) {
        this.entityID = entityID;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.onGround = onGround;
    }

    public WrappedPacketOutEntity(Entity entity, double deltaX, double deltaY, double deltaZ,
                                  byte pitch, byte yaw, boolean onGround) {
        this(entity.getEntityId(), deltaX, deltaY, deltaZ, pitch, yaw, onGround);
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
        int dz = (int) (deltaZ * dXYZDivisor);
        if (packet != null) {
            switch (mode) {
                case 0:
                    writeByte(2, (byte) dz);
                case 1:
                    writeInt(3, dz);
                case 2:
                    writeShort(2, (short) dz);
            }
        } else {
            this.deltaZ = deltaZ;
        }
    }

    @Nullable
    public Entity getEntity() {
        return getEntity(null);
    }

    @Nullable
    public Entity getEntity(@Nullable World world) {
       if (entity == null) {
           entity = NMSUtils.getEntityById(world, getEntityId());
       }
       return entity;
    }

    public void setEntity(Entity entity) {
        setEntityId(entity.getEntityId());
        this.entity = entity;
    }

    public int getEntityId() {
        if (entityID != -1) {
            return entityID;
        } else {
            return entityID = readInt(0);
        }
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(0, this.entityID = entityID);
        } else {
            this.entityID = entityID;
        }
        this.entity = null;
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
    public Object asNMSPacket() {
        try {
            Object packetInstance = entityPacketConstructor.newInstance(entityID);
            int dx = (int) (getDeltaX() * dXYZDivisor);
            int dy = (int) (getDeltaY() * dXYZDivisor);
            int dz = (int) (getDeltaZ() * dXYZDivisor);
            WrappedPacket wrapper = new WrappedPacket(new NMSPacket(packetInstance));
            switch (mode) {
                case 0:
                    wrapper.writeByte(0, (byte) dx);
                    wrapper.writeByte(1, (byte) dy);
                    wrapper.writeByte(2, (byte) dz);
                    break;
                case 1:
                    wrapper.writeInt(1, dx);
                    wrapper.writeInt(2, dy);
                    wrapper.writeInt(3, dz);
                    break;
                case 2:
                    wrapper.writeShort(0, (short) dx);
                    wrapper.writeShort(1, (short) dy);
                    wrapper.writeShort(2, (short) dz);
                    break;
            }
            wrapper.writeByte(yawByteIndex, yaw);
            wrapper.writeByte(pitchByteIndex, pitch);
            wrapper.writeBoolean(0, onGround);
            return packetInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
