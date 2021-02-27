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

package io.github.retrooper.packetevents.packetwrappers.play.out.blockchange;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WrappedPacketOutBlockChange extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private static Method getNMSBlockMethodCache = null;
    private static Method getNMSWorldTypeMethodCache = null;


    private Object blockPosObj = null;

    private int blockPosX;
    private int blockPosY;
    private int blockPosZ;
    private World world;
    private Material material;

    public WrappedPacketOutBlockChange(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutBlockChange(World world, Vector3i blockPos) {
        this.blockPosX = blockPos.x;
        this.blockPosY = blockPos.y;
        this.blockPosZ = blockPos.z;
        this.world = world;
    }

    public WrappedPacketOutBlockChange(Location location) {
        this.blockPosX = location.getBlockX();
        this.blockPosY = location.getBlockY();
        this.blockPosZ = location.getBlockZ();
        this.world = location.getWorld();
    }

    public WrappedPacketOutBlockChange(World world, Vector3i blockPos, Material material) {
        this(world, blockPos);
        this.material = material;
    }

    public WrappedPacketOutBlockChange(Location location, Material material) {
        this(location);
        this.material = material;
    }

    @Override
    protected void load() {
        getNMSBlockMethodCache = Reflection.getMethod(NMSUtils.iBlockDataClass, "getBlock", 0);
        getNMSWorldTypeMethodCache = Reflection.getMethod(NMSUtils.nmsWorldClass, "getType", 0);
        if (version.equals(ServerVersion.v_1_7_10)) {
            try {
                packetConstructor = PacketTypeClasses.Play.Server.BLOCK_CHANGE.getConstructor(int.class, int.class, int.class, NMSUtils.nmsWorldClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            try {
                packetConstructor = PacketTypeClasses.Play.Server.BLOCK_CHANGE.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public Vector3i getBlockPosition() {
        int x = 0;
        int y = 0;
        int z = 0;
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                x = readInt(0);
                y = readInt(1);
                z = readInt(2);
            } else {
                if (blockPosObj == null) {
                    blockPosObj = readObject(0, NMSUtils.blockPosClass);
                }
                try {
                    x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
                    y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
                    z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } else {
            x = blockPosX;
            y = blockPosY;
            z = blockPosZ;
        }
        return new Vector3i(x, y, z);
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                writeInt(0, blockPos.x);
                writeInt(1, blockPos.y);
                writeInt(2, blockPos.z);
            } else {
                blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.x, blockPos.y, blockPos.z);
                write(NMSUtils.blockPosClass, 0, blockPosObj);
            }
        } else {
            blockPosX = blockPos.x;
            blockPosY = blockPos.y;
            blockPosZ = blockPos.z;
        }
    }

    public Material getMaterial() {
        if (packet != null) {
            Object nmsBlock = null;
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                nmsBlock = readObject(0, NMSUtils.blockClass);
            } else {
                Object iBlockDataObj = readObject(0, NMSUtils.iBlockDataClass);
                try {
                    nmsBlock = getNMSBlockMethodCache.invoke(iBlockDataObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            return NMSUtils.getMaterialFromNMSBlock(nmsBlock);
        } else {
            return material;
        }
    }

    public void setMaterial(Material material) {
        if (packet != null) {
            Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(material);
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                write(NMSUtils.blockClass, 0, nmsBlock);
            } else {
                WrappedPacket nmsBlockWrapper = new WrappedPacket(new NMSPacket(nmsBlock), NMSUtils.blockClass);
                Object iBlockData = nmsBlockWrapper.readObject(0, NMSUtils.iBlockDataClass);
                write(NMSUtils.iBlockDataClass, 0, iBlockData);
            }
        } else {
            this.material = material;
        }
    }

    @Override
    public Object asNMSPacket() {
        Object nmsPacket = null;
        WrappedPacketOutBlockChange blockChange;
        Vector3i blockPosition = getBlockPosition();
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            try {
                Object nmsWorld = NMSUtils.convertBukkitWorldToNMSWorld(world);

                nmsPacket = packetConstructor.newInstance(blockPosition.x, blockPosition.y, blockPosition.z, nmsWorld);
                blockChange = new WrappedPacketOutBlockChange(new NMSPacket(nmsPacket));
                if (material != null) {
                    blockChange.setMaterial(material);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            try {
                nmsPacket = packetConstructor.newInstance();
                blockChange = new WrappedPacketOutBlockChange(new NMSPacket(nmsPacket));
                if (material != null) {
                    blockChange.setMaterial(material);
                } else {
                    Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPosition.x, blockPosition.y, blockPosition.z);
                    Object nmsWorld = NMSUtils.convertBukkitWorldToNMSWorld(world);
                    Object nmsBlockData = getNMSWorldTypeMethodCache.invoke(nmsWorld, nmsBlockPos);
                    blockChange.write(NMSUtils.iBlockDataClass, 0, nmsBlockData);
                }
                blockChange.setBlockPosition(blockPosition);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return nmsPacket;
    }
}
