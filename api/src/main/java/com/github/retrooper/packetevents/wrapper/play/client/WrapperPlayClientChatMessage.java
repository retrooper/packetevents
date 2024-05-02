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
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

/**
 * This packet is used to send a chat message to the server.
 */
public class WrapperPlayClientChatMessage extends PacketWrapper<WrapperPlayClientChatMessage> {
    private String message;
    private MessageSignData messageSignData;
    private @Nullable LastSeenMessages.Update lastSeenMessages;

    public WrapperPlayClientChatMessage(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatMessage(String message, MessageSignData messageSignData) {
        super(PacketType.Play.Client.CHAT_MESSAGE);
        this.message = message;
        this.messageSignData = messageSignData;
    }

    public WrapperPlayClientChatMessage(String message, MessageSignData messageSignData, @Nullable LastSeenMessages.Update lastSeenMessages) {
        super(PacketType.Play.Client.CHAT_MESSAGE);
        this.message = message;
        this.messageSignData = messageSignData;
        this.lastSeenMessages = lastSeenMessages;
    }

    @Override
    public void read() {
        int maxMessageLength = 256;
        this.message = readString(maxMessageLength);
        Instant timestamp = readTimestamp();
        this.messageSignData = new MessageSignData(readSaltSignature(), timestamp);
        this.lastSeenMessages = readLastSeenMessagesUpdate();
    }

    @Override
    public void write() {
        int maxMessageLength = 256;
        writeString(this.message, maxMessageLength);
        writeTimestamp(messageSignData.getTimestamp());
        writeSaltSignature(messageSignData.getSaltSignature());
        if (lastSeenMessages != null)
            writeLastSeenMessagesUpdate(lastSeenMessages);
    }

    @Override
    public void copy(WrapperPlayClientChatMessage wrapper) {
        this.message = wrapper.message;
        this.messageSignData = wrapper.messageSignData;
        this.lastSeenMessages = wrapper.lastSeenMessages;
    }

    /**
     * The message.
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
        return Optional.ofNullable(messageSignData);
    }

    public void setMessageSignData(@Nullable MessageSignData messageSignData) {
        this.messageSignData = messageSignData;
    }

    public @Nullable LastSeenMessages.Update getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(LastSeenMessages.@Nullable Update lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }
}
