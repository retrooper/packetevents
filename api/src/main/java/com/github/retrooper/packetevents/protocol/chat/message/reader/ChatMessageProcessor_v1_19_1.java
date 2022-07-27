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
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_1;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.time.Instant;
import java.util.UUID;

public class ChatMessageProcessor_v1_19_1 implements ChatMessageProcessor {
    @Override
    public ChatMessage readChatMessage(PacketWrapper<?> wrapper) {
        ServerVersion serverVersion = wrapper.getServerVersion();
        byte[] previousSignature = wrapper.readOptional(PacketWrapper::readByteArray);
        UUID senderUUID = wrapper.readUUID();
        byte[] signature = wrapper.readByteArray();
        String plainContent = wrapper.readString(256);
        Component chatContent = wrapper.readOptional(PacketWrapper::readComponent);
        if (chatContent == null) {
            chatContent = Component.text(plainContent);
        }
        Instant timestamp = wrapper.readTimestamp();
        long salt = wrapper.readLong();
        LastSeenMessages lastSeenMessages = wrapper.readLastSeenMessages();
        Component unsignedChatContent = wrapper.readOptional(PacketWrapper::readComponent);
        int id = wrapper.readVarInt();
        ChatType type = ChatType.getById(serverVersion, id);
        Component name = wrapper.readComponent();
        Component targetName = wrapper.readOptional(PacketWrapper::readComponent);
        ChatMessage_v1_19_1.ChatTypeBoundNetwork chatType =
                new ChatMessage_v1_19_1.ChatTypeBoundNetwork(type, name, targetName);
        return new ChatMessage_v1_19_1(plainContent, chatContent, unsignedChatContent,
                senderUUID, chatType, previousSignature, signature, timestamp, salt, lastSeenMessages);
    }

    @Override
    public void writeChatMessage(PacketWrapper<?> wrapper, ChatMessage d) {
        ServerVersion serverVersion = wrapper.getServerVersion();
        ChatMessage_v1_19_1 data = (ChatMessage_v1_19_1) d;
        wrapper.writeOptional(data.getPreviousSignature(), PacketWrapper::writeByteArray);
        wrapper.writeUUID(data.getSenderUUID());
        wrapper.writeByteArray(data.getSignature());
        wrapper.writeString(data.getPlainContent(), 256);
        wrapper.writeOptional(data.getChatContent(), PacketWrapper::writeComponent);
        wrapper.writeTimestamp(data.getTimestamp());
        wrapper.writeLong(data.getSalt());
        wrapper.writeLastSeenMessages(data.getLastSeenMessages());
        wrapper.writeOptional(data.getUnsignedChatContent(), PacketWrapper::writeComponent);
        wrapper.writeVarInt(data.getChatType().getType().getId(serverVersion));
        wrapper.writeComponent(data.getChatType().getName());
        wrapper.writeOptional(data.getChatType().getTargetName(), PacketWrapper::writeComponent);
    }
}
