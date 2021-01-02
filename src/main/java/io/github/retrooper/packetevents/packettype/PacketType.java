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

/**
 * Packet Type IDs.
 * This is the Packet ID system, it is recommended to use this over packet comparisons by packet name.
 * This is also faster than comparing packet names.
 *
 * @author retrooper
 * @since 1.6.8
 */
public class PacketType {
    /**
     * Status Packet IDs.
     *
     * @author retrooper
     * @see <a href="https://wiki.vg/Protocol#Status">https://wiki.vg/Protocol#Status</a>
     * @since 1.7
     */
    public static class Status {
        /**
         * Server-bound (client-sided) Status Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Serverbound_2">https://wiki.vg/Protocol#Serverbound_2</a>
         * @since 1.8
         */
        public static class Client {
            public static final byte START = 0, PING = 1;
            public static final Map<Class<?>, Byte> packetIds = new HashMap<>();

            /**
             * Load the server-bound status Packet IDs.
             */
            public static void init() {
                packetIds.put(PacketTypeClasses.Status.Client.START, Client.START);
                packetIds.put(PacketTypeClasses.Status.Client.PING, Client.PING);
            }
        }

        /**
         * Client-bound (server-sided) Status Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Clientbound_2">https://wiki.vg/Protocol#Clientbound_2</a>
         * @since 1.8
         */
        public static class Server {
            public static final byte PONG = 0, SERVER_INFO = 1;
            public static final Map<Class<?>, Byte> packetIds = new HashMap<>();

            /**
             * Load the client-bound Status Packet IDs.
             */
            public static void init() {
                packetIds.put(PacketTypeClasses.Status.Server.PONG, Server.PONG);
                packetIds.put(PacketTypeClasses.Status.Server.SERVER_INFO, Server.SERVER_INFO);
            }
        }
    }

    /**
     * Login Packet IDs.
     *
     * @author retrooper
     * @see <a href="https://wiki.vg/Protocol#Login">https://wiki.vg/Protocol#Login</a>
     * @since 1.7
     */
    public static class Login {
        /**
         * Server-bound (client-sided) Login Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Serverbound_3">https://wiki.vg/Protocol#Serverbound_3</a>
         * @since 1.8
         */
        public static class Client {
            public static final Map<Class<?>, Byte> packetIds = new HashMap<>();
            public static final byte HANDSHAKE = 0, CUSTOM_PAYLOAD = 1, START = 2, ENCRYPTION_BEGIN = 3;

            /**
             * Load the server-bound Login Packet IDs.
             */
            public static void init() {
                packetIds.put(PacketTypeClasses.Login.Client.HANDSHAKE, Client.HANDSHAKE);
                packetIds.put(PacketTypeClasses.Login.Client.CUSTOM_PAYLOAD, Client.CUSTOM_PAYLOAD);
                packetIds.put(PacketTypeClasses.Login.Client.START, Client.START);
                packetIds.put(PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN, Client.ENCRYPTION_BEGIN);
            }
        }

        /**
         * Client-bound (server-sided) Login Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Clientbound_3">https://wiki.vg/Protocol#Clientbound_3</a>
         * @since 1.8
         */
        public static class Server {
            public static final Map<Class<?>, Byte> packetIds = new HashMap<>();
            public static final byte CUSTOM_PAYLOAD = 0, DISCONNECT = 1, ENCRYPTION_BEGIN = 2, SUCCESS = 3, SET_COMPRESSION = 4;

            /**
             * Load the client-bound Login Packet IDs.
             */
            public static void init() {
                packetIds.put(PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD, Server.CUSTOM_PAYLOAD);
                packetIds.put(PacketTypeClasses.Login.Server.DISCONNECT, Server.DISCONNECT);
                packetIds.put(PacketTypeClasses.Login.Server.ENCRYPTION_BEGIN, Server.ENCRYPTION_BEGIN);
                packetIds.put(PacketTypeClasses.Login.Server.SUCCESS, Server.SUCCESS);
                packetIds.put(PacketTypeClasses.Login.Server.SET_COMPRESSION, Server.SET_COMPRESSION);
            }
        }
    }

    /**
     * Play Packet IDs.
     *
     * @author retrooper
     * @see <a href="https://wiki.vg/Protocol#Play">https://wiki.vg/Protocol#Play</a>
     * @since 1.8
     */
    public static class Play {
        /**
         * Server-bound (client-sided) Play Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Serverbound_4">https://wiki.vg/Protocol#Serverbound_4</a>
         * @since 1.8
         */
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

