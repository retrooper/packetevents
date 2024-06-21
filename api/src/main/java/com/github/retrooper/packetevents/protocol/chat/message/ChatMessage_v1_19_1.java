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
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;
//We'll extend ChatMessage_v1_16 for now, hopefully no breaking changes in the future
public class ChatMessage_v1_19_1 extends ChatMessage_v1_16 {
    private String plainContent;
    private @Nullable Component unsignedChatContent;
    private ChatType.Bound chatFormatting;
    private byte @Nullable [] previousSignature;
    private byte[] signature;
    private Instant timestamp;
    private long salt;
    private LastSeenMessages lastSeenMessages;
    private FilterMask filterMask;

    public ChatMessage_v1_19_1(String plainContent, Component decoratedChatContent,
                               @Nullable Component unsignedChatContent,
                               UUID senderUUID, ChatType.Bound chatFormatting,
                               byte @Nullable [] previousSignature, byte[] signature,
                               Instant timestamp, long salt,
                               LastSeenMessages lastSeenMessages,
                               FilterMask filterMask) {
        super(decoratedChatContent, chatFormatting.getType(), senderUUID);
        this.plainContent = plainContent;
        this.unsignedChatContent = unsignedChatContent;
        this.chatFormatting = chatFormatting;
        this.previousSignature = previousSignature;
        this.signature = signature;
        this.timestamp = timestamp;
        this.salt = salt;
        this.lastSeenMessages = lastSeenMessages;
        this.filterMask = filterMask;
    }

    public String getPlainContent() {
        return plainContent;
    }

    public void setPlainContent(String plainContent) {
        this.plainContent = plainContent;
    }

    public boolean isChatContentDecorated() {
        return !getChatContent().equals(Component.text(plainContent));
    }

    public @Nullable Component getUnsignedChatContent() {
        return unsignedChatContent;
    }

    public void setUnsignedChatContent(@Nullable Component unsignedChatContent) {
        this.unsignedChatContent = unsignedChatContent;
    }

    @Override
    public ChatType getType() {
        return chatFormatting.getType();
    }

    @Override
    public void setType(ChatType type) {
        chatFormatting.setType(type);
    }

    public ChatType.Bound getChatFormatting() {
        return chatFormatting;
    }

    public void setChatFormatting(ChatType.Bound chatFormatting) {
        this.chatFormatting = chatFormatting;
    }

    /**
     * Retrieves the chat formatting. Please refer to {@link ChatMessage_v1_19_1#getChatFormatting}
     * @return chat formatting which contains the actual chat type.
     */
    @Deprecated
    public ChatType.Bound getChatType() {
        return chatFormatting;
    }

    /**
     * Sets the chat formatting. Please refer to {@link ChatMessage_v1_19_1#setChatFormatting}
     * @param chatFormatting formatting which contains the actual chat type.
     */
    @Deprecated
    public void setChatType(ChatType.Bound chatFormatting) {
        this.chatFormatting = chatFormatting;
    }

    public byte @Nullable [] getPreviousSignature() {
        return previousSignature;
    }

    public void setPreviousSignature(byte @Nullable [] previousSignature) {
        this.previousSignature = previousSignature;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public long getSalt() {
        return salt;
    }

    public void setSalt(long salt) {
        this.salt = salt;
    }

    public LastSeenMessages getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(LastSeenMessages lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }

    public FilterMask getFilterMask() {
        return filterMask;
    }

    public void setFilterMask(FilterMask filterMask) {
        this.filterMask = filterMask;
    }

    /**
     * Please refer to {@link com.github.retrooper.packetevents.protocol.chat.ChatType.Bound}
     */
    @Deprecated
    public static class ChatTypeBoundNetwork {
        private ChatType type;
        private Component name;
        private @Nullable Component targetName;

        public ChatTypeBoundNetwork(ChatType type, Component name, @Nullable Component targetName) {
            this.type = type;
            this.name = name;
            this.targetName = targetName;
        }

        public ChatType getType() {
            return type;
        }

        public void setType(ChatType type) {
            this.type = type;
        }

        public Component getName() {
            return name;
        }

        public void setName(Component name) {
            this.name = name;
        }

        public @Nullable Component getTargetName() {
            return targetName;
        }

        public void setTargetName(@Nullable Component targetName) {
            this.targetName = targetName;
        }
    }
}
