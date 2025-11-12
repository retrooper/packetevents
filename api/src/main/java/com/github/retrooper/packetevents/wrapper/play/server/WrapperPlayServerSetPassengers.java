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

public class WrapperPlayServerSetPassengers extends PacketWrapper<WrapperPlayServerSetPassengers> {
    private int entityId;
    private int[] passengers;

    public WrapperPlayServerSetPassengers(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetPassengers(int entityId, int[] passengers) {
        super(PacketType.Play.Server.SET_PASSENGERS);
        this.entityId = entityId;
        this.passengers = passengers;
    }

    @Override
    public void read() {
        this.entityId = readVarInt();
        this.passengers = readVarIntArray();
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        writeVarIntArray(passengers);
    }

    @Override
    public void copy(WrapperPlayServerSetPassengers wrapper) {
        entityId = wrapper.entityId;
        passengers = wrapper.passengers;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int[] getPassengers() {
        return passengers;
    }

    public void setPassengers(int[] passengers) {
        this.passengers = passengers;
    }
}
