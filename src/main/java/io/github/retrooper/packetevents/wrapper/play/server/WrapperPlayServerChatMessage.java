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

package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.protocol.data.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;
    private String jsonMessage;
    private ChatPosition position;
    private UUID senderUUID;

    public WrapperPlayServerChatMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChatMessage(String jsonMessage, ChatPosition position) {
        super(PacketType.Play.Server.CHAT_MESSAGE.getID());
        this.jsonMessage = jsonMessage;
        this.position = position;
        this.senderUUID = new UUID(0L, 0L);
    }

    public WrapperPlayServerChatMessage(String jsonMessage, ChatPosition position, UUID senderUUID) {
        super(PacketType.Play.Server.CHAT_MESSAGE.getID());
        this.jsonMessage = jsonMessage;
        this.position = position;
        this.senderUUID = senderUUID;
    }

    @Override
    public void readData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        this.jsonMessage = readString(maxMessageLength);
        //Is the server 1.8+ or is the client 1.8+? 1.7.10 servers support 1.8 clients, and send the chat position.
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            byte positionIndex = readByte();
            position = ChatPosition.VALUES[positionIndex];
        } else {
            //Always chat in 1.7.10 protocol.
            position = ChatPosition.CHAT;
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16)) {
            this.senderUUID = readUUID();
        } else {
            this.senderUUID = new UUID(0L, 0L);
        }
    }

    @Override
    public void readData(WrapperPlayServerChatMessage wrapper) {
        this.jsonMessage = wrapper.jsonMessage;
        this.position = wrapper.position;
        this.senderUUID = wrapper.senderUUID;
    }

    @Override
    public void writeData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        writeString(jsonMessage, maxMessageLength);

        //Is the server 1.8+ or is the client 1.8+? (1.7.10 servers support 1.8 clients, and send the chat position for 1.8 clients)
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            writeByte(position.ordinal());
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16)) {
            writeUUID(senderUUID);
        }
    }

    public String getJSONMessage() {
        return jsonMessage;
    }

    public void setJSONMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public ChatPosition getPosition() {
        return position;
    }

    public void setPosition(ChatPosition position) {
        this.position = position;
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(UUID senderUUID) {
        this.senderUUID = senderUUID;
    }

    public enum ChatPosition {
        CHAT, SYSTEM_MESSAGE, GAME_INFO;

        public static final ChatPosition[] VALUES = values();
    }
}
