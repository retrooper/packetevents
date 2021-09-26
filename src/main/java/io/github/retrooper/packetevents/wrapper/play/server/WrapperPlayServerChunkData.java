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

package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.protocol.data.nbt.NBTCompound;
import io.github.retrooper.packetevents.protocol.data.stream.NetStreamInput;
import io.github.retrooper.packetevents.protocol.data.stream.NetStreamOutput;
import io.github.retrooper.packetevents.protocol.data.world.chunk.Chunk;
import io.github.retrooper.packetevents.protocol.data.world.chunk.Column;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.BitSet;

//TODO Finish
public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {
    private Column column;

    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData(Column column) {
        super(PacketType.Play.Server.CHUNK_DATA.getID());
        this.column = column;
    }

    private BitSet readChunkMask() {
        BitSet chunkMask;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17)) {
            int bitMaskLength = readVarInt();
            //Read primary bit mask
            chunkMask = BitSet.valueOf(readLongArray(bitMaskLength));
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            //Read primary bit mask
            chunkMask = BitSet.valueOf(new long[]{readVarInt()});
        } else {
            if (serverVersion == ServerVersion.v_1_7_10) {
                //Read primary bit mask and add bit mask
                chunkMask = BitSet.valueOf(new long[]{readUnsignedShort(), readUnsignedShort()});
            } else {
                //Read primary bit mask
                chunkMask = BitSet.valueOf(new long[]{readUnsignedShort()});
            }
        }
        return chunkMask;
    }

    private void writeChunkMask(BitSet chunkMask) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17)) {
            //Write primary bit mask
            long[] longArray = chunkMask.toLongArray();
            writeVarInt(longArray.length);
            writeLongArray(longArray);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            //Write primary bit mask
            writeVarInt((int) chunkMask.toLongArray()[0]);
        } else {
            if (serverVersion == ServerVersion.v_1_7_10) {
                //Write primary bit mask, add bit mask
                long[] longArray = chunkMask.toLongArray();
                writeShort((int) longArray[0]);
                writeShort((int) longArray[1]);
            } else {
                //Write primary bit mask
                writeShort((int) chunkMask.toLongArray()[0]);
            }
        }
    }

    @Override
    public void readData() {
        //Currently, only 1.14 -> 1.17.1 support (only tested 1.16.5 -> 1.17.1)
        //Chunk X and Z
        int chunkX = readInt();
        int chunkZ = readInt();

        boolean hasBiomeData = true;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2) && serverVersion.isOlderThan(ServerVersion.v_1_17)) {
            hasBiomeData = readBoolean();
        }

        boolean ignoreOldData = false;
        if (serverVersion == ServerVersion.v_1_16 ||
                serverVersion == ServerVersion.v_1_16_1) {
            ignoreOldData = readBoolean();
        }
        //Older than 1.13.2 has ground up continuous

        //1.16.0 and 1.16.1 has "ignore old data"

        BitSet chunkMask = readChunkMask();
        boolean hasHeightMaps = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14);
        NBTCompound heightMaps = null;
        if (hasHeightMaps) {
            heightMaps = readTag();
        }

        int[] biomeData = new int[0];
        if (hasBiomeData) {
            boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17);
            int biomesLength;
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16_2)) {
                biomesLength = readVarInt();
            } else {
                biomesLength = 1024;//Always 1024
            }
            biomeData = new int[biomesLength];
            for (int index = 0; index < biomeData.length; index++) {
                if (v1_17) {
                    biomeData[index] = readVarInt();
                } else {
                    biomeData[index] = readInt();
                }
            }
        }
        //TODO Decompress on 1.7.10
        //https://github.com/retrooper/packetevents/blob/794ad6b042c1c89a931d322f4f83317b573e891a/src/main/java/io/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChunkData.java
        byte[] data = readByteArray(readVarInt());
        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
        Chunk[] chunks = new Chunk[chunkMask.size()];
        for (int index = 0; index < chunks.length; index++) {
            if (chunkMask.get(index)) {
                chunks[index] = Chunk.read(dataIn);
            }
        }

        NBTCompound[] tileEntities = new NBTCompound[readVarInt()];
        for (int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = readTag();
        }

        //TODO Support 1.9 - 1.11 with cursed biomes order

        if (hasBiomeData) {
            column = new Column(chunkX, chunkZ, chunks, tileEntities, heightMaps, biomeData);
        } else {
            if (hasHeightMaps) {
                column = new Column(chunkX, chunkZ, chunks, tileEntities, heightMaps);
            } else {
                column = new Column(chunkX, chunkZ, chunks, tileEntities);
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerChunkData wrapper) {
        column = wrapper.column;
    }

    @Override
    public void writeData() {
        //TODO Update
        ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        NetStreamOutput dataOut = new NetStreamOutput(dataBytes);

        BitSet chunkMask = new BitSet();
        Chunk[] chunks = column.getChunks();
        for (int index = 0; index < chunks.length; index++) {
            Chunk chunk = chunks[index];
            if (chunk != null && !chunk.isEmpty()) {
                chunkMask.set(index);
                Chunk.write(dataOut, chunk);
            }
        }

        writeInt(column.getX());
        writeInt(column.getZ());

        boolean fullChunk = column.hasBiomeData();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2) && serverVersion.isOlderThan(ServerVersion.v_1_17)) {
            writeBoolean(fullChunk);
        }

        writeChunkMask(chunkMask);

        writeTag(column.getHeightMaps());
        if (fullChunk) {
            writeVarInt(column.getBiomeData().length);
            for (int biomeData : column.getBiomeData()) {
                writeVarInt(biomeData);
            }
        }
        writeVarInt(dataBytes.size());
        writeByteArray(dataBytes.toByteArray());
        writeVarInt(column.getTileEntities().length);
        for (NBTCompound tag : column.getTileEntities()) {
            writeTag(tag);
        }
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
