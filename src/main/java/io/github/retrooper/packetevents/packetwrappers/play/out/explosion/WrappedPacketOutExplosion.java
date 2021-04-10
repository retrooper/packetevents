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

package io.github.retrooper.packetevents.packetwrappers.play.out.explosion;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class WrappedPacketOutExplosion extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> chunkPosConstructor, blockPosConstructor, packetConstructor, vec3dConstructor;

    private double x, y, z;
    private float strength;
    private List<Vector3i> records;
    private float playerMotionX, playerMotionY, playerMotionZ;

    public WrappedPacketOutExplosion(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutExplosion(double x, double y, double z, float strength, List<Vector3i> records,
                                     float playerMotionX, float playerMotionY, float playerMotionZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.strength = strength;
        this.records = records;
        this.playerMotionX = playerMotionX;
        this.playerMotionY = playerMotionY;
        this.playerMotionZ = playerMotionZ;
    }

    @Override
    protected void load() {
        Class<?> chunkPosClass = NMSUtils.getNMSClassWithoutException("ChunkPosition");
        Class<?> blockPosClass = NMSUtils.getNMSClassWithoutException("BlockPosition");
        Class<?> packetClass = NMSUtils.getNMSClassWithoutException("PacketPlayOutExplosion");
        Class<?> vec3DClass = NMSUtils.getNMSClassWithoutException("Vec3D");
        try {
            packetConstructor = packetClass.getConstructor(double.class, double.class, double.class, float.class, List.class, vec3DClass);
            vec3dConstructor = vec3DClass.getDeclaredConstructor(double.class, double.class, double.class);
            if (chunkPosClass != null) {
                chunkPosConstructor = chunkPosClass.getConstructor(int.class, int.class, int.class);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            if (blockPosClass != null) {
                blockPosConstructor = blockPosClass.getConstructor(int.class, int.class, int.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public double getX() {
        if (packet != null) {
            return readDouble(0);
        } else {
            return x;
        }
    }

    public void setX(double x) {
        if (packet != null) {
            writeDouble(0, x);
        } else {
            this.x = x;
        }
    }

    public double getY() {
        if (packet != null) {
            return readDouble(1);
        } else {
            return y;
        }
    }

    public void setY(double y) {
        if (packet != null) {
            writeDouble(1, y);
        } else {
            this.y = y;
        }
    }

    public double getZ() {
        if (packet != null) {
            return readDouble(2);
        } else {
            return z;
        }
    }

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
                Object[] arguments = {record.getX(), record.getY(), record.getZ()};
                Object position = null; //construct position
                try {
                    position = version.isNewerThan(ServerVersion.v_1_7_10) ? blockPosConstructor.newInstance(arguments) : chunkPosConstructor.newInstance(arguments);
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

    public float getPlayerMotionX() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return playerMotionX;
        }
    }

    public void setPlayerMotionX(float playerMotionX) {
        if (packet != null) {
            writeFloat(1, playerMotionX);
        } else {
            this.playerMotionX = playerMotionX;
        }
    }

    public float getPlayerMotionY() {
        if (packet != null) {
            return readFloat(2);
        } else {
            return playerMotionY;
        }
    }

    public void setPlayerMotionY(float playerMotionY) {
        if (packet != null) {
            writeFloat(2, playerMotionY);
        } else {
            this.playerMotionY = playerMotionY;
        }
    }

    public float getPlayerMotionZ() {
        if (packet != null) {
            return readFloat(3);
        } else {
            return playerMotionZ;
        }
    }

    public void setPlayerMotionZ(float playerMotionZ) {
        if (packet != null) {
            writeFloat(3, playerMotionZ);
        } else {
            this.playerMotionZ = playerMotionZ;
        }
    }

    @Override
    public Object asNMSPacket() {
        List<Object> positions = new ArrayList<>();
        for (Vector3i record : getRecords()) {
            Object[] arguments = {record.getX(), record.getY(), record.getZ()};
            Object position = null;
            try {
                position = version.isNewerThan(ServerVersion.v_1_7_10) ? blockPosConstructor.newInstance(arguments)
                        : chunkPosConstructor.newInstance(arguments);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            positions.add(position);

        }
        try {
            Object vec = vec3dConstructor.newInstance(getPlayerMotionX(), getPlayerMotionY(), getPlayerMotionZ());
            return packetConstructor.newInstance(getX(), getY(), getZ(), getStrength(), positions, vec);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
