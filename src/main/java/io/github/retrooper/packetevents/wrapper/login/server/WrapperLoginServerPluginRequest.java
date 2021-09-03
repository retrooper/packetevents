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

package io.github.retrooper.packetevents.wrapper.login.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.SendablePacketWrapper;

public class WrapperLoginServerPluginRequest extends SendablePacketWrapper<WrapperLoginServerPluginRequest> {
    private int messageID;
    private String channelName;
    private byte[] data;

    public WrapperLoginServerPluginRequest(PacketSendEvent event) {
        super(event);
    }

    public WrapperLoginServerPluginRequest(int messageID, String channelName, byte[] data) {
        super(PacketType.Login.Server.LOGIN_PLUGIN_REQUEST.getID());
        this.messageID = messageID;
        this.channelName = channelName;
        this.data = data;
    }

    @Override
    public void readData() {
        this.messageID = readVarInt();
        this.channelName = readString();
        this.data = readByteArray(byteBuf.readableBytes());
    }

    @Override
    public void readData(WrapperLoginServerPluginRequest wrapper) {
        this.messageID = wrapper.messageID;
        this.channelName = wrapper.channelName;
        this.data = wrapper.data;
    }

    @Override
    public void writeData() {
        writeVarInt(messageID);
        writeString(channelName);
        writeByteArray(data);
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}