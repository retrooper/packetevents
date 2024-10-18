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

package com.github.retrooper.packetevents.protocol.packettype.serverbound;

public enum ServerboundPacketType_1_21_2 {

    TELEPORT_CONFIRM,
    QUERY_BLOCK_NBT,
    SELECT_BUNDLE_ITEM, // new packet
    SET_DIFFICULTY,
    CHAT_ACK,
    CHAT_COMMAND_UNSIGNED,
    CHAT_COMMAND,
    CHAT_MESSAGE,
    CHAT_SESSION_UPDATE,
    CHUNK_BATCH_ACK,
    CLIENT_STATUS,
    CLIENT_TICK_END, // new packet
    CLIENT_SETTINGS,
    TAB_COMPLETE,
    CONFIGURATION_ACK,
    CLICK_WINDOW_BUTTON,
    CLICK_WINDOW,
    CLOSE_WINDOW,
    SLOT_STATE_CHANGE,
    COOKIE_RESPONSE,
    PLUGIN_MESSAGE,
    DEBUG_SAMPLE_SUBSCRIPTION,
    EDIT_BOOK,
    QUERY_ENTITY_NBT,
    INTERACT_ENTITY,
    GENERATE_STRUCTURE,
    KEEP_ALIVE,
    LOCK_DIFFICULTY,
    PLAYER_POSITION,
    PLAYER_POSITION_AND_ROTATION,
    PLAYER_ROTATION,
    PLAYER_FLYING,
    VEHICLE_MOVE,
    STEER_BOAT,
    PICK_ITEM,
    DEBUG_PING,
    CRAFT_RECIPE_REQUEST,
    PLAYER_ABILITIES,
    PLAYER_DIGGING,
    ENTITY_ACTION,
    STEER_VEHICLE,
    PONG,
    SET_RECIPE_BOOK_STATE,
    SET_DISPLAYED_RECIPE,
    NAME_ITEM,
    RESOURCE_PACK_STATUS,
    ADVANCEMENT_TAB,
    SELECT_TRADE,
    SET_BEACON_EFFECT,
    HELD_ITEM_CHANGE,
    UPDATE_COMMAND_BLOCK,
    UPDATE_COMMAND_BLOCK_MINECART,
    CREATIVE_INVENTORY_ACTION,
    UPDATE_JIGSAW_BLOCK,
    UPDATE_STRUCTURE_BLOCK,
    UPDATE_SIGN,
    ANIMATION,
    SPECTATE,
    PLAYER_BLOCK_PLACEMENT,
    USE_ITEM,
}
