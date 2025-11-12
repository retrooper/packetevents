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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatAck extends PacketWrapper<WrapperPlayClientChatAck> {
    private LastSeenMessages.LegacyUpdate lastSeenMessages;
    private int offset;

    public WrapperPlayClientChatAck(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatAck(LastSeenMessages.LegacyUpdate lastSeenMessages) {
        super(PacketType.Play.Client.CHAT_ACK);
        this.lastSeenMessages = lastSeenMessages;
    }

    public WrapperPlayClientChatAck(int offset) {
        super(PacketType.Play.Client.CHAT_ACK);
        this.offset = offset;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            offset = readVarInt();
        } else {
            lastSeenMessages = readLegacyLastSeenMessagesUpdate();
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            writeVarInt(offset);
        } else {
            writeLegacyLastSeenMessagesUpdate(lastSeenMessages);
        }
    }

    @Override
    public void copy(WrapperPlayClientChatAck wrapper) {
        this.lastSeenMessages = wrapper.lastSeenMessages;
        this.offset = wrapper.offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public LastSeenMessages.LegacyUpdate getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(LastSeenMessages.LegacyUpdate lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }
}
