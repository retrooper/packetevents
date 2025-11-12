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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * Mojang name: ClientboundDebugBlockValuePacket
 *
 * @versions 1.21.9+
 */
@NullMarked
public class WrapperPlayServerDebugBlockValue extends PacketWrapper<WrapperPlayServerDebugBlockValue> {

    private Vector3i blockPos;
    private DebugSubscription.Update<?> update;

    public WrapperPlayServerDebugBlockValue(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDebugBlockValue(Vector3i blockPos, DebugSubscription.Update<?> update) {
        super(PacketType.Play.Server.DEBUG_BLOCK_VALUE);
        this.blockPos = blockPos;
        this.update = update;
    }

    @Override
    public void read() {
        this.blockPos = this.readBlockPosition();
        this.update = DebugSubscription.Update.read(this);
    }

    @Override
    public void write() {
        this.writeBlockPosition(this.blockPos);
        DebugSubscription.Update.write(this, this.update);
    }

    @Override
    public void copy(WrapperPlayServerDebugBlockValue wrapper) {
        this.blockPos = wrapper.blockPos;
        this.update = wrapper.update;
    }

    public Vector3i getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(Vector3i blockPos) {
        this.blockPos = blockPos;
    }

    public DebugSubscription.Update<?> getUpdate() {
        return this.update;
    }

    public void setUpdate(DebugSubscription.Update<?> update) {
        this.update = update;
    }
}
