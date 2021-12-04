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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_9;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.blockstate.MagicBlockState;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.LegacyFlexibleStorage;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;

import java.util.ArrayList;
import java.util.List;

public class Chunk_v1_9 implements BaseChunk {
    private static final MagicBlockState AIR = new MagicBlockState(0, 0);
    private final List<MagicBlockState> states;
    private int bitsPerEntry;
    private LegacyFlexibleStorage storage;

    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;

    public Chunk_v1_9(NetStreamInput in, boolean hasSkyLight) {
        this.bitsPerEntry = in.readUnsignedByte();

        this.states = new ArrayList<>();
        int stateCount = in.readVarInt();
        for (int i = 0; i < stateCount; i++) {
            this.states.add(readBlockState(in));
        }

        this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, in.readLongs(in.readVarInt()));

        this.blocklight = new NibbleArray3d(in, 2048);
        this.skylight = hasSkyLight ? new NibbleArray3d(in, 2048) : null;
    }

    public Chunk_v1_9(int bitsPerEntry, List<MagicBlockState> states, LegacyFlexibleStorage storage) {
        this.bitsPerEntry = bitsPerEntry;
        this.states = states;
        this.storage = storage;
    }

    public static Chunk_v1_9 read(NetStreamInput in) {
        int bitsPerEntry = in.readUnsignedByte();

        List<MagicBlockState> states = new ArrayList<>();
        int stateCount = bitsPerEntry > 8 ? 0 : in.readVarInt();
        for (int i = 0; i < stateCount; i++) {
            states.add(readBlockState(in));
        }

        LegacyFlexibleStorage storage = new LegacyFlexibleStorage(bitsPerEntry, in.readLongs(in.readVarInt()));
        return new Chunk_v1_9(bitsPerEntry, states, storage);
    }

    public void write(NetStreamOutput out) {
        out.writeByte(bitsPerEntry);

        if (bitsPerEntry <= 8) {
            out.writeVarInt(states.size());
            for (MagicBlockState state : states) {
                writeBlockState(out, state);
            }
        }

        long[] data = storage.getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);

        out.writeVarInt(blocklight.getData().length);
        out.writeBytes(blocklight.getData());

        if (skylight != null) {
            out.writeVarInt(skylight.getData().length);
            out.writeBytes(skylight.getData());
        }
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
        out.writeVarInt((blockState.getId() << 4) | (blockState.getBlockData() & 0xF));
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
        int id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getCombinedId();
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
                    this.storage.set(index, this.bitsPerEntry <= 8 ? oldStorage.get(index) : oldStates.get(oldStorage.get(index)).getCombinedId());
                }
            }

            id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getCombinedId();
        }

        this.storage.set(index(x, y, z), id);
    }
}
