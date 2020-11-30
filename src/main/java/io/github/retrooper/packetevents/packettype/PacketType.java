/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packettype;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.HashMap;
import java.util.Map;

public class PacketType {
    public static class Status {
        public static final Map<Class<?>, Byte> packetIds = new HashMap<>();

        public static void init() {
            packetIds.put(PacketTypeClasses.Status.Client.START, Client.START);
            packetIds.put(PacketTypeClasses.Status.Client.PING, Client.PING);
            packetIds.put(PacketTypeClasses.Status.Server.PONG, Server.PONG);
            packetIds.put(PacketTypeClasses.Status.Server.SERVER_INFO, Server.SERVER_INFO);
        }

        public static class Client {
            public static final byte START = 0, PING = 1;
        }

        public static class Server {
            public static final byte PONG = 2, SERVER_INFO = 3;
        }
    }

    public static class Login {
        public static final Map<Class<?>, Byte> packetIds = new HashMap<>();

        public static void init() {
            packetIds.put(PacketTypeClasses.Login.Client.HANDSHAKE, Client.HANDSHAKE);
            packetIds.put(PacketTypeClasses.Login.Client.CUSTOM_PAYLOAD, Client.CUSTOM_PAYLOAD);
            packetIds.put(PacketTypeClasses.Login.Client.START, Client.START);
            packetIds.put(PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN, Client.ENCRYPTION_BEGIN);

            packetIds.put(PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD, Server.CUSTOM_PAYLOAD);
            packetIds.put(PacketTypeClasses.Login.Server.DISCONNECT, Server.DISCONNECT);
            packetIds.put(PacketTypeClasses.Login.Server.ENCRYPTION_BEGIN, Server.ENCRYPTION_BEGIN);
            packetIds.put(PacketTypeClasses.Login.Server.SUCCESS, Server.SUCCESS);
            packetIds.put(PacketTypeClasses.Login.Server.SET_COMPRESSION, Server.SET_COMPRESSION);
        }

        public static class Client {
            public static final byte HANDSHAKE = 0, CUSTOM_PAYLOAD = 1, START = 2, ENCRYPTION_BEGIN = 3;
        }

        public static class Server {
            public static final byte CUSTOM_PAYLOAD = 4, DISCONNECT = 5, ENCRYPTION_BEGIN = 6, SUCCESS = 7, SET_COMPRESSION = 8;
        }
    }

    public static class Client {
        public static final Map<Class<?>, Byte> packetIds = new HashMap<>();
        public static final byte TELEPORT_ACCEPT = 0,
                TILE_NBT_QUERY = 1, DIFFICULTY_CHANGE = 2, CHAT = 3, CLIENT_COMMAND = 4,
                SETTINGS = 5, TAB_COMPLETE = 6, TRANSACTION = 7, ENCHANT_ITEM = 8,
                WINDOW_CLICK = 9, CLOSE_WINDOW = 10, CUSTOM_PAYLOAD = 11, B_EDIT = 12,
                ENTITY_NBT_QUERY = 13, USE_ENTITY = 14, JIGSAW_GENERATE = 15, KEEP_ALIVE = 16,
                DIFFICULTY_LOCK = 17, POSITION = 18, POSITION_LOOK = 19, LOOK = 20,
                FLYING = 21, VEHICLE_MOVE = 22, BOAT_MOVE = 23, PICK_ITEM = 24,
                AUTO_RECIPE = 25, ABILITIES = 26, BLOCK_DIG = 27, ENTITY_ACTION = 28,
                STEER_VEHICLE = 29, RECIPE_DISPLAYED = 30, ITEM_NAME = 31, RESOURCE_PACK_STATUS = 32,
                ADVANCEMENTS = 33, TR_SEL = 34, BEACON = 35, HELD_ITEM_SLOT = 36,
                SET_COMMAND_BLOCK = 37, SET_COMMAND_MINECART = 38, SET_CREATIVE_SLOT = 39, SET_JIGSAW = 40,
                STRUCT = 41, UPDATE_SIGN = 42, ARM_ANIMATION = 43, SPECTATE = 44,
                USE_ITEM = 45, BLOCK_PLACE = 46;

