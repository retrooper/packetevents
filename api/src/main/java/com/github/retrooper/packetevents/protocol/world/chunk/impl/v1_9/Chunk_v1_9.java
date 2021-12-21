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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.LegacyFlexibleStorage;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

import java.util.ArrayList;
import java.util.List;

public class Chunk_v1_9 implements BaseChunk {
    private final WrappedBlockState AIR = WrappedBlockState.getByGlobalId(0);
    private final List<WrappedBlockState> states;

    private int blockCount;

    private int bitsPerEntry;
    private LegacyFlexibleStorage storage;

    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;

    public Chunk_v1_9(NetStreamInput in, boolean hasSkyLight, boolean hasBlockLight) {
        this.bitsPerEntry = in.readUnsignedByte();

        boolean isFourteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);

        // 1.14+ includes block count in chunk data
        if (isFourteen) {
            blockCount = in.readShort();
        }

        this.states = new ArrayList<>();
        int stateCount = in.readVarInt();
        for (int i = 0; i < stateCount; i++) {
            this.states.add(readBlockState(in));
        }

        this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, in.readLongs(in.readVarInt()));

        this.blocklight = hasBlockLight ? new NibbleArray3d(in, 2048) : null;
        this.skylight = hasSkyLight ? new NibbleArray3d(in, 2048) : null;
    }

    public void write(NetStreamOutput out) {
        out.writeByte(bitsPerEntry);

        if (bitsPerEntry <= 8) {
            out.writeVarInt(states.size());
            for (WrappedBlockState state : states) {
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

    private static WrappedBlockState rawToState(int raw) {
        return WrappedBlockState.getByGlobalId(raw);
    }

    public static WrappedBlockState readBlockState(NetStreamInput in) {
        return WrappedBlockState.getByGlobalId(in.readVarInt());
    }

    public static void writeBlockState(NetStreamOutput out, WrappedBlockState blockState) {
        out.writeVarInt(blockState.getGlobalId());
    }

    public WrappedBlockState get(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return this.bitsPerEntry <= 8 ? (id >= 0 && id < this.states.size() ? this.states.get(id) : AIR) : rawToState(id);
    }

    // This method only works post-flattening
    // This is due to the palette system
    @Override
    public boolean isKnownEmpty() {
        return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14) && blockCount == 0;
    }

    public void set(int x, int y, int z, int combinedID) {
        set(x, y, z, WrappedBlockState.getByGlobalId(combinedID));
    }

    public void set(int x, int y, int z, WrappedBlockState state) {
        int id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getGlobalId();
        if (id == -1) {
            this.states.add(state);
            if (this.states.size() > 1 << this.bitsPerEntry) {
                this.bitsPerEntry++;

                List<WrappedBlockState> oldStates = this.states;
                if (this.bitsPerEntry > 8) {
                    oldStates = new ArrayList<>(this.states);
                    this.states.clear();
                    this.bitsPerEntry = 16;
                }

                LegacyFlexibleStorage oldStorage = this.storage;
                this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, this.storage.getSize());
                for (int index = 0; index < this.storage.getSize(); index++) {
                    this.storage.set(index, this.bitsPerEntry <= 8 ? oldStorage.get(index) : oldStates.get(oldStorage.get(index)).getGlobalId());
                }
            }

            id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getGlobalId();
        }

        this.storage.set(index(x, y, z), id);
    }
}
