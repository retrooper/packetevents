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

    private Vector3i blockPos;
    private World world;
    private Material material;

    public WrappedPacketOutBlockChange(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutBlockChange(World world, Vector3i blockPos) {
        this.blockPos = blockPos;
        this.world = world;
    }

    public WrappedPacketOutBlockChange(Location location) {
        this.blockPos = new Vector3i(location);
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

        if (packet != null) {
            int x = 0;
            int y = 0;
            int z = 0;
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
            return new Vector3i(x, y, z);
        } else {
            return blockPos;
        }
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                writeInt(0, blockPos.getX());
                writeInt(1, blockPos.getY());
                writeInt(2, blockPos.getZ());
            } else {
                blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                write(NMSUtils.blockPosClass, 0, blockPosObj);
            }
        } else {
            this.blockPos = blockPos;
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
        Object packetPlayOutBlockChangeInstance = null;
        WrappedPacketOutBlockChange blockChange;
        Vector3i blockPosition = getBlockPosition();
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            try {
                Object nmsWorld = NMSUtils.convertBukkitWorldToWorldServer(world);
                packetPlayOutBlockChangeInstance = packetConstructor.newInstance(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), nmsWorld);
                blockChange = new WrappedPacketOutBlockChange(new NMSPacket(packetPlayOutBlockChangeInstance));
                if (getMaterial() != null) {
                    blockChange.setMaterial(getMaterial());
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            try {
                packetPlayOutBlockChangeInstance = packetConstructor.newInstance();
                blockChange = new WrappedPacketOutBlockChange(new NMSPacket(packetPlayOutBlockChangeInstance));
                if (getMaterial() != null) {
                    blockChange.setMaterial(getMaterial());
                } else {
                    Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
                    Object worldServer = NMSUtils.convertBukkitWorldToWorldServer(world);
                    Object nmsBlockData = getNMSWorldTypeMethodCache.invoke(worldServer, nmsBlockPos);
                    blockChange.write(NMSUtils.iBlockDataClass, 0, nmsBlockData);
                }
                blockChange.setBlockPosition(blockPosition);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return packetPlayOutBlockChangeInstance;
    }
}
