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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityteleport;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutEntityTeleport extends WrappedPacket implements SendableWrapper {
    private static final float rotationMultiplier = 256.0F / 360.0F;
    private static boolean legacyVersionMode;
    private static boolean ultraLegacyVersionMode;
    private static Constructor<?> constructor;
    private Entity entity = null;
    private int entityID = -1;
    private Vector3d position;
    private float yaw, pitch;
    private boolean onGround;

    public WrappedPacketOutEntityTeleport(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityTeleport(int entityID, Location loc, boolean onGround) {
        this(entityID, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
    }

    public WrappedPacketOutEntityTeleport(int entityID, Vector3d position, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
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

    private static int floor(double value) {
        int i = (int) value;
        return value < (double) i ? i - 1 : i;
    }

    @Override
    protected void load() {
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
                    constructor = packetClass.getConstructor();
                    ultraLegacyVersionMode = false;
                    legacyVersionMode = false;
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                }
            }

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

    public Vector3d getPosition() {
        if (packet != null) {
            double x, y, z;
            if (legacyVersionMode) {
                x = readInt(1) / 32.0D;
                y = readInt(2) / 32.0D;
                z = readInt(3) / 32.0D;
            }
            else {
                x = readDouble(0);
                y = readDouble(1);
                z = readDouble(2);
            }
            return new Vector3d(x, y, z);
        }
        else {
            return position;
        }
    }

    public void setPosition(Vector3d position) {
        if (packet != null) {
            if (legacyVersionMode) {
                writeInt(1, floor(position.getX() * 32.0D));
                writeInt(2, floor(position.getY() * 32.0D));
                writeInt(3, floor(position.getZ() * 32.0D));
            }
            else {
                writeDouble(0, position.getX());
                writeDouble(1, position.getY());
                writeDouble(2, position.getZ());
            }
        }
        else {
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
    public Object asNMSPacket() {
        Vector3d pos = getPosition();
        if (ultraLegacyVersionMode) {
            //1.7.10
            try {
                return constructor.newInstance(entityID, floor(pos.getX() * 32.0D), floor(pos.getY() * 32.0D), floor(pos.getZ() * 32.0D),
                        (byte) ((int) getYaw() * rotationMultiplier), (byte) (int) (getPitch() * rotationMultiplier), false, false);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (legacyVersionMode) {
            //1.8.x
            try {
                return constructor.newInstance(entityID, floor(pos.getX() * 32.0D), floor(pos.getY() * 32.0D), floor(pos.getZ() * 32.0D),
                        (byte) ((int) getYaw() * rotationMultiplier), (byte) (int) (getPitch() * rotationMultiplier), false);
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
        return null;
    }
}
