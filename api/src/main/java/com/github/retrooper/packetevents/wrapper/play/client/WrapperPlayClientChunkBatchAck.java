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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChunkBatchAck extends PacketWrapper<WrapperPlayClientChunkBatchAck> {

    private float desiredChunksPerTick;

    public WrapperPlayClientChunkBatchAck(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChunkBatchAck(float desiredChunksPerTick) {
        super(PacketType.Play.Client.CHUNK_BATCH_ACK);
        this.desiredChunksPerTick = desiredChunksPerTick;
    }

    @Override
    public void read() {
        this.desiredChunksPerTick = this.readFloat();
    }

    @Override
    public void write() {
        this.writeFloat(this.desiredChunksPerTick);
    }

    @Override
    public void copy(WrapperPlayClientChunkBatchAck wrapper) {
        this.desiredChunksPerTick = wrapper.desiredChunksPerTick;
    }

    public float getDesiredChunksPerTick() {
        return this.desiredChunksPerTick;
    }

    public void setDesiredChunksPerTick(float desiredChunksPerTick) {
        this.desiredChunksPerTick = desiredChunksPerTick;
    }
}
