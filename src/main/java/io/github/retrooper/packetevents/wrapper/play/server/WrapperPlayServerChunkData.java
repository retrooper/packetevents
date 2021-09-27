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
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
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
        super(PacketType.Play.Server.CHUNK_DATA);
        this.column = column;
    }

    private long[] readBitSetLongs() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17)) {
            int bitMaskLength = readVarInt();
            //Read primary bit mask
            return readLongArray(bitMaskLength);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            //Read primary bit mask
            return new long[]{readVarInt()};
        } else {
            if (serverVersion == ServerVersion.v_1_7_10) {
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
        //TODO Add 1.7.10 support by decompressing and compressing data
        int chunkX = readInt();
        int chunkZ = readInt();

        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17);
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2);

        boolean fullChunk = readBoolean();
        boolean hasBiomeData;
        boolean hasReadBiomeData = false;
        if (v1_17) {
            hasBiomeData = true;
        } else if (v1_13_2) {
            hasBiomeData = fullChunk;
        } else if (serverVersion.isOlderThanOrEquals(ServerVersion.v_1_11_2) && serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            hasBiomeData = fullChunk;
        } else {
            //Full chunk boolean exists, but the biome data is not present in the packet
            hasBiomeData = false;
        }


        boolean ignoreOldData = false;
        if (serverVersion == ServerVersion.v_1_16 ||
                serverVersion == ServerVersion.v_1_16_1) {
            ignoreOldData = readBoolean();
        }

        BitSet chunkMask = readChunkMask();
        boolean hasHeightMaps = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14);
        NBTCompound heightMaps = null;
        if (hasHeightMaps) {
            heightMaps = readTag();
        }

        int[] biomeData = new int[0];
        if (hasBiomeData && v1_13_2 && serverVersion.isNewerThanOrEquals(ServerVersion.v_1_15)) {
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
            hasReadBiomeData = true;
        }

        //TODO Decompress on 1.7.10
        //https://github.com/retrooper/packetevents/blob/794ad6b042c1c89a931d322f4f83317b573e891a/src/main/java/io/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChunkData.java
        int dataLength = readVarInt();
        byte[] data = readByteArray(dataLength);
        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
        Chunk[] chunks = new Chunk[chunkMask.size()];
        for (int index = 0; index < chunks.length; index++) {
            if (chunkMask.get(index)) {
                chunks[index] = Chunk.read(dataIn);
            }
        }

        if (hasBiomeData && !hasReadBiomeData) {
            byte[] biomeDataBytes = readByteArray(256);
            biomeData = new int[256];
            for (int i = 0; i < biomeDataBytes.length; i++) {
                biomeData[i] = biomeDataBytes[i];
            }
        }

        int tileEntityCount = serverVersion.isOlderThan(ServerVersion.v_1_9) ? 0 : readVarInt();
        NBTCompound[] tileEntities = new NBTCompound[tileEntityCount];
        for (int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = readTag();
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

        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_17);
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_2);

        writeBoolean(column.isFullChunk());
        boolean hasWrittenBiomeData = false;


        if (serverVersion == ServerVersion.v_1_16 ||
                serverVersion == ServerVersion.v_1_16_1) {
            //Ignore old data = true, use existing lighting
            //TODO See what we can do with this field
            writeBoolean(true);
        }

        boolean hasHeightMaps = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14);
        NBTCompound heightMaps = null;
        if (hasHeightMaps) {
            writeTag(column.getHeightMaps());
        }

        if (column.hasBiomeData() && v1_13_2 && serverVersion.isNewerThanOrEquals(ServerVersion.v_1_15)) {

            int[] biomeData = column.getBiomeData();
            int biomesLength = biomeData.length;
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16_2)) {
                writeVarInt(biomesLength);
                biomesLength = readVarInt();
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
        Chunk[] chunks = column.getChunks();
        for (int index = 0; index < chunks.length; index++) {
            Chunk chunk = chunks[index];
            if (chunk != null && !chunk.isEmpty()) {
                chunkMask.set(index);
                Chunk.write(dataOut, chunk);
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

        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            NBTCompound[] tileEntities = column.getTileEntities();
            writeVarInt(tileEntities.length);
            for (int i = 0; i < tileEntities.length; i++) {
                writeTag(tileEntities[i]);
            }
        }
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