            /**
             * Load the server-bound Play Packet IDs.
             */
            public static void init() {
                packetIds.put(PacketTypeClasses.Play.Client.TELEPORT_ACCEPT, TELEPORT_ACCEPT);
                packetIds.put(PacketTypeClasses.Play.Client.TILE_NBT_QUERY, TILE_NBT_QUERY);
                packetIds.put(PacketTypeClasses.Play.Client.DIFFICULTY_CHANGE, DIFFICULTY_CHANGE);
                packetIds.put(PacketTypeClasses.Play.Client.CHAT, CHAT);
                packetIds.put(PacketTypeClasses.Play.Client.CLIENT_COMMAND, CLIENT_COMMAND);
                packetIds.put(PacketTypeClasses.Play.Client.SETTINGS, SETTINGS);
                packetIds.put(PacketTypeClasses.Play.Client.TAB_COMPLETE, TAB_COMPLETE);
                packetIds.put(PacketTypeClasses.Play.Client.TRANSACTION, TRANSACTION);
                packetIds.put(PacketTypeClasses.Play.Client.ENCHANT_ITEM, ENCHANT_ITEM);
                packetIds.put(PacketTypeClasses.Play.Client.WINDOW_CLICK, WINDOW_CLICK);
                packetIds.put(PacketTypeClasses.Play.Client.CLOSE_WINDOW, CLOSE_WINDOW);
                packetIds.put(PacketTypeClasses.Play.Client.CUSTOM_PAYLOAD, CUSTOM_PAYLOAD);
                packetIds.put(PacketTypeClasses.Play.Client.B_EDIT, B_EDIT);
                packetIds.put(PacketTypeClasses.Play.Client.ENTITY_NBT_QUERY, ENTITY_NBT_QUERY);
                packetIds.put(PacketTypeClasses.Play.Client.USE_ENTITY, USE_ENTITY);
                packetIds.put(PacketTypeClasses.Play.Client.JIGSAW_GENERATE, JIGSAW_GENERATE);
                packetIds.put(PacketTypeClasses.Play.Client.KEEP_ALIVE, KEEP_ALIVE);
                packetIds.put(PacketTypeClasses.Play.Client.DIFFICULTY_LOCK, DIFFICULTY_LOCK);
                packetIds.put(PacketTypeClasses.Play.Client.POSITION, POSITION);
                packetIds.put(PacketTypeClasses.Play.Client.POSITION_LOOK, POSITION_LOOK);
                packetIds.put(PacketTypeClasses.Play.Client.LOOK, LOOK);
                packetIds.put(PacketTypeClasses.Play.Client.FLYING, FLYING);
                packetIds.put(PacketTypeClasses.Play.Client.VEHICLE_MOVE, VEHICLE_MOVE);
                packetIds.put(PacketTypeClasses.Play.Client.BOAT_MOVE, BOAT_MOVE);
                packetIds.put(PacketTypeClasses.Play.Client.PICK_ITEM, PICK_ITEM);
                packetIds.put(PacketTypeClasses.Play.Client.AUTO_RECIPE, AUTO_RECIPE);
                packetIds.put(PacketTypeClasses.Play.Client.ABILITIES, ABILITIES);
                packetIds.put(PacketTypeClasses.Play.Client.BLOCK_DIG, BLOCK_DIG);
                packetIds.put(PacketTypeClasses.Play.Client.ENTITY_ACTION, ENTITY_ACTION);
                packetIds.put(PacketTypeClasses.Play.Client.STEER_VEHICLE, STEER_VEHICLE);
                packetIds.put(PacketTypeClasses.Play.Client.RECIPE_DISPLAYED, RECIPE_DISPLAYED);
                packetIds.put(PacketTypeClasses.Play.Client.ITEM_NAME, ITEM_NAME);
                packetIds.put(PacketTypeClasses.Play.Client.RESOURCE_PACK_STATUS, RESOURCE_PACK_STATUS);
                packetIds.put(PacketTypeClasses.Play.Client.ADVANCEMENTS, ADVANCEMENTS);
                packetIds.put(PacketTypeClasses.Play.Client.TR_SEL, TR_SEL);
                packetIds.put(PacketTypeClasses.Play.Client.BEACON, BEACON);
                packetIds.put(PacketTypeClasses.Play.Client.HELD_ITEM_SLOT, HELD_ITEM_SLOT);
                packetIds.put(PacketTypeClasses.Play.Client.SET_COMMAND_BLOCK, SET_COMMAND_BLOCK);
                packetIds.put(PacketTypeClasses.Play.Client.SET_COMMAND_MINECART, SET_COMMAND_MINECART);
                packetIds.put(PacketTypeClasses.Play.Client.SET_CREATIVE_SLOT, SET_CREATIVE_SLOT);
                packetIds.put(PacketTypeClasses.Play.Client.SET_JIGSAW, SET_JIGSAW);
                packetIds.put(PacketTypeClasses.Play.Client.STRUCT, STRUCT);
                packetIds.put(PacketTypeClasses.Play.Client.UPDATE_SIGN, UPDATE_SIGN);
                packetIds.put(PacketTypeClasses.Play.Client.ARM_ANIMATION, ARM_ANIMATION);
                packetIds.put(PacketTypeClasses.Play.Client.SPECTATE, SPECTATE);
                packetIds.put(PacketTypeClasses.Play.Client.USE_ITEM, USE_ITEM);
                packetIds.put(PacketTypeClasses.Play.Client.BLOCK_PLACE, BLOCK_PLACE);
            }

