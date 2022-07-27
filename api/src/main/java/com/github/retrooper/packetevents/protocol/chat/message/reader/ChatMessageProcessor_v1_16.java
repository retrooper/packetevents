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
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class ChatMessageProcessor_v1_16 implements ChatMessageProcessor {
    @Override
    public ChatMessage readChatMessage(PacketWrapper<?> wrapper) {
        ServerVersion serverVersion = wrapper.getServerVersion();
        Component chatContent = wrapper.readComponent();
        int id = wrapper.readByte();
        ChatType type = ChatType.getById(serverVersion, id);
        UUID senderUUID = wrapper.readUUID();
        return new ChatMessage_v1_16(chatContent, type, senderUUID);
    }

    @Override
    public void writeChatMessage(PacketWrapper<?> wrapper, ChatMessage data) {
        ServerVersion serverVersion = wrapper.getServerVersion();
        wrapper.writeComponent(data.getChatContent());
        wrapper.writeByte(data.getType().getId(serverVersion));
        wrapper.writeUUID(((ChatMessage_v1_16)data).getSenderUUID());
    }
}
