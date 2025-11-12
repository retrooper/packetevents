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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.debug.DebugSubscription;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector2i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * Mojang name: ClientboundDebugChunkValuePacket
 *
 * @versions 1.21.9+
 */
@NullMarked
public class WrapperPlayServerDebugChunkValue extends PacketWrapper<WrapperPlayServerDebugChunkValue> {

    private Vector2i chunkPos;
    private DebugSubscription.Update<?> update;

    public WrapperPlayServerDebugChunkValue(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDebugChunkValue(Vector2i chunkPos, DebugSubscription.Update<?> update) {
        super(PacketType.Play.Server.DEBUG_CHUNK_VALUE);
        this.chunkPos = chunkPos;
        this.update = update;
    }

    @Override
    public void read() {
        this.chunkPos = Vector2i.read(this);
        this.update = DebugSubscription.Update.read(this);
    }

    @Override
    public void write() {
        Vector2i.write(this, this.chunkPos);
        DebugSubscription.Update.write(this, this.update);
    }

    @Override
    public void copy(WrapperPlayServerDebugChunkValue wrapper) {
        this.chunkPos = wrapper.chunkPos;
        this.update = wrapper.update;
    }

    public Vector2i getChunkPos() {
        return this.chunkPos;
    }

    public void setChunkPos(Vector2i chunkPos) {
        this.chunkPos = chunkPos;
    }

    public DebugSubscription.Update<?> getUpdate() {
        return this.update;
    }

    public void setUpdate(DebugSubscription.Update<?> update) {
        this.update = update;
    }
}
