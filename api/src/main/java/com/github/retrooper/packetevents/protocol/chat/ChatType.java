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

package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_1;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface ChatType extends MappedEntity, CopyableEntity<ChatType> {

    ChatTypeDecoration getChatDecoration();

    @ApiStatus.Obsolete(since = "1.19.1")
    ChatTypeDecoration getOverlayDecoration();

    ChatTypeDecoration getNarrationDecoration();

    @ApiStatus.Obsolete(since = "1.19.1")
    NarrationPriority getNarrationPriority();

    static ChatType readDirect(PacketWrapper<?> wrapper) {
        ChatTypeDecoration chatDecoration = ChatTypeDecoration.read(wrapper);
        ChatTypeDecoration narrationDecoration = ChatTypeDecoration.read(wrapper);
        return new StaticChatType(chatDecoration, narrationDecoration);
    }

    static void writeDirect(PacketWrapper<?> wrapper, ChatType chatType) {
        ChatTypeDecoration.write(wrapper, chatType.getChatDecoration());
        ChatTypeDecoration.write(wrapper, chatType.getNarrationDecoration());
    }

    static ChatType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        NBTCompound chatTag = compound.getCompoundTagOrThrow("chat");
        NBTCompound narrationTag = compound.getCompoundTagOrThrow("narration");

        ChatTypeDecoration overlay = null;
        NarrationPriority narrationPriority = null;
        if (version.isOlderThan(ClientVersion.V_1_19_1)) {
            overlay = ChatTypeDecoration.decode(compound.getCompoundTagOrThrow("overlay"), version);
            narrationPriority = NarrationPriority.ID_INDEX.valueOrThrow(
                    narrationTag.getStringTagValueOrThrow("priority"));
            chatTag = chatTag.getCompoundTagOrThrow("description");
            narrationTag = narrationTag.getCompoundTagOrThrow("description");
        }

        ChatTypeDecoration chat = ChatTypeDecoration.decode(chatTag, version);
        ChatTypeDecoration narration = ChatTypeDecoration.decode(narrationTag, version);
        return new StaticChatType(data, chat, overlay, narration, narrationPriority);
    }

    static NBT encode(ChatType chatType, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        NBT chatTag = ChatTypeDecoration.encode(chatType.getChatDecoration(), version);
        NBT narrationTag = ChatTypeDecoration.encode(chatType.getNarrationDecoration(), version);

        if (version.isOlderThan(ClientVersion.V_1_19_1)) {
            if (chatType.getOverlayDecoration() != null) {
                compound.setTag("overlay",
                        ChatTypeDecoration.encode(chatType.getOverlayDecoration(), version));
            }
            NBTCompound narrationCompound = new NBTCompound();
            narrationCompound.setTag("description", narrationTag);
            if (chatType.getNarrationPriority() != null) {
                narrationCompound.setTag("priority", new NBTString(chatType.getNarrationPriority().getId()));
            }
            narrationTag = narrationCompound;
            NBTCompound chatCompound = new NBTCompound();
            chatCompound.setTag("description", chatTag);
            chatTag = chatCompound;
        }

        compound.setTag("chat", chatTag);
        compound.setTag("narration", narrationTag);
        return compound;
    }

    class Bound extends ChatMessage_v1_19_1.ChatTypeBoundNetwork {
        public Bound(ChatType type, Component name, @Nullable Component targetName) {
            super(type, name, targetName);
        }
    }

    @ApiStatus.Obsolete(since = "1.19.1")
    enum NarrationPriority {

        CHAT("chat"),
        SYSTEM("system");

        public static final Index<String, NarrationPriority> ID_INDEX = Index.create(
                NarrationPriority.class, NarrationPriority::getId);

        private final String id;

        NarrationPriority(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }
}
