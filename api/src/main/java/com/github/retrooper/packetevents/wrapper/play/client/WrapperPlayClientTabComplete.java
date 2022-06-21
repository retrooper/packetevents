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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public class WrapperPlayClientTabComplete extends PacketWrapper<WrapperPlayClientTabComplete> {
    private int transactionId;
    private String text;
    private @Nullable Vector3i blockPosition;

    public WrapperPlayClientTabComplete(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientTabComplete(int transactionId, String text, @Nullable Vector3i blockPosition) {
        super(PacketType.Play.Client.TAB_COMPLETE);
        this.transactionId = transactionId;
        this.text = text;
        this.blockPosition = blockPosition;
    }

    @Override
    public void read() {
        boolean v1_13 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13);
        int textLength;
        if (v1_13) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_1)) {
                //1.13.1+ text length
                textLength = 32500;
            } else {
                //1.13 text length
                textLength = 256;
            }
            transactionId = readVarInt();
            text = readString(textLength);
        } else {
            textLength = 32767;
            text = readString(textLength);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
                boolean hasPosition = readBoolean();
                if (hasPosition) {
                    blockPosition = new Vector3i(readLong());
                }
            }
        }
    }

    @Override
    public void write() {
        boolean v1_13 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13);
        int textLength;
        if (v1_13) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_1)) {
                //1.13.1+ text length
                textLength = 32500;
            } else {
                //1.13 text length
                textLength = 256;
            }
            writeVarInt(transactionId);
            writeString(text, textLength);
        } else {
            textLength = 32767;
            writeString(text, textLength);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
                boolean hasPosition = blockPosition != null;
                writeBoolean(hasPosition);
                if (hasPosition) {
                    writeLong(blockPosition.getSerializedPosition());
                }
            }
        }
    }

    @Override
    public void copy(WrapperPlayClientTabComplete wrapper) {
        text = wrapper.text;
        transactionId = wrapper.transactionId;
        blockPosition = wrapper.blockPosition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OptionalInt getTransactionId() {
        return OptionalInt.of(transactionId);
    }

    public void setTransactionId(@Nullable Integer transactionId) {
        if (transactionId != null) {
            this.transactionId = transactionId;
        }
    }

    public Optional<Vector3i> getBlockPosition() {
        return Optional.ofNullable(blockPosition);
    }

    public void setBlockPosition(@Nullable Vector3i blockPosition) {
        if (blockPosition != null) {
            this.blockPosition = blockPosition;
        }
    }
}
