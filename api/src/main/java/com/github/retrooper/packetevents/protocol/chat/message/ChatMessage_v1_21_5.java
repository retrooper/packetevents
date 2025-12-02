/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

//We'll extend ChatMessage_v1_19_3 for now, hopefully no breaking changes in the future
public class ChatMessage_v1_21_5 extends ChatMessage_v1_19_3 {

    int globalIndex;

    public ChatMessage_v1_21_5(int globalIndex, UUID senderUUID, int index, byte[] signature, String plainContent,
                               Instant timestamp, long salt, LastSeenMessages.Packed lastSeenMessagesPacked, @Nullable Component unsignedChatContent,
                               FilterMask filterMask, ChatType.Bound chatFormatting) {
        super(senderUUID, index, signature, plainContent, timestamp, salt, lastSeenMessagesPacked, unsignedChatContent, filterMask, chatFormatting);
        this.globalIndex = globalIndex;
    }

    public int getGlobalIndex() {
        return this.globalIndex;
    }

    public void setGlobalIndex(int globalIndex) {
        this.globalIndex = globalIndex;
    }
}
