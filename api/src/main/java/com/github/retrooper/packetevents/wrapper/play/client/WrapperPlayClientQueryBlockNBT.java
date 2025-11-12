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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientQueryBlockNBT extends PacketWrapper<WrapperPlayClientQueryBlockNBT> {
    private int transactionID;
    private Vector3i blockPosition;

    public WrapperPlayClientQueryBlockNBT(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientQueryBlockNBT(int transactionID, Vector3i blockPosition) {
        super(PacketType.Play.Client.QUERY_BLOCK_NBT);
        this.transactionID = transactionID;
        this.blockPosition = blockPosition;
    }

    @Override
    public void read() {
        transactionID = readVarInt();
        blockPosition = readBlockPosition();
    }

    @Override
    public void write() {
        writeVarInt(transactionID);
        writeBlockPosition(blockPosition);
    }

    @Override
    public void copy(WrapperPlayClientQueryBlockNBT wrapper) {
        transactionID = wrapper.transactionID;
        blockPosition = wrapper.blockPosition;
    }

    public int getTransactionId() {
        return transactionID;
    }

    public void setTransactionId(int transactionID) {
        this.transactionID = transactionID;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }
}
