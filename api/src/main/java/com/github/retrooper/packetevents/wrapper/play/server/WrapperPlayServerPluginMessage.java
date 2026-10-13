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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerPluginMessage;

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 */
public class WrapperPlayServerPluginMessage extends WrapperCommonServerPluginMessage<WrapperPlayServerPluginMessage> {

    public WrapperPlayServerPluginMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPluginMessage(ResourceLocation channelName, byte[] data) {
        this(channelName.toString(), data);
    }

    public WrapperPlayServerPluginMessage(String channelName, byte[] data) {
        super(PacketType.Play.Server.PLUGIN_MESSAGE, channelName, data);
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.channelName = readString();
        }
        else {
            this.channelName = readString(20);
        }
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            //We ignore this, because it's not needed.
            int legacyDataSize = readShort();
        }
        if (ByteBufHelper.readableBytes(buffer) > 1048576) {
            throw new RuntimeException("Payload may not be larger than 1048576 bytes");
        }
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
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            writeShort(data.length);
        }
        writeBytes(data);
    }

}
