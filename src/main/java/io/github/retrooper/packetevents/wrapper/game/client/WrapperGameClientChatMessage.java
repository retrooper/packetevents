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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is used to send a chat message to the server.
 */
public class WrapperGameClientChatMessage extends PacketWrapper {
    private final int packetID;
    private final int maxMessageLength;
    private String message;

    public WrapperGameClientChatMessage(PacketReceiveEvent event) {
        super(event);
        this.packetID = event.getPacketID();
        this.maxMessageLength = clientVersion.isNewerThanOrEquals(ClientVersion.v_1_11) ? 256 : 100;
        this.message = readString(maxMessageLength);
    }

    /**
     * The message.
     * On {@link ClientVersion#v_1_10} and older clients, the message should never exceed 100 characters.
     * On {@link ClientVersion#v_1_11} and newer clients, the message should never exceed 256 characters.
     *
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        //TODO For now we basically reset the packet, but we don't want that
        this.message = message;
        byteBuf.clear();
        writeVarInt(packetID);
        writeString(message, maxMessageLength);
    }
}
