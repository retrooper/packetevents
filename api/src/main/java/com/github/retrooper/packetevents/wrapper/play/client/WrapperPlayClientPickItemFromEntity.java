/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

/**
 * Added with 1.21.4
 */
public class WrapperPlayClientPickItemFromEntity extends PacketWrapper<WrapperPlayClientPickItemFromEntity> {

    private int entityId;
    private boolean includeData;

    public WrapperPlayClientPickItemFromEntity(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPickItemFromEntity(int entityId, boolean includeData) {
        super(PacketType.Play.Client.PICK_ITEM_FROM_ENTITY);
        this.entityId = entityId;
        this.includeData = includeData;
    }

    @Override
    public void read() {
        this.entityId = this.readVarInt();
        this.includeData = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeVarInt(this.entityId);
        this.writeBoolean(this.includeData);
    }

    @Override
    public void copy(WrapperPlayClientPickItemFromEntity wrapper) {
        this.entityId = wrapper.entityId;
        this.includeData = wrapper.includeData;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public boolean isIncludeData() {
        return this.includeData;
    }

    public void setIncludeData(boolean includeData) {
        this.includeData = includeData;
    }
}