            /**
             * Server-bound Play Packet Type utility.
             * Save a few lines of code by using this.
             *
             * @author retrooper
             * @since 1.8
             */
            public static class Util {
                /**
                 * Is the play packet a PacketPlayInFlying, PacketPlayInPosition, PacketPlayInPositionLook
                 * or a PacketPlayInLook packet?
                 *
                 * @param packetID Play Packet ID.
                 * @return Is the Packet ID an instance of the PacketPlayInFlying packet?
                 */
                public static boolean isInstanceOfFlying(final byte packetID) {
                    return packetID == FLYING
                            || packetID == POSITION
                            || packetID == POSITION_LOOK
                            || packetID == LOOK;
                }

                /**
                 * Is this the packet where the client places a block?
                 * On 1.7.10 - 1.8.8 the client sends a PacketPlayInBlockPlace packet when actually placing the block.
                 * On 1.9 - now the client sends a PacketPlayInUseItem packet when placing the block and the
                 * PacketPlayInBlockPlace has a different use.
                 * This method is a nice utility supporting all these versions letting you know
                 * if the client played a block.
                 * The {@link io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace} wrapper
                 * only works on an actual block place.
                 * Use this method before using the wrapper to support all minecraft versions.
                 *
                 * @param packetID Play Packet ID.
                 * @return Has the client placed a block?
                 */
                public static boolean isBlockPlace(final byte packetID) {
                    final ServerVersion version = PacketEvents.get().getServerUtils().getVersion();
                    return version.isHigherThan(ServerVersion.v_1_8_8) ?
                            packetID == Client.USE_ITEM
                            : packetID == Client.BLOCK_PLACE;
                }
            }
        }

        /**
         * Client-bound (server-sided) Play Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Clientbound_4">https://wiki.vg/Protocol#Clientbound_4</a>
         * @since 1.8
         */
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
                    ENTITY_EFFECT = 90, RECIPE_UPDATE = 91, TAGS = 92, MAP_CHUNK_BULK = 93, NAMED_ENTITY_SPAWN = 94;

