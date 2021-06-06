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

package io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.UUID;

public class WrappedPacketOutNamedEntitySpawn extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static final float rotationDividend = 256.0F / 360.0F;
    private static boolean doublesPresent, dataWatcherPresent;
    private static Constructor<?> packetDefaultConstructor;
    private UUID uuid;
    private Vector3d position;
    private float yaw, pitch;

    public WrappedPacketOutNamedEntitySpawn(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutNamedEntitySpawn(int entityID, UUID uuid, Location location) {
        this.entityID = entityID;
        this.uuid = uuid;
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public WrappedPacketOutNamedEntitySpawn(Entity entity, UUID uuid, Location location) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = uuid;
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public WrappedPacketOutNamedEntitySpawn(Entity entity, Location location) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = entity.getUniqueId();
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public WrappedPacketOutNamedEntitySpawn(Entity entity) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = entity.getUniqueId();
        Location location = entity.getLocation();
        this.position = new Vector3d(location.getX(), location.getY(), location.getZ());
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public WrappedPacketOutNamedEntitySpawn(int entityID, UUID uuid, Vector3d position, float yaw, float pitch) {
        this.entityID = entityID;
        this.uuid = uuid;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public WrappedPacketOutNamedEntitySpawn(Entity entity, UUID uuid, Vector3d position, float yaw, float pitch) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.uuid = uuid;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }


    @Override
    protected void load() {
        doublesPresent = Reflection.getField(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, double.class, 1) != null;
        dataWatcherPresent = Reflection.getField(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, NMSUtils.dataWatcherClass, 0) != null;
        try {
            packetDefaultConstructor = PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public UUID getUUID() {
        if (packet != null) {
            return readObject(0, UUID.class);
        } else {
            return uuid;
        }
    }

    public void setUUID(UUID uuid) {
        if (packet != null) {
            write(UUID.class, 0, uuid);
        } else {
            this.uuid = uuid;
        }
    }

    public Vector3d getPosition() {
        if (packet != null) {
            double x;
            double y;
            double z;
            if (doublesPresent) {
                x = readDouble(0);
                y = readDouble(1);
                z = readDouble(2);
            } else {
                x = readInt(1) / 32.0D;
                y = readInt(2) / 32.0D;
                z = readInt(3) / 32.0D;
            }
            return new Vector3d(x, y, z);
        } else {
            return position;
        }
    }

    public void setPosition(Vector3d position) {
        if (packet != null) {
            if (doublesPresent) {
                writeDouble(0, position.x);
                writeDouble(1, position.y);
                writeDouble(2, position.z);
            } else {
                writeInt(1, (int) (position.x * 32.0D));
                writeInt(2, (int) (position.y * 32.0D));
                writeInt(3, (int) (position.z * 32.0D));
            }
            writeByte(0, (byte) (yaw * rotationDividend));
            writeByte(1, (byte) (pitch * rotationDividend));
        } else {
            this.position = position;
        }
    }

    public float getYaw() {
        if (packet != null) {
            return readByte(0) / rotationDividend;
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            writeByte(0, (byte) (yaw * rotationDividend));
        } else {
            this.yaw = yaw;
        }
    }

    public float getPitch() {
        if (packet != null) {
            return readByte(1) / rotationDividend;
        } else {
            return pitch;
        }
    }

    public void setPitch(float pitch) {
        if (packet != null) {
            writeByte(1, (byte) (pitch * rotationDividend));
        } else {
            this.pitch = pitch;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetPlayOutNamedEntitySpawnInstance = packetDefaultConstructor.newInstance();
        WrappedPacketOutNamedEntitySpawn wrappedPacketOutNamedEntitySpawn = new WrappedPacketOutNamedEntitySpawn(new NMSPacket(packetPlayOutNamedEntitySpawnInstance));
        wrappedPacketOutNamedEntitySpawn.setEntityId(getEntityId());
        wrappedPacketOutNamedEntitySpawn.setUUID(getUUID());
        wrappedPacketOutNamedEntitySpawn.setPosition(getPosition());
        wrappedPacketOutNamedEntitySpawn.setYaw(getYaw());
        wrappedPacketOutNamedEntitySpawn.setPitch(getPitch());
        if (dataWatcherPresent) {
            wrappedPacketOutNamedEntitySpawn.write(NMSUtils.dataWatcherClass, 0, NMSUtils.generateDataWatcher(null));
        }

        return packetPlayOutNamedEntitySpawnInstance;
    }
}
