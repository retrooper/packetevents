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

public class WrapperPlayClientQueryEntityNBT extends PacketWrapper<WrapperPlayClientQueryEntityNBT> {
    private int transactionID;
    private int entityID;

    public WrapperPlayClientQueryEntityNBT(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientQueryEntityNBT(int transactionID, int entityID) {
        super(PacketType.Play.Client.QUERY_ENTITY_NBT);
        this.transactionID = transactionID;
        this.entityID = entityID;
    }

    @Override
    public void read() {
        transactionID = readVarInt();
        entityID = readVarInt();
    }

    @Override
    public void write() {
        writeVarInt(transactionID);
        writeVarInt(entityID);
    }

    @Override
    public void copy(WrapperPlayClientQueryEntityNBT wrapper) {
        transactionID = wrapper.transactionID;
        entityID = wrapper.entityID;
    }

    public int getTransactionId() {
        return transactionID;
    }

    public void setTransactionId(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }
}
