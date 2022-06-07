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
import com.github.retrooper.packetevents.protocol.chat.ChatPosition;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
    public static boolean HANDLE_JSON = true;
    private String chatComponentJson;
    private Component chatComponent;
    private ChatPosition position;
    private UUID senderUUID;

    public WrapperPlayServerChatMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChatMessage(Component chatComponent, ChatPosition position) {
        this(chatComponent, position, new UUID(0L, 0L));
    }

    public WrapperPlayServerChatMessage(Component chatComponent, ChatPosition position, UUID senderUUID) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.chatComponent = chatComponent;
        this.position = position;
        this.senderUUID = senderUUID;
    }


    public WrapperPlayServerChatMessage(String chatComponentJson, ChatPosition position) {
        this(chatComponentJson, position, new UUID(0L, 0L));
    }

    public WrapperPlayServerChatMessage(String chatComponentJson, ChatPosition position, UUID senderUUID) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.chatComponentJson = chatComponentJson;
        this.position = position;
        this.senderUUID = senderUUID;
    }

    @Override
    public void read() {
        this.chatComponentJson = readString(getMaxMessageLength());

        //Parse JSON message
        if (HANDLE_JSON) {
            chatComponent = AdventureSerializer.parseComponent(this.chatComponentJson);
        }

        //Is the server 1.8+ or is the client 1.8+? 1.7.10 servers support 1.8 clients, and send the chat position.
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
            byte positionIndex = readByte();
            position = ChatPosition.getById(positionIndex);
        } else {
            //Always chat in 1.7.10 protocol.
            position = ChatPosition.CHAT;
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            this.senderUUID = readUUID();
        } else {
            this.senderUUID = new UUID(0L, 0L);
        }
    }

    @Override
    public void copy(WrapperPlayServerChatMessage wrapper) {
        this.chatComponentJson = wrapper.chatComponentJson;
        this.chatComponent = wrapper.chatComponent;
        this.position = wrapper.position;
        this.senderUUID = wrapper.senderUUID;
    }

    @Override
    public void write() {
        if (HANDLE_JSON) {
            chatComponentJson = AdventureSerializer.toJson(chatComponent);
        }
        writeString(chatComponentJson, getMaxMessageLength());

        //Is the server 1.8+ or is the client 1.8+? (1.7.10 servers support 1.8 clients, and send the chat position for 1.8 clients)
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
            writeByte(position.getId());
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            writeUUID(senderUUID);
        }
    }

    public Component getChatComponent() {
        return chatComponent;
    }

    public void setChatComponent(Component chatComponent) {
        this.chatComponent = chatComponent;
    }

    public String getChatComponentJson() {
        return chatComponentJson;
    }

    public void setChatComponentJson(String chatComponentJson) {
        this.chatComponentJson = chatComponentJson;
    }

    public ChatPosition getPosition() {
        return position;
    }

    public void setPosition(ChatPosition position) {
        this.position = position;
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(UUID senderUUID) {
        this.senderUUID = senderUUID;
    }
}
