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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * Mojang name: ClientboundDebugEntityValuePacket
 *
 * @versions 1.21.9+
 */
@NullMarked
public class WrapperPlayServerDebugEntityValue extends PacketWrapper<WrapperPlayServerDebugEntityValue> {

    private int entityId;
    private DebugSubscription.Update<?> update;

    public WrapperPlayServerDebugEntityValue(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDebugEntityValue(int entityId, DebugSubscription.Update<?> update) {
        super(PacketType.Play.Server.DEBUG_ENTITY_VALUE);
        this.entityId = entityId;
        this.update = update;
    }

    @Override
    public void read() {
        this.entityId = this.readVarInt();
        this.update = DebugSubscription.Update.read(this);
    }

    @Override
    public void write() {
        this.writeVarInt(this.entityId);
        DebugSubscription.Update.write(this, this.update);
    }

    @Override
    public void copy(WrapperPlayServerDebugEntityValue wrapper) {
        this.entityId = wrapper.entityId;
        this.update = wrapper.update;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public DebugSubscription.Update<?> getUpdate() {
        return this.update;
    }

    public void setUpdate(DebugSubscription.Update<?> update) {
        this.update = update;
    }
}
