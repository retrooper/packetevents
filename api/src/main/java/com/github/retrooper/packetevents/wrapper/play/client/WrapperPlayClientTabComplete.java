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

public class WrapperPlayClientTabComplete extends PacketWrapper<WrapperPlayClientTabComplete> {
    private String text;
    private Optional<Integer> transactionID;
    private Optional<Vector3i> blockPosition;

    public WrapperPlayClientTabComplete(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientTabComplete(Optional<Integer> transactionID, String text, Optional<Vector3i> blockPosition) {
        super(PacketType.Play.Client.TAB_COMPLETE);
        this.transactionID = transactionID;
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
            transactionID = Optional.of(readVarInt());
            blockPosition = Optional.empty();
            text = readString(textLength);
        }
        else {
            textLength = 32767;
            text = readString(textLength);
            transactionID = Optional.empty();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
                boolean hasPosition = readBoolean();
                if (hasPosition) {
                    blockPosition = Optional.of(new Vector3i(readLong()));
                }
                else {
                    blockPosition = Optional.empty();
                }
            }
            else {
                blockPosition = Optional.empty();
            }
        }
    }

    @Override
    public void copy(WrapperPlayClientTabComplete wrapper) {
        text = wrapper.text;
        transactionID = wrapper.transactionID;
        blockPosition = wrapper.blockPosition;
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
            writeVarInt(transactionID.get());
            writeString(text, textLength);
        }
        else {
            textLength = 32767;
            writeString(text, textLength);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
                boolean hasPosition = blockPosition.isPresent();
                writeBoolean(hasPosition);
                if (hasPosition) {
                    writeLong(blockPosition.get().getSerializedPosition());
                }
            }
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Optional<Integer> getTransactionId() {
        return transactionID;
    }

    public void setTransactionId(@Nullable Integer transactionID) {
        if (transactionID != null) {
            this.transactionID = Optional.of(transactionID);
        }
        else {
            this.transactionID = Optional.empty();
        }
    }

    public Optional<Vector3i> getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(@Nullable Vector3i blockPosition) {
        if (blockPosition != null) {
            this.blockPosition = Optional.of(blockPosition);
        }
        else {
            this.blockPosition = Optional.empty();
        }
    }
}
