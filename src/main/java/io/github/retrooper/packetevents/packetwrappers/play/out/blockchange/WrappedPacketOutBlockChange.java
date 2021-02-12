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
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//TODO TEST ON 1.7.10
public class WrappedPacketOutBlockChange extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private static Method getBlockIdMethodCache = null;
    private static Method getNMSWorldTypeMethodCache = null;
    private Object blockPosObj = null;

    private int blockPosX;
    private int blockPosY;
    private int blockPosZ;
    private World world;

    public WrappedPacketOutBlockChange(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutBlockChange(World world, int blockPosX, int blockPosY, int blockPosZ) {
        this.blockPosX = blockPosX;
        this.blockPosY = blockPosY;
        this.blockPosZ = blockPosZ;
        this.world = world;
    }

    public WrappedPacketOutBlockChange(Location location) {
        this.blockPosX = location.getBlockX();
        this.blockPosY = location.getBlockY();
        this.blockPosZ = location.getBlockZ();
        this.world = location.getWorld();
    }

    @Override
    protected void load() {
        getBlockIdMethodCache = Reflection.getMethod(NMSUtils.blockClass, "getCombinedId", int.class, NMSUtils.iBlockDataClass);
        if (getBlockIdMethodCache == null) {
            //1.7.10
            getBlockIdMethodCache = Reflection.getMethod(NMSUtils.blockClass, "getId", int.class, NMSUtils.blockClass);
        }
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

    public int getBlockX() {
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                return readInt(0);
            } else {
                if (blockPosObj == null) {
                    blockPosObj = readObject(0, NMSUtils.blockPosClass);
                }
                try {
                    return (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        } else {
            return blockPosX;
        }
    }

    public int getBlockY() {
        if (packet != null) {
            if (version.equals(ServerVersion.v_1_7_10)) {
                return readInt(1);
            } else {
                if (blockPosObj == null) {
                    blockPosObj = readObject(0, NMSUtils.blockPosClass);
                }
                try {
                    return (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        } else {
            return blockPosY;
        }
    }

    public int getBlockZ() {
        if (packet != null) {
            if (version.equals(ServerVersion.v_1_7_10)) {
                return readInt(2);
            } else {
                if (blockPosObj == null) {
                    blockPosObj = readObject(0, NMSUtils.blockPosClass);
                }
                try {
                    return (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        } else {
            return blockPosZ;
        }
    }

    public int getBlockId() {
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            Object nmsBlock = readObject(0, NMSUtils.blockClass);
            try {
                return (int) getBlockIdMethodCache.invoke(null, nmsBlock);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Object iBlockDataObj = readObject(0, NMSUtils.iBlockDataClass);
            try {
                return (int) getBlockIdMethodCache.invoke(null, iBlockDataObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public Object asNMSPacket() {
        if (version.isOlderThan(ServerVersion.v_1_8)) {
            try {
                Object nmsWorld = NMSUtils.convertBukkitWorldToNMSWorld(world);
                return packetConstructor.newInstance(getBlockX(), getBlockY(), getBlockZ(), nmsWorld);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Object packetInstance = packetConstructor.newInstance();
                WrappedPacket packetWrapper = new WrappedPacket(new NMSPacket(packetInstance));
                Object nmsBlockPos = NMSUtils.generateNMSBlockPos(getBlockX(), getBlockY(), getBlockZ());
                Object nmsWorld = NMSUtils.convertBukkitWorldToNMSWorld(world);
                Object nmsBlockData = getNMSWorldTypeMethodCache.invoke(nmsWorld, nmsBlockPos);
                packetWrapper.write(NMSUtils.blockPosClass, 0, nmsBlockPos);
                packetWrapper.write(NMSUtils.iBlockDataClass, 0, nmsBlockData);
                return packetInstance;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
