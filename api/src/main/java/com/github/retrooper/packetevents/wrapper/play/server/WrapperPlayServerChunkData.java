/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.Column;
import com.github.retrooper.packetevents.protocol.world.chunk.TileEntity;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_18;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.BitSet;

public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {
    private static ChunkReader_v1_18 chunkReader_v1_18 = new ChunkReader_v1_18();

    private Column column;

    //TODO Make accessible??
    private boolean ignoreOldData;

    // 1.18 only (lighting) - for writing data
    // TODO: Make accessible?? Include in chunk data?? What do we do with this?
    private boolean trustEdges;
    private BitSet blockLightMask;
    private BitSet skyLightMask;
    private BitSet emptyBlockLightMask;
    private BitSet emptySkyLightMask;
    private int skyLightCount;
    private int blockLightCount;
    private byte[][] skyLightArray;
    private byte[][] blockLightArray;

    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData(Column column) {
        super(PacketType.Play.Server.CHUNK_DATA);
        this.column = column;
    }

    private long[] readBitSetLongs() {
        //Read primary bit mask
        return readLongArray();
    }

    private BitSet readChunkMask() {
        return BitSet.valueOf(readBitSetLongs());
    }

    private void writeChunkMask(BitSet chunkMask) {
        //Write primary bit mask
        long[] longArray = chunkMask.toLongArray();
        writeLongArray(longArray);
    }

    @Override
    public void read() {
        int chunkX = readInt();
        int chunkZ = readInt();
        boolean fullChunk = true;

        ignoreOldData = readBoolean();

        BitSet chunkMask = null;
        NBTCompound heightMaps = readNBT();

        BitSet secondaryChunkMask = null;

        int chunkSize = user.getTotalWorldHeight() >> 4;

        byte[] data = readByteArray();

        boolean hasBlocklight = true;
        boolean checkForSky = true;

        NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
        BaseChunk[] chunks = getChunkReader().read(user.getDimension(), chunkMask, secondaryChunkMask, fullChunk, hasBlocklight, checkForSky, chunkSize, data, dataIn);

        int tileEntityCount = readVarInt();
        TileEntity[] tileEntities = new TileEntity[tileEntityCount];

        for (int i = 0; i < tileEntities.length; i++) {
            tileEntities[i] = new TileEntity(readByte(), readShort(), readVarInt(), readNBT());
        }

        skyLightMask = readChunkMask();
        blockLightMask = readChunkMask();
        emptySkyLightMask = readChunkMask();
        emptyBlockLightMask = readChunkMask();

        skyLightCount = readVarInt();
        this.skyLightArray = new byte[skyLightCount][];
        for (int x = 0; x < skyLightCount; x++) {
            skyLightArray[x] = readByteArray();
        }

        blockLightCount = readVarInt();
        this.blockLightArray = new byte[blockLightCount][];
        for (int x = 0; x < blockLightCount; x++) {
            blockLightArray[x] = readByteArray();
        }

        column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities, heightMaps);
    }

    private byte[] deflate(byte[] toDeflate, BitSet mask, boolean fullChunk) {
        return toDeflate;
    }

    @Override
    public void write() {
        writeInt(column.getX());
        writeInt(column.getZ());

        ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        NetStreamOutput dataOut = new NetStreamOutput(dataBytes);

        BaseChunk[] chunks = column.getChunks();

        for (int index = 0; index < chunks.length; index++) {
            BaseChunk chunk = chunks[index];
            Chunk_v1_18.write(dataOut, (Chunk_v1_18) chunk);
        }

        writeNBT(column.getHeightMaps());

        byte[] data = dataBytes.toByteArray();
        writeByteArray(data);

        if (column.hasBiomeData()) {
            byte[] biomeDataBytes = new byte[256];
            int[] biomeData = column.getBiomeDataInts();
            for (int i = 0; i < biomeDataBytes.length; i++) {
                biomeDataBytes[i] = (byte) biomeData[i];
            }
            writeByteArray(biomeDataBytes);
        }

        writeVarInt(column.getTileEntities().length);
        for (TileEntity tileEntity : column.getTileEntities()) {
            writeByte(tileEntity.getPackedByte());
            writeShort(tileEntity.getYShort());
            writeVarInt(tileEntity.getType());
            writeNBT(tileEntity.getNBT());
        }

        writeChunkMask(skyLightMask);
        writeChunkMask(blockLightMask);
        writeChunkMask(emptySkyLightMask);
        writeChunkMask(emptyBlockLightMask);

        writeVarInt(skyLightCount);
        for (int x = 0; x < skyLightCount; x++) {
            writeByteArray(skyLightArray[x]);
        }

        writeVarInt(blockLightCount);
        for (int x = 0; x < blockLightCount; x++) {
            writeByteArray(blockLightArray[x]);
        }
    }

    @Override
    public void copy(WrapperPlayServerChunkData wrapper) {
        this.column = wrapper.column;
        this.ignoreOldData = wrapper.ignoreOldData;
        this.trustEdges = wrapper.trustEdges;
        this.blockLightMask = wrapper.blockLightMask;
        this.skyLightMask = wrapper.skyLightMask;
        this.emptyBlockLightMask = wrapper.emptyBlockLightMask;
        this.emptySkyLightMask = wrapper.emptySkyLightMask;
        this.skyLightCount = wrapper.skyLightCount;
        this.blockLightCount = wrapper.blockLightCount;
        this.skyLightArray = wrapper.skyLightArray;
        this.blockLightArray = wrapper.blockLightArray;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    private ChunkReader getChunkReader() {
        return chunkReader_v1_18;
    }
}
