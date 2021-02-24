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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutEntityVelocity extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> velocityConstructor;
    private static boolean isVec3dPresent;
    private int entityID = -1;
    private double velocityX, velocityY, velocityZ;
    private Entity entity;

    public WrappedPacketOutEntityVelocity(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityVelocity(final Entity entity, final double velocityX, final double velocityY, final double velocityZ) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    public WrappedPacketOutEntityVelocity(int entityID, double velX, double velY, double velZ) {
        this.entityID = entityID;
        this.velocityX = velX;
        this.velocityY = velY;
        this.velocityZ = velZ;
    }

    @Override
    protected void load() {
        Class<?> velocityClass = PacketTypeClasses.Play.Server.ENTITY_VELOCITY;
        try {
            velocityConstructor = velocityClass.getConstructor(int.class, double.class, double.class, double.class);
        } catch (NoSuchMethodException e) {
            //That is fine, just a newer version
            try {
                velocityConstructor = velocityClass.getConstructor(int.class, NMSUtils.vec3DClass);
                isVec3dPresent = true;
                //vec3d constructor
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }

        }
    }

    public Entity getEntity() {
        if (entity != null) {
            return entity;
        }
        return entity = NMSUtils.getEntityById(getEntityId());
    }

    public void setEntity(Entity entity) {
        setEntityId(entity.getEntityId());
        this.entity = null;
    }

    public int getEntityId() {
        if (entityID != -1) {
            return entityID;
        }
        return entityID = readInt(0);
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(0, this.entityID = entityID);
        }
        else {
            this.entityID = entityID;
        }
        this.entity = null;
    }

    public double getVelocityX() {
        if (packet != null) {
            return readInt(1) / 8000.0D;
        } else {
            return velocityX;
        }
    }

    public void setVelocityX(double x) {
        if (packet != null) {
            writeInt(1, (int) (x * 8000.0D));
        }
        else {
            this.velocityX = x;
        }
    }

    public double getVelocityY() {
        if (packet != null) {
            return readInt(2) / 8000.0D;
        } else {
            return velocityY;
        }
    }

    public void setVelocityY(double y) {
        if (packet != null) {
            writeInt(2, (int) (y * 8000.0D));
        }
    }

    public double getVelocityZ() {
        if (packet != null) {
            return readInt(3) / 8000.0D;
        } else {
            return velocityZ;
        }
    }

    public void setVelocityZ(double z) {
        if (packet != null) {
            writeInt(3, (int) (z * 8000.0D));
        }
    }

    @Override
    public Object asNMSPacket() {
        if (!isVec3dPresent) {
            try {
                return velocityConstructor.newInstance(getEntityId(), getVelocityX(), getVelocityY(), getVelocityZ());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return velocityConstructor.newInstance(entityID, NMSUtils.generateVec3D(getVelocityX(), getVelocityY(), getVelocityZ()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
