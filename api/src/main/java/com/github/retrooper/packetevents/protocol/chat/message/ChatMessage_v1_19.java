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

package com.github.retrooper.packetevents.protocol.chat.message;

import com.github.retrooper.packetevents.protocol.chat.ChatType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;
//We'll extend ChatMessage_v1_16 for now, hopefully no breaking changes in the future
public class ChatMessage_v1_19 extends ChatMessage_v1_16 {
    private @Nullable Component unsignedChatContent;
    private Component senderDisplayName;
    private @Nullable Component teamName;
    private Instant timestamp;
    private long salt;
    private byte[] signature;

    public ChatMessage_v1_19(Component chatContent, @Nullable Component unsignedChatContent, ChatType type,
                             UUID senderUUID, Component senderDisplayName, @Nullable Component teamName, Instant timestamp,
                             long salt, byte[] signature) {
        super(chatContent, type, senderUUID);
        this.unsignedChatContent = unsignedChatContent;
        this.senderDisplayName = senderDisplayName;
        this.teamName = teamName;
        this.timestamp = timestamp;
        this.salt = salt;
        this.signature = signature;
    }

    public @Nullable Component getUnsignedChatContent() {
        return unsignedChatContent;
    }

    public Component getSenderDisplayName() {
        return senderDisplayName;
    }

    public @Nullable Component getTeamName() {
        return teamName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public long getSalt() {
        return salt;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setUnsignedChatContent(@Nullable Component unsignedChatContent) {
        this.unsignedChatContent = unsignedChatContent;
    }

    public void setSenderDisplayName(Component senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
    }

    public void setTeamName(@Nullable Component teamName) {
        this.teamName = teamName;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setSalt(long salt) {
        this.salt = salt;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
