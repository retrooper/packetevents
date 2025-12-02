/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_21_5;
import com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public class ChatMessageProcessor_v1_21_5 implements ChatMessageProcessor {

    @Override
    public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
        int globalIndex = wrapper.readVarInt();
        UUID senderUUID = wrapper.readUUID();
        int index = wrapper.readVarInt();
        byte[] signature = wrapper.readOptional((w) -> w.readBytes(256));
        String plainContent = wrapper.readString(256);
        Instant timestamp = wrapper.readTimestamp();
        long salt = wrapper.readLong();
        LastSeenMessages.Packed lastSeenMessagesPacked = wrapper.readLastSeenMessagesPacked();
        Component unsignedChatContent = wrapper.readOptional(PacketWrapper::readComponent);
        FilterMask filterMask = wrapper.readFilterMask();
        ChatType.Bound chatType = wrapper.readChatTypeBoundNetwork();

        return new ChatMessage_v1_21_5(globalIndex, senderUUID, index, signature, plainContent, timestamp, salt, lastSeenMessagesPacked, unsignedChatContent, filterMask, chatType);
    }

    @Override
    public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
        ChatMessage_v1_21_5 newData = (ChatMessage_v1_21_5) data;
        wrapper.writeVarInt(newData.getGlobalIndex());
        wrapper.writeUUID(newData.getSenderUUID());
        wrapper.writeVarInt(newData.getIndex());
        wrapper.writeOptional(newData.getSignature(), PacketWrapper::writeBytes);
        wrapper.writeString(newData.getPlainContent());
        wrapper.writeTimestamp(newData.getTimestamp());
        wrapper.writeLong(newData.getSalt());
        wrapper.writeLastSeenMessagesPacked(newData.getLastSeenMessagesPacked());
        wrapper.writeOptional(newData.getUnsignedChatContent().orElse(null), PacketWrapper::writeComponent);
        wrapper.writeFilterMask(newData.getFilterMask());
        wrapper.writeChatTypeBoundNetwork(newData.getChatFormatting());
    }
}
