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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.util.crypto.MessageVerifier;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * This packet is used to send a chat message to the server.
 */
public class WrapperPlayClientChatMessage extends PacketWrapper<WrapperPlayClientChatMessage> {
    private String message;
    private Optional<MessageSignData> messageSignData;

    public WrapperPlayClientChatMessage(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatMessage(String message, @Nullable MessageSignData messageSignData) {
        super(PacketType.Play.Client.CHAT_MESSAGE);
        this.message = message;
        this.messageSignData = Optional.ofNullable(messageSignData);
    }

    @Override
    public void read() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11) ? 256 : 100;
        this.message = readString(maxMessageLength);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            Instant timestamp = readTimestamp();
            SaltSignature saltSignature = readSaltSignature();
            boolean signedPreview = readBoolean();
            this.messageSignData = Optional.of(new MessageSignData(saltSignature, timestamp, signedPreview));
        }
        else {
            this.messageSignData = Optional.empty();
        }
    }

    @Override
    public void copy(WrapperPlayClientChatMessage wrapper) {
        this.message = wrapper.message;
        this.messageSignData = wrapper.messageSignData;
    }

    @Override
    public void write() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11) ? 256 : 100;
        writeString(this.message, maxMessageLength);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            writeTimestamp(messageSignData.get().getTimestamp());
            writeSaltSignature(messageSignData.get().getSaltSignature());
            writeBoolean(messageSignData.get().isSignedPreview());
        }
    }

    /**
     * The message.
     * On {@link ClientVersion#V_1_10} and older clients, the message should never exceed 100 characters.
     * On {@link ClientVersion#V_1_11} and newer clients, the message should never exceed 256 characters.
     *
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Modify the message.
     * On {@link ClientVersion#V_1_10} and older clients, the message should never exceed 100 characters.
     * On {@link ClientVersion#V_1_11} and newer clients, the message should never exceed 256 characters.
     *
     * @param message Message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public Optional<MessageSignData> getMessageSignData() {
        return messageSignData;
    }

    public void setMessageSignData(@Nullable MessageSignData messageSignData) {
        this.messageSignData = Optional.ofNullable(messageSignData);
    }

    protected boolean verify(UUID uuid, PublicKey key) {
        if (!messageSignData.isPresent()) {
            System.out.println("wait a minute!");
            return false;
        }
        Component component = Component.text(message);
        System.out.println("str: " + AdventureSerializer.toJson(component));
        try {
            return MessageVerifier.verify(uuid, messageSignData.get(), key, String.format("{\"text\":\"%s\"}", message));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }
}
