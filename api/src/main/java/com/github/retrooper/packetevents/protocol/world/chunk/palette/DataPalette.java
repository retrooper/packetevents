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

package com.github.retrooper.packetevents.protocol.world.chunk.palette;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.BaseStorage;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.LegacyFlexibleStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataPalette {

    // this is the amount of bits required to store the biggest state id number
    public static final int GLOBAL_PALETTE_BITS_PER_ENTRY = 15;

    public @NotNull Palette palette;
    public BaseStorage storage;
    public final PaletteType paletteType;

    public static DataPalette createForChunk() {
        return createEmpty(PaletteType.CHUNK);
    }

    public static DataPalette createForBiome() {
        return createEmpty(PaletteType.BIOME);
    }

    public static DataPalette createEmpty(PaletteType paletteType) {
        return new DataPalette(new ListPalette(paletteType.getMinBitsPerEntry()),
                new BitStorage(paletteType.getMinBitsPerEntry(), paletteType.getStorageSize()), paletteType);
    }

    public DataPalette(@NotNull Palette palette, @Nullable BaseStorage storage, PaletteType paletteType) {
        this.palette = palette;
        this.storage = storage;
        this.paletteType = paletteType;
    }

    public static DataPalette read(NetStreamInput in, PaletteType paletteType) {
        return read(in, paletteType, true);
    }

    public static DataPalette read(NetStreamInput in, PaletteType paletteType, boolean allowSingletonPalette) {
        int bitsPerEntry = in.readByte();
        Palette palette = readPalette(paletteType, bitsPerEntry, in, allowSingletonPalette);
        BitStorage storage;
        if (!(palette instanceof SingletonPalette)) {
            int length = in.readVarInt();
            storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize(), in.readLongs(length));
        } else {
            in.readVarInt();
            storage = null;
        }

        return new DataPalette(palette, storage, paletteType);
    }

    public static void write(NetStreamOutput out, DataPalette palette) {
        if (palette.palette instanceof SingletonPalette) {
            out.writeByte(0); // Bits per entry
            out.writeVarInt(palette.palette.idToState(0));
            out.writeVarInt(0); // Data length
            return;
        }

        out.writeByte(palette.storage.getBitsPerEntry());

        if (!(palette.palette instanceof GlobalPalette)) {
            int paletteLength = palette.palette.size();
            out.writeVarInt(paletteLength);
            for (int i = 0; i < paletteLength; i++) {
                out.writeVarInt(palette.palette.idToState(i));
            }
        }

        long[] data = palette.storage.getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);
    }

    public static DataPalette readLegacy(NetStreamInput in) {
        int bitsPerEntry = in.readByte() & 0xff;
        Palette palette = readPalette(PaletteType.CHUNK, bitsPerEntry, in, false);
        BaseStorage storage;
        if (!(palette instanceof SingletonPalette)) {
            int length = in.readVarInt();
            storage = new LegacyFlexibleStorage(bitsPerEntry, in.readLongs(length));
        } else {
            in.readVarInt();
            storage = null;
        }
        return new DataPalette(palette, storage, PaletteType.CHUNK);
    }

    public int get(int x, int y, int z) {
        if (storage != null) {
            int id = this.storage.get(index(this.paletteType, x, y, z));
            return this.palette.idToState(id);
        } else {
            return this.palette.idToState(0);
        }
    }

    /**
     * @return the old value present in the storage.
     */
    public int set(int x, int y, int z, int state) {
        int id = this.palette.stateToId(state);
        if (id == -1) {
            resize();
            id = this.palette.stateToId(state);
        }

        if (this.storage != null) {
            int index = index(this.paletteType, x, y, z);
            int curr = this.storage.get(index);

            this.storage.set(index, id);
            return curr;
        } else {
            // Singleton palette and the block has not changed because the palette hasn't resized
            return state;
        }
    }

    private static Palette readPalette(
            PaletteType paletteType,
            int bitsPerEntry,
            NetStreamInput in,
            boolean allowSingletonPalette
    ) {
        if (bitsPerEntry > paletteType.getMaxBitsPerEntry()) {
            return new GlobalPalette();
        }
        if (bitsPerEntry == 0 && allowSingletonPalette) {
            return new SingletonPalette(in);
        }
        if (bitsPerEntry <= paletteType.getMinBitsPerEntry()) {
            return new ListPalette(bitsPerEntry, in);
        } else {
            return new MapPalette(bitsPerEntry, in);
        }
    }

    private int sanitizeBitsPerEntry(int bitsPerEntry) {
        if (bitsPerEntry <= this.paletteType.getMaxBitsPerEntry()) {
            return Math.max(this.paletteType.getMinBitsPerEntry(), bitsPerEntry);
        } else {
            return GLOBAL_PALETTE_BITS_PER_ENTRY;
        }
    }

    private void resize() {
        Palette oldPalette = this.palette;
        BaseStorage oldData = this.storage;

        int bitsPerEntry = sanitizeBitsPerEntry(oldPalette instanceof SingletonPalette ? 1 : oldData.getBitsPerEntry() + 1);
        this.palette = createPalette(bitsPerEntry, paletteType);
        this.storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize());

        if (oldPalette instanceof SingletonPalette) {
            this.palette.stateToId(oldPalette.idToState(0));
        } else {
            for (int i = 0; i < paletteType.getStorageSize(); i++) {
                this.storage.set(i, this.palette.stateToId(oldPalette.idToState(oldData.get(i))));
            }
        }
    }

    private static Palette createPalette(int bitsPerEntry, PaletteType paletteType) {
        if (bitsPerEntry <= paletteType.getMinBitsPerEntry()) {
            return new ListPalette(bitsPerEntry);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntry()) {
            return new MapPalette(bitsPerEntry);
        } else {
            return new GlobalPalette();
        }
    }

    private static int index(PaletteType paletteType, int x, int y, int z) {
        return (y << paletteType.getBitShift() | z) << paletteType.getBitShift() | x;
    }
}
