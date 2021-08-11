/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packettype.protocols;

public class PacketType_1_8 {
    public static class Play {
        public enum Client {
            KEEP_ALIVE,
            CHAT_MESSAGE,
            INTERACT_ENTITY,
            PLAYER_MOVEMENT,
            PLAYER_POSITION,
            PLAYER_ROTATION,
            PLAYER_POSITION_AND_ROTATION,
            PLAYER_DIGGING,
            PLAYER_BLOCK_PLACEMENT,
            HELD_ITEM_CHANGE,
            ANIMATION,
            ENTITY_ACTION,
            STEER_VEHICLE,
            CLOSE_WINDOW,
            CLICK_WINDOW,
            CONFIRM_TRANSACTION,
            CREATIVE_INVENTORY_ACTION,
            CLICK_WINDOW_BUTTON,
            UPDATE_SIGN,
            PLAYER_ABILITIES,
            TAB_COMPLETE,
            CLIENT_SETTINGS,
            CLIENT_STATUS,
            PLUGIN_MESSAGE,
            SPECTATE,
            RESOURCE_PACK_STATUS
        }
    }
}
