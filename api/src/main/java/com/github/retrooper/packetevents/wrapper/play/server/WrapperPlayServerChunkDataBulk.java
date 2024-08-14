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
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7.Chunk_v1_7;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_7;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_8;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

// Credit to MCProtocolLib for this wrapper
public class WrapperPlayServerChunkDataBulk extends PacketWrapper<WrapperPlayServerChunkDataBulk> {
    private int[] x;
    private int[] z;
    private BaseChunk[][] chunks;
    private byte[][] biomeData;

    public WrapperPlayServerChunkDataBulk(PacketSendEvent event) {
        super(event);
    }

    //TODO Constructor?

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            read_1_8();
        } else {
            read_1_7();
        }
    }

    private void read_1_8() {
        boolean skylight = readBoolean();
        int columns = readVarInt();
        this.x = new int[columns];
        this.z = new int[columns];
        this.chunks = new BaseChunk[columns][];
        this.biomeData = new byte[columns][];
        NetworkChunkData[] data = new NetworkChunkData[columns];
        for (int column = 0; column < columns; column++) {
            this.x[column] = readInt();
            this.z[column] = readInt();
            int mask = readUnsignedShort();
            int chunks = Integer.bitCount(mask);
            int length = (chunks * ((4096 * 2) + 2048)) + (skylight ? chunks * 2048 : 0);
            byte[] dat = new byte[length];
            data[column] = new NetworkChunkData(mask, true, skylight, dat);
        }
        //TODO Fix ChunkDataBulk for 1.8
        for (int column = 0; column < columns; column++) {
            data[column].setData(readBytes(data[column].getData().length));
            BitSet mask = BitSet.valueOf(new long[]{data[column].getMask()});
            BaseChunk[] chunkData = new ChunkReader_v1_8().read(this.user.getDimensionType(), mask, null, true, skylight, false, 16, data[column].getData(), null);
            this.chunks[column] = chunkData;
            this.biomeData[column] = readBytes(256);
        }
    }

    private void read_1_7() {
        // Read packet base data.
        short columns = readShort();
        int deflatedLength = readInt();
        boolean skylight = readBoolean();
        byte[] deflatedBytes = readBytes(deflatedLength);
        // Inflate chunk data.
        byte[] inflated = new byte[196864 * columns];
        Inflater inflater = new Inflater();
        inflater.setInput(deflatedBytes, 0, deflatedLength);
        try {
            inflater.inflate(inflated);
        } catch (DataFormatException e) {
            new IOException("Bad compressed data format").printStackTrace();
            return;
        } finally {
            inflater.end();
        }

        this.x = new int[columns];
        this.z = new int[columns];
        this.chunks = new BaseChunk[columns][];
        this.biomeData = new byte[columns][];
        // Cycle through and read all columns.
        int pos = 0;
        for (int count = 0; count < columns; count++) {
            // Read column-specific data.
            int x = readInt();
            int z = readInt();
            BitSet chunkMask = BitSet.valueOf(new long[]{readUnsignedShort()});
            BitSet extendedChunkMask = BitSet.valueOf(new long[]{readUnsignedShort()});

            // Determine column data length.
            int chunks = 0;
            int extended = 0;
            for (int ch = 0; ch < 16; ch++) {
                chunks += chunkMask.get(ch) ? 1 : 0;
                extended += extendedChunkMask.get(ch) ? 1 : 0;
            }

            int length = (8192 * chunks + 256) + (2048 * extended);
            if (skylight) {
                length += 2048 * chunks;
            }

            // Copy column data into a new array.
            byte[] dat = new byte[length];
            System.arraycopy(inflated, pos, dat, 0, length);

            // Read data into chunks and biome data.
            // BitSet set, BitSet sevenExtendedMask, boolean fullChunk, boolean hasSkyLight, boolean checkForSky, int chunkSize, byte[] data, NetStreamInput dataIn
            BaseChunk[] chunkData = new ChunkReader_v1_7().read(user.getDimensionType(), chunkMask, extendedChunkMask, true, skylight, false, 16, dat, null);
            byte[] biomeDataBytes = Arrays.copyOfRange(dat, dat.length - 256, dat.length); // let's hope the server knows the right data length

            this.x[count] = x;
            this.z[count] = z;
            this.chunks[count] = chunkData;
            this.biomeData[count] = biomeDataBytes;
            pos += length;
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            write_1_8();
        } else {
            write_1_7();
        }
    }

    @Override
    public void copy(WrapperPlayServerChunkDataBulk wrapper) {
        this.x = wrapper.x;
        this.z = wrapper.z;
        this.chunks = wrapper.chunks;
        this.biomeData = wrapper.biomeData;
    }

    private void write_1_8() {
        boolean skylight = false;
        NetworkChunkData data[] = new NetworkChunkData[this.chunks.length];
        for (int column = 0; column < this.chunks.length; column++) {
            data[column] = ChunkReader_v1_8.chunksToData((Chunk_v1_8[]) this.chunks[column], this.biomeData[column]);
            if (data[column].hasSkyLight()) {
                skylight = true;
            }
        }

        writeBoolean(skylight);
        writeVarInt(this.chunks.length);
        for (int column = 0; column < this.x.length; column++) {
            writeInt(this.x[column]);
            writeInt(this.z[column]);
            writeShort(data[column].getMask());
        }

        for (int column = 0; column < this.x.length; column++) {
            writeBytes(data[column].getData());
        }
    }

    private void write_1_7() {
        // Prepare chunk data arrays.
        int[] chunkMask = new int[this.chunks.length];
        int[] extendedChunkMask = new int[this.chunks.length];
        // Determine values to be written by cycling through columns.
        int pos = 0;
        byte[] bytes = new byte[0];
        boolean skylight = false;

        for (int count = 0; count < this.chunks.length; ++count) {
            BaseChunk[] column = this.chunks[count];
            // Convert chunks into network data.
            NetworkChunkData data = ChunkReader_v1_7.chunksToData((Chunk_v1_7[]) column, this.biomeData[count]);
            if (bytes.length < pos + data.getData().length) {
                byte[] newArray = new byte[pos + data.getData().length];
                System.arraycopy(bytes, 0, newArray, 0, bytes.length);
                bytes = newArray;
            }

            if (data.hasSkyLight()) {
                skylight = true;
            }

            // Copy column data into data array.
            System.arraycopy(data.getData(), 0, bytes, pos, data.getData().length);
            pos += data.getData().length;
            // Set column-specific values.
            chunkMask[count] = data.getMask();
            extendedChunkMask[count] = data.getExtendedChunkMask();
        }

        // Deflate chunk data.
        Deflater deflater = new Deflater(-1);
        byte[] deflatedData = new byte[pos];
        int deflatedLength = pos;
        try {
            deflater.setInput(bytes, 0, pos);
            deflater.finish();
            deflatedLength = deflater.deflate(deflatedData);
        } finally {
            deflater.end();
        }

        // Write data to the network.
        writeShort(this.chunks.length);
        writeInt(deflatedLength);
        writeBoolean(skylight);
        for (int i = 0; i < deflatedLength; i++) {
            writeByte(deflatedData[i]);
        }

        for (int count = 0; count < this.chunks.length; ++count) {
            writeInt(this.x[count]);
            writeInt(this.z[count]);
            writeShort((short) (chunkMask[count] & 65535));
            writeShort((short) (extendedChunkMask[count] & 65535));
        }
    }

    public int[] getX() {
        return x;
    }

    public int[] getZ() {
        return z;
    }

    public BaseChunk[][] getChunks() {
        return chunks;
    }

    public byte[][] getBiomeData() {
        return biomeData;
    }
}
