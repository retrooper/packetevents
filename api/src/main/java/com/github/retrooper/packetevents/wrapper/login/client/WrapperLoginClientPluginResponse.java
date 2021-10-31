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

package com.github.retrooper.packetevents.wrapper.login.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientPluginResponse extends PacketWrapper<WrapperLoginClientPluginResponse> {
    private int messageID;
    private boolean successful;
    private byte[] data;

    public WrapperLoginClientPluginResponse(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperLoginClientPluginResponse(ClientVersion clientVersion, int messageID, boolean successful, byte[] data) {
        super(PacketType.Login.Client.LOGIN_PLUGIN_RESPONSE.getID(), clientVersion);
        this.messageID = messageID;
        this.successful = successful;
        this.data = data;
    }

    @Override
    public void readData() {
        this.messageID = readVarInt();
        this.successful = readBoolean();
        if (this.successful) {
            this.data = readByteArray(byteBuf.readableBytes());
        } else {
            this.data = new byte[0];
        }
    }

    @Override
    public void readData(WrapperLoginClientPluginResponse wrapper) {
        this.messageID = wrapper.messageID;
        this.successful = wrapper.successful;
        this.data = wrapper.data;
    }

    @Override
    public void writeData() {
        writeVarInt(messageID);
        writeBoolean(successful);
        if (successful) {
            writeByteArray(data);
        }
    }

    public int getMessageID() {
        return this.messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
