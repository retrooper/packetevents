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
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 */
public class WrapperPlayServerPluginMessage extends PacketWrapper<WrapperPlayServerPluginMessage> {
    private String channelNameLegacy;
    private ResourceLocation channelName;
    private byte[] data;

    public WrapperPlayServerPluginMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPluginMessage(String channelNameLegacy, byte[] data) {
        super(PacketType.Play.Server.PLUGIN_MESSAGE);
        this.channelNameLegacy = channelNameLegacy;
        this.data = data;
    }

    public WrapperPlayServerPluginMessage(ResourceLocation channelName, byte[] data) {
        super(PacketType.Play.Server.PLUGIN_MESSAGE);
        this.channelName = channelName;
        this.data = data;
    }

    @Override
    public void read() {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_13) && clientVersion.isOlderThan(ClientVersion.V_1_13)) {
            this.channelNameLegacy = readString(20);
        } else {
            this.channelName = readIdentifier();
        }
        this.data = readRemainingBytes();
    }

    @Override
    public void write() {
        if (serverVersion.isOlderThan(ServerVersion.V_1_13) && clientVersion.isOlderThan(ClientVersion.V_1_13)) {
            writeString(channelNameLegacy);
        } else {
            writeIdentifier(channelName);
        }
        writeBytes(data);
    }

    @Override
    public void copy(WrapperPlayServerPluginMessage wrapper) {
        this.channelNameLegacy = wrapper.channelNameLegacy;
        this.channelName = wrapper.channelName;
        this.data = wrapper.data;
    }

    /**
     * The channel name of the plugin message.
     * <p>
     * <b>IMPORTANT</b>
     * <p>
     * <h3>Modern Server and Client versions use {@code ResourceLocation} for the channel name.
     * Older versions use {@code String} for the channel name.</h3>
     *
     * @param <T> The type of the channel name.
     * @return The channel name.
     */
    public <T> T getChannelName() {
        if (serverVersion.isOlderThan(ServerVersion.V_1_13) && clientVersion.isOlderThan(ClientVersion.V_1_13)) {
            return (T) channelNameLegacy;
        }
        return (T) channelName;
    }

    /**
     * Sets the channel name of the plugin message.
     * <p>
     * <b>IMPORTANT</b>
     * <p>
     * <h3>Modern Server and Client versions use {@code ResourceLocation} for the channel name.
     * Older versions use {@code String} for the channel name.</h3>
     *
     * @param <T>         The type of the channel name.
     * @param channelName The channel name.
     */
    public <T> void setChannelName(T channelName) {
        if (serverVersion.isOlderThan(ServerVersion.V_1_13) && clientVersion.isOlderThan(ClientVersion.V_1_13)) {
            this.channelNameLegacy = (String) channelName;
        } else {
            this.channelName = (ResourceLocation) channelName;
        }
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
