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

package com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_13;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import com.github.retrooper.packetevents.protocol.world.blockstate.BaseBlockState;
import com.github.retrooper.packetevents.protocol.world.blockstate.FlatBlockState;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.LegacyFlexibleStorage;
import com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;

import java.util.ArrayList;
import java.util.List;

public class Chunk_v1_13 implements BaseChunk {
    // Air is ALWAYS 0
    private static final FlatBlockState AIR = new FlatBlockState(0);
    private static final int AIR_ID = 0;

    private int blockCount;
    private int bitsPerEntry;

    private List<FlatBlockState> states;
    private LegacyFlexibleStorage storage;

    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;

    public Chunk_v1_13(NetStreamInput in) {
        blockCount = 0;

        boolean isFourteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);

        // 1.14 and 1.15 include block count in chunk data
        // In 1.13 we don't send that, so there is no need to keep track of it
        if (isFourteen) {
            blockCount = in.readShort();
        }

        bitsPerEntry = in.readUnsignedByte();

        states = new ArrayList<>();
        int stateCount = bitsPerEntry > 8 ? 0 : in.readVarInt();
        for (int i = 0; i < stateCount; i++) {
            states.add(new FlatBlockState(in.readVarInt()));
        }

        storage = new LegacyFlexibleStorage(bitsPerEntry, in.readLongs(in.readVarInt()));

        blocklight = isFourteen ? null : new NibbleArray3d(in, 2048);
        skylight = isFourteen ? null : new NibbleArray3d(in, 2048);
    }

    public void write(NetStreamOutput out) {
        // ViaVersion should handle not writing block count in 1.13, as vanilla doesn't include it
        // It would probably crash the client if we tried writing it
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14)) {
            out.writeShort(blockCount);
        }

        out.writeByte(bitsPerEntry);

        if (bitsPerEntry <= 8) {
            out.writeVarInt(states.size());
            for (FlatBlockState state : states) {
                out.writeVarInt(state.getId());
            }
        }

        long[] data = storage.getData();
        out.writeVarInt(data.length);
        out.writeLongs(data);

        if (blocklight != null) {
            out.writeVarInt(blocklight.getData().length);
            out.writeBytes(blocklight.getData());
        }

        if (skylight != null) {
            out.writeVarInt(skylight.getData().length);
            out.writeBytes(skylight.getData());
        }
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
        set(x, y, z, new FlatBlockState(state));
    }

    public void set(int x, int y, int z, FlatBlockState state) {
        int id = this.bitsPerEntry <= 8 ? this.states.indexOf(state) : state.getId();
        if (id == -1) {
            this.states.add(state);
            if (this.states.size() > 1 << this.bitsPerEntry) {
                this.bitsPerEntry++;

                List<FlatBlockState> oldStates = this.states;
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

    @Override
    public boolean isKnownEmpty() {
        return blockCount == 0 && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);
    }
}
