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

package com.github.retrooper.packetevents.protocol.world.chunk;

public class NetworkChunkData {
    private final int mask;
    private int extendedChunkMask; // 1.7 only
    private final boolean fullChunk;
    private final boolean sky;
    private byte[] data;

    public NetworkChunkData(int mask, boolean fullChunk, boolean sky, byte[] data) {
        this.mask = mask;
        this.fullChunk = fullChunk;
        this.sky = sky;
        this.data = data;
    }

    public NetworkChunkData(int chunkMask, int extendedChunkMask, boolean fullChunk, boolean sky, byte[] data) {
        this(chunkMask, fullChunk, sky, data);
        this.extendedChunkMask = extendedChunkMask;
    }

    public int getMask() {
        return this.mask;
    }

    public int getExtendedChunkMask() {
        return this.extendedChunkMask;
    }

    public boolean isFullChunk() {
        return this.fullChunk;
    }

    public boolean hasSkyLight() {
        return this.sky;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
