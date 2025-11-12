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

package com.github.retrooper.packetevents.wrapper.login.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginServerPluginRequest extends PacketWrapper<WrapperLoginServerPluginRequest> {
    private int messageID;
    private String channelName;
    private byte[] data;

    public WrapperLoginServerPluginRequest(PacketSendEvent event) {
        super(event);
    }

    public WrapperLoginServerPluginRequest(int messageID, String channelName, byte[] data) {
        super(PacketType.Login.Server.LOGIN_PLUGIN_REQUEST);
        this.messageID = messageID;
        this.channelName = channelName;
        this.data = data;
    }

    @Override
    public void read() {
        this.messageID = readVarInt();
        this.channelName = readString();
        this.data = readByteArray(ByteBufHelper.readableBytes(buffer));
    }

    @Override
    public void write() {
        writeVarInt(messageID);
        writeString(channelName);
        writeByteArray(data);
    }

    @Override
    public void copy(WrapperLoginServerPluginRequest wrapper) {
        this.messageID = wrapper.messageID;
        this.channelName = wrapper.channelName;
        this.data = wrapper.data;
    }

    public int getMessageId() {
        return messageID;
    }

    public void setMessageId(int messageID) {
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