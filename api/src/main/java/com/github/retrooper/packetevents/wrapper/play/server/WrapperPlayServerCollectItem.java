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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerCollectItem extends PacketWrapper<WrapperPlayServerCollectItem> {
    private int collectedEntityId;
    private int collectorEntityId;
    private int pickupItemCount;

    public WrapperPlayServerCollectItem(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerCollectItem(int collectedEntityId, int collectorEntityId, int pickupItemCount) {
        super(PacketType.Play.Server.COLLECT_ITEM);
        this.collectedEntityId = collectedEntityId;
        this.collectorEntityId = collectorEntityId;
        this.pickupItemCount = pickupItemCount;
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            collectedEntityId = readInt();
            collectorEntityId = readInt();
        } else {
            collectedEntityId = readVarInt();
            collectorEntityId = readVarInt();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
                pickupItemCount = readVarInt();
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(collectedEntityId);
            writeInt(collectorEntityId);
        } else {
            writeVarInt(collectedEntityId);
            writeVarInt(collectorEntityId);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
                writeVarInt(pickupItemCount);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerCollectItem wrapper) {
        collectedEntityId = wrapper.collectedEntityId;
        collectorEntityId = wrapper.collectorEntityId;
        pickupItemCount = wrapper.pickupItemCount;
    }

    public int getCollectedEntityId() {
        return collectedEntityId;
    }

    public void setCollectedEntityId(int collectedEntityId) {
        this.collectedEntityId = collectedEntityId;
    }

    public int getCollectorEntityId() {
        return collectorEntityId;
    }

    public void setCollectorEntityId(int collectorEntityId) {
        this.collectorEntityId = collectorEntityId;
    }

    public int getPickupItemCount() {
        return pickupItemCount;
    }

    public void setPickupItemCount(int pickupItemCount) {
        this.pickupItemCount = pickupItemCount;
    }
}
