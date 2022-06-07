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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is used to send a chat message to the server.
 */
public class WrapperPlayClientChatMessage extends PacketWrapper<WrapperPlayClientChatMessage> {
    private String message;

    public WrapperPlayClientChatMessage(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatMessage(String message) {
        super(PacketType.Play.Client.CHAT_MESSAGE);
        this.message = message;
    }

    @Override
    public void read() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11) ? 256 : 100;
        this.message = readString(maxMessageLength);
    }

    @Override
    public void copy(WrapperPlayClientChatMessage wrapper) {
        this.message = wrapper.message;
    }

    @Override
    public void write() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11) ? 256 : 100;
        writeString(this.message, maxMessageLength);
    }

    /**
     * The message.
     * On {@link ClientVersion#V_1_10} and older clients, the message should never exceed 100 characters.
     * On {@link ClientVersion#V_1_11} and newer clients, the message should never exceed 256 characters.
     *
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Modify the message.
     * On {@link ClientVersion#V_1_10} and older clients, the message should never exceed 100 characters.
     * On {@link ClientVersion#V_1_11} and newer clients, the message should never exceed 256 characters.
     *
     * @param message Message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
