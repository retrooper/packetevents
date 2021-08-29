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

package io.github.retrooper.packetevents.wrapper.game.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.SendablePacketWrapper;

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 */
public class WrapperGameServerPluginMessage extends SendablePacketWrapper {
    private final String channelName;
    private final byte[] data;

    public WrapperGameServerPluginMessage(PacketSendEvent event) {
        super(event);
        this.channelName = readString();
        if (getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_8)) {
            this.data = readByteArray(byteBuf.readableBytes());
        } else {
            int dataLength = readUnsignedShort();
            this.data = readByteArray(dataLength);
        }
    }

    public WrapperGameServerPluginMessage(String channelName, byte[] data) {
        super(PacketType.Game.Server.PLUGIN_MESSAGE.getID());
        this.channelName = channelName;
        this.data = data;
    }

    /**
     * Name of the plugin channel used to send the data.
     *
     * @return Plugin channel name
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Any data, depending on the channel.
     *
     * @return Data
     */
    public byte[] getData() {
        return data;
    }

    @Override
    public void createPacket() {
        writeString(channelName);

        //To simplify, only if less than 1.8, we are just handling the dev builds inbetween for some reason.
        if (protocolVersion < 29) {
            writeShort(data.length);
        }

        writeByteArray(data);
    }
}
