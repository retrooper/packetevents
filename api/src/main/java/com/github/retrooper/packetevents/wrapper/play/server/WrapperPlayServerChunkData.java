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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.ChunkBitMask;
import com.github.retrooper.packetevents.protocol.world.chunk.Column;
import com.github.retrooper.packetevents.protocol.world.chunk.HeightmapType;
import com.github.retrooper.packetevents.protocol.world.chunk.LightData;
import com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import com.github.retrooper.packetevents.protocol.world.chunk.TileEntity;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_9;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7.Chunk_v1_7;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_16;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_7;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_8;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_9;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.BitSet;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class WrapperPlayServerChunkData extends PacketWrapper<WrapperPlayServerChunkData> {
    private static ChunkReader_v1_7 chunkReader_v1_7 = new ChunkReader_v1_7();
    private static ChunkReader_v1_8 chunkReader_v1_8 = new ChunkReader_v1_8();
    private static ChunkReader_v1_9 chunkReader_v1_9 = new ChunkReader_v1_9();
    private static ChunkReader_v1_16 chunkReader_v1_16 = new ChunkReader_v1_16();
    private static ChunkReader_v1_18 chunkReader_v1_18 = new ChunkReader_v1_18();

    private Column column;
    // 1.18 only (lighting) - for writing data
    private LightData lightData;
    private boolean ignoreOldData;

    public WrapperPlayServerChunkData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChunkData(Column column) {
        this(column, null, false);
    }

    public WrapperPlayServerChunkData(Column column, LightData lightData) {
        this(column, lightData, false);
    }

    public WrapperPlayServerChunkData(Column column, LightData lightData, boolean ignoreOldData) {
        super(PacketType.Play.Server.CHUNK_DATA);
        this.column = column;
        this.lightData = lightData;
        this.ignoreOldData = ignoreOldData;
    }

    @Override
    public void read() {
        int chunkX = readInt();
        int chunkZ = readInt();

        // All chunks are full chunks in 1.17 and above to avoid issues with arbitrary world height
        boolean checkFullChunk = serverVersion.isOlderThan(ServerVersion.V_1_17);
        // Don't read a boolean if there isn't a boolean to be read
        boolean fullChunk = !checkFullChunk || readBoolean();

        if (serverVersion == ServerVersion.V_1_16 || serverVersion == ServerVersion.V_1_16_1) {
            ignoreOldData = readBoolean();
        }

        // There is no bitset on 1.18 and above, instead the SingletonPalette is used to represent a chunk with all air
        BitSet chunkMask = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18) ? null : ChunkBitMask.readChunkMask(this);
        boolean hasHeightMaps = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        NBTCompound heightmapsNbt = null;
        Map<HeightmapType, long[]> modernHeightmaps = null;
        if (hasHeightMaps) {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                modernHeightmaps = this.readMap(HeightmapType::read, PacketWrapper::readLongArray);
            } else {
                heightmapsNbt = this.readNBT();
            }
        }

        // 1.7 sends a secondary bit mask for the block metadata
        BitSet secondaryChunkMask = null;
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            secondaryChunkMask = ChunkBitMask.readChunkMask(this);
        }

        int chunkSize = 16;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            chunkSize = user.getTotalWorldHeight() >> 4;
        }

        // 1.7 logic is the same
        // 1.8 logic is the same, however, MCProtocolLib checks for remaining bytes... is this needed?
        // 1.9 logic is the same
        // 1.12 logic is the same
        // 1.14 logic for having biome data - is full chunk
        // 1.16 logic is the same
        // 1.17 logic ALWAYS sends biome data because it is always a full chunk
        // 1.18 logic makes it all a palette type system for biome data...
        boolean hasBiomeData = fullChunk && serverVersion.isOlderThan(ServerVersion.V_1_18);

        boolean bytesInsteadOfInts = serverVersion.isOlderThan(ServerVersion.V_1_13);
        int[] biomeDataInts = null;
        byte[] biomeDataBytes = null;

        // 1.7 sends the chunk data as a byte array of size 256 when it is a full chunk
        // This also applies to 1.8 through 1.12
        //
        // 1.13 uses an integer array of size 256 at the end of chunk data
        // This applies from 1.14
        //
        // 1.16.2+ send a var int array instead of an int array
        if (hasBiomeData && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
            biomeDataInts = readVarIntArray();
        } else if (hasBiomeData && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            biomeDataInts = new int[1024];
            for (int i = 0; i < biomeDataInts.length; i++) {
                biomeDataInts[i] = readInt();
            }
        }

        boolean hasBlockLight = (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) || serverVersion.isOlderThan(ServerVersion.V_1_14))
                && !serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8);
        boolean hasSkyLight = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)
                || this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)
                || this.user != null && this.user.getDimensionType().hasSkyLight()
                && this.serverVersion.isOlderThan(ServerVersion.V_1_14);

        Object originalBuffer = this.buffer;
        int dataLength;
        if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            // decompress data and replace contents of this packet wrapper with the chunk data temporarily
            byte[] data = this.inflate(this.readByteArray(), chunkMask, fullChunk);
            this.buffer = UnpooledByteBufAllocationHelper.wrappedBuffer(data);
            dataLength = data.length;
        } else {
            // let the chunk reader decide how to handle reading
            dataLength = this.readVarInt();
        }
        BaseChunk[] chunks;
        try {
            int expectedReaderIndex = ByteBufHelper.readerIndex(this.buffer) + dataLength;
            chunks = this.getChunkReader().read(this.user.getDimensionType(), chunkMask, secondaryChunkMask,
                    fullChunk, hasBlockLight, hasSkyLight, chunkSize, dataLength, this);

            if (hasBiomeData && this.serverVersion.isOlderThan(ServerVersion.V_1_15)) {
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) { // Uses ints
                    biomeDataInts = new int[16 * 16];
                    for (int i = 0; i < biomeDataInts.length; i++) {
                        biomeDataInts[i] = this.readInt();
                    }
                } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) { // Uses bytes
                    biomeDataBytes = new byte[16 * 16];
                    for (int i = 0; i < biomeDataBytes.length; i++) {
                        biomeDataBytes[i] = this.readByte();
                    }
                } else if (dataLength == 0) {
                    // if cache-chunk-maps is enabled in paper, paper doesn't send any biome data on chunk unload
                    biomeDataBytes = new byte[0];
                } else {
                    biomeDataBytes = this.readBytes(16 * 16);
                }
            }

            // verify the full chunk has been read
            int readerIndex = ByteBufHelper.readerIndex(this.buffer);
            if (expectedReaderIndex != readerIndex) {
                throw new RuntimeException("Error while decoding chunk at " + chunkX + " " + chunkZ
                        + "; expected reader index " + expectedReaderIndex + ", got " + readerIndex);
            }
        } finally {
            // change buffer back if it has been switched
            if (this.buffer != originalBuffer) {
                ByteBufHelper.release(this.buffer);
                this.buffer = originalBuffer;
            }
        }

        // Tile entities are not sent with this packet on 1.8 and below
        // on 1.9 and above for all versions, tile entities are sent with the chunk data
        // (And can be sent with their own packet too!)
        int tileEntityCount = serverVersion.isOlderThan(ServerVersion.V_1_9) ? 0 : readVarInt();
        TileEntity[] tileEntities = new TileEntity[tileEntityCount];

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            for (int i = 0; i < tileEntities.length; i++) {
                tileEntities[i] = new TileEntity(readByte(), readShort(), readVarInt(), readNBT());
            }
        } else {
            for (int i = 0; i < tileEntities.length; i++) {
                tileEntities[i] = new TileEntity(readNBT());
            }
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.lightData = LightData.read(this);
        }

        if (hasBiomeData) {
            if (hasHeightMaps) {
                if (bytesInsteadOfInts) {
                    column = new Column(chunkX, chunkZ, true, chunks, tileEntities, heightmapsNbt, biomeDataBytes);
                } else {
                    column = new Column(chunkX, chunkZ, true, chunks, tileEntities, heightmapsNbt, biomeDataInts);
                }
            } else {
                if (bytesInsteadOfInts) {
                    column = new Column(chunkX, chunkZ, true, chunks, tileEntities, biomeDataBytes);
                } else {
                    column = new Column(chunkX, chunkZ, true, chunks, tileEntities, biomeDataInts);
                }
            }
        } else {
            if (hasHeightMaps) {
                if (modernHeightmaps != null) {
                    this.column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities, modernHeightmaps);
                } else {
                    this.column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities, heightmapsNbt);
                }
            } else {
                column = new Column(chunkX, chunkZ, fullChunk, chunks, tileEntities);
            }
        }
    }

    // this step is only needed for 1.7.x
    private byte[] inflate(byte[] input, BitSet mask, boolean fullChunk) {
        // Determine inflated data length.
        int chunkCount = 0;

        for (int count = 0; count < 16; count++) {
            chunkCount += mask.get(count) ? 1 : 0;
        }

        int len = 12288 * chunkCount;
        if (fullChunk) {
            len += 256;
        }

        byte[] data = new byte[len];
        // Inflate chunk data.
        Inflater inflater = new Inflater();
        inflater.setInput(input, 0, input.length);

        try {
            inflater.inflate(data);
        } catch (DataFormatException e) {
            e.printStackTrace();
        } finally {
            inflater.end();
        }

        return data;
    }

    @Override
    public void write() {
        writeInt(column.getX());
        writeInt(column.getZ());

        boolean v1_18 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18);
        boolean v1_17 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17);
        boolean v1_9 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9);
        boolean v1_8 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8);

        if (!v1_17) {
            writeBoolean(column.isFullChunk());
        }

        boolean hasWrittenBiomeData = false;

        if (serverVersion == ServerVersion.V_1_16 ||
                serverVersion == ServerVersion.V_1_16_1) {
            //Ignore old data = true, use existing lighting
            //TODO See what we can do with this field
            writeBoolean(ignoreOldData);
        }

        BitSet chunkMask = new BitSet();
        BaseChunk[] chunks = column.getChunks();

        Object dataBuffer; // chunk data holder
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            // allocate new buffer which holds all chunk data
            Object originalBuffer = this.buffer;
            dataBuffer = ByteBufHelper.allocateNewBuffer(this.buffer);
            // temporarily replace backing buffer of wrapper
            this.buffer = dataBuffer;

            for (int index = 0; index < chunks.length; index++) {
                BaseChunk chunk = chunks[index];
                if (v1_18) {
                    Chunk_v1_18.write(this, (Chunk_v1_18) chunk);
                } else if (v1_9 && chunk != null) {
                    chunkMask.set(index);
                    Chunk_v1_9.write(this, (Chunk_v1_9) chunk);
                }
            }
            // switch backing buffer back
            this.buffer = originalBuffer;

            // write the same amount of zero bytes mojang also writes
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                int zeroBytes = ChunkReader_v1_18.getMojangZeroByteSuffixLength(chunks);
                int newWriterIndex = ByteBufHelper.writerIndex(dataBuffer) + zeroBytes;
                // allocate enough space for the zeros
                if (newWriterIndex > ByteBufHelper.capacity(dataBuffer)) {
                    ByteBufHelper.capacity(dataBuffer, newWriterIndex);
                }
                // create zeros by just skipping the writer index
                ByteBufHelper.writerIndex(dataBuffer, newWriterIndex);
            }
        } else if (v1_8) {
            NetworkChunkData data = ChunkReader_v1_8.chunksToData((Chunk_v1_8[]) chunks, column.getBiomeDataBytes());
            writeShort(data.getMask());
            writeByteArray(data.getData());
            return;
        } else {
            NetworkChunkData data = ChunkReader_v1_7.chunksToData((Chunk_v1_7[]) chunks, column.getBiomeDataBytes());
            Deflater deflater = new Deflater(-1);

            byte[] deflated = new byte[data.getData().length];
            int len;
            try {
                deflater.setInput(data.getData(), 0, data.getData().length);
                deflater.finish();
                len = deflater.deflate(deflated);
            } finally {
                deflater.end();
            }
            writeShort(data.getMask());
            writeShort(data.getExtendedChunkMask());
            writeInt(len);
            ByteBufHelper.writeBytes(this.buffer, deflated, 0, len);
            return;
        }

        if (column.isFullChunk() && serverVersion.isOlderThan(ServerVersion.V_1_15)) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                for (int i : column.getBiomeDataInts()) {
                    ByteBufHelper.writeInt(dataBuffer, i);
                }
            } else {
                for (byte i : column.getBiomeDataBytes()) {
                    ByteBufHelper.writeByte(dataBuffer, i);
                }
            }
            hasWrittenBiomeData = true;
        }

        if (!v1_18) {
            ChunkBitMask.writeChunkMask(this, chunkMask);
        }

        boolean hasHeightMaps = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        if (hasHeightMaps) {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                this.writeMap(this.column.getHeightmaps(), HeightmapType::write, PacketWrapper::writeLongArray);
            } else {
                this.writeNBT(this.column.getHeightMaps());
            }
        }

        if (column.hasBiomeData() && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15) && !v1_18) {
            boolean bytesInsteadOfInts = serverVersion.isOlderThan(ServerVersion.V_1_13);
            int[] biomeDataInts = column.getBiomeDataInts();
            byte[] biomeDataByes = column.getBiomeDataBytes();

            if (bytesInsteadOfInts) {
                for (byte biomeDataBye : biomeDataByes) {
                    writeByte(biomeDataBye);
                }
            } else {
                if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
                    writeVarIntArray(biomeDataInts);
                } else {
                    for (int biomeDataInt : biomeDataInts) {
                        writeInt(biomeDataInt);
                    }
                }
            }

            hasWrittenBiomeData = true;
        }

        // copy data buffer to this buffer
        this.writeVarInt(ByteBufHelper.readableBytes(dataBuffer));
        ByteBufHelper.writeBytes(this.buffer, dataBuffer);
        ByteBufHelper.release(dataBuffer);

        if (column.hasBiomeData() && !hasWrittenBiomeData) {
            byte[] biomeDataBytes = new byte[256];
            int[] biomeData = column.getBiomeDataInts();
            for (int i = 0; i < biomeDataBytes.length; i++) {
                biomeDataBytes[i] = (byte) biomeData[i];
            }
            writeByteArray(biomeDataBytes);
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            writeVarInt(column.getTileEntities().length);
            for (TileEntity tileEntity : column.getTileEntities()) {
                writeByte(tileEntity.getPackedByte());
                writeShort(tileEntity.getYShort());
                writeVarInt(tileEntity.getType());
                writeNBT(tileEntity.getNBT());
            }
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            TileEntity[] tileEntities = column.getTileEntities();
            writeVarInt(tileEntities.length);

            for (TileEntity tileEntity : tileEntities) {
                writeNBT(tileEntity.getNBT());
            }
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            LightData.write(this, lightData);
        }
    }

    @Override
    public void copy(WrapperPlayServerChunkData wrapper) {
        this.column = wrapper.column;
        this.lightData = wrapper.lightData != null
                ? wrapper.lightData.clone() : null;
        this.ignoreOldData = wrapper.ignoreOldData;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public LightData getLightData() {
        return lightData;
    }

    public void setLightData(LightData lightData) {
        this.lightData = lightData;
    }

    public boolean isIgnoreOldData() {
        return ignoreOldData;
    }

    public void setIgnoreOldData(boolean ignoreOldData) {
        this.ignoreOldData = ignoreOldData;
    }

    private ChunkReader getChunkReader() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            return chunkReader_v1_18;
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            return chunkReader_v1_16;
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return chunkReader_v1_9;
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            return chunkReader_v1_8;
        } else {
            return chunkReader_v1_7;
        }
    }
}
