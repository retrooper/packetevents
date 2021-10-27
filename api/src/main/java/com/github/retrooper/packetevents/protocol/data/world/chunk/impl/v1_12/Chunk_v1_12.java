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

package com.github.retrooper.packetevents.protocol.data.world.chunk.impl.v1_12;

import com.github.retrooper.packetevents.protocol.data.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.data.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.data.world.blockstate.MagicBlockState;
import com.github.retrooper.packetevents.protocol.data.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.data.world.chunk.LegacyFlexibleStorage;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chunk_v1_12 implements BaseChunk {
    private static final MagicBlockState AIR = new MagicBlockState(0, 0);
    private final List<MagicBlockState> states;
    private int bitsPerEntry;
    private LegacyFlexibleStorage storage;

    public Chunk_v1_12(NetStreamInput in) {
        this.bitsPerEntry = in.readUnsignedByte();

        this.states = new ArrayList<>();
        int stateCount = in.readVarInt();
        for (int i = 0; i < stateCount; i++) {
            this.states.add(readBlockState(in));
        }

        this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, in.readLongs(in.readVarInt()));
    }

    public Chunk_v1_12(ShortBuffer in) {
        Map<Integer, Integer> reversePalette = new HashMap<>(32);

        states = new ArrayList<>();
        states.add(AIR);
        reversePalette.put(0, 0);

        this.bitsPerEntry = 4;
        this.storage = new LegacyFlexibleStorage(bitsPerEntry, 4096);

        int lastNext = -1;
        int lastID = -1;

        for (int i = 0; i < 4096; i++) {
            int next = in.get();

            if (next != lastNext) {
                lastNext = next;
                next = ((next & 15) << 12) | (next >> 4);
                lastID = this.bitsPerEntry <= 8 ? reversePalette.getOrDefault(next, -1) : next;

                if (lastID == -1) {
                    reversePalette.put(next, reversePalette.size());
                    states.add(new MagicBlockState(next));

                    if (reversePalette.size() > 1 << this.bitsPerEntry) {
                        this.bitsPerEntry++;

                        List<MagicBlockState> oldStates = this.states;
                        if (this.bitsPerEntry > 8) {
                            oldStates = new ArrayList<>(this.states);
                            this.states.clear();
                            reversePalette.clear();
                            this.bitsPerEntry = 16;
                        }

                        LegacyFlexibleStorage oldStorage = this.storage;
                        this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, this.storage.getSize());
                        for (int index = 0; index < this.storage.getSize(); index++) {
                            this.storage.set(index, this.bitsPerEntry <= 8 ? oldStorage.get(index) : oldStates.get(oldStorage.get(index)).getCombinedID());
                        }
                    }

                    lastID = this.bitsPerEntry <= 8 ? reversePalette.getOrDefault(next, -1) : next;
                }
            }

            this.storage.set(i, lastID);
        }
    }

    public Chunk_v1_12() {
        this.bitsPerEntry = 4;

        this.states = new ArrayList<>();
        this.states.add(AIR);

        this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, 4096);
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    private static MagicBlockState rawToState(int raw) {
        return new MagicBlockState(raw & 0xFF, raw >> 12);
    }

    public static MagicBlockState readBlockState(NetStreamInput in) {
        int rawId = in.readVarInt();
        return new MagicBlockState(rawId >> 4, rawId & 0xF);
    }

    public static void writeBlockState(NetStreamOutput out, MagicBlockState blockState) {
        out.writeVarInt((blockState.getID() << 4) | (blockState.getBlockData() & 0xF));
    }

    public MagicBlockState get(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return this.bitsPerEntry <= 8 ? (id >= 0 && id < this.states.size() ? this.states.get(id) : AIR) : rawToState(id);
    }

    // This method only works post-flattening
    // This is due to the palette system
    @Override
    public boolean isKnownEmpty() {
        return false;
    }

    public void set(int x, int y, int z, int combinedID) {
        set(x, y, z, new MagicBlockState(combinedID));
    }

    public void set(int x, int y, int z, MagicBlockState state) {
        int id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getCombinedID();
        if (id == -1) {
            this.states.add(state);
            if (this.states.size() > 1 << this.bitsPerEntry) {
                this.bitsPerEntry++;

                List<MagicBlockState> oldStates = this.states;
                if (this.bitsPerEntry > 8) {
                    oldStates = new ArrayList<>(this.states);
                    this.states.clear();
                    this.bitsPerEntry = 16;
                }

                LegacyFlexibleStorage oldStorage = this.storage;
                this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, this.storage.getSize());
                for (int index = 0; index < this.storage.getSize(); index++) {
                    this.storage.set(index, this.bitsPerEntry <= 8 ? oldStorage.get(index) : oldStates.get(oldStorage.get(index)).getCombinedID());
                }
            }

            id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getCombinedID();
        }

        this.storage.set(index(x, y, z), id);
    }
}
