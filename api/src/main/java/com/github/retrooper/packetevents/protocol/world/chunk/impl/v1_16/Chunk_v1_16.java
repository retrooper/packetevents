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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.blockstate.BaseBlockState;
import com.github.retrooper.packetevents.protocol.world.blockstate.FlatBlockState;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.*;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;

public class Chunk_v1_16 implements BaseChunk {
    private int blockCount;
    private Palette palette;
    private BitStorage storage;

    public Chunk_v1_16() {
        this(0, new ListPalette(4), new BitStorage(4, 4096));
    }

    public Chunk_v1_16(int blockCount, Palette palette, BitStorage storage) {
        this.blockCount = blockCount;
        this.palette = palette;
        this.storage = storage;
    }

    public Chunk_v1_16(NetStreamInput in) {
        blockCount = in.readShort();

        int bitsPerEntry = in.readUnsignedByte();

        palette = readPalette(bitsPerEntry, in);

        long[] longs = in.readLongs(in.readVarInt());

        // Size of global palette serialized is 0, linear/hashmap is size varInt, then read the varInt # of entries, single is one varInt
        if (!(palette instanceof SingletonPalette)) {
            storage = new BitStorage(bitsPerEntry, 4096, longs);
        }
    }

    public static void write(NetStreamOutput out, Chunk_v1_16 chunk) {
        out.writeShort(chunk.blockCount);
        out.writeByte(chunk.storage.getBitsPerEntry());

        if (!(chunk.palette instanceof GlobalPalette) && !(chunk.palette instanceof SingletonPalette)) {
            int paletteLength = chunk.palette.size();
            out.writeVarInt(paletteLength);

            for (int i = 0; i < paletteLength; ++i) {
                out.writeVarInt(chunk.palette.idToState(i));
            }
        }

        long[] data = chunk.storage.getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);
    }

    private static Palette createPalette(int bitsPerEntry) {
        if (bitsPerEntry <= 4) {
            return new ListPalette(bitsPerEntry);
        } else {
            return bitsPerEntry <= 8 ? new MapPalette(bitsPerEntry) : new GlobalPalette();
        }
    }

    private static Palette readPalette(int bitsPerEntry, NetStreamInput in) {
        if (bitsPerEntry == 0) {
            return new SingletonPalette(in);
        } else if (bitsPerEntry <= 4) {
            return new ListPalette(bitsPerEntry, in);
        } else {
            return bitsPerEntry <= 8 ? new MapPalette(bitsPerEntry, in) : new GlobalPalette();
        }
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public BaseBlockState get(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return new FlatBlockState(this.palette.idToState(id));
    }

    public void set(int x, int y, int z, int state) {
        int id = this.palette.stateToId(state);
        if (id == -1) {
            this.resizePalette();
            id = this.palette.stateToId(state);
        }

        int index = index(x, y, z);
        int curr = this.storage.get(index);
        if (state != 0 && curr == 0) {
            ++this.blockCount;
        } else if (state == 0 && curr != 0) {
            --this.blockCount;
        }

        this.storage.set(index, id);
    }

    public boolean isKnownEmpty() {
        return this.blockCount == 0;
    }

    private int sanitizeBitsPerEntry(int bitsPerEntry) {
        return bitsPerEntry <= 8 ? Math.max(4, bitsPerEntry) : 14;
    }

    private void resizePalette() {
        Palette oldPalette = this.palette;
        BitStorage oldData = this.storage;
        int bitsPerEntry = this.sanitizeBitsPerEntry(oldData.getBitsPerEntry() + 1);
        this.palette = createPalette(bitsPerEntry);
        this.storage = new BitStorage(bitsPerEntry, 4096);

        for (int i = 0; i < 4096; ++i) {
            this.storage.set(i, this.palette.stateToId(oldPalette.idToState(oldData.get(i))));
        }
    }

    public int getBlockCount() {
        return this.blockCount;
    }

    public Palette getPalette() {
        return this.palette;
    }

    public BitStorage getStorage() {
        return this.storage;
    }
}