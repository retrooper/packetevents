/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.utils.chunk.palette;

import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.io.IOException;

/**
 * A palette backed by a List.
 */
//TODO Equals & hashcode
public class ListPalette implements Palette {
    private final int maxId;

    private final int[] data;
    private int nextId = 0;

    public ListPalette(int bitsPerEntry) {
        this.maxId = (1 << bitsPerEntry) - 1;

        this.data = new int[this.maxId + 1];
    }

    public ListPalette(int bitsPerEntry, PacketWrapper<?> in) throws IOException {
        this(bitsPerEntry);

        int paletteLength = in.readVarInt();
        for(int i = 0; i < paletteLength; i++) {
            this.data[i] = in.readVarInt();
        }
        this.nextId = paletteLength;
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        int id = -1;
        for(int i = 0; i < this.nextId; i++) { // Linear search for state
            if(this.data[i] == state) {
                id = i;
                break;
            }
        }
        if(id == -1 && this.size() < this.maxId + 1) {
            id = this.nextId++;
            this.data[id] = state;
        }

        return id;
    }

    @Override
    public int idToState(int id) {
        if(id >= 0 && id < this.size()) {
            return this.data[id];
        } else {
            return 0;
        }
    }
}
