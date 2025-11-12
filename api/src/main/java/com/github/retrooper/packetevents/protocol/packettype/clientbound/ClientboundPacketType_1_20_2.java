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

package com.github.retrooper.packetevents.protocol.packettype.clientbound;

public enum ClientboundPacketType_1_20_2 {

    BUNDLE,
    SPAWN_ENTITY,
    SPAWN_EXPERIENCE_ORB,
    ENTITY_ANIMATION,
    STATISTICS,
    ACKNOWLEDGE_BLOCK_CHANGES,
    BLOCK_BREAK_ANIMATION,
    BLOCK_ENTITY_DATA,
    BLOCK_ACTION,
    BLOCK_CHANGE,
    BOSS_BAR,
    SERVER_DIFFICULTY,
    CHUNK_BATCH_END, // New packet
    CHUNK_BATCH_BEGIN, // New packet
    CHUNK_BIOMES,
    CLEAR_TITLES,
    TAB_COMPLETE,
    DECLARE_COMMANDS,
    CLOSE_WINDOW,
    WINDOW_ITEMS,
    WINDOW_PROPERTY,
    SET_SLOT,
    SET_COOLDOWN,
    CUSTOM_CHAT_COMPLETIONS,
    PLUGIN_MESSAGE,
    DAMAGE_EVENT,
    DELETE_CHAT,
    DISCONNECT,
    DISGUISED_CHAT,
    ENTITY_STATUS,
    EXPLOSION,
    UNLOAD_CHUNK,
    CHANGE_GAME_STATE,
    OPEN_HORSE_WINDOW,
    HURT_ANIMATION,
    INITIALIZE_WORLD_BORDER,
    KEEP_ALIVE,
    CHUNK_DATA,
    EFFECT,
    PARTICLE,
    UPDATE_LIGHT,
    JOIN_GAME,
    MAP_DATA,
    MERCHANT_OFFERS,
    ENTITY_RELATIVE_MOVE,
    ENTITY_RELATIVE_MOVE_AND_ROTATION,
    ENTITY_ROTATION,
    VEHICLE_MOVE,
    OPEN_BOOK,
    OPEN_WINDOW,
    OPEN_SIGN_EDITOR,
    PING,
    DEBUG_PONG, // New packet
    CRAFT_RECIPE_RESPONSE,
    PLAYER_ABILITIES,
    CHAT_MESSAGE,
    END_COMBAT_EVENT,
    ENTER_COMBAT_EVENT,
    DEATH_COMBAT_EVENT,
    PLAYER_INFO_REMOVE,
    PLAYER_INFO_UPDATE,
    FACE_PLAYER,
    PLAYER_POSITION_AND_LOOK,
    UNLOCK_RECIPES,
    DESTROY_ENTITIES,
    REMOVE_ENTITY_EFFECT,
    RESOURCE_PACK_SEND,
    RESPAWN,
    ENTITY_HEAD_LOOK,
    MULTI_BLOCK_CHANGE,
    SELECT_ADVANCEMENTS_TAB,
    SERVER_DATA,
    ACTION_BAR,
    WORLD_BORDER_CENTER,
    WORLD_BORDER_LERP_SIZE,
    WORLD_BORDER_SIZE,
    WORLD_BORDER_WARNING_DELAY,
    WORLD_BORDER_WARNING_REACH,
    CAMERA,
    HELD_ITEM_CHANGE,
    UPDATE_VIEW_POSITION,
    UPDATE_VIEW_DISTANCE,
    SPAWN_POSITION,
    DISPLAY_SCOREBOARD,
    ENTITY_METADATA,
    ATTACH_ENTITY,
    ENTITY_VELOCITY,
    ENTITY_EQUIPMENT,
    SET_EXPERIENCE,
    UPDATE_HEALTH,
    SCOREBOARD_OBJECTIVE,
    SET_PASSENGERS,
    TEAMS,
    UPDATE_SCORE,
    UPDATE_SIMULATION_DISTANCE,
    SET_TITLE_SUBTITLE,
    TIME_UPDATE,
    SET_TITLE_TEXT,
    SET_TITLE_TIMES,
    ENTITY_SOUND_EFFECT,
    SOUND_EFFECT,
    CONFIGURATION_START, // New packet
    STOP_SOUND,
    SYSTEM_CHAT_MESSAGE,
    PLAYER_LIST_HEADER_AND_FOOTER,
    NBT_QUERY_RESPONSE,
    COLLECT_ITEM,
    ENTITY_TELEPORT,
    UPDATE_ADVANCEMENTS,
    UPDATE_ATTRIBUTES,
    // UPDATE_ENABLED_FEATURES, removed
    ENTITY_EFFECT,
    DECLARE_RECIPES,
    TAGS,
}
