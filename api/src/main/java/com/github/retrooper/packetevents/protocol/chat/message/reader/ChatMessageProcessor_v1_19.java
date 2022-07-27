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

package com.github.retrooper.packetevents.protocol.chat.message.reader;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public class ChatMessageProcessor_v1_19 implements ChatMessageProcessor {
    @Override
    public ChatMessage readChatMessage(PacketWrapper<?> wrapper) {
        ServerVersion serverVersion = wrapper.getServerVersion();
        Component chatContent = wrapper.readComponent();
        Component unsignedChatContent = wrapper.readOptional(PacketWrapper::readComponent);
        int id = wrapper.readVarInt();
        ChatType type = ChatType.getById(serverVersion, id);
        UUID senderUUID = wrapper.readUUID();
        Component senderDisplayName = wrapper.readComponent();
        @Nullable Component teamName = wrapper.readOptional(PacketWrapper::readComponent);
        Instant timestamp = wrapper.readTimestamp();
        long salt = wrapper.readLong();
        byte[] signature = wrapper.readByteArray();
        return new ChatMessage_v1_19(chatContent, unsignedChatContent, type, senderUUID,
                senderDisplayName, teamName, timestamp, salt, signature);
    }

    @Override
    public void writeChatMessage(PacketWrapper<?> wrapper, ChatMessage d) {
        ServerVersion serverVersion = wrapper.getServerVersion();
        ChatMessage_v1_19 data = (ChatMessage_v1_19) d;
        wrapper.writeComponent(data.getChatContent());
        wrapper.writeOptional(data.getUnsignedChatContent(), PacketWrapper::writeComponent);
        wrapper.writeVarInt(data.getType().getId(serverVersion));
        wrapper.writeUUID(data.getSenderUUID());
        wrapper.writeComponent(data.getSenderDisplayName());
        wrapper.writeOptional(data.getTeamName(), PacketWrapper::writeComponent);
        wrapper.writeTimestamp(data.getTimestamp());
        wrapper.writeLong(data.getSalt());
        wrapper.writeByteArray(data.getSignature());
    }
}
