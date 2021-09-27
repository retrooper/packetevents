/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.protocol.data.world.chunk;

import io.github.retrooper.packetevents.protocol.data.stream.NetStreamInput;
import io.github.retrooper.packetevents.protocol.data.stream.NetStreamOutput;
import io.github.retrooper.packetevents.protocol.data.world.chunk.palette.GlobalPalette;
import io.github.retrooper.packetevents.protocol.data.world.chunk.palette.ListPalette;
import io.github.retrooper.packetevents.protocol.data.world.chunk.palette.MapPalette;
import io.github.retrooper.packetevents.protocol.data.world.chunk.palette.Palette;

import java.io.IOException;

public class Chunk {
    private static final int CHUNK_SIZE = 4096;
    private static final int MIN_PALETTE_BITS_PER_ENTRY = 4;
    private static final int MAX_PALETTE_BITS_PER_ENTRY = 8;
    private static final int GLOBAL_PALETTE_BITS_PER_ENTRY = 14;

    private static final int AIR = 0;

    private int blockCount;
    private Palette palette;
    private BitStorage storage;

    public Chunk(int blockCount, Palette palette, BitStorage storage) {
        this.blockCount = blockCount;
        this.palette = palette;
        this.storage = storage;
    }

    public Chunk() {
        this(0, new ListPalette(MIN_PALETTE_BITS_PER_ENTRY), new BitStorage(MIN_PALETTE_BITS_PER_ENTRY, CHUNK_SIZE));
    }


    //TODO Fix on older versions
    public static Chunk read(NetStreamInput in) {
        int blockCount = in.readShort();
        int bitsPerEntry = in.readUnsignedByte();
        Palette palette = null;
        try {
            palette = readPalette(bitsPerEntry, in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitStorage storage = new BitStorage(bitsPerEntry, CHUNK_SIZE, in.readLongs(in.readVarInt()));
        return new Chunk(blockCount, palette, storage);
    }

    public static void write(NetStreamOutput out, Chunk chunk) {
        out.writeShort(chunk.blockCount);
        out.writeByte(chunk.storage.getBitsPerEntry());

        if (!(chunk.palette instanceof GlobalPalette)) {
            int paletteLength = chunk.palette.size();
            out.writeVarInt(paletteLength);
            for (int i = 0; i < paletteLength; i++) {
                out.writeVarInt(chunk.palette.idToState(i));
            }
        }

        long[] data = chunk.storage.getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);
    }

    private static Palette createPalette(int bitsPerEntry) {
        if (bitsPerEntry <= MIN_PALETTE_BITS_PER_ENTRY) {
            return new ListPalette(bitsPerEntry);
        } else if (bitsPerEntry <= MAX_PALETTE_BITS_PER_ENTRY) {
            return new MapPalette(bitsPerEntry);
        } else {
            return new GlobalPalette();
        }
    }

    private static Palette readPalette(int bitsPerEntry, NetStreamInput in) throws IOException {
        if (bitsPerEntry <= MIN_PALETTE_BITS_PER_ENTRY) {
            return new ListPalette(bitsPerEntry, in);
        } else if (bitsPerEntry <= MAX_PALETTE_BITS_PER_ENTRY) {
            return new MapPalette(bitsPerEntry, in);
        } else {
            return new GlobalPalette();
        }
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public int get(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return this.palette.idToState(id);
    }

    public void set(int x, int y, int z, int state) {
        int id = this.palette.stateToId(state);
        if (id == -1) {
            this.resizePalette();
            id = this.palette.stateToId(state);
        }

        int index = index(x, y, z);
        int curr = this.storage.get(index);
        if (state != AIR && curr == AIR) {
            this.blockCount++;
        } else if (state == AIR && curr != AIR) {
            this.blockCount--;
        }

        this.storage.set(index, id);
    }

    public boolean isEmpty() {
        return this.blockCount == 0;
    }

    private int sanitizeBitsPerEntry(int bitsPerEntry) {
        if (bitsPerEntry <= MAX_PALETTE_BITS_PER_ENTRY) {
            return Math.max(MIN_PALETTE_BITS_PER_ENTRY, bitsPerEntry);
        } else {
            return GLOBAL_PALETTE_BITS_PER_ENTRY;
        }
    }

    private void resizePalette() {
        Palette oldPalette = this.palette;
        BitStorage oldData = this.storage;

        int bitsPerEntry = sanitizeBitsPerEntry(oldData.getBitsPerEntry() + 1);
        this.palette = createPalette(bitsPerEntry);
        this.storage = new BitStorage(bitsPerEntry, CHUNK_SIZE);

        for (int i = 0; i < CHUNK_SIZE; i++) {
            this.storage.set(i, this.palette.stateToId(oldPalette.idToState(oldData.get(i))));
        }
    }
}
