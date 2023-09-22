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

package com.github.retrooper.packetevents.wrapper.configuration.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerPluginMessage extends PacketWrapper<WrapperConfigServerPluginMessage> {

    private String channelName;
    private byte[] data;

    public WrapperConfigServerPluginMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperConfigServerPluginMessage(ResourceLocation channelName, byte[] data) {
        this(channelName.toString(), data);
    }

    public WrapperConfigServerPluginMessage(String channelName, byte[] data) {
        super(PacketType.Configuration.Server.PLUGIN_MESSAGE);
        this.channelName = channelName;
        this.data = data;
    }

    @Override
    public void read() {
        this.channelName = this.readString();
        if (ByteBufHelper.readableBytes(this.buffer) > 32767) {
            throw new RuntimeException("Payload may not be larger than 32767 bytes");
        }
        this.data = this.readRemainingBytes();
    }

    @Override
    public void write() {
        this.writeString(this.channelName);
        this.writeBytes(this.data);
    }

    @Override
    public void copy(WrapperConfigServerPluginMessage wrapper) {
        this.channelName = wrapper.channelName;
        this.data = wrapper.data;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
