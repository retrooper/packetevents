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

package com.github.retrooper.packetevents.protocol.packettype;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.*;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public final class PacketType {
    public static PacketTypeCommon getById(PacketSide side, ConnectionState state, int protocolVersion, int packetID) {
        switch (state) {
            case HANDSHAKING:
                return Handshaking.Client.getById(packetID);
            case STATUS:
                if (side == PacketSide.CLIENT) {
                    return Status.Client.getById(packetID);
                } else {
                    return Status.Server.getById(packetID);
                }
            case LOGIN:
                if (side == PacketSide.CLIENT) {
                    return Login.Client.getById(packetID);
                } else {
                    return Login.Server.getById(packetID);
                }
            case PLAY:
                if (side == PacketSide.CLIENT) {
                    return Play.Client.getById(protocolVersion, packetID);
                } else {
                    return Play.Server.getById(packetID);
                }
            default:
                return null;
        }
    }

    public static PacketTypeCommon getById(PacketSide side, ConnectionState state, @Nullable ClientVersion version, int packetID) {
        int protocolVersion = version != null ? version.getProtocolVersion() : -1;
        return getById(side, state, protocolVersion, packetID);
    }

    public static PacketTypeCommon getById(PacketSide side, ConnectionState state, @Nullable ServerVersion version, int packetID) {
        int protocolVersion = version != null ? version.getProtocolVersion() : -1;
        return getById(side, state, protocolVersion, packetID);
    }

    public static class Handshaking {
        public enum Client implements PacketTypeCommon {
            HANDSHAKE(0),
            /**
             * Technically not part of the current protocol, but clients older than 1.7 will send this to initiate Server List Ping.
             * 1.8 and newer servers will handle it correctly though.
             */
            LEGACY_SERVER_LIST_PING(254);//0xFE in hex

            private final int id;

            Client(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return HANDSHAKE;
                } else if (packetID == 254) {
                    return LEGACY_SERVER_LIST_PING;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }
        }
    }

    public static class Status {
        public enum Client implements PacketTypeCommon {
            REQUEST(0),
            PING(1);

            private final int id;

            Client(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return REQUEST;
                } else if (packetID == 1) {
                    return PING;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }
        }

        public enum Server implements PacketTypeCommon {
            RESPONSE(0),
            PONG(1);

            private final int id;

            Server(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return RESPONSE;
                } else if (packetID == 1) {
                    return PONG;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }
        }
    }

    public static class Login {
        public enum Client implements PacketTypeCommon {
            LOGIN_START(0),
            ENCRYPTION_RESPONSE(1),
            ///Added in 1.13
            LOGIN_PLUGIN_RESPONSE(2);

            private final int id;

            Client(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return LOGIN_START;
                } else if (packetID == 1) {
                    return ENCRYPTION_RESPONSE;
                } else if (packetID == 2) {
                    return LOGIN_PLUGIN_RESPONSE;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }
        }

        public enum Server implements PacketTypeCommon {
            DISCONNECT(0),
            ENCRYPTION_REQUEST(1),
            LOGIN_SUCCESS(2),
            //Added in 1.8
            SET_COMPRESSION(3),
            ///Added in 1.13
            LOGIN_PLUGIN_REQUEST(4);

            private final int id;

            Server(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                switch (packetID) {
                    case 0:
                        return DISCONNECT;
                    case 1:
                        return ENCRYPTION_REQUEST;
                    case 2:
                        return LOGIN_SUCCESS;
                    case 3:
                        return SET_COMPRESSION;
                    case 4:
                        return LOGIN_PLUGIN_REQUEST;
                    default:
                        return null;
                }
            }

            public int getId() {
                return id;
            }
        }
    }

    public static class Play {
        public enum Client implements PacketTypeCommon {
            TELEPORT_CONFIRM,
            QUERY_BLOCK_NBT,
            SET_DIFFICULTY,
            CHAT_MESSAGE,
            CLIENT_STATUS,
            CLIENT_SETTINGS,
            TAB_COMPLETE,
            WINDOW_CONFIRMATION,
            CLICK_WINDOW_BUTTON,
            CLICK_WINDOW,
            CLOSE_WINDOW,
            PLUGIN_MESSAGE,
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
            CRAFT_RECIPE_REQUEST,
            PLAYER_ABILITIES,
            PLAYER_DIGGING,
            ENTITY_ACTION,
            STEER_VEHICLE,
            PONG,
            RECIPE_BOOK_DATA,
            SET_DISPLAYED_RECIPE,
            SET_RECIPE_BOOK_STATE,
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
            USE_ITEM;

            private static final Map<Integer, Map<Integer, PacketTypeCommon>> PACKET_ID_CACHE = new HashMap<>();
            private static final Map<Integer, Map<PacketTypeCommon, Integer>> PACKET_TYPE_CACHE = new HashMap<>();
            private int id = -1;

            @Nullable
            public static PacketTypeCommon getById(int protocolVersion, int packetID) {
                Map<Integer, PacketTypeCommon> innerMap = PACKET_ID_CACHE.get(protocolVersion);
                if (innerMap != null) {
                    return innerMap.get(packetID);
                }
                return null;
            }

            private static void loadPacketIds(ClientVersion version, Enum<?>[] enumConstants) {
                int serverProtocolVersion = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion();
                Map<Integer, PacketTypeCommon> innerMap = new IdentityHashMap<>();
                Map<PacketTypeCommon, Integer> secondInnerMap = new IdentityHashMap<>();
                for (int id = 0; id < enumConstants.length; id++) {
                    Client value = Client.valueOf(enumConstants[id].name());
                    if (version.getProtocolVersion() == serverProtocolVersion) {
                        value.id = id;
                    }
                    innerMap.put(id, value);
                    secondInnerMap.put(value, id);
                }
                PACKET_ID_CACHE.put(version.getProtocolVersion(), innerMap);
                PACKET_TYPE_CACHE.put(version.getProtocolVersion(), secondInnerMap);
            }

            public static void load() {
                loadPacketIds(ClientVersion.V_1_7_10, ServerboundPacketType_1_7_10.values());
                loadPacketIds(ClientVersion.V_1_8, ServerboundPacketType_1_8.values());
                loadPacketIds(ClientVersion.V_1_9, ServerboundPacketType_1_9.values());
                loadPacketIds(ClientVersion.V_1_9_1, ServerboundPacketType_1_9.values());
                loadPacketIds(ClientVersion.V_1_9_2, ServerboundPacketType_1_9.values());
                loadPacketIds(ClientVersion.V_1_9_3, ServerboundPacketType_1_9.values());
                loadPacketIds(ClientVersion.V_1_10, ServerboundPacketType_1_9.values());
                loadPacketIds(ClientVersion.V_1_11, ServerboundPacketType_1_9.values());
                loadPacketIds(ClientVersion.V_1_11_1, ServerboundPacketType_1_9.values());
                loadPacketIds(ClientVersion.V_1_12, ServerboundPacketType_1_12.values());
                loadPacketIds(ClientVersion.V_1_12_1, ServerboundPacketType_1_12_1.values());
                loadPacketIds(ClientVersion.V_1_12_2, ServerboundPacketType_1_12_1.values());
                loadPacketIds(ClientVersion.V_1_13, ServerboundPacketType_1_13.values());
                loadPacketIds(ClientVersion.V_1_14, ServerboundPacketType_1_14.values());
                loadPacketIds(ClientVersion.V_1_14_1, ServerboundPacketType_1_14.values());
                loadPacketIds(ClientVersion.V_1_14_2, ServerboundPacketType_1_14.values());
                loadPacketIds(ClientVersion.V_1_14_3, ServerboundPacketType_1_14.values());
                loadPacketIds(ClientVersion.V_1_14_4, ServerboundPacketType_1_14.values());
                loadPacketIds(ClientVersion.V_1_15, ServerboundPacketType_1_14.values());
                loadPacketIds(ClientVersion.V_1_15_1, ServerboundPacketType_1_14.values());
                loadPacketIds(ClientVersion.V_1_15_2, ServerboundPacketType_1_15_2.values());
                loadPacketIds(ClientVersion.V_1_16, ServerboundPacketType_1_16.values());
                loadPacketIds(ClientVersion.V_1_16_1, ServerboundPacketType_1_16.values());
                loadPacketIds(ClientVersion.V_1_16_2, ServerboundPacketType_1_16_2.values());
                loadPacketIds(ClientVersion.V_1_16_3, ServerboundPacketType_1_16_2.values());
                loadPacketIds(ClientVersion.V_1_16_4, ServerboundPacketType_1_16_2.values());
                loadPacketIds(ClientVersion.V_1_17, ServerboundPacketType_1_17.values());
                loadPacketIds(ClientVersion.V_1_17_1, ServerboundPacketType_1_17.values());
                loadPacketIds(ClientVersion.V_1_18, ServerboundPacketType_1_17.values());
                loadPacketIds(ClientVersion.V_1_18_2, ServerboundPacketType_1_17.values());
                //TODO UPDATE Map client version
            }

            public int getId() {
                return id;
            }

            public int getPacketId(int protocolVersion) {
                return PACKET_TYPE_CACHE.get(protocolVersion).getOrDefault(this, -1);
            }
        }

        public enum Server implements PacketTypeCommon {
            SET_COMPRESSION,
            MAP_CHUNK_BULK,
            UPDATE_ENTITY_NBT,
            UPDATE_SIGN,
            USE_BED,
            SPAWN_WEATHER_ENTITY,
            TITLE,
            WORLD_BORDER,
            COMBAT_EVENT,
            ENTITY_MOVEMENT,

            WINDOW_CONFIRMATION,
            SPAWN_ENTITY,
            SPAWN_EXPERIENCE_ORB,
            SPAWN_LIVING_ENTITY,
            SPAWN_PAINTING,
            SPAWN_PLAYER,
            SCULK_VIBRATION_SIGNAL,
            ENTITY_ANIMATION,
            STATISTICS,
            ACKNOWLEDGE_PLAYER_DIGGING,
            BLOCK_BREAK_ANIMATION,
            BLOCK_ENTITY_DATA,
            BLOCK_ACTION,
            BLOCK_CHANGE,
            BOSS_BAR,
            SERVER_DIFFICULTY,
            CHAT_MESSAGE,
            CLEAR_TITLES,
            TAB_COMPLETE,
            MULTI_BLOCK_CHANGE,
            DECLARE_COMMANDS,
            CLOSE_WINDOW,
            WINDOW_ITEMS,
            WINDOW_PROPERTY,
            SET_SLOT,
            SET_COOLDOWN,
            PLUGIN_MESSAGE,
            NAMED_SOUND_EFFECT,
            DISCONNECT,
            ENTITY_STATUS,
            EXPLOSION,
            UNLOAD_CHUNK,
            CHANGE_GAME_STATE,
            OPEN_HORSE_WINDOW,
            INITIALIZE_WORLD_BORDER,
            KEEP_ALIVE,
            CHUNK_DATA,
            EFFECT,
            PARTICLE,
            UPDATE_LIGHT,
            JOIN_GAME,
            MAP_DATA,
            TRADE_LIST,
            ENTITY_RELATIVE_MOVE,
            ENTITY_RELATIVE_MOVE_AND_ROTATION,
            ENTITY_ROTATION,
            VEHICLE_MOVE,
            OPEN_BOOK,
            OPEN_WINDOW,
            OPEN_SIGN_EDITOR,
            PING,
            CRAFT_RECIPE_RESPONSE,
            PLAYER_ABILITIES,
            END_COMBAT_EVENT,
            ENTER_COMBAT_EVENT,
            DEATH_COMBAT_EVENT,
            PLAYER_INFO,
            FACE_PLAYER,
            PLAYER_POSITION_AND_LOOK,
            UNLOCK_RECIPES,
            DESTROY_ENTITIES,
            REMOVE_ENTITY_EFFECT,
            RESOURCE_PACK_SEND,
            RESPAWN,
            ENTITY_HEAD_LOOK,
            SELECT_ADVANCEMENT_TAB,
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
            SET_TITLE_TIME,
            ENTITY_SOUND_EFFECT,
            SOUND_EFFECT,
            STOP_SOUND,
            PLAYER_LIST_HEADER_AND_FOOTER,
            NBT_QUERY_RESPONSE,
            COLLECT_ITEM,
            ENTITY_TELEPORT,
            ADVANCEMENTS,
            ENTITY_PROPERTIES,
            ENTITY_EFFECT,
            DECLARE_RECIPES,
            TAGS;

            private static final Map<Integer, PacketTypeCommon> PACKET_ID_CACHE = new IdentityHashMap<>();
            private int id = -1;

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                return PACKET_ID_CACHE.get(packetID);
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                for (int id = 0; id < enumConstants.length; id++) {
                    Server value = Server.valueOf(enumConstants[id].name());
                    value.id = id;
                    PACKET_ID_CACHE.put(id, value);
                }
            }

            public static void load() {
                ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
                //1.7.10
                if (version == ServerVersion.V_1_7_10) {
                    loadPacketIds(ClientboundPacketType_1_7_10.values());
                }
                //1.8 - 1.8.8
                else if (version.isOlderThan(ServerVersion.V_1_9)) {
                    loadPacketIds(ClientboundPacketType_1_8.values());
                }
                //1.9 - 1.9.2
                //(Should be 1.9 - 1.9.3, but I can't find a 1.9.3 build of Spigot)
                else if (version.isOlderThan(ServerVersion.V_1_9_4)) {
                    loadPacketIds(ClientboundPacketType_1_9.values());
                }
                //1.10 - 1.11.2
                else if (version.isOlderThan(ServerVersion.V_1_12)) {
                    loadPacketIds(ClientboundPacketType_1_9_3.values());
                }
                //1.12
                else if (version.isOlderThan(ServerVersion.V_1_12_1)) {
                    loadPacketIds(ClientboundPacketType_1_12.values());
                }
                //1.12.1 - 1.12.2
                else if (version.isOlderThan(ServerVersion.V_1_13)) {
                    loadPacketIds(ClientboundPacketType_1_12_1.values());
                }
                //1.13 - 1.13.2
                else if (version.isOlderThan(ServerVersion.V_1_14)) {
                    loadPacketIds(ClientboundPacketType_1_13.values());
                }
                //1.14 - 1.14.3
                else if (version.isOlderThan(ServerVersion.V_1_14_4)) {
                    loadPacketIds(ClientboundPacketType_1_14.values());
                }
                //1.14.4
                else if (version.isOlderThan(ServerVersion.V_1_15)) {
                    loadPacketIds(ClientboundPacketType_1_14_4.values());
                }
                //1.15 - 1.15.1
                else if (version.isOlderThan(ServerVersion.V_1_15_2)) {
                    loadPacketIds(ClientboundPacketType_1_15.values());
                }
                //1.15.2
                else if (version.isOlderThan(ServerVersion.V_1_16)) {
                    loadPacketIds(ClientboundPacketType_1_15_2.values());
                }
                //1.16 - 1.16.1
                else if (version.isOlderThan(ServerVersion.V_1_16_2)) {
                    loadPacketIds(ClientboundPacketType_1_16.values());
                }
                //1.16.2 - 1.16.5
                else if (version.isOlderThan(ServerVersion.V_1_17)) {
                    loadPacketIds(ClientboundPacketType_1_16_2.values());
                }
                //1.17 - 1.17.1
                else if (version.isOlderThan(ServerVersion.V_1_18)) {
                    loadPacketIds(ClientboundPacketType_1_17.values());
                }
                else {
                    loadPacketIds(ClientboundPacketType_1_18.values());
                }
            }

            public int getId() {
                return id;
            }
        }
    }
}
