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

package io.github.retrooper.packetevents.wrapper.play.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.protocol.data.player.ClientVersion;
import io.github.retrooper.packetevents.utils.Vector3i;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

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
    public void readData() {
        //1.7 -> 1.12.2 text length
        int textLength = 32767;
        boolean v1_13 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13);
        if (v1_13) {

            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_1)) {
                //1.13.1+ text length
                textLength = 32500;
            } else {
                //1.13 text length
                textLength = 256;
            }
            transactionID = Optional.of(readVarInt());
            blockPosition = Optional.empty();
        }

        text = readString(textLength);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            if (!v1_13) {
                transactionID = Optional.empty();
                //1.13+ removed this
                boolean hasPosition = readBoolean();
                if (hasPosition) {
                    blockPosition = Optional.of(new Vector3i(readLong()));
                }
            }
        }
    }

    @Override
    public void readData(WrapperPlayClientTabComplete wrapper) {
        text = wrapper.text;
        transactionID = wrapper.transactionID;
        blockPosition = wrapper.blockPosition;
    }

    @Override
    public void writeData() {
        //1.7 -> 1.12.2 text length
        int textLength = 32767;
        boolean v1_13 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13);
        if (v1_13) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13_1)) {
                //1.13.1+ text length
                textLength = 32500;
            } else {
                //1.13 text length
                textLength = 256;
            }
            writeVarInt(transactionID.orElse(-1));
        }
        writeString(text, textLength);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            if (!v1_13) {
                //1.13+ removed this
                boolean hasPosition = blockPosition.isPresent();
                writeBoolean(hasPosition);
                if (hasPosition) {
                    writeLong(blockPosition.orElse(new Vector3i(-1, -1, -1)).getSerializedPosition());
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

    public Optional<Integer> getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Optional<Integer> transactionID) {
        this.transactionID = transactionID;
    }

    public Optional<Vector3i> getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Optional<Vector3i> blockPosition) {
        this.blockPosition = blockPosition;
    }
}
