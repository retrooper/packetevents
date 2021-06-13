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

package io.github.retrooper.packetevents.packetwrappers.play.out.blockchange;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
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
    private static boolean v_1_7_10, v_1_17;
    private static Constructor<?> packetConstructor;
    private static Method getNMSBlockMethodCache = null;
    private static Method getNMSWorldTypeMethodCache = null;

    private Vector3i blockPos;
    private World world;
    private Material blockType;

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

    public WrappedPacketOutBlockChange(World world, Vector3i blockPos, Material blockType) {
        this(world, blockPos);
        this.blockType = blockType;
    }

    public WrappedPacketOutBlockChange(Location location, Material blockType) {
        this(location);
        this.blockType = blockType;
    }

    @Override
    protected void load() {
        getNMSBlockMethodCache = Reflection.getMethod(NMSUtils.iBlockDataClass, "getBlock", 0);
        getNMSWorldTypeMethodCache = Reflection.getMethod(NMSUtils.nmsWorldClass, "getType", 0);
        v_1_7_10 = version.isOlderThan(ServerVersion.v_1_8);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        if (v_1_17) {
            try {
                packetConstructor = PacketTypeClasses.Play.Server.BLOCK_CHANGE.getConstructor(NMSUtils.blockPosClass, NMSUtils.iBlockDataClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else if (v_1_7_10) {
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
            if (v_1_7_10) {
                int x = readInt(0);
                int y = readInt(1);
                int z = readInt(2);
                return new Vector3i(x, y, z);

            } else {
                return readBlockPosition(0);
            }
        } else {
            return blockPos;
        }
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (packet != null) {
            if (v_1_7_10) {
                writeInt(0, blockPos.x);
                writeInt(1, blockPos.y);
                writeInt(2, blockPos.z);
            } else {
                writeBlockPosition(0, blockPos);
            }
        } else {
            this.blockPos = blockPos;
        }
    }

    @Deprecated
    public Material getMaterial() {
        return getBlockType();
    }

    @Deprecated
    public void setMaterial(Material material) {
        setBlockType(material);
    }

    public Material getBlockType() {
        if (packet != null) {
            Object nmsBlock = null;
            if (v_1_7_10) {
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
            return blockType;
        }
    }

    public void setBlockType(Material blockType) {
        if (packet != null) {
            Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(blockType);
            if (v_1_7_10) {
                write(NMSUtils.blockClass, 0, nmsBlock);
            } else {
                WrappedPacket nmsBlockWrapper = new WrappedPacket(new NMSPacket(nmsBlock), NMSUtils.blockClass);
                Object iBlockData = nmsBlockWrapper.readObject(0, NMSUtils.iBlockDataClass);
                write(NMSUtils.iBlockDataClass, 0, iBlockData);
            }
        } else {
            this.blockType = blockType;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetPlayOutBlockChangeInstance;
        WrappedPacketOutBlockChange blockChange;
        Vector3i blockPosition = getBlockPosition();
        if (v_1_17) {
            Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPosition);
            Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(getBlockType());
            WrappedPacket nmsBlockWrapper = new WrappedPacket(new NMSPacket(nmsBlock), NMSUtils.blockClass);
            Object nmsIBlockData = nmsBlockWrapper.readObject(0, NMSUtils.iBlockDataClass);
            packetPlayOutBlockChangeInstance = packetConstructor.newInstance(nmsBlockPos, nmsIBlockData);
        } else if (v_1_7_10) {
            Object nmsWorld = NMSUtils.convertBukkitWorldToWorldServer(world);
            packetPlayOutBlockChangeInstance = packetConstructor.newInstance(blockPosition.x, blockPosition.y, blockPosition.z, nmsWorld);
            blockChange = new WrappedPacketOutBlockChange(new NMSPacket(packetPlayOutBlockChangeInstance));
            Material bt = getBlockType();
            if (bt != null) {
                blockChange.setBlockType(bt);
            }

        } else {
            packetPlayOutBlockChangeInstance = packetConstructor.newInstance();
            blockChange = new WrappedPacketOutBlockChange(new NMSPacket(packetPlayOutBlockChangeInstance));
            Material bt = getBlockType();
            if (bt != null) {
                blockChange.setBlockType(bt);
            } else {
                Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPosition);
                Object worldServer = NMSUtils.convertBukkitWorldToWorldServer(world);
                Object nmsBlockData = getNMSWorldTypeMethodCache.invoke(worldServer, nmsBlockPos);
                blockChange.write(NMSUtils.iBlockDataClass, 0, nmsBlockData);
            }
            blockChange.setBlockPosition(blockPosition);

        }
        return packetPlayOutBlockChangeInstance;
    }
}
