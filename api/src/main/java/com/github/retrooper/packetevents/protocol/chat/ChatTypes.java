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
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;

import java.util.HashMap;
import java.util.Map;

public class ChatTypes {
    private static final Map<String, ChatType> CHAT_TYPE_MAP = new HashMap<>();
    //Key - mappings version, value - map with chat type ids and chat types
    private static final Map<Byte, Map<Integer, ChatType>> CHAT_TYPE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("chat/chat_type_mappings",
            ClientVersion.V_1_18_2,
            ClientVersion.V_1_19,
            ClientVersion.V_1_19_1);

    static {
        TYPES_BUILDER.unloadFileMappings();
    }

    public static ChatType define(String key) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        ChatType chatType = new ChatType() {
            private final int[] ids = data.getData();

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                int index = TYPES_BUILDER.getDataIndex(version);
                return ids[index];
            }
        };
        CHAT_TYPE_MAP.put(chatType.getName().toString(), chatType);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            int index = TYPES_BUILDER.getDataIndex(version);
            Map<Integer, ChatType> typeIdMap = CHAT_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
            typeIdMap.put(chatType.getId(version), chatType);
        }
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
    public static final ChatType SAY_COMMAND = define("say_command");
    public static final ChatType MSG_COMMAND_INCOMING = define("msg_command_incoming");
    public static final ChatType MSG_COMMAND_OUTGOING = define("msg_command_outgoing");
    public static final ChatType TEAM_MSG_COMMAND_INCOMING = define("team_msg_command_incoming");
    public static final ChatType TEAM_MSG_COMMAND_OUTGOING = define("team_msg_command_outgoing");
    public static final ChatType EMOTE_COMMAND = define("emote_command");
    public static final ChatType RAW = define("raw");

    @Deprecated
    public static final ChatType SYSTEM = define("system");
    @Deprecated
    public static final ChatType GAME_INFO = define("game_info");
    @Deprecated
    public static final ChatType MSG_COMMAND = define("msg_command");
    @Deprecated
    public static final ChatType TEAM_MSG_COMMAND = define("team_msg_command");
}
