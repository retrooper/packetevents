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

package io.github.retrooper.packetevents.packetwrappers.out.entityvelocity;

import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutEntityVelocity extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> velocityConstructor, vec3dConstructor;
    private static Class<?> velocityClass, vec3dClass;
    private static boolean isVec3dPresent;
    private int entityID;
    private double velocityX, velocityY, velocityZ;
    private Entity entity;
    public WrappedPacketOutEntityVelocity(final Object packet) {
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

    /**
     * Velocity Constructor parameter types:
     * 1.7.10 -&gt; 1.13.2 use int, double, double, double style,
     * 1.14+ use int, Vec3D style
     */
    public static void load() {
        velocityClass = PacketTypeClasses.Server.ENTITY_VELOCITY;
        if (version.isHigherThan(ServerVersion.v_1_13_2)) {
            try {
                vec3dClass = NMSUtils.getNMSClass("Vec3D");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            velocityConstructor = velocityClass.getConstructor(int.class, double.class, double.class, double.class);
        } catch (NoSuchMethodException e) {
            //That is fine, just a newer version
            try {
                velocityConstructor = velocityClass.getConstructor(int.class, vec3dClass);
                isVec3dPresent = true;
                //vec3d constructor
                vec3dConstructor = vec3dClass.getConstructor(double.class, double.class, double.class);
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }

        }
    }

    @Override
    protected void setup() {
        //ENTITY ID
        this.entityID = readInt(0);

        int x = readInt(1);
        int y = readInt(2);
        int z = readInt(3);

        //VELOCITY XYZ
        this.velocityX = x / 8000.0D;
        this.velocityY = y / 8000.0D;
        this.velocityZ = z / 8000.0D;
    }

    /**
     * Lookup the associated entity by the ID that was sent in the packet.
     * @return Entity
     */
    public Entity getEntity() {
        if(entity != null) {
            return entity;
        }
        return entity = NMSUtils.getEntityById(this.entityID);
    }

    /**
     * Get the ID of the entity.
     * If you do not want to use {@link #getEntity()},
     * you lookup the entity by yourself with this entity ID.
     * @return Entity ID
     */
    public int getEntityId() {
        return entityID;
    }

    /**
     * Get the Velocity X
     * @return Get Velocity X
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Get the Velocity Y
     * @return Get Velocity Y
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Get the Velocity Z
     * @return Get Velocity Z
     */
    public double getVelocityZ() {
        return velocityZ;
    }

    @Override
    public Object asNMSPacket() {
        if (!isVec3dPresent) {
            try {
                return velocityConstructor.newInstance(entityID, velocityX, velocityY, velocityZ);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return velocityConstructor.newInstance(entityID, vec3dConstructor.newInstance(velocityX, velocityY, velocityZ));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
