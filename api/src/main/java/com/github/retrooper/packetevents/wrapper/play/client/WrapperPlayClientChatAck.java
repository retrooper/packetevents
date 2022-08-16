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
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatAck extends PacketWrapper<WrapperPlayClientChatAck> {
    private LastSeenMessages.Update lastSeenMessages;

    public WrapperPlayClientChatAck(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatAck(LastSeenMessages.Update lastSeenMessages) {
        super(PacketType.Play.Client.CHAT_ACK);
        this.lastSeenMessages = lastSeenMessages;
    }

    @Override
    public void read() {
        lastSeenMessages = readLastSeenMessagesUpdate();
    }

    @Override
    public void write() {
        writeLastSeenMessagesUpdate(lastSeenMessages);
    }

    @Override
    public void copy(WrapperPlayClientChatAck wrapper) {
        this.lastSeenMessages = wrapper.lastSeenMessages;
    }

    public LastSeenMessages.Update getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(LastSeenMessages.Update lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }
}
