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
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.util.UUIDUtil;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
    public static boolean HANDLE_JSON = true;
    private MessageSender sender;
    private String messageJson;
    private Component message;
    private @Nullable String unsignedMessageJson;
    private Optional<Component> unsignedMessage = Optional.empty();

    private ChatType type;
    private Optional<Instant> timestamp = Optional.empty();
    private Optional<SaltSignature> saltSignature = Optional.empty();

    public WrapperPlayServerChatMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChatMessage(ChatType type, Component message) {
        this(new MessageSender(null, null), type, message);
    }

    public WrapperPlayServerChatMessage(MessageSender sender, ChatType type, Component message) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.sender = sender;
        this.type = type;
        this.message = message;
    }

    public WrapperPlayServerChatMessage(ChatType type, String messageJson) {
        this(new MessageSender(null, null), type, messageJson);
    }

    public WrapperPlayServerChatMessage(MessageSender sender, ChatType type, String messageJson) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.sender = sender;
        this.type = type;
        this.messageJson = messageJson;
    }

    public WrapperPlayServerChatMessage(MessageSender sender, ChatType type, Component signedMessage, @Nullable Component unsignedMessage, @Nullable Instant timestamp, @Nullable SaltSignature saltSignature) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.sender = sender;
        this.type = type;
        this.message = signedMessage;
        this.unsignedMessage = Optional.ofNullable(unsignedMessage);
        this.timestamp = Optional.ofNullable(timestamp);
        this.saltSignature = Optional.ofNullable(saltSignature);
    }

    public WrapperPlayServerChatMessage(MessageSender sender, ChatType type, String signedMessageJson, @Nullable String unsignedMessageJson, @Nullable Instant timestamp, @Nullable SaltSignature saltSignature) {
        super(PacketType.Play.Server.CHAT_MESSAGE);
        this.sender = sender;
        this.type = type;
        this.messageJson = signedMessageJson;
        this.unsignedMessageJson = unsignedMessageJson;
        this.timestamp = Optional.ofNullable(timestamp);
        this.saltSignature = Optional.ofNullable(saltSignature);
    }

    @Override
    public void read() {
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        this.messageJson = readString(getMaxMessageLength());
        if (v1_19 && readBoolean()) {
            this.unsignedMessageJson = readString(getMaxMessageLength());
        }
        else {
            this.unsignedMessageJson = null;
        }
        //Parse JSON message
        if (HANDLE_JSON) {
            message = AdventureSerializer.parseComponent(this.messageJson);
            if (unsignedMessageJson != null) {
                unsignedMessage = Optional.of(AdventureSerializer.parseComponent(this.unsignedMessageJson));
            }
        }

        //Is the server 1.8+ or is the client 1.8+? 1.7.10 servers support 1.8 clients, and send the chat position.
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
            int positionIndex;
            if (v1_19) {
                positionIndex = readVarInt();
            } else {
                positionIndex = readByte();
            }
            type = ChatType.getById(positionIndex);
        } else {
            //Always chat in 1.7.10 protocol.
            type = ChatType.CHAT;
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            //Read sender data.
            this.sender = new MessageSender(readUUID(), null, null);
            if (v1_19) {
                this.sender.setDisplayName(readComponent());
                if (readBoolean()) {
                    this.sender.setTeamName(readComponent());
                }

                //Read timestamp
                timestamp = Optional.of(readTimestamp());

                saltSignature = Optional.of(readSaltSignature());
            }
        } else {
            this.sender = new MessageSender(null, null);
        }
    }

    @Override
    public void copy(WrapperPlayServerChatMessage wrapper) {
        this.messageJson = wrapper.messageJson;
        this.message = wrapper.message;
        this.type = wrapper.type;
        this.sender = wrapper.sender; 
    }

    @Override
    public void write() {
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        if (HANDLE_JSON) {
            if (message != null) {
                messageJson = AdventureSerializer.toJson(message);
            }
            unsignedMessage.ifPresent(component -> unsignedMessageJson = AdventureSerializer.toJson(component));
        }
        writeString(messageJson, getMaxMessageLength());
        if (v1_19) {
            writeBoolean(unsignedMessageJson != null);
            if (unsignedMessageJson != null) {
                writeString(unsignedMessageJson, getMaxMessageLength());
            }
        }

        //Is the server 1.8+ or is the client 1.8+? (1.7.10 servers support 1.8 clients, and send the chat position for 1.8 clients)
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) || clientVersion.isNewerThanOrEquals(ClientVersion.V_1_8)) {
            if (v1_19) {
                writeVarInt(type.getId());
            } else {
                writeByte(type.getId());
            }
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            //UUID never null, if unspecified, it should be 0L, 0L
            UUID uuid = sender.getUUID();
            if (uuid == null) {
                uuid = UUIDUtil.DUMMY;
            }
            writeUUID(uuid);
            if (v1_19) {
                writeComponent(this.sender.getDisplayName());
                writeBoolean(this.sender.getTeamName() != null);
                if (this.sender.getTeamName() != null) {
                    writeComponent(this.sender.getTeamName());
                }
                writeTimestamp(this.timestamp.get());
                writeSaltSignature(this.saltSignature.get());
            }
        }
    }

    public MessageSender getSender() {
        return sender;
    }

    public void setSender(MessageSender sender) {
        this.sender = sender;
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component chatComponent) {
        this.message = chatComponent;
    }

    public String getMessageJson() {
        return messageJson;
    }

    public void setMessageJson(String chatComponentJson) {
        this.messageJson = chatComponentJson;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }


}
