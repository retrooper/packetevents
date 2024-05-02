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

package com.github.retrooper.packetevents.protocol.world.chunk;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public interface BaseChunk {
    int getBlockId(int x, int y, int z);

    default WrappedBlockState get(ClientVersion version, int x, int y, int z) {
        return get(version, x, y, z, true);
    }

    default WrappedBlockState get(ClientVersion version, int x, int y, int z, boolean clone) {
        return WrappedBlockState.getByGlobalId(version, getBlockId(x, y, z), clone);
    }

    default WrappedBlockState get(int x, int y, int z) {
        return get(x, y, z, true);
    }

    default WrappedBlockState get(int x, int y, int z, boolean clone) {
        return get(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), x, y, z, clone);
    }

    default void set(int x, int y, int z, WrappedBlockState state) {
        set(x, y, z, state.getGlobalId());
    }

    void set(int x, int y, int z, int combinedID);

    // We don't use ClientVersion, but it's here to maintain backwards compatibility.
    default void set(ClientVersion version, int x, int y, int z, int combinedID) {
        set(x, y, z, combinedID);
    }

    boolean isEmpty();

    static BaseChunk create() {
        return new Chunk_v1_18();
    }
}
