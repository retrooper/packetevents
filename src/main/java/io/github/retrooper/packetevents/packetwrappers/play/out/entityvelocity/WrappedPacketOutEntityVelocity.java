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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public final class WrappedPacketOutEntityVelocity extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> velocityConstructor;
    private static boolean isVec3dPresent;
    private double velocityX, velocityY, velocityZ;

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

    public Vector3d getVelocity() {
        if (packet != null) {
            double velX = readInt(1) / 8000.0;
            double velY = readInt(2) / 8000.0;
            double velZ = readInt(3) / 8000.0;
            return new Vector3d(velX, velY, velZ);
        }
        else {
            return new Vector3d(velocityX, velocityY, velocityZ);
        }
    }

    public void setVelocity(Vector3d velocity) {
        if (packet != null) {
            writeInt(1, (int) (velocity.x * 8000.0));
            writeInt(2, (int) (velocity.y * 8000.0));
            writeInt(3, (int) (velocity.z * 8000.0));
        }
        else {
            this.velocityX = velocity.x;
            this.velocityY = velocity.y;
            this.velocityZ = velocity.z;
        }
    }

    //TODO Cut off at 1.8.1 and optimize the methods above.
    @Deprecated
    public double getVelocityX() {
        if (packet != null) {
            return readInt(1) / 8000.0;
        } else {
            return velocityX;
        }
    }

    @Deprecated
    public void setVelocityX(double x) {
        if (packet != null) {
            writeInt(1, (int) (x * 8000.0));
        } else {
            this.velocityX = x;
        }
    }

    @Deprecated
    public double getVelocityY() {
        if (packet != null) {
            return readInt(2) / 8000.0;
        } else {
            return velocityY;
        }
    }

    @Deprecated
    public void setVelocityY(double y) {
        if (packet != null) {
            writeInt(2, (int) (y * 8000.0));
        }
    }

    @Deprecated
    public double getVelocityZ() {
        if (packet != null) {
            return readInt(3) / 8000.0;
        } else {
            return velocityZ;
        }
    }

    @Deprecated
    public void setVelocityZ(double z) {
        if (packet != null) {
            writeInt(3, (int) (z * 8000.0));
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        if (!isVec3dPresent) {
            return velocityConstructor.newInstance(getEntityId(), getVelocityX(), getVelocityY(), getVelocityZ());
        } else {
            return velocityConstructor.newInstance(getEntityId(), NMSUtils.generateVec3D(getVelocityX(), getVelocityY(), getVelocityZ()));
        }
    }

}
