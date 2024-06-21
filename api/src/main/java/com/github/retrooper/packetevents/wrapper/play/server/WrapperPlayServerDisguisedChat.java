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
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_1;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

public class WrapperPlayServerDisguisedChat extends PacketWrapper<WrapperPlayServerDisguisedChat> {
    private Component message;
    private ChatType.Bound chatFormatting;

    public WrapperPlayServerDisguisedChat(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDisguisedChat(Component message, ChatType.Bound chatType) {
        super(PacketType.Play.Server.DISGUISED_CHAT);
        this.message = message;
        this.chatFormatting = chatType;
    }

    @Override
    public void read() {
        message = readComponent();
        chatFormatting = readChatTypeBoundNetwork();
    }

    @Override
    public void write() {
        writeComponent(message);
        writeChatTypeBoundNetwork(chatFormatting);
    }

    @Override
    public void copy(WrapperPlayServerDisguisedChat wrapper) {
        this.message = wrapper.message;
        this.chatFormatting = wrapper.chatFormatting;
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public ChatType.Bound getChatFormatting() {
        return chatFormatting;
    }

    public void setChatFormatting(ChatType.Bound chatFormatting) {
        this.chatFormatting = chatFormatting;
    }

    /**
     * Retrieves the chat formatting. Please refer to {@link WrapperPlayServerDisguisedChat#getChatFormatting}
     * @return chat formatting which contains the actual chat type.
     */
    @Deprecated
    public ChatType.Bound getChatType() {
        return chatFormatting;
    }

    /**
     * Sets the chat formatting. Please refer to {@link WrapperPlayServerDisguisedChat#setChatFormatting}
     * @param chatFormatting formatting which contains the actual chat type.
     */
    @Deprecated
    public void setChatType(ChatType.Bound chatFormatting) {
        this.chatFormatting = chatFormatting;
    }
}
