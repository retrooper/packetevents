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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUnloadChunk extends PacketWrapper<WrapperPlayServerUnloadChunk> {
    private int chunkX;
    private int chunkZ;

    public WrapperPlayServerUnloadChunk(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUnloadChunk(int chunkX, int chunkZ) {
        super(PacketType.Play.Server.UNLOAD_CHUNK);
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(com.github.retrooper.packetevents.manager.server.ServerVersion.V_1_20_2)) {
            long chunkKey = readLong();
            this.chunkX = getChunkX(chunkKey);
            this.chunkZ = getChunkZ(chunkKey);
        } else {
            this.chunkX = readInt();
            this.chunkZ = readInt();
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(com.github.retrooper.packetevents.manager.server.ServerVersion.V_1_20_2)) {
            writeLong(getChunkKey(this.chunkX, this.chunkZ));
        } else {
            writeInt(this.chunkX);
            writeInt(this.chunkZ);
        }
    }

    @Override
    public void copy(WrapperPlayServerUnloadChunk wrapper) {
        this.chunkX = wrapper.chunkX;
        this.chunkZ = wrapper.chunkZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }
}
