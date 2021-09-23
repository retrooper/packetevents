/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.utils.chunk.palette;

import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;

import java.io.IOException;

/**
 * A palette backed by a map.
 */
//TODO Equals & hashcode
public class MapPalette implements Palette {
    private final int maxId;

    private final int[] idToState;
    private final IntObjectMap<Integer> stateToId = new IntObjectHashMap<>();
    private int nextId = 0;

    public MapPalette(int bitsPerEntry) {
        this.maxId = (1 << bitsPerEntry) - 1;

        this.idToState = new int[this.maxId + 1];
    }

    public MapPalette(int bitsPerEntry, PacketWrapper<?> in) throws IOException {
        this(bitsPerEntry);

        int paletteLength = in.readVarInt();
        for(int i = 0; i < paletteLength; i++) {
            int state = in.readVarInt();
            this.idToState[i] = state;
            this.stateToId.putIfAbsent(state, i);
        }
        this.nextId = paletteLength;
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        Integer id = this.stateToId.get(state);
        if(id == null && this.size() < this.maxId + 1) {
            id = this.nextId++;
            this.idToState[id] = state;
            this.stateToId.put(state, id);
        }

        if(id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public int idToState(int id) {
        if(id >= 0 && id < this.size()) {
            return this.idToState[id];
        } else {
            return 0;
        }
    }
}
