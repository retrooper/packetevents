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

package io.github.retrooper.packetevents.packetwrappers.out.entityteleport;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutEntityTeleport extends WrappedPacket implements SendableWrapper {
    private static boolean legacyVersionMode;
    private static boolean ultraLegacyVersionMode;
    private boolean listeningMode = false;
    private static final float rotationMultiplier = 256.0F / 360.0F;
    private static Constructor<?> constructor;
    private Entity entity = null;
    private int entityID = -1;
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;

    public static void load() {
        Class<?> packetClass = PacketTypeClasses.Server.ENTITY_TELEPORT;
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
                    constructor = packetClass.getConstructor();
                    ultraLegacyVersionMode = false;
                    legacyVersionMode = false;
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                }
            }

        }
    }

    public WrappedPacketOutEntityTeleport(Object packet) {
        super(packet);
        listeningMode = true;
    }

    public WrappedPacketOutEntityTeleport(int entityID, Location loc, boolean onGround) {
        this(entityID, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
    }

    public WrappedPacketOutEntityTeleport(int entityID, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public Entity getEntity() {
        if(entity == null) {
            entity = NMSUtils.getEntityById(getEntityId());
        }
        return entity;
    }

    public int getEntityId() {
        if (entityID != -1) {
            return entityID;
        } else {
            return entityID = readInt(0);
        }
    }

    public double getX() {
        if (!listeningMode) {
            return x;
        } else {
            if (legacyVersionMode) {
                return readInt(1) / 32.0D;
            } else {
                return readDouble(0);
            }
        }
    }

    public double getY() {
        if (!listeningMode) {
            return y;
        } else {
            if (legacyVersionMode) {
                return readInt(2) / 32.0D;
            } else {
                return readDouble(1);
            }
        }
    }

    public double getZ() {
        if (!listeningMode) {
            return z;
        } else {
            if (legacyVersionMode) {
                return readInt(3) / 32.0D;
            } else {
                return readDouble(2);
            }
        }
    }

    public float getYaw() {
        if (!listeningMode) {
            return yaw;
        } else {
            return (readByte(0) / rotationMultiplier);
        }
    }

    public boolean isOnGround() {
        if (listeningMode) {
            return readBoolean(0);
        } else {
            return onGround;
        }
    }

    @Override
    public Object asNMSPacket() {
        if (ultraLegacyVersionMode) {
            //1.7.10
            try {
                return constructor.newInstance(entityID, floor(x * 32.0D), floor(y * 32.0D), floor(z * 32.0D),
                        (byte) ((int) yaw * rotationMultiplier), (byte) (int) (pitch * rotationMultiplier), false, false);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (legacyVersionMode) {
            //1.8.x
            try {
                return constructor.newInstance(entityID, floor(x * 32.0D), floor(y * 32.0D), floor(z * 32.0D),
                        (byte) ((int) yaw * rotationMultiplier), (byte) (int) (pitch * rotationMultiplier), false);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            //newer versions
            Object instance = null;
            try {
                instance = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            WrappedPacket instanceWrapper = new WrappedPacket(instance);
            instanceWrapper.writeInt(0, entityID);
            instanceWrapper.writeDouble(0, x);
            instanceWrapper.writeDouble(1, y);
            instanceWrapper.writeDouble(2, z);

            instanceWrapper.writeByte(0, (byte) (yaw * rotationMultiplier));
            instanceWrapper.writeByte(1, (byte) (pitch * rotationMultiplier));
            if (onGround) {
                //default value is false, so we can save a reflection call
                instanceWrapper.writeBoolean(0, true);
            }
            return instance;
        }
        return null;
    }

    public float getPitch() {
        if (listeningMode) {
            return pitch;
        } else {
            return (readByte(1) / rotationMultiplier);
        }
    }

    private static int floor(double value) {
        int i = (int) value;
        return value < (double) i ? i - 1 : i;
    }
}
