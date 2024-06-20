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

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.incomingDirectMessage;
import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.outgoingDirectMessage;
import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.teamMessage;
import static com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration.withSender;

public class ChatTypes {

    private static final Map<String, ChatType> CHAT_TYPE_MAP = new HashMap<>();
    //Key - mappings version, value - map with chat type ids and chat types
    private static final Map<Byte, Map<Integer, ChatType>> CHAT_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("chat/chat_type_mappings");

    public static ChatType define(String key) {
        return define(key, withSender("chat.type.text"));
    }

    public static ChatType define(String key, ChatTypeDecoration chatDeco) {
        return define(key, chatDeco, withSender("chat.type.text.narrate"));
    }

    public static ChatType define(String key, ChatTypeDecoration chatDeco, ChatTypeDecoration narrationDeco) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        ChatType chatType = new ChatType() {
            @Override
            public ChatTypeDecoration getChatDecoration() {
                return chatDeco;
            }

            @Override
            public ChatTypeDecoration getNarrationDecoration() {
                return narrationDeco;
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                return MappingHelper.getId(version, TYPES_BUILDER, data);
            }
        };
        MappingHelper.registerMapping(TYPES_BUILDER, CHAT_TYPE_MAP, CHAT_TYPE_ID_MAP, chatType);
        return chatType;
    }

    //with minecraft:key
    public static ChatType getByName(String name) {
        return CHAT_TYPE_MAP.get(name);
    }

    public static ChatType getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        return CHAT_TYPE_ID_MAP.get((byte) index).get(id);
    }

    public static final ChatType CHAT = define("chat");
    public static final ChatType SAY_COMMAND = define("say_command",
            withSender("chat.type.announcement"));
    public static final ChatType MSG_COMMAND_INCOMING = define("msg_command_incoming",
            incomingDirectMessage("commands.message.display.incoming"));
    public static final ChatType MSG_COMMAND_OUTGOING = define("msg_command_outgoing",
            outgoingDirectMessage("commands.message.display.outgoing"));
    public static final ChatType TEAM_MSG_COMMAND_INCOMING = define("team_msg_command_incoming",
            teamMessage("chat.type.team.text"));
    public static final ChatType TEAM_MSG_COMMAND_OUTGOING = define("team_msg_command_outgoing",
            teamMessage("chat.type.team.sent"));
    public static final ChatType EMOTE_COMMAND = define("emote_command",
            withSender("chat.type.emote"),
            withSender("chat.type.emote"));

    // added by CraftBukkit for a few versions
    public static final ChatType RAW = define("raw");

    @Deprecated
    public static final ChatType SYSTEM = define("system");
    @Deprecated
    public static final ChatType GAME_INFO = define("game_info");
    @Deprecated
    public static final ChatType MSG_COMMAND = define("msg_command");
    @Deprecated
    public static final ChatType TEAM_MSG_COMMAND = define("team_msg_command");

    /**
     * Returns an immutable view of the chat types.
     * @return Chat Types
     */
    public static Collection<ChatType> values() {
        return Collections.unmodifiableCollection(CHAT_TYPE_MAP.values());
    }

    static {
        TYPES_BUILDER.unloadFileMappings();
    }
}