        public static void init() {
            packetIds.put(PacketTypeClasses.Client.TELEPORT_ACCEPT, TELEPORT_ACCEPT);
            packetIds.put(PacketTypeClasses.Client.TILE_NBT_QUERY, TILE_NBT_QUERY);
            packetIds.put(PacketTypeClasses.Client.DIFFICULTY_CHANGE, DIFFICULTY_CHANGE);
            packetIds.put(PacketTypeClasses.Client.CHAT, CHAT);
            packetIds.put(PacketTypeClasses.Client.CLIENT_COMMAND, CLIENT_COMMAND);
            packetIds.put(PacketTypeClasses.Client.SETTINGS, SETTINGS);
            packetIds.put(PacketTypeClasses.Client.TAB_COMPLETE, TAB_COMPLETE);
            packetIds.put(PacketTypeClasses.Client.TRANSACTION, TRANSACTION);
            packetIds.put(PacketTypeClasses.Client.ENCHANT_ITEM, ENCHANT_ITEM);
            packetIds.put(PacketTypeClasses.Client.WINDOW_CLICK, WINDOW_CLICK);
            packetIds.put(PacketTypeClasses.Client.CLOSE_WINDOW, CLOSE_WINDOW);
            packetIds.put(PacketTypeClasses.Client.CUSTOM_PAYLOAD, CUSTOM_PAYLOAD);
            packetIds.put(PacketTypeClasses.Client.B_EDIT, B_EDIT);
            packetIds.put(PacketTypeClasses.Client.ENTITY_NBT_QUERY, ENTITY_NBT_QUERY);
            packetIds.put(PacketTypeClasses.Client.USE_ENTITY, USE_ENTITY);
            packetIds.put(PacketTypeClasses.Client.JIGSAW_GENERATE, JIGSAW_GENERATE);
            packetIds.put(PacketTypeClasses.Client.KEEP_ALIVE, KEEP_ALIVE);
            packetIds.put(PacketTypeClasses.Client.DIFFICULTY_LOCK, DIFFICULTY_LOCK);
            packetIds.put(PacketTypeClasses.Client.POSITION, POSITION);
            packetIds.put(PacketTypeClasses.Client.POSITION_LOOK, POSITION_LOOK);
            packetIds.put(PacketTypeClasses.Client.LOOK, LOOK);
            packetIds.put(PacketTypeClasses.Client.FLYING, FLYING);
            packetIds.put(PacketTypeClasses.Client.VEHICLE_MOVE, VEHICLE_MOVE);
            packetIds.put(PacketTypeClasses.Client.BOAT_MOVE, BOAT_MOVE);
            packetIds.put(PacketTypeClasses.Client.PICK_ITEM, PICK_ITEM);
            packetIds.put(PacketTypeClasses.Client.AUTO_RECIPE, AUTO_RECIPE);
            packetIds.put(PacketTypeClasses.Client.ABILITIES, ABILITIES);
            packetIds.put(PacketTypeClasses.Client.BLOCK_DIG, BLOCK_DIG);
            packetIds.put(PacketTypeClasses.Client.ENTITY_ACTION, ENTITY_ACTION);
            packetIds.put(PacketTypeClasses.Client.STEER_VEHICLE, STEER_VEHICLE);
            packetIds.put(PacketTypeClasses.Client.RECIPE_DISPLAYED, RECIPE_DISPLAYED);
            packetIds.put(PacketTypeClasses.Client.ITEM_NAME, ITEM_NAME);
            packetIds.put(PacketTypeClasses.Client.RESOURCE_PACK_STATUS, RESOURCE_PACK_STATUS);
            packetIds.put(PacketTypeClasses.Client.ADVANCEMENTS, ADVANCEMENTS);
            packetIds.put(PacketTypeClasses.Client.TR_SEL, TR_SEL);
            packetIds.put(PacketTypeClasses.Client.BEACON, BEACON);
            packetIds.put(PacketTypeClasses.Client.HELD_ITEM_SLOT, HELD_ITEM_SLOT);
            packetIds.put(PacketTypeClasses.Client.SET_COMMAND_BLOCK, SET_COMMAND_BLOCK);
            packetIds.put(PacketTypeClasses.Client.SET_COMMAND_MINECART, SET_COMMAND_MINECART);
            packetIds.put(PacketTypeClasses.Client.SET_CREATIVE_SLOT, SET_CREATIVE_SLOT);
            packetIds.put(PacketTypeClasses.Client.SET_JIGSAW, SET_JIGSAW);
            packetIds.put(PacketTypeClasses.Client.STRUCT, STRUCT);
            packetIds.put(PacketTypeClasses.Client.UPDATE_SIGN, UPDATE_SIGN);
            packetIds.put(PacketTypeClasses.Client.ARM_ANIMATION, ARM_ANIMATION);
            packetIds.put(PacketTypeClasses.Client.SPECTATE, SPECTATE);
            packetIds.put(PacketTypeClasses.Client.USE_ITEM, USE_ITEM);
            packetIds.put(PacketTypeClasses.Client.BLOCK_PLACE, BLOCK_PLACE);
        }

