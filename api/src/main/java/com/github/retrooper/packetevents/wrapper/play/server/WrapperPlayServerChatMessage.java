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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import com.github.retrooper.packetevents.protocol.chat.message.reader.impl.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus.Internal;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
    private static final ChatMessageProcessor CHAT_LEGACY_PROCESSOR;
    private static final ChatMessageProcessor CHAT_V1_16_PROCESSOR;
    private static final ChatMessageProcessor CHAT_V1_19_PROCESSOR;
    private static final ChatMessageProcessor CHAT_V1_19_1_PROCESSOR;
    private static final ChatMessageProcessor CHAT_V1_19_3_PROCESSOR;

    static {
        CHAT_LEGACY_PROCESSOR = new ChatMessageProcessorLegacy();
        CHAT_V1_16_PROCESSOR = new ChatMessageProcessor_v1_16();
        CHAT_V1_19_PROCESSOR = new ChatMessageProcessor_v1_19();
        CHAT_V1_19_1_PROCESSOR = new ChatMessageProcessor_v1_19_1();
        CHAT_V1_19_3_PROCESSOR = new ChatMessageProcessor_v1_19_3();
    }

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
        this.message = this.getProcessor().readChatMessage(this);
    }

    @Override
    public void write() {
        this.getProcessor().writeChatMessage(this, this.message);
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

    @Internal
    protected ChatMessageProcessor getProcessor() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            return CHAT_V1_19_3_PROCESSOR;
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            return CHAT_V1_19_1_PROCESSOR;
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            return CHAT_V1_19_PROCESSOR;
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            return CHAT_V1_16_PROCESSOR;
        } else {
            return CHAT_LEGACY_PROCESSOR;
        }
    }
}
