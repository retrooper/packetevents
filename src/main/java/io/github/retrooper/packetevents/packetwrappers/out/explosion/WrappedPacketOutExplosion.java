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

package io.github.retrooper.packetevents.packetwrappers.out.explosion;

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

    public static void load() {
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

    private boolean isListening = false;
    private double x, y, z;
    private float strength;
    private List<Vector3i> records;
    private float playerMotionX, playerMotionY, playerMotionZ;

    public WrappedPacketOutExplosion(Object packet) {
        super(packet);
        isListening = true;
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

    public double getX() {
        if (isListening) {
            return readDouble(0);
        } else {
            return x;
        }
    }

    public double getY() {
        if (isListening) {
            return readDouble(1);
        } else {
            return y;
        }
    }

    public double getZ() {
        if (isListening) {
            return readDouble(2);
        } else {
            return z;
        }
    }

    public float getStrength() {
        if (isListening) {
            return readFloat(0);
        } else {
            return strength;
        }
    }

    public List<Vector3i> getRecords() {
        if (isListening) {
            List<Vector3i> recordsList = new ArrayList<>();
            List<?> rawRecordsList = (List<?>) readObject(0, List.class);
            if (rawRecordsList.isEmpty()) {
                return new ArrayList<>();
            }
            for (Object position : rawRecordsList) {
                WrappedPacket posWrapper = new WrappedPacket(position);
                recordsList.add(new Vector3i(posWrapper.readInt(0), posWrapper.readInt(1), posWrapper.readInt(2)));
            }
            return recordsList;
        } else {
            return records;
        }
    }

    public float getPlayerMotionX() {
        if (isListening) {
            return readFloat(1);
        } else {
            return playerMotionX;
        }
    }

    public float getPlayerMotionY() {
        if (isListening) {
            return readFloat(2);
        } else {
            return playerMotionY;
        }
    }

    public float getPlayerMotionZ() {
        if (isListening) {
            return readFloat(3);
        } else {
            return playerMotionZ;
        }
    }

    @Override
    public Object asNMSPacket() {
        if (version.equals(ServerVersion.v_1_7_10)) {
            List<Object> chunkPositions = new ArrayList<>();
            for (Vector3i vec : records) {
                try {
                    chunkPositions.add(chunkPosConstructor.newInstance(vec.x, vec.y, vec.z));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            try {
                Object vec = vec3dConstructor.newInstance(playerMotionX, playerMotionY, playerMotionZ);
                return packetConstructor.newInstance(x, y, z, strength, chunkPositions, vec);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            List<Object> blockPositions = new ArrayList<>();
            for (Vector3i vec : records) {
                try {
                    blockPositions.add(blockPosConstructor.newInstance(vec.x, vec.y, vec.z));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            try {
                Object vec = vec3dConstructor.newInstance(playerMotionX, playerMotionY, playerMotionZ);
                return packetConstructor.newInstance(x, y, z, strength, blockPositions, vec);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