        public static class Util {
            /**
             * Is the packet an instance of the PacketPlayInFlying packet?
             *
             * @param packetID Packet ID
             * @return packetID == FLYING or POSITION or POSITION_LOOK or LOOK
             */
            public static boolean isInstanceOfFlying(final byte packetID) {
                return packetID == FLYING
                        || packetID == POSITION
                        || packetID == POSITION_LOOK
                        || packetID == LOOK;
            }

            public static boolean isBlockPlace(final byte packetID) {
                final ServerVersion version = PacketEvents.get().getServerUtils().getVersion();
                return version.isHigherThan(ServerVersion.v_1_8_8) ?
                        packetID == Client.USE_ITEM
                        : packetID == Client.BLOCK_PLACE;
            }
        }
    }

    public static class Server {
        public static final Map<Class<?>, Byte> packetIds = new HashMap<>();
        public static final byte SPAWN_ENTITY = 0, SPAWN_ENTITY_EXPERIENCE_ORB = 1, SPAWN_ENTITY_WEATHER = 2, SPAWN_ENTITY_LIVING = 3,
                SPAWN_ENTITY_PAINTING = 4, SPAWN_ENTITY_SPAWN = 5, ANIMATION = 6, STATISTIC = 7,
                BLOCK_BREAK = 8, BLOCK_BREAK_ANIMATION = 9, TILE_ENTITY_DATA = 10, BLOCK_ACTION = 11,
                BLOCK_CHANGE = 12, BOSS = 13, SERVER_DIFFICULTY = 14, CHAT = 15, MULTI_BLOCK_CHANGE = 16,
                TAB_COMPLETE = 17, COMMANDS = 18, TRANSACTION = 19, CLOSE_WINDOW = 20,
                WINDOW_ITEMS = 21, WINDOW_DATA = 22, SET_SLOT = 23, SET_COOLDOWN = 24,
                CUSTOM_PAYLOAD = 25, CUSTOM_SOUND_EFFECT = 26, KICK_DISCONNECT = 27, ENTITY_STATUS = 28,
                EXPLOSION = 29, UNLOAD_CHUNK = 30, GAME_STATE_CHANGE = 31, OPEN_WINDOW_HORSE = 32,
                KEEP_ALIVE = 33, MAP_CHUNK = 34, WORLD_EVENT = 35, WORLD_PARTICLES = 36,
                LIGHT_UPDATE = 37, LOGIN = 38, MAP = 39, OPEN_WINDOW_MERCHANT = 40,
                REL_ENTITY_MOVE = 41, REL_ENTITY_MOVE_LOOK = 42, ENTITY_LOOK = 43, ENTITY = 44,
                VEHICLE_MOVE = 45, OPEN_BOOK = 46, OPEN_WINDOW = 47, OPEN_SIGN_EDITOR = 48,
                AUTO_RECIPE = 49, ABILITIES = 50, COMBAT_EVENT = 51, PLAYER_INFO = 52,
                LOOK_AT = 53, POSITION = 54, RECIPES = 55, ENTITY_DESTROY = 56,
                REMOVE_ENTITY_EFFECT = 57, RESOURCE_PACK_SEND = 58, RESPAWN = 59, ENTITY_HEAD_ROTATION = 60,
                SELECT_ADVANCEMENT_TAB = 61, WORLD_BORDER = 62, CAMERA = 63, HELD_ITEM_SLOT = 64,
                VIEW_CENTRE = 65, VIEW_DISTANCE = 66, SCOREBOARD_DISPLAY_OBJECTIVE = 67, ENTITY_METADATA = 68,
                ATTACH_ENTITY = 69, ENTITY_VELOCITY = 70, ENTITY_EQUIPMENT = 71, EXPERIENCE = 72,
                UPDATE_HEALTH = 73, SCOREBOARD_OBJECTIVE = 74, MOUNT = 75, SCOREBOARD_TEAM = 76,
                SCOREBOARD_SCORE = 77, SPAWN_POSITION = 78, UPDATE_TIME = 79, TITLE = 80,
                ENTITY_SOUND = 81, NAMED_SOUND_EFFECT = 82, STOP_SOUND = 83, PLAYER_LIST_HEADER_FOOTER = 84,
                NBT_QUERY = 85, COLLECT = 86, ENTITY_TELEPORT = 87, ADVANCEMENTS = 88, UPDATE_ATTRIBUTES = 89,
                ENTITY_EFFECT = 90, RECIPE_UPDATE = 91, TAGS = 92, MAP_CHUNK_BULK = 93;

