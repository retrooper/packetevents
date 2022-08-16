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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 */
public class WrapperPlayClientPluginMessage extends PacketWrapper<WrapperPlayClientPluginMessage> {
    private String channelName;
    private byte[] data;

    public WrapperPlayClientPluginMessage(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPluginMessage(String channelName, byte[] data) {
        super(PacketType.Play.Client.PLUGIN_MESSAGE);
        this.channelName = channelName;
        this.data = data;
    }

    public WrapperPlayClientPluginMessage(ResourceLocation channelName, byte[] data) {
        super(PacketType.Play.Client.PLUGIN_MESSAGE);
        this.channelName = channelName.toString();
        this.data = data;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.channelName = readString();
        } else {
            this.channelName = readString(20);
        }

        if (serverVersion == ServerVersion.V_1_7_10) {
            //It is ignored, because we don't need it
            int legacyDataSize = readShort();
        }

        if (ByteBufHelper.readableBytes(buffer) > 32767) throw new RuntimeException("Payload may not be larger than 32767 bytes");
        this.data = readRemainingBytes();
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            writeString(this.channelName);
        }
        else {
            writeString(this.channelName, 20);
        }
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeShort(this.data.length);
        }
        writeBytes(this.data);
    }

    @Override
    public void copy(WrapperPlayClientPluginMessage wrapper) {
        this.channelName = wrapper.channelName;
        this.data = wrapper.data;
    }

    /**
     * The channel name of the plugin message.
     * @return The channel name.
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Sets the channel name of the plugin message.
     * @param channelName The channel name.
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * The data of the plugin message.
     *
     * @return The data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the data of the plugin message.
     *
     * @param data The data.
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
