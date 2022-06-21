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
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.MessageSender;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
    private Component signedChatContent;
    private @Nullable Component unsignedChatContent;
    private ChatType type;
    private MessageSender sender;
    private @Nullable MessageSignData messageSignData;

    public WrapperPlayServerChatMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChatMessage(Component signedChatContent, ChatType type, MessageSender sender) {
        this(signedChatContent, null, type, sender, null);
    }

    public WrapperPlayServerChatMessage(Component signedChatContent, @Nullable Component unsignedChatContent, ChatType type,
                                        MessageSender sender, @Nullable MessageSignData messageSignData) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.signedChatContent = signedChatContent;
        this.unsignedChatContent = unsignedChatContent;
        this.type = type;
        this.sender = sender;
        this.messageSignData = messageSignData;
    }

    @Override
    public void read() {
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        signedChatContent = AdventureSerializer.parseComponent(readString(getMaxMessageLength()));
        if (v1_19) {
            unsignedChatContent = readOptional(reader -> {
                String json = readString(getMaxMessageLength());
                if (json != null) {
                    return AdventureSerializer.parseComponent(json);
                }
                return null;
            });
        }

        if (v1_19) {
            type = ChatType.getById(readVarInt());
        } else {
            type = ChatType.getById(readByte());
        }

        sender = new MessageSender(null, null);
        if (v1_19) {
            sender.setUUID(readUUID());
            sender.setDisplayName(readComponent());
            sender.setTeamName(readOptional(PacketWrapper::readComponent));
            Instant timestamp = readTimestamp();
            SaltSignature saltSignature = readSaltSignature();
            messageSignData = new MessageSignData(saltSignature, timestamp, true);
        }
    }

    @Override
    public void write() {
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        writeString(AdventureSerializer.toJson(signedChatContent), getMaxMessageLength());
        if (v1_19) {
            writeOptional(unsignedChatContent, (writer, innerComponent) -> {
                if (innerComponent != null) {
                    writeString(AdventureSerializer.toJson(innerComponent), getMaxMessageLength());
                }
            });
        }

        if (v1_19) {
            writeVarInt(type.getId());
        } else {
            writeByte(type.getId());
        }

        writeUUID(sender.getUUID());
        if (v1_19) {
            writeComponent(sender.getDisplayName());
            writeOptional(sender.getTeamName(), (writer, component) -> {
                if (component != null) {
                    writeComponent(component);
                }
            });
            if (messageSignData != null) {
                writeTimestamp(messageSignData.getTimestamp());
                writeSaltSignature(messageSignData.getSaltSignature());
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerChatMessage wrapper) {
        this.signedChatContent = wrapper.signedChatContent;
        this.unsignedChatContent = wrapper.unsignedChatContent;
        this.type = wrapper.type;
        this.sender = wrapper.sender;
        this.messageSignData = wrapper.messageSignData;
    }

    public Component getSignedChatContent() {
        return signedChatContent;
    }

    public void setSignedChatContent(Component signedChatContent) {
        this.signedChatContent = signedChatContent;
    }

    public Optional<Component> getUnsignedChatContent() {
        return Optional.ofNullable(unsignedChatContent);
    }

    public void setUnsignedChatContent(@Nullable Component unsignedChatContent) {
        this.unsignedChatContent = unsignedChatContent;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public MessageSender getSender() {
        return sender;
    }

    public void setSender(MessageSender sender) {
        this.sender = sender;
    }

    public Optional<MessageSignData> getMessageSignData() {
        return Optional.ofNullable(messageSignData);
    }

    public void setMessageSignData(@Nullable MessageSignData messageSignData) {
        this.messageSignData = messageSignData;
    }
}
