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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatSessionUpdate extends PacketWrapper<WrapperPlayClientChatSessionUpdate> {
    private RemoteChatSession chatSession;

    public WrapperPlayClientChatSessionUpdate(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatSessionUpdate(RemoteChatSession chatSession) {
        super(PacketType.Play.Client.CHAT_SESSION_UPDATE);
        this.chatSession = chatSession;
    }

    @Override
    public void read() {
        chatSession = readRemoteChatSession();
    }

    @Override
    public void write() {
        writeRemoteChatSession(chatSession);
    }

    @Override
    public void copy(WrapperPlayClientChatSessionUpdate wrapper) {
        this.chatSession = wrapper.chatSession;
    }

    public RemoteChatSession getChatSession() {
        return chatSession;
    }

    public void setChatSession(RemoteChatSession chatSession) {
        this.chatSession = chatSession;
    }
}
