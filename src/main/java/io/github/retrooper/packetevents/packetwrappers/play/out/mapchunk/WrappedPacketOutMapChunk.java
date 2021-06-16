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

package io.github.retrooper.packetevents.packetwrappers.play.out.mapchunk;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//TODO finish wrapper
public class WrappedPacketOutMapChunk extends WrappedPacket {
    private static boolean v_1_8_x;
    private Class<?> chunkMapClass;
    private Constructor<?> chunkMapConstructor;
    private Object nmsChunkMap;

    public WrappedPacketOutMapChunk(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_8_x = version.isNewerThan(ServerVersion.v_1_7_10) && version.isOlderThan(ServerVersion.v_1_9);
        if (v_1_8_x) {
            chunkMapClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.MAP_CHUNK, "ChunkMap");
            try {
                chunkMapConstructor = chunkMapClass.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        net.minecraft.server.v1_7_R4.PacketPlayOutMapChunk a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutMapChunk a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutMapChunk a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutMapChunk a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutMapChunk a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutMapChunk a7;
        net.minecraft.network.protocol.game.PacketPlayOutMapChunk a8;
    }


    public int getChunkX() {
        return readInt(0);
    }

    public void setChunkX(int chunkX) {
        writeInt(0, chunkX);
    }

    public int getChunkZ() {
        return readInt(1);
    }

    public void setChunkZ(int chunkZ) {
        writeInt(1, chunkZ);
    }

    //TODO Add 1.17 support
    public int getPrimaryBitMap() {
        if (v_1_8_x) {
            if (nmsChunkMap == null) {
                nmsChunkMap = readObject(0, chunkMapClass);
            }
            WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(nmsChunkMap));
            return nmsChunkMapWrapper.readInt(0);
        } else {
            return readInt(2);
        }
    }

    //TODO Add 1.17 support
    public void setPrimaryBitMap(int primaryBitMap) {
        if (version.isNewerThan(ServerVersion.v_1_7_10) && version.isOlderThan(ServerVersion.v_1_9)) {
            if (nmsChunkMap == null) {
                try {
                    nmsChunkMap = chunkMapConstructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(nmsChunkMap));
            nmsChunkMapWrapper.writeInt(0, primaryBitMap);
            write(chunkMapClass, 0, nmsChunkMap);

        } else {
            writeInt(2, primaryBitMap);
        }
    }

    //TODO Add 1.17 support
    public boolean isGroundUpContinuous() {
        return readBoolean(0);
    }

    //TODO Add 1.17 support
    public void setGroundUpContinuous(boolean groundUpContinuous) {
        writeBoolean(0, groundUpContinuous);
    }

    public byte[] getCompressedData() {
        if (v_1_8_x) {
            if (nmsChunkMap == null) {
                nmsChunkMap = readObject(0, chunkMapClass);
            }
            WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(nmsChunkMap));
            return nmsChunkMapWrapper.readByteArray(0);
        } else {
            return readByteArray(0);
        }
    }

    public void setCompressedData(byte[] data) {
        if (v_1_8_x) {
            if (nmsChunkMap == null) {
                try {
                    nmsChunkMap = chunkMapConstructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            WrappedPacket nmsChunkMapWrapper = new WrappedPacket(new NMSPacket(nmsChunkMap));
            nmsChunkMapWrapper.writeByteArray(0, data);
            write(chunkMapClass, 0, nmsChunkMap);
        } else {
            writeByteArray(0, data);
        }
    }
}
