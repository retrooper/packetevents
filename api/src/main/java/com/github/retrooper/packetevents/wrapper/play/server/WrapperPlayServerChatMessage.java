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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.reader.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
    private static final ChatMessageProcessor CHAT_LEGACY_PROCESSOR = new ChatMessageProcessorLegacy();
    private static final ChatMessageProcessor CHAT_V1_16_PROCESSOR = new ChatMessageProcessor_v1_16();
    private static final ChatMessageProcessor CHAT_V1_19_PROCESSOR = new ChatMessageProcessor_v1_19();
    private static final ChatMessageProcessor CHAT_V1_19_1_PROCESSOR = new ChatMessageProcessor_v1_19_1();
    private ChatMessage message;

    public WrapperPlayServerChatMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChatMessage(ChatMessage message) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.message = message;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            message = CHAT_V1_19_1_PROCESSOR.readChatMessage(this);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            message = CHAT_V1_19_PROCESSOR.readChatMessage(this);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            message = CHAT_V1_16_PROCESSOR.readChatMessage(this);
        } else {
            message = CHAT_LEGACY_PROCESSOR.readChatMessage(this);
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            CHAT_V1_19_1_PROCESSOR.writeChatMessage(this, message);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            CHAT_V1_19_PROCESSOR.writeChatMessage(this, message);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            CHAT_V1_16_PROCESSOR.writeChatMessage(this, message);
        } else {
            CHAT_LEGACY_PROCESSOR.writeChatMessage(this, message);
        }
    }

    @Override
    public void copy(WrapperPlayServerChatMessage wrapper) {
        this.message = wrapper.message;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }
}
