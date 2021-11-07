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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_15;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.blockstate.BaseBlockState;
import com.github.retrooper.packetevents.protocol.world.blockstate.FlatBlockState;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.LegacyFlexibleStorage;

import java.util.ArrayList;
import java.util.List;

public class Chunk_v1_15 implements BaseChunk {
    private static final BlockState AIR = new BlockState(0);
    private static final int AIR_ID = 0;

    private int blockCount;
    private int bitsPerEntry;

    private List<BlockState> states;
    private LegacyFlexibleStorage storage;

    public Chunk_v1_15() {
        this.bitsPerEntry = 4;

        this.states = new ArrayList<>();
        this.states.add(AIR);

        this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, 4096);
    }

    public Chunk_v1_15(int blockCount, int bitsPerEntry, List<BlockState> states, LegacyFlexibleStorage storage) {
        this.blockCount = blockCount;
        this.bitsPerEntry = bitsPerEntry;
        this.states = states;
        this.storage = storage;
    }

    public static Chunk_v1_15 read(NetStreamInput in) {
        int blockCount = 0;
        // 1.14 and 1.15 include block count in chunk data
        // In 1.13 we don't send that, so there is no need to keep track of it
        //TODO Confirm if 1.13.2 counts too
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.v_1_14)) {
            blockCount = in.readShort();
        }

        int bitsPerEntry = in.readUnsignedByte();

        List<BlockState> states = new ArrayList<>();
        int stateCount = bitsPerEntry > 8 ? 0 : in.readVarInt();
        for (int i = 0; i < stateCount; i++) {
            states.add(BlockState.read(in));
        }

        LegacyFlexibleStorage storage = new LegacyFlexibleStorage(bitsPerEntry, in.readLongs(in.readVarInt()));
        return new Chunk_v1_15(blockCount, bitsPerEntry, states, storage);
    }

    public static void write(NetStreamOutput out, Chunk_v1_15 chunk) {
        // ViaVersion should handle not writing block count in 1.13, as vanilla doesn't include it
        // It would probably crash the client if we tried writing it
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.v_1_14)) {
            out.writeShort(chunk.getBlockCount());
        }

        out.writeByte(chunk.getBitsPerEntry());

        if (chunk.getBitsPerEntry() <= 8) {
            out.writeVarInt(chunk.getStates().size());
            for (BlockState state : chunk.getStates()) {
                BlockState.write(out, state);
            }
        }

        long[] data = chunk.getStorage().getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public BaseBlockState get(int x, int y, int z) {
        return new FlatBlockState(getInt(x, y, z));
    }

    public int getInt(int x, int y, int z) {
        int id = this.storage.get(index(x, y, z));
        return this.bitsPerEntry <= 8 ? (id >= 0 && id < this.states.size() ? this.states.get(id).getId() : AIR_ID) : id;
    }

    public void set(int x, int y, int z, int state) {
        set(x, y, z, new BlockState(state));
    }

    public void set(int x, int y, int z, BlockState state) {
        int id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getId();
        if (id == -1) {
            this.states.add(state);
            if (this.states.size() > 1 << this.bitsPerEntry) {
                this.bitsPerEntry++;

                List<BlockState> oldStates = this.states;
                if (this.bitsPerEntry > 8) {
                    oldStates = new ArrayList<>(this.states);
                    this.states.clear();
                    this.bitsPerEntry = 13;
                }

                LegacyFlexibleStorage oldStorage = this.storage;
                this.storage = new LegacyFlexibleStorage(this.bitsPerEntry, this.storage.getSize());
                for (int index = 0; index < this.storage.getSize(); index++) {
                    this.storage.set(index, this.bitsPerEntry <= 8 ? oldStorage.get(index) : oldStates.get(oldStorage.get(index)).getId());
                }
            }

            id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getId();
        }

        int ind = index(x, y, z);
        int curr = this.storage.get(ind);
        if (state.getId() != AIR.getId() && curr == AIR.getId()) {
            this.blockCount++;
        } else if (state.getId() == AIR.getId() && curr != AIR.getId()) {
            this.blockCount--;
        }

        this.storage.set(ind, id);
    }

    public boolean isKnownEmpty() {
        return blockCount == 0 && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.v_1_14);
    }

    public int getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public int getBitsPerEntry() {
        return bitsPerEntry;
    }

    public void setBitsPerEntry(int bitsPerEntry) {
        this.bitsPerEntry = bitsPerEntry;
    }

    public List<BlockState> getStates() {
        return states;
    }

    public void setStates(List<BlockState> states) {
        this.states = states;
    }

    public LegacyFlexibleStorage getStorage() {
        return storage;
    }

    public void setStorage(LegacyFlexibleStorage storage) {
        this.storage = storage;
    }
}
