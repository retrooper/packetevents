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

package io.github.retrooper.packetevents.packettype;

import io.github.retrooper.packetevents.packettype.protocols.PacketType_1_7_10;
import io.github.retrooper.packetevents.packettype.protocols.PacketType_1_8;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public final class PacketType {
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
            WINDOW_CONFIRMATION,
            CREATIVE_INVENTORY_ACTION,
            CLICK_WINDOW_BUTTON,
            UPDATE_SIGN,
            PLAYER_ABILITIES,
            TAB_COMPLETE,
            CLIENT_SETTINGS,
            CLIENT_STATUS,
            PLUGIN_MESSAGE,
            SPECTATE,
            RESOURCE_PACK_STATUS;

            public int packetID = -1;
            private static final Map<ClientVersion, Map<Integer, Enum<?>>> PACKET_ID_CACHE = new IdentityHashMap<>();

            Client() {
            }

            public boolean isSupported() {
                return packetID != -1;
            }

            @Nullable
            public static Client getById(ClientVersion version, int packetID) {
                Map<Integer, Enum<?>> innerMap = PACKET_ID_CACHE.get(version);
                if (innerMap != null) {
                    Object client = innerMap.get(packetID);
                    if (client instanceof Client) {
                        return (Client) client;
                    }
                }
                return null;
            }

            private static void loadPacketIDs(ClientVersion version, Enum<?>[] enumConstants) {
                Map<Integer, Enum<?>> innerMap = new IdentityHashMap<>();
                for (int i = 0; i < enumConstants.length; i++) {
                    Client.valueOf(enumConstants[i].name()).packetID = i;
                    innerMap.put(i, Client.valueOf(enumConstants[i].name()));
                }
                PACKET_ID_CACHE.put(version, innerMap);
            }

            public static void load() {
                loadPacketIDs(ClientVersion.v_1_7_10, PacketType_1_7_10.Play.Client.values());
                loadPacketIDs(ClientVersion.v_1_8, PacketType_1_8.Play.Client.values());
            }
        }
    }
}
