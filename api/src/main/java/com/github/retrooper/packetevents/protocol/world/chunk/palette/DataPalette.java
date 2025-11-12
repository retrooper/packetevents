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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class DataPalette {

    public final PaletteType paletteType;
    public Palette palette;
    public BaseStorage storage;

    public DataPalette(Palette palette, BaseStorage storage, PaletteType paletteType) {
        this.palette = palette;
        this.storage = storage;
        this.paletteType = paletteType;
    }

    /**
     * @deprecated use {@link PaletteType#create()} instead
     */
    @Deprecated
    public static DataPalette createForChunk() {
        return PaletteType.CHUNK.create();
    }

    /**
     * @deprecated use {@link PaletteType#create()} instead
     */
    @Deprecated
    public static DataPalette createForBiome() {
        return PaletteType.BIOME.create();
    }

    /**
     * @deprecated use {@link PaletteType#create()} instead
     */
    @Deprecated
    public static DataPalette createEmpty(PaletteType paletteType) {
        return paletteType.create();
    }

    /**
     * @deprecated use {@link PaletteType#read(PacketWrapper)} instead
     */
    @Deprecated
    public static DataPalette read(NetStreamInput in, PaletteType paletteType) {
        return read(in, paletteType, true);
    }

    /**
     * @deprecated use {@link PaletteType#read(PacketWrapper)} instead
     */
    @Deprecated
    public static DataPalette read(NetStreamInput in, PaletteType paletteType, boolean allowSingletonPalette) {
        return read(in, paletteType, allowSingletonPalette, true);
    }

    /**
     * @deprecated use {@link PaletteType#read(PacketWrapper)} instead
     */
    @Deprecated
    public static DataPalette read(
            NetStreamInput in, PaletteType paletteType,
            boolean allowSingletonPalette, boolean lengthPrefix
    ) {
        int bitsPerEntry = in.readByte();
        Palette palette = readPalette(paletteType, bitsPerEntry, in, allowSingletonPalette);
        BitStorage storage;
        if (!(palette instanceof SingletonPalette)) {
            long[] data = lengthPrefix ? in.readLongs(in.readVarInt()) : null;
            storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize(), data);
            if (!lengthPrefix) {
                // TODO what happens if "bitsPerEntry" != "palette.getBits()"?
                in.readLongs(storage.getData());
            }
        } else {
            if (lengthPrefix) {
                in.readLongs(in.readVarInt());
            }
            storage = null;
        }

        return new DataPalette(palette, storage, paletteType);
    }

    /**
     * @deprecated use {@link PaletteType#write(PacketWrapper, DataPalette)} instead
     */
    @Deprecated
    public static void write(NetStreamOutput out, DataPalette palette) {
        write(out, palette, true);
    }

    /**
     * @deprecated use {@link PaletteType#write(PacketWrapper, DataPalette)} instead
     */
    @Deprecated
    public static void write(NetStreamOutput out, DataPalette palette, boolean lengthPrefix) {
        if (palette.palette instanceof SingletonPalette) {
            out.writeByte(0); // Bits per entry
            out.writeVarInt(palette.palette.idToState(0)); // data value
            if (lengthPrefix) {
                out.writeVarInt(0); // Data length
            }
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
        if (lengthPrefix) {
            out.writeVarInt(data.length);
        }
        out.writeLongs(data);
    }

    /**
     * @deprecated use {@link PaletteType#read(PacketWrapper)} instead
     */
    @Deprecated
    public static DataPalette readLegacy(NetStreamInput in) {
        int bitsPerEntry = Math.max(4, in.readByte() & 0xff);
        Palette palette = readPalette(PaletteType.CHUNK, bitsPerEntry, in, false);
        BaseStorage storage = new LegacyFlexibleStorage(bitsPerEntry, in.readLongs(in.readVarInt()));
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
            this.resizeOneUp();
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

    @Deprecated
    private static Palette readPalette(
            PaletteType paletteType,
            int bitsPerEntry,
            NetStreamInput in,
            boolean allowSingletonPalette
    ) {
        if (bitsPerEntry == 0 && allowSingletonPalette) {
            return new SingletonPalette(in);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntryForList()) {
            // vanilla forces a blockstate-list-palette to always be the maximum size
            int bits = paletteType.isForceMaxListPaletteSize() ? paletteType.getMaxBitsPerEntryForList() : bitsPerEntry;
            return new ListPalette(bits, in);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntryForMap()) {
            return new MapPalette(bitsPerEntry, in);
        } else {
            return GlobalPalette.INSTANCE;
        }
    }

    private void resizeOneUp() {
        Palette oldPalette = this.palette;
        BaseStorage oldData = this.storage;

        int prevBitsPerEntry = oldData != null ? oldData.getBitsPerEntry() : 0;
        this.palette = createPalette(prevBitsPerEntry + 1, this.paletteType);
        this.storage = new BitStorage(this.palette.getBits(), this.paletteType.getStorageSize());

        if (oldData != null) {
            // copy over storage
            for (int i = 0, len = this.paletteType.getStorageSize(); i < len; ++i) {
                this.storage.set(i, this.palette.stateToId(oldPalette.idToState(oldData.get(i))));
            }
        } else {
            this.palette.stateToId(oldPalette.idToState(0));
        }
    }

    private static Palette createPalette(int bitsPerEntry, PaletteType paletteType) {
        if (bitsPerEntry <= paletteType.getMaxBitsPerEntryForList()) {
            int bits = paletteType.isForceMaxListPaletteSize() ? paletteType.getMaxBitsPerEntryForList() : bitsPerEntry;
            return new ListPalette(bits);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntryForMap()) {
            return new MapPalette(bitsPerEntry);
        } else {
            return GlobalPalette.INSTANCE;
        }
    }

    private static int index(PaletteType paletteType, int x, int y, int z) {
        return (y << paletteType.getBitShift() | z) << paletteType.getBitShift() | x;
    }
}
