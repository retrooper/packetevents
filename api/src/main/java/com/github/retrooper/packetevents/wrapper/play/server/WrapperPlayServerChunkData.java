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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.Column;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_16;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_7;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_8;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_9;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;

//TODO Finish
public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {
    private static ChunkReader_v1_7 chunkReader_v1_7 = new ChunkReader_v1_7();
    private static ChunkReader_v1_8 chunkReader_v1_8 = new ChunkReader_v1_8();
    private static ChunkReader_v1_9 chunkReader_v1_9 = new ChunkReader_v1_9();
    private static ChunkReader_v1_16 chunkReader_v1_16 = new ChunkReader_v1_16();
    private Column column;
    //TODO Make accessible??
    private boolean ignoreOldData;

    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData(Column column) {
        super(PacketType.Play.Server.CHUNK_DATA);
        this.column = column;
    }

    private long[] readBitSetLongs() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            int bitMaskLength = readVarInt();
            //Read primary bit mask
            return readLongArray(bitMaskLength);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            //Read primary bit mask
            return new long[]{readVarInt()};
        } else {
            if (serverVersion == ServerVersion.V_1_7_10) {
                //Read primary bit mask and add bit mask
                return new long[]{readUnsignedShort(), readUnsignedShort()};
            } else {
                //Read primary bit mask
                return new long[]{readUnsignedShort()};
            }
        }
    }

    private BitSet readChunkMask() {
        return BitSet.valueOf(readBitSetLongs());
    }

    private void writeChunkMask(BitSet chunkMask) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            //Write primary bit mask
            long[] longArray = chunkMask.toLongArray();
            writeVarInt(longArray.length);
            writeLongArray(longArray);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            //Write primary bit mask
            writeVarInt((int) chunkMask.toLongArray()[0]);
        } else {
            if (serverVersion == ServerVersion.V_1_7_10) {
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
        //TODO Add 1.7.10 support by decompressing and compressing data
        int chunkX = readInt();
        int chunkZ = readInt();

        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        boolean v1_16_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2);
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);

        boolean fullChunk = readBoolean();
        boolean hasBiomeData;
        boolean hasReadBiomeData = false;
        if (v1_17) {
            hasBiomeData = true;
        } else if (v1_13_2) {
            hasBiomeData = fullChunk;
        } else if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_11_2) && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            hasBiomeData = fullChunk;
        } else {
            //Full chunk boolean exists, but the biome data is not present in the packet
            hasBiomeData = false;
        }


        if (serverVersion == ServerVersion.V_1_16 ||
                serverVersion == ServerVersion.V_1_16_1) {
            ignoreOldData = readBoolean();
        }

        BitSet chunkMask = readChunkMask();
        boolean hasHeightMaps = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        NBTCompound heightMaps = null;
        if (hasHeightMaps) {
            heightMaps = readNBT();
        }

        int[] biomeData = new int[0];
        if (hasBiomeData && v1_13_2 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            int biomesLength;
            if (v1_16_2) {
                biomesLength = readVarInt();
            } else {
                biomesLength = 1024;//Always 1024
            }
            biomeData = new int[biomesLength];
            for (int index = 0; index < biomeData.length; index++) {
                if (v1_16_2) {
                    biomeData[index] = readVarInt();
                } else {
                    biomeData[index] = readInt();
                }
            }
            hasReadBiomeData = true;
        }

        int dataLength = readVarInt();
        byte[] data = readByteArray(dataLength);
        BaseChunk[] chunks = getChunkReader().read(chunkMask, chunkMask.size(), data);

        if (hasBiomeData && !hasReadBiomeData) {
            byte[] biomeDataBytes = readByteArray(256);
            biomeData = new int[256];
            for (int i = 0; i < biomeDataBytes.length; i++) {
                biomeData[i] = biomeDataBytes[i];
            }
        }

        int tileEntityCount = serverVersion.isOlderThan(ServerVersion.V_1_9) ? 0 : readVarInt();
        NBTCompound[] tileEntities = new NBTCompound[tileEntityCount];
        for (int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = readNBT();
        }


        if (hasBiomeData) {
            if (hasHeightMaps) {
                column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities, heightMaps, biomeData);
            } else {
                column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities, biomeData);
            }
        } else {
            if (hasHeightMaps) {
                column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities, heightMaps);
            } else {
                column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities);
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerChunkData wrapper) {
        column = wrapper.column;
    }

    @Override
    public void writeData() {
        writeInt(column.getX());
        writeInt(column.getZ());

        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);

        writeBoolean(column.isFullChunk());
        boolean hasWrittenBiomeData = false;


        if (serverVersion == ServerVersion.V_1_16 ||
                serverVersion == ServerVersion.V_1_16_1) {
            //Ignore old data = true, use existing lighting
            //TODO See what we can do with this field
            writeBoolean(true);
        }

        boolean hasHeightMaps = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        if (hasHeightMaps) {
            writeNBT(column.getHeightMaps());
        }

        if (column.hasBiomeData() && v1_13_2 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {

            int[] biomeData = column.getBiomeData();
            int biomesLength = biomeData.length;
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
                writeVarInt(biomesLength);
            }
            for (int index = 0; index < column.getBiomeData().length; index++) {
                if (v1_17) {
                    writeVarInt(biomeData[index]);
                } else {
                    writeInt(biomeData[index]);
                }
            }
            hasWrittenBiomeData = true;
        }

        //TODO Decompress data on 1.7.10
        //https://github.com/retrooper/packetevents/blob/794ad6b042c1c89a931d322f4f83317b573e891a/src/main/java/io/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChunkData.java

        ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        NetStreamOutput dataOut = new NetStreamOutput(dataBytes);

        BitSet chunkMask = new BitSet();
        BaseChunk[] chunks = column.getChunks();
        for (int index = 0; index < chunks.length; index++) {
            BaseChunk chunk = chunks[index];
            if (chunk != null && !chunk.isKnownEmpty()) {
                chunkMask.set(index);
               // Chunk_v1_15.write(dataOut, chunk);
            }
        }

        byte[] data = dataBytes.toByteArray();
        writeVarInt(data.length);
        writeByteArray(data);

        if (column.hasBiomeData() && !hasWrittenBiomeData) {
            byte[] biomeDataBytes = new byte[256];
            int[] biomeData = column.getBiomeData();
            for (int i = 0; i < biomeDataBytes.length; i++) {
                biomeDataBytes[i] = (byte) biomeData[i];
            }
            writeByteArray(biomeDataBytes);
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            NBTCompound[] tileEntities = column.getTileEntities();
            writeVarInt(tileEntities.length);
            for (int i = 0; i < tileEntities.length; i++) {
                writeNBT(tileEntities[i]);
            }
        }
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    private ChunkReader getChunkReader() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            return chunkReader_v1_16;
        }
        else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return chunkReader_v1_9;
        }
        else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            return chunkReader_v1_8;
        }
        else {
            return chunkReader_v1_7;
        }
    }
}