            /**
             * Load the client-bound Play Packet IDs.
             */
            public static void init() {
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY, SPAWN_ENTITY);
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB, SPAWN_ENTITY_EXPERIENCE_ORB);
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_WEATHER, SPAWN_ENTITY_WEATHER);
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_LIVING, SPAWN_ENTITY_LIVING);
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_PAINTING, SPAWN_ENTITY_PAINTING);
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_SPAWN, SPAWN_ENTITY_SPAWN);
                packetIds.put(PacketTypeClasses.Play.Server.ANIMATION, ANIMATION);
                packetIds.put(PacketTypeClasses.Play.Server.STATISTIC, STATISTIC);
                packetIds.put(PacketTypeClasses.Play.Server.BLOCK_BREAK, BLOCK_BREAK);
                packetIds.put(PacketTypeClasses.Play.Server.BLOCK_BREAK_ANIMATION, BLOCK_BREAK_ANIMATION);
                packetIds.put(PacketTypeClasses.Play.Server.TILE_ENTITY_DATA, TILE_ENTITY_DATA);
                packetIds.put(PacketTypeClasses.Play.Server.BLOCK_ACTION, BLOCK_ACTION);
                packetIds.put(PacketTypeClasses.Play.Server.BLOCK_CHANGE, BLOCK_CHANGE);
                packetIds.put(PacketTypeClasses.Play.Server.BOSS, BOSS);
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY, SPAWN_ENTITY);
                packetIds.put(PacketTypeClasses.Play.Server.SERVER_DIFFICULTY, SERVER_DIFFICULTY);
                packetIds.put(PacketTypeClasses.Play.Server.CHAT, CHAT);
                packetIds.put(PacketTypeClasses.Play.Server.MULTI_BLOCK_CHANGE, MULTI_BLOCK_CHANGE);
                packetIds.put(PacketTypeClasses.Play.Server.TAB_COMPLETE, TAB_COMPLETE);
                packetIds.put(PacketTypeClasses.Play.Server.COMMANDS, COMMANDS);
                packetIds.put(PacketTypeClasses.Play.Server.TRANSACTION, TRANSACTION);
                packetIds.put(PacketTypeClasses.Play.Server.CLOSE_WINDOW, CLOSE_WINDOW);
                packetIds.put(PacketTypeClasses.Play.Server.WINDOW_ITEMS, WINDOW_ITEMS);
                packetIds.put(PacketTypeClasses.Play.Server.WINDOW_DATA, WINDOW_DATA);
                packetIds.put(PacketTypeClasses.Play.Server.SET_SLOT, SET_SLOT);
                packetIds.put(PacketTypeClasses.Play.Server.SET_COOLDOWN, SET_COOLDOWN);
                packetIds.put(PacketTypeClasses.Play.Server.CUSTOM_PAYLOAD, CUSTOM_PAYLOAD);
                packetIds.put(PacketTypeClasses.Play.Server.CUSTOM_SOUND_EFFECT, CUSTOM_SOUND_EFFECT);
                packetIds.put(PacketTypeClasses.Play.Server.KICK_DISCONNECT, KICK_DISCONNECT);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_STATUS, ENTITY_STATUS);
                packetIds.put(PacketTypeClasses.Play.Server.EXPLOSION, EXPLOSION);
                packetIds.put(PacketTypeClasses.Play.Server.UNLOAD_CHUNK, UNLOAD_CHUNK);
                packetIds.put(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, GAME_STATE_CHANGE);
                packetIds.put(PacketTypeClasses.Play.Server.OPEN_WINDOW_HORSE, OPEN_WINDOW_HORSE);
                packetIds.put(PacketTypeClasses.Play.Server.KEEP_ALIVE, KEEP_ALIVE);
                packetIds.put(PacketTypeClasses.Play.Server.MAP_CHUNK, MAP_CHUNK);
                packetIds.put(PacketTypeClasses.Play.Server.WORLD_EVENT, WORLD_EVENT);
                packetIds.put(PacketTypeClasses.Play.Server.WORLD_EVENT, SPAWN_ENTITY);
                packetIds.put(PacketTypeClasses.Play.Server.WORLD_PARTICLES, WORLD_PARTICLES);
                packetIds.put(PacketTypeClasses.Play.Server.LIGHT_UPDATE, LIGHT_UPDATE);
                packetIds.put(PacketTypeClasses.Play.Server.LOGIN, LOGIN);
                packetIds.put(PacketTypeClasses.Play.Server.MAP, MAP);
                packetIds.put(PacketTypeClasses.Play.Server.OPEN_WINDOW_MERCHANT, OPEN_WINDOW_MERCHANT);
                packetIds.put(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE, REL_ENTITY_MOVE);
                packetIds.put(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK, REL_ENTITY_MOVE_LOOK);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_LOOK, ENTITY_LOOK);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY, ENTITY);
                packetIds.put(PacketTypeClasses.Play.Server.VEHICLE_MOVE, VEHICLE_MOVE);
                packetIds.put(PacketTypeClasses.Play.Server.OPEN_BOOK, OPEN_BOOK);
                packetIds.put(PacketTypeClasses.Play.Server.OPEN_WINDOW, OPEN_WINDOW);
                packetIds.put(PacketTypeClasses.Play.Server.OPEN_SIGN_EDITOR, OPEN_SIGN_EDITOR);
                packetIds.put(PacketTypeClasses.Play.Server.AUTO_RECIPE, AUTO_RECIPE);
                packetIds.put(PacketTypeClasses.Play.Server.ABILITIES, ABILITIES);
                packetIds.put(PacketTypeClasses.Play.Server.COMBAT_EVENT, COMBAT_EVENT);
                packetIds.put(PacketTypeClasses.Play.Server.PLAYER_INFO, PLAYER_INFO);
                packetIds.put(PacketTypeClasses.Play.Server.LOOK_AT, LOOK_AT);
                packetIds.put(PacketTypeClasses.Play.Server.POSITION, POSITION);
                packetIds.put(PacketTypeClasses.Play.Server.RECIPES, RECIPES);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_DESTROY, ENTITY_DESTROY);
                packetIds.put(PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT, REMOVE_ENTITY_EFFECT);
                packetIds.put(PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND, RESOURCE_PACK_SEND);
                packetIds.put(PacketTypeClasses.Play.Server.RESPAWN, RESPAWN);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION, ENTITY_HEAD_ROTATION);
                packetIds.put(PacketTypeClasses.Play.Server.SELECT_ADVANCEMENT_TAB, SELECT_ADVANCEMENT_TAB);
                packetIds.put(PacketTypeClasses.Play.Server.WORLD_BORDER, WORLD_BORDER);
                packetIds.put(PacketTypeClasses.Play.Server.CAMERA, CAMERA);
                packetIds.put(PacketTypeClasses.Play.Server.HELD_ITEM_SLOT, HELD_ITEM_SLOT);
                packetIds.put(PacketTypeClasses.Play.Server.VIEW_CENTRE, VIEW_CENTRE);
                packetIds.put(PacketTypeClasses.Play.Server.VIEW_DISTANCE, VIEW_DISTANCE);
                packetIds.put(PacketTypeClasses.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE, SCOREBOARD_DISPLAY_OBJECTIVE);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_METADATA, ENTITY_METADATA);
                packetIds.put(PacketTypeClasses.Play.Server.ATTACH_ENTITY, ATTACH_ENTITY);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_VELOCITY, ENTITY_VELOCITY);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_EQUIPMENT, ENTITY_EQUIPMENT);
                packetIds.put(PacketTypeClasses.Play.Server.EXPERIENCE, EXPERIENCE);
                packetIds.put(PacketTypeClasses.Play.Server.UPDATE_HEALTH, UPDATE_HEALTH);
                packetIds.put(PacketTypeClasses.Play.Server.SCOREBOARD_OBJECTIVE, SCOREBOARD_OBJECTIVE);
                packetIds.put(PacketTypeClasses.Play.Server.MOUNT, MOUNT);
                packetIds.put(PacketTypeClasses.Play.Server.SCOREBOARD_TEAM, SCOREBOARD_TEAM);
                packetIds.put(PacketTypeClasses.Play.Server.SCOREBOARD_SCORE, SCOREBOARD_SCORE);
                packetIds.put(PacketTypeClasses.Play.Server.SPAWN_POSITION, SPAWN_POSITION);
                packetIds.put(PacketTypeClasses.Play.Server.UPDATE_TIME, UPDATE_TIME);
                packetIds.put(PacketTypeClasses.Play.Server.TITLE, TITLE);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_SOUND, ENTITY_SOUND);
                packetIds.put(PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT, NAMED_SOUND_EFFECT);
                packetIds.put(PacketTypeClasses.Play.Server.STOP_SOUND, STOP_SOUND);
                packetIds.put(PacketTypeClasses.Play.Server.PLAYER_LIST_HEADER_FOOTER, PLAYER_LIST_HEADER_FOOTER);
                packetIds.put(PacketTypeClasses.Play.Server.NBT_QUERY, NBT_QUERY);
                packetIds.put(PacketTypeClasses.Play.Server.COLLECT, COLLECT);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_TELEPORT, ENTITY_TELEPORT);
                packetIds.put(PacketTypeClasses.Play.Server.ADVANCEMENTS, ADVANCEMENTS);
                packetIds.put(PacketTypeClasses.Play.Server.UPDATE_ATTRIBUTES, UPDATE_ATTRIBUTES);
                packetIds.put(PacketTypeClasses.Play.Server.ENTITY_EFFECT, ENTITY_EFFECT);
                packetIds.put(PacketTypeClasses.Play.Server.RECIPE_UPDATE, RECIPE_UPDATE);
                packetIds.put(PacketTypeClasses.Play.Server.TAGS, TAGS);
                packetIds.put(PacketTypeClasses.Play.Server.MAP_CHUNK_BULK, MAP_CHUNK_BULK);
                packetIds.put(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, NAMED_ENTITY_SPAWN);
            }

            /**
             * Client-bound Play Packet Type utility.
             * Save a few lines of code by using this.
             *
             * @author retrooper
             * @since 1.8
             */
            public static class Util {
                /**
                 * Is the play packet a PacketPlayOutEntity, PacketPlayOutRelEntityMove, PacketPlayOutRelEntityMoveLook
                 * or a PacketPlayOutEntityLook packet?
                 *
                 * @param packetID Play Packet ID.
                 * @return Is the Packet ID an instance of the PacketPlayOutEntity packet?
                 */
                public static boolean isInstanceOfEntity(final byte packetID) {
                    return packetID == ENTITY || packetID == REL_ENTITY_MOVE ||
                            packetID == REL_ENTITY_MOVE_LOOK || packetID == ENTITY_LOOK;
                }
            }
        }
    }
}
