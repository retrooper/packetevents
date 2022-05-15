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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerNBTQueryResponse extends PacketWrapper<WrapperPlayServerNBTQueryResponse> {
    private int transactionId;
    private NBTCompound tag;

    public WrapperPlayServerNBTQueryResponse(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerNBTQueryResponse(int transactionId, NBTCompound tag) {
        super(PacketType.Play.Server.NBT_QUERY_RESPONSE);
        this.transactionId = transactionId;
        this.tag = tag;
    }

    @Override
    public void read() {
        transactionId = readVarInt();
        tag = readNBT();
    }

    @Override
    public void write() {
        writeVarInt(transactionId);
        writeNBT(tag);
    }

    @Override
    public void copy(WrapperPlayServerNBTQueryResponse wrapper) {
        transactionId = wrapper.transactionId;
        tag = wrapper.tag;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public NBTCompound getTag() {
        return tag;
    }

    public void setTag(NBTCompound tag) {
        this.tag = tag;
    }
}
