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

import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.SendablePacketWrapper;


import java.util.UUID;

public class WrapperGameServerChatMessage extends SendablePacketWrapper {
    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;

    public enum Position {
        CHAT, SYSTEM_MESSAGE, GAME_INFO;

        public static final Position[] VALUES = values();
    }

    private String jsonMessage;
    private final Position position;
    private final UUID senderUUID;
    public WrapperGameServerChatMessage(ByteBufAbstract byteBuf) {
        super(byteBuf);
        if (getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
            this.jsonMessage = readString(MODERN_MESSAGE_LENGTH);
        }
        else {
            this.jsonMessage = readString();
        }
        byte positionIndex = readByte();
        position = Position.VALUES[positionIndex];
        if (getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_16)) {
            this.senderUUID = readUUID();
        }
        else {
            this.senderUUID = new UUID(0L, 0L);
        }
    }

    public WrapperGameServerChatMessage(String jsonMessage, Position position) {
        super(PacketType.Game.Server.CHAT_MESSAGE.getID());
        this.jsonMessage = jsonMessage;
        this.position = position;
        this.senderUUID = new UUID(0L, 0L);
    }

    public WrapperGameServerChatMessage(String jsonMessage, Position position, UUID senderUUID) {
        super(PacketType.Game.Server.CHAT_MESSAGE.getID());
        this.jsonMessage = jsonMessage;
        this.position = position;
        this.senderUUID = senderUUID;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }

    public Position getPosition() {
        return position;
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    @Override
    public void createPacket() {
        int maxLen = protocolVersion >= 393 ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        if (jsonMessage.length() > maxLen) {
            jsonMessage = jsonMessage.substring(0, maxLen);
        }
        writeString(jsonMessage, maxLen);
        writeByte((byte) position.ordinal());
        if (protocolVersion >= 735) {
            writeUUID(senderUUID);
        }
    }
}
