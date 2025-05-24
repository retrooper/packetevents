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
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
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
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

import static com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil.indexValueOrThrow;

public interface ChatType extends MappedEntity, CopyableEntity<ChatType>, DeepComparableEntity {

    @UnknownNullability("only nullable for 1.19")
    ChatTypeDecoration getChatDecoration();

    @ApiStatus.Obsolete(since = "1.19.1")
    @Nullable ChatTypeDecoration getOverlayDecoration();

    @UnknownNullability("only nullable for 1.19")
    ChatTypeDecoration getNarrationDecoration();

    @ApiStatus.Obsolete(since = "1.19.1")
    @Nullable NarrationPriority getNarrationPriority();

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
        // everything is nullable in 1.19, even the chat format!
        NBTCompound compound = (NBTCompound) nbt;
        NBTCompound chatTag = compound.getCompoundTagOrNull("chat");
        NBTCompound narrationTag = compound.getCompoundTagOrNull("narration");

        ChatTypeDecoration overlay = null;
        NarrationPriority narrationPriority = null;
        if (version.isOlderThan(ClientVersion.V_1_19_1)) {
            NBTCompound overlayTag = compound.getCompoundTagOrNull("overlay");
            if (overlayTag != null) {
                // why is this nullable? why is this wrapped? There is no reason to wrap this...
                overlayTag = overlayTag.getCompoundTagOrNull("description");
                if (overlayTag != null) {
                    overlay = ChatTypeDecoration.decode(overlayTag, version);
                }
            }
            if (chatTag != null) {
                chatTag = chatTag.getCompoundTagOrNull("description");
            }
            if (narrationTag != null) {
                // the priority is the ONLY value which isn't nullable!
                narrationPriority = indexValueOrThrow(NarrationPriority.ID_INDEX,
                        narrationTag.getStringTagValueOrThrow("priority"));
                narrationTag = narrationTag.getCompoundTagOrNull("description");
            }
        } else {
            // ensure this isn't null in everything but 1.19
            Objects.requireNonNull(chatTag, "NBT chat does not exist");
            Objects.requireNonNull(narrationTag, "NBT narration does not exist");
        }

        ChatTypeDecoration chat = chatTag == null ? null : ChatTypeDecoration.decode(chatTag, version);
        ChatTypeDecoration narration = narrationTag == null ? null : ChatTypeDecoration.decode(narrationTag, version);
        return new StaticChatType(data, chat, overlay, narration, narrationPriority);
    }

    static NBT encode(ChatType chatType, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        NBT chatTag = chatType.getChatDecoration() == null ? null :
                ChatTypeDecoration.encode(chatType.getChatDecoration(), version);
        NBT narrationTag = chatType.getNarrationDecoration() == null ? null :
                ChatTypeDecoration.encode(chatType.getNarrationDecoration(), version);

        if (version.isOlderThan(ClientVersion.V_1_19_1)) {
            ChatTypeDecoration overlayDeco = chatType.getOverlayDecoration();
            if (overlayDeco != null) {
                NBTCompound overlayCompound = new NBTCompound();
                overlayCompound.setTag("description",
                        ChatTypeDecoration.encode(overlayDeco, version));
                compound.setTag("overlay", overlayCompound);
            }
            if (narrationTag != null) {
                NBTCompound narrationCompound = new NBTCompound();
                narrationCompound.setTag("description", narrationTag);
                if (chatType.getNarrationPriority() != null) {
                    narrationCompound.setTag("priority", new NBTString(chatType.getNarrationPriority().getId()));
                }
                narrationTag = narrationCompound;
            }
            if (chatTag != null) {
                NBTCompound chatCompound = new NBTCompound();
                chatCompound.setTag("description", chatTag);
                chatTag = chatCompound;
            }
        }

        if (chatTag != null) {
            compound.setTag("chat", chatTag);
        }
        if (narrationTag != null) {
            compound.setTag("narration", narrationTag);
        }
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
