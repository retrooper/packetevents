/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.Nullable;

public class StaticChatType extends AbstractMappedEntity implements ChatType {

    private final ChatTypeDecoration chatDecoration;
    private final ChatTypeDecoration narrationDecoration;

    public StaticChatType(
            ChatTypeDecoration chatDecoration,
            ChatTypeDecoration narrationDecoration
    ) {
        this(null, chatDecoration, narrationDecoration);
    }

    public StaticChatType(
            @Nullable TypesBuilderData data,
            ChatTypeDecoration chatDecoration,
            ChatTypeDecoration narrationDecoration
    ) {
        super(data);
        this.chatDecoration = chatDecoration;
        this.narrationDecoration = narrationDecoration;
    }

    @Override
    public ChatType copy(@Nullable TypesBuilderData newData) {
        return new StaticChatType(newData, this.chatDecoration, this.narrationDecoration);
    }

    @Override
    public ChatTypeDecoration getChatDecoration() {
        return this.chatDecoration;
    }

    @Override
    public ChatTypeDecoration getNarrationDecoration() {
        return this.narrationDecoration;
    }
}