        public static void init() {
            packetIds.put(PacketTypeClasses.Server.SPAWN_ENTITY, SPAWN_ENTITY);
            packetIds.put(PacketTypeClasses.Server.SPAWN_ENTITY_EXPERIENCE_ORB, SPAWN_ENTITY_EXPERIENCE_ORB);
            packetIds.put(PacketTypeClasses.Server.SPAWN_ENTITY_WEATHER, SPAWN_ENTITY_WEATHER);
            packetIds.put(PacketTypeClasses.Server.SPAWN_ENTITY_LIVING, SPAWN_ENTITY_LIVING);
            packetIds.put(PacketTypeClasses.Server.SPAWN_ENTITY_PAINTING, SPAWN_ENTITY_PAINTING);
            packetIds.put(PacketTypeClasses.Server.SPAWN_ENTITY_SPAWN, SPAWN_ENTITY_SPAWN);
            packetIds.put(PacketTypeClasses.Server.ANIMATION, ANIMATION);
            packetIds.put(PacketTypeClasses.Server.STATISTIC, STATISTIC);
            packetIds.put(PacketTypeClasses.Server.BLOCK_BREAK, BLOCK_BREAK);
            packetIds.put(PacketTypeClasses.Server.BLOCK_BREAK_ANIMATION, BLOCK_BREAK_ANIMATION);
            packetIds.put(PacketTypeClasses.Server.TILE_ENTITY_DATA, TILE_ENTITY_DATA);
            packetIds.put(PacketTypeClasses.Server.BLOCK_ACTION, BLOCK_ACTION);
            packetIds.put(PacketTypeClasses.Server.BLOCK_CHANGE, BLOCK_CHANGE);
            packetIds.put(PacketTypeClasses.Server.BOSS, BOSS);
            packetIds.put(PacketTypeClasses.Server.SPAWN_ENTITY, SPAWN_ENTITY);
            packetIds.put(PacketTypeClasses.Server.SERVER_DIFFICULTY, SERVER_DIFFICULTY);
            packetIds.put(PacketTypeClasses.Server.CHAT, CHAT);
            packetIds.put(PacketTypeClasses.Server.MULTI_BLOCK_CHANGE, MULTI_BLOCK_CHANGE);
            packetIds.put(PacketTypeClasses.Server.TAB_COMPLETE, TAB_COMPLETE);
            packetIds.put(PacketTypeClasses.Server.COMMANDS, COMMANDS);
            packetIds.put(PacketTypeClasses.Server.TRANSACTION, TRANSACTION);
            packetIds.put(PacketTypeClasses.Server.CLOSE_WINDOW, CLOSE_WINDOW);
            packetIds.put(PacketTypeClasses.Server.WINDOW_ITEMS, WINDOW_ITEMS);
            packetIds.put(PacketTypeClasses.Server.WINDOW_DATA, WINDOW_DATA);
            packetIds.put(PacketTypeClasses.Server.SET_SLOT, SET_SLOT);
            packetIds.put(PacketTypeClasses.Server.SET_COOLDOWN, SET_COOLDOWN);
            packetIds.put(PacketTypeClasses.Server.CUSTOM_PAYLOAD, CUSTOM_PAYLOAD);
            packetIds.put(PacketTypeClasses.Server.CUSTOM_SOUND_EFFECT, CUSTOM_SOUND_EFFECT);
            packetIds.put(PacketTypeClasses.Server.KICK_DISCONNECT, KICK_DISCONNECT);
            packetIds.put(PacketTypeClasses.Server.ENTITY_STATUS, ENTITY_STATUS);
            packetIds.put(PacketTypeClasses.Server.EXPLOSION, EXPLOSION);
            packetIds.put(PacketTypeClasses.Server.UNLOAD_CHUNK, UNLOAD_CHUNK);
            packetIds.put(PacketTypeClasses.Server.GAME_STATE_CHANGE, GAME_STATE_CHANGE);
            packetIds.put(PacketTypeClasses.Server.OPEN_WINDOW_HORSE, OPEN_WINDOW_HORSE);
            packetIds.put(PacketTypeClasses.Server.KEEP_ALIVE, KEEP_ALIVE);
            packetIds.put(PacketTypeClasses.Server.MAP_CHUNK, MAP_CHUNK);
            packetIds.put(PacketTypeClasses.Server.WORLD_EVENT, WORLD_EVENT);
            packetIds.put(PacketTypeClasses.Server.WORLD_EVENT, SPAWN_ENTITY);
            packetIds.put(PacketTypeClasses.Server.WORLD_PARTICLES, WORLD_PARTICLES);
            packetIds.put(PacketTypeClasses.Server.LIGHT_UPDATE, LIGHT_UPDATE);
            packetIds.put(PacketTypeClasses.Server.LOGIN, LOGIN);
            packetIds.put(PacketTypeClasses.Server.MAP, MAP);
            packetIds.put(PacketTypeClasses.Server.OPEN_WINDOW_MERCHANT, OPEN_WINDOW_MERCHANT);
            packetIds.put(PacketTypeClasses.Server.REL_ENTITY_MOVE, REL_ENTITY_MOVE);
            packetIds.put(PacketTypeClasses.Server.REL_ENTITY_MOVE_LOOK, REL_ENTITY_MOVE_LOOK);
            packetIds.put(PacketTypeClasses.Server.ENTITY_LOOK, ENTITY_LOOK);
            packetIds.put(PacketTypeClasses.Server.ENTITY, ENTITY);
            packetIds.put(PacketTypeClasses.Server.VEHICLE_MOVE, VEHICLE_MOVE);
            packetIds.put(PacketTypeClasses.Server.OPEN_BOOK, OPEN_BOOK);
            packetIds.put(PacketTypeClasses.Server.OPEN_WINDOW, OPEN_WINDOW);
            packetIds.put(PacketTypeClasses.Server.OPEN_SIGN_EDITOR, OPEN_SIGN_EDITOR);
            packetIds.put(PacketTypeClasses.Server.AUTO_RECIPE, AUTO_RECIPE);
            packetIds.put(PacketTypeClasses.Server.ABILITIES, ABILITIES);
            packetIds.put(PacketTypeClasses.Server.COMBAT_EVENT, COMBAT_EVENT);
            packetIds.put(PacketTypeClasses.Server.PLAYER_INFO, PLAYER_INFO);
            packetIds.put(PacketTypeClasses.Server.LOOK_AT, LOOK_AT);
            packetIds.put(PacketTypeClasses.Server.POSITION, POSITION);
            packetIds.put(PacketTypeClasses.Server.RECIPES, RECIPES);
            packetIds.put(PacketTypeClasses.Server.ENTITY_DESTROY, ENTITY_DESTROY);
            packetIds.put(PacketTypeClasses.Server.REMOVE_ENTITY_EFFECT, REMOVE_ENTITY_EFFECT);
            packetIds.put(PacketTypeClasses.Server.RESOURCE_PACK_SEND, RESOURCE_PACK_SEND);
            packetIds.put(PacketTypeClasses.Server.RESPAWN, RESPAWN);
            packetIds.put(PacketTypeClasses.Server.ENTITY_HEAD_ROTATION, ENTITY_HEAD_ROTATION);
            packetIds.put(PacketTypeClasses.Server.SELECT_ADVANCEMENT_TAB, SELECT_ADVANCEMENT_TAB);
            packetIds.put(PacketTypeClasses.Server.WORLD_BORDER, WORLD_BORDER);
            packetIds.put(PacketTypeClasses.Server.CAMERA, CAMERA);
            packetIds.put(PacketTypeClasses.Server.HELD_ITEM_SLOT, HELD_ITEM_SLOT);
            packetIds.put(PacketTypeClasses.Server.VIEW_CENTRE, VIEW_CENTRE);
            packetIds.put(PacketTypeClasses.Server.VIEW_DISTANCE, VIEW_DISTANCE);
            packetIds.put(PacketTypeClasses.Server.SCOREBOARD_DISPLAY_OBJECTIVE, SCOREBOARD_DISPLAY_OBJECTIVE);
            packetIds.put(PacketTypeClasses.Server.ENTITY_METADATA, ENTITY_METADATA);
            packetIds.put(PacketTypeClasses.Server.ATTACH_ENTITY, ATTACH_ENTITY);
            packetIds.put(PacketTypeClasses.Server.ENTITY_VELOCITY, ENTITY_VELOCITY);
            packetIds.put(PacketTypeClasses.Server.ENTITY_EQUIPMENT, ENTITY_EQUIPMENT);
            packetIds.put(PacketTypeClasses.Server.EXPERIENCE, EXPERIENCE);
            packetIds.put(PacketTypeClasses.Server.UPDATE_HEALTH, UPDATE_HEALTH);
            packetIds.put(PacketTypeClasses.Server.SCOREBOARD_OBJECTIVE, SCOREBOARD_OBJECTIVE);
            packetIds.put(PacketTypeClasses.Server.MOUNT, MOUNT);
            packetIds.put(PacketTypeClasses.Server.SCOREBOARD_TEAM, SCOREBOARD_TEAM);
            packetIds.put(PacketTypeClasses.Server.SCOREBOARD_SCORE, SCOREBOARD_SCORE);
            packetIds.put(PacketTypeClasses.Server.SPAWN_POSITION, SPAWN_POSITION);
            packetIds.put(PacketTypeClasses.Server.UPDATE_TIME, UPDATE_TIME);
            packetIds.put(PacketTypeClasses.Server.TITLE, TITLE);
            packetIds.put(PacketTypeClasses.Server.ENTITY_SOUND, ENTITY_SOUND);
            packetIds.put(PacketTypeClasses.Server.NAMED_SOUND_EFFECT, NAMED_SOUND_EFFECT);
            packetIds.put(PacketTypeClasses.Server.STOP_SOUND, STOP_SOUND);
            packetIds.put(PacketTypeClasses.Server.PLAYER_LIST_HEADER_FOOTER, PLAYER_LIST_HEADER_FOOTER);
            packetIds.put(PacketTypeClasses.Server.NBT_QUERY, NBT_QUERY);
            packetIds.put(PacketTypeClasses.Server.COLLECT, COLLECT);
            packetIds.put(PacketTypeClasses.Server.ENTITY_TELEPORT, ENTITY_TELEPORT);
            packetIds.put(PacketTypeClasses.Server.ADVANCEMENTS, ADVANCEMENTS);
            packetIds.put(PacketTypeClasses.Server.UPDATE_ATTRIBUTES, UPDATE_ATTRIBUTES);
            packetIds.put(PacketTypeClasses.Server.ENTITY_EFFECT, ENTITY_EFFECT);
            packetIds.put(PacketTypeClasses.Server.RECIPE_UPDATE, RECIPE_UPDATE);
            packetIds.put(PacketTypeClasses.Server.TAGS, TAGS);
            packetIds.put(PacketTypeClasses.Server.MAP_CHUNK_BULK, MAP_CHUNK_BULK);
        }

        public static class Util {
            /**
             * Is the packet an instance of the PacketPlayOutEntity packet?
             *
             * @param packetID
             * @return packetID == ENTITY or REL_ENTITY_MOVE or REL_ENTITY_MOVE_LOOK or ENTITY_LOOK
             */
            public static boolean isInstanceOfEntity(final byte packetID) {
                return packetID == ENTITY || packetID == REL_ENTITY_MOVE ||
                        packetID == REL_ENTITY_MOVE_LOOK || packetID == ENTITY_LOOK;
            }
        }
    }


}
