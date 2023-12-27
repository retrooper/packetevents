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

package io.github.retrooper.packetevents.packetwrappers.play.out.explosion;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class WrappedPacketOutExplosion extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_8;
    private static Constructor<?> chunkPosConstructor, packetConstructor;

    private double x, y, z;
    private float strength;
    private List<Vector3i> records;
    private float playerVelocityX, playerVelocityY, playerVelocityZ;

    public WrappedPacketOutExplosion(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutExplosion(Vector3d position, float strength, List<Vector3i> records,
                                     Vector3f playerVelocity) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.strength = strength;
        this.records = records;
        this.playerVelocityX = playerVelocity.x;
        this.playerVelocityY = playerVelocity.y;
        this.playerVelocityZ = playerVelocity.z;
    }

    public WrappedPacketOutExplosion(double x, double y, double z, float strength, List<Vector3i> records,
                                     float playerVelocityX, float playerVelocityY, float playerVelocityZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.strength = strength;
        this.records = records;
        this.playerVelocityX = playerVelocityX;
        this.playerVelocityY = playerVelocityY;
        this.playerVelocityZ = playerVelocityZ;
    }

    @Override
    protected void load() {
        v_1_8 = version.isNewerThanOrEquals(ServerVersion.v_1_8);

        try {
            Class<?> chunkPosClass = NMSUtils.getNMSClassWithoutException("ChunkPosition");
            packetConstructor = PacketTypeClasses.Play.Server.EXPLOSION.getConstructor(double.class, double.class, double.class, float.class, List.class, NMSUtils.vec3DClass);
            if (chunkPosClass != null) {
                chunkPosConstructor = chunkPosClass.getConstructor(int.class, int.class, int.class);
            }
        } catch (NoSuchMethodException e) {
            //1.20.4 or newer
        }
    }

    public Vector3d getPosition() {
        if (packet != null) {
            return new Vector3d(readDouble(0), readDouble(1), readDouble(2));
        }
        else {
            return new Vector3d(x, y, z);
        }
    }

    public void setPosition(Vector3d position) {
        if (packet != null) {
            writeDouble(0, position.x);
            writeDouble(1, position.y);
            writeDouble(2, position.z);
        }
        else {
            this.x = position.x;
            this.y = position.y;
            this.z = position.z;
        }
    }

    @Deprecated
    public double getX() {
        if (packet != null) {
            return readDouble(0);
        } else {
            return x;
        }
    }

    @Deprecated
    public void setX(double x) {
        if (packet != null) {
            writeDouble(0, x);
        } else {
            this.x = x;
        }
    }

    @Deprecated
    public double getY() {
        if (packet != null) {
            return readDouble(1);
        } else {
            return y;
        }
    }

    @Deprecated
    public void setY(double y) {
        if (packet != null) {
            writeDouble(1, y);
        } else {
            this.y = y;
        }
    }

    @Deprecated
    public double getZ() {
        if (packet != null) {
            return readDouble(2);
        } else {
            return z;
        }
    }

    @Deprecated
    public void setZ(double z) {
        if (packet != null) {
            writeDouble(2, z);
        } else {
            this.z = z;
        }
    }


    public float getStrength() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return strength;
        }
    }

    public void setStrength(float strength) {
        if (packet != null) {
            writeFloat(0, strength);
        } else {
            this.strength = strength;
        }
    }

    public List<Vector3i> getRecords() {
        if (packet != null) {
            List<Vector3i> recordsList = new ArrayList<>();
            List<?> rawRecordsList = (List<?>) readObject(0, List.class);
            if (rawRecordsList.isEmpty()) {
                return new ArrayList<>();
            }
            for (Object position : rawRecordsList) {
                WrappedPacket posWrapper = new WrappedPacket(new NMSPacket(position));
                int x = posWrapper.readInt(0);
                int y = posWrapper.readInt(1);
                int z = posWrapper.readInt(2);
                recordsList.add(new Vector3i(x, y, z));
            }
            return recordsList;
        } else {
            return records;
        }
    }

    public void setRecords(List<Vector3i> records) {
        if (packet != null) {
            List<Object> nmsRecordsList = new ArrayList<>();
            for (Vector3i record : records) {
                Object position = null; //construct position
                try {
                    position = v_1_8 ? NMSUtils.generateNMSBlockPos(record) : chunkPosConstructor.newInstance(record.x, record.y, record.z);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                nmsRecordsList.add(position);
            }
            write(List.class, 0, nmsRecordsList);
        } else {
            this.records = records;
        }
    }

    public Vector3f getPlayerVelocity() {
        if (packet != null) {
            return new Vector3f(readFloat(1), readFloat(2), readFloat(3));
        }
        else {
            return new Vector3f(playerVelocityX, playerVelocityY, playerVelocityZ);
        }
    }

    public void setPlayerVelocity(Vector3f playerVelocity) {
        if (packet != null) {
            writeFloat(1, playerVelocity.x);
            writeFloat(2, playerVelocity.y);
            writeFloat(3, playerVelocity.z);
        }
        else {
            this.playerVelocityX = playerVelocity.x;
            this.playerVelocityY = playerVelocity.y;
            this.playerVelocityZ = playerVelocity.z;
        }
    }

    @Deprecated
    public float getPlayerMotionX() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return playerVelocityX;
        }
    }

    @Deprecated
    public void setPlayerMotionX(float playerMotionX) {
        if (packet != null) {
            writeFloat(1, playerMotionX);
        } else {
            this.playerVelocityX = playerMotionX;
        }
    }

    @Deprecated
    public float getPlayerMotionY() {
        if (packet != null) {
            return readFloat(2);
        } else {
            return playerVelocityY;
        }
    }

    @Deprecated
    public void setPlayerMotionY(float playerMotionY) {
        if (packet != null) {
            writeFloat(2, playerMotionY);
        } else {
            this.playerVelocityY = playerMotionY;
        }
    }

    @Deprecated
    public float getPlayerMotionZ() {
        if (packet != null) {
            return readFloat(3);
        } else {
            return playerVelocityZ;
        }
    }

    @Deprecated
    public void setPlayerMotionZ(float playerMotionZ) {
        if (packet != null) {
            writeFloat(3, playerMotionZ);
        } else {
            this.playerVelocityZ = playerMotionZ;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        List<Object> positions = new ArrayList<>();
        for (Vector3i record : getRecords()) {
            Object position = v_1_8 ? NMSUtils.generateNMSBlockPos(record)
                    : chunkPosConstructor.newInstance(record.x, record.y, record.z);
            positions.add(position);
        }
        Vector3f velocity = getPlayerVelocity();
        Vector3f pos = getPlayerVelocity();
        Object vec = NMSUtils.generateVec3D(velocity);
        return packetConstructor.newInstance(pos.x, pos.y, pos.z, getStrength(), positions, vec);
    }
}
