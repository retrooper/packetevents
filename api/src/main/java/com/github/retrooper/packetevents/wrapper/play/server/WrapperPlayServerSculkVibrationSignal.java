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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSculkVibrationSignal extends PacketWrapper<WrapperPlayServerSculkVibrationSignal> {
    private Vector3i sourcePosition;
    private ResourceLocation destinationIdentifier;
    private Vector3i blockPosition;
    private int arrivalTicks;

    public WrapperPlayServerSculkVibrationSignal(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSculkVibrationSignal(Vector3i sourcePosition, ResourceLocation destinationIdentifier, Vector3i blockPosition, int arrivalTicks) {
        super(PacketType.Play.Server.SCULK_VIBRATION_SIGNAL);
        this.sourcePosition = sourcePosition;
        this.destinationIdentifier = destinationIdentifier;
        this.blockPosition = blockPosition;
        this.arrivalTicks = arrivalTicks;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.sourcePosition = new Vector3i(readLong());
        } else {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            this.sourcePosition = new Vector3i(x, y, z);
        }
        this.destinationIdentifier = readIdentifier();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.blockPosition = new Vector3i(readLong());
        } else {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            this.blockPosition = new Vector3i(x, y, z);
        }
        this.arrivalTicks = readVarInt();
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            long positionVector = this.sourcePosition.getSerializedPosition();
            writeLong(positionVector);
        } else {
            writeInt(this.sourcePosition.x);
            writeShort(this.sourcePosition.y);
            writeInt(this.sourcePosition.z);
        }
        writeIdentifier(this.destinationIdentifier);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            long positionVector = this.blockPosition.getSerializedPosition();
            writeLong(positionVector);
        } else {
            writeInt(this.blockPosition.x);
            writeShort(this.blockPosition.y);
            writeInt(this.blockPosition.z);
        }
        writeVarInt(this.arrivalTicks);
    }

    @Override
    public void copy(WrapperPlayServerSculkVibrationSignal wrapper) {
        this.sourcePosition = wrapper.sourcePosition;
        this.destinationIdentifier = wrapper.destinationIdentifier;
        this.blockPosition = wrapper.blockPosition;
        this.arrivalTicks = wrapper.arrivalTicks;
    }

    public Vector3i getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(Vector3i sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public ResourceLocation getDestinationIdentifier() {
        return destinationIdentifier;
    }

    public void setDestinationIdentifier(ResourceLocation destinationIdentifier) {
        this.destinationIdentifier = destinationIdentifier;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getArrivalTicks() {
        return arrivalTicks;
    }

    public void setArrivalTicks(int arrivalTicks) {
        this.arrivalTicks = arrivalTicks;
    }
}
