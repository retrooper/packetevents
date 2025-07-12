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

package com.github.retrooper.packetevents.protocol.chat.message;

import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import net.kyori.adventure.text.Component;

public class ChatMessage {
    private Component chatContent;
    private ChatType type;

    protected ChatMessage(Component chatContent, ChatType type) {
        this.chatContent = chatContent;
        this.type = type;
    }

    public Component getChatContent() {
        return chatContent;
    }

    public String getChatContentJson(ClientVersion version) {
        return AdventureSerializer.serializer(version).asJson(this.getChatContent());
    }

    public void setChatContent(Component chatContent) {
        this.chatContent = chatContent;
    }

    public void setChatContentJson(ClientVersion version, String json) {
        this.setChatContent(AdventureSerializer.serializer(version).fromJson(json));
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }
}
