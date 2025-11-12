/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.waypoint;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChunkWaypointInfo implements WaypointInfo {

    private final int chunkX;
    private final int chunkZ;

    public ChunkWaypointInfo(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @ApiStatus.Internal
    public static ChunkWaypointInfo read(PacketWrapper<?> wrapper) {
        return new ChunkWaypointInfo(wrapper.readVarInt(), wrapper.readVarInt());
    }

    @ApiStatus.Internal
    public static void write(PacketWrapper<?> wrapper, WaypointInfo info) {
        ChunkWaypointInfo chunkInfo = (ChunkWaypointInfo) info;
        wrapper.writeVarInt(chunkInfo.chunkX);
        wrapper.writeVarInt(chunkInfo.chunkZ);
    }

    @Override
    public Type getType() {
        return Type.CHUNK;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }
}
