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

package com.github.retrooper.packetevents.protocol.chat.message.reader.impl;

import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19;
import com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public class ChatMessageProcessor_v1_19 implements ChatMessageProcessor {
    @Override
    public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
        Component chatContent = wrapper.readComponent();
        Component unsignedChatContent = wrapper.readOptional(PacketWrapper::readComponent);
        ChatType type = wrapper.readMappedEntity(ChatTypes.getRegistry());
        UUID senderUUID = wrapper.readUUID();
        Component senderDisplayName = wrapper.readComponent();
        @Nullable Component teamName = wrapper.readOptional(PacketWrapper::readComponent);
        Instant timestamp = wrapper.readTimestamp();
        long salt = wrapper.readLong();
        byte[] signature = wrapper.readByteArray();
        return new ChatMessage_v1_19(chatContent, unsignedChatContent, type, senderUUID, senderDisplayName, teamName,
                timestamp, salt, signature);
    }

    @Override
    public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
        ChatMessage_v1_19 newData = (ChatMessage_v1_19) data;
        wrapper.writeComponent(newData.getChatContent());
        wrapper.writeOptional(newData.getUnsignedChatContent(), PacketWrapper::writeComponent);
        wrapper.writeMappedEntity(newData.getType());
        wrapper.writeUUID(newData.getSenderUUID());
        wrapper.writeComponent(newData.getSenderDisplayName());
        wrapper.writeOptional(newData.getTeamName(), PacketWrapper::writeComponent);
        wrapper.writeTimestamp(newData.getTimestamp());
        wrapper.writeLong(newData.getSalt());
        wrapper.writeByteArray(newData.getSignature());
    }
}
