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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * If a Packet Type could not be resolved, it will be set to the current value of this constant.
     * This value may change over the versions so it is important to use the variable and not hard code its value.
     */
    public static final byte INVALID = -128;
    public static final Map<Class<?>, Byte> packetIDMap = new ConcurrentHashMap<>();

    public static void load() {
        Status.Client.load();
        Status.Server.load();

        Handshaking.Client.load();

        Login.Client.load();
        Login.Server.load();

        Play.Client.load();
        Play.Server.load();
    }

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
            public static final byte START = -127, PING = -126;
            private static void load() {
                packetIDMap.put(PacketTypeClasses.Status.Client.START, Client.START);
                packetIDMap.put(PacketTypeClasses.Status.Client.PING, Client.PING);
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
            public static final byte PONG = -125, SERVER_INFO = -124;
            private static void load() {
                packetIDMap.put(PacketTypeClasses.Status.Server.PONG, Server.PONG);
                packetIDMap.put(PacketTypeClasses.Status.Server.SERVER_INFO, Server.SERVER_INFO);
            }
        }
    }

    /**
     * Handshaking Packet IDs.
     *
     * @author retrooper
     * @see <a href="https://wiki.vg/Protocol#Handshaking">https://wiki.vg/Protocol#Handshaking</a>
     * @since 1.8
     */
    public static class Handshaking {
        /**
         * Server-bound (client-sided) Handshaking Packet IDs.
         *
         * @author retrooper
         * @since 1.8
         */
        public static class Client {
            public static final byte SET_PROTOCOL = -123;
            private static void load() {
                packetIDMap.put(PacketTypeClasses.Handshaking.Client.SET_PROTOCOL, Client.SET_PROTOCOL);
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
            public static final byte CUSTOM_PAYLOAD = -122, START = -121, ENCRYPTION_BEGIN = -120;
            private static void load() {
                packetIDMap.put(PacketTypeClasses.Login.Client.CUSTOM_PAYLOAD, Client.CUSTOM_PAYLOAD);
                packetIDMap.put(PacketTypeClasses.Login.Client.START, Client.START);
                packetIDMap.put(PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN, Client.ENCRYPTION_BEGIN);
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
            public static final byte CUSTOM_PAYLOAD = -119, DISCONNECT = -118, ENCRYPTION_BEGIN = -117, SUCCESS = -116, SET_COMPRESSION = -115;
            private static void load() {
                packetIDMap.put(PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD, Server.CUSTOM_PAYLOAD);
                packetIDMap.put(PacketTypeClasses.Login.Server.DISCONNECT, Server.DISCONNECT);
                packetIDMap.put(PacketTypeClasses.Login.Server.ENCRYPTION_BEGIN, Server.ENCRYPTION_BEGIN);
                packetIDMap.put(PacketTypeClasses.Login.Server.SUCCESS, Server.SUCCESS);
                packetIDMap.put(PacketTypeClasses.Login.Server.SET_COMPRESSION, Server.SET_COMPRESSION);
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
            public static final byte TELEPORT_ACCEPT = -114,
                    TILE_NBT_QUERY = -113, DIFFICULTY_CHANGE = -112, CHAT = -111, CLIENT_COMMAND = -110,
                    SETTINGS = -109, TAB_COMPLETE = -108, TRANSACTION = -107, ENCHANT_ITEM = -106,
                    WINDOW_CLICK = -105, CLOSE_WINDOW = -104, CUSTOM_PAYLOAD = -103, B_EDIT = -102,
                    ENTITY_NBT_QUERY = -101, USE_ENTITY = -100, JIGSAW_GENERATE = -99, KEEP_ALIVE = -98,
                    DIFFICULTY_LOCK = -97, POSITION = -96, POSITION_LOOK = -95, LOOK = -94,
                    FLYING = -93, VEHICLE_MOVE = -92, BOAT_MOVE = -91, PICK_ITEM = -90,
                    AUTO_RECIPE = -89, ABILITIES = -88, BLOCK_DIG = -87, ENTITY_ACTION = -86,
                    STEER_VEHICLE = -85, RECIPE_DISPLAYED = -84, ITEM_NAME = -83, RESOURCE_PACK_STATUS = -82,
                    ADVANCEMENTS = -81, TR_SEL = -80, BEACON = -79, HELD_ITEM_SLOT = -78,
                    SET_COMMAND_BLOCK = -77, SET_COMMAND_MINECART = -76, SET_CREATIVE_SLOT = -75, SET_JIGSAW = -74,
                    STRUCT = -73, UPDATE_SIGN = -72, ARM_ANIMATION = -71, SPECTATE = -70,
                    USE_ITEM = -69, BLOCK_PLACE = -68;

            private static void load() {
                packetIDMap.put(PacketTypeClasses.Play.Client.TELEPORT_ACCEPT, TELEPORT_ACCEPT);
                packetIDMap.put(PacketTypeClasses.Play.Client.TILE_NBT_QUERY, TILE_NBT_QUERY);
                packetIDMap.put(PacketTypeClasses.Play.Client.DIFFICULTY_CHANGE, DIFFICULTY_CHANGE);
                packetIDMap.put(PacketTypeClasses.Play.Client.CHAT, CHAT);
                packetIDMap.put(PacketTypeClasses.Play.Client.CLIENT_COMMAND, CLIENT_COMMAND);
                packetIDMap.put(PacketTypeClasses.Play.Client.SETTINGS, SETTINGS);
                packetIDMap.put(PacketTypeClasses.Play.Client.TAB_COMPLETE, TAB_COMPLETE);
                packetIDMap.put(PacketTypeClasses.Play.Client.TRANSACTION, TRANSACTION);
                packetIDMap.put(PacketTypeClasses.Play.Client.ENCHANT_ITEM, ENCHANT_ITEM);
                packetIDMap.put(PacketTypeClasses.Play.Client.WINDOW_CLICK, WINDOW_CLICK);
                packetIDMap.put(PacketTypeClasses.Play.Client.CLOSE_WINDOW, CLOSE_WINDOW);
                packetIDMap.put(PacketTypeClasses.Play.Client.CUSTOM_PAYLOAD, CUSTOM_PAYLOAD);
                packetIDMap.put(PacketTypeClasses.Play.Client.B_EDIT, B_EDIT);
                packetIDMap.put(PacketTypeClasses.Play.Client.ENTITY_NBT_QUERY, ENTITY_NBT_QUERY);
                packetIDMap.put(PacketTypeClasses.Play.Client.USE_ENTITY, USE_ENTITY);
                packetIDMap.put(PacketTypeClasses.Play.Client.JIGSAW_GENERATE, JIGSAW_GENERATE);
                packetIDMap.put(PacketTypeClasses.Play.Client.KEEP_ALIVE, KEEP_ALIVE);
                packetIDMap.put(PacketTypeClasses.Play.Client.DIFFICULTY_LOCK, DIFFICULTY_LOCK);
                packetIDMap.put(PacketTypeClasses.Play.Client.POSITION, POSITION);
                packetIDMap.put(PacketTypeClasses.Play.Client.POSITION_LOOK, POSITION_LOOK);
                packetIDMap.put(PacketTypeClasses.Play.Client.LOOK, LOOK);
                packetIDMap.put(PacketTypeClasses.Play.Client.FLYING, FLYING);
                packetIDMap.put(PacketTypeClasses.Play.Client.VEHICLE_MOVE, VEHICLE_MOVE);
                packetIDMap.put(PacketTypeClasses.Play.Client.BOAT_MOVE, BOAT_MOVE);
                packetIDMap.put(PacketTypeClasses.Play.Client.PICK_ITEM, PICK_ITEM);
                packetIDMap.put(PacketTypeClasses.Play.Client.AUTO_RECIPE, AUTO_RECIPE);
                packetIDMap.put(PacketTypeClasses.Play.Client.ABILITIES, ABILITIES);
                packetIDMap.put(PacketTypeClasses.Play.Client.BLOCK_DIG, BLOCK_DIG);
                packetIDMap.put(PacketTypeClasses.Play.Client.ENTITY_ACTION, ENTITY_ACTION);
                packetIDMap.put(PacketTypeClasses.Play.Client.STEER_VEHICLE, STEER_VEHICLE);
                packetIDMap.put(PacketTypeClasses.Play.Client.RECIPE_DISPLAYED, RECIPE_DISPLAYED);
                packetIDMap.put(PacketTypeClasses.Play.Client.ITEM_NAME, ITEM_NAME);
                packetIDMap.put(PacketTypeClasses.Play.Client.RESOURCE_PACK_STATUS, RESOURCE_PACK_STATUS);
                packetIDMap.put(PacketTypeClasses.Play.Client.ADVANCEMENTS, ADVANCEMENTS);
                packetIDMap.put(PacketTypeClasses.Play.Client.TR_SEL, TR_SEL);
                packetIDMap.put(PacketTypeClasses.Play.Client.BEACON, BEACON);
                packetIDMap.put(PacketTypeClasses.Play.Client.HELD_ITEM_SLOT, HELD_ITEM_SLOT);
                packetIDMap.put(PacketTypeClasses.Play.Client.SET_COMMAND_BLOCK, SET_COMMAND_BLOCK);
                packetIDMap.put(PacketTypeClasses.Play.Client.SET_COMMAND_MINECART, SET_COMMAND_MINECART);
                packetIDMap.put(PacketTypeClasses.Play.Client.SET_CREATIVE_SLOT, SET_CREATIVE_SLOT);
                packetIDMap.put(PacketTypeClasses.Play.Client.SET_JIGSAW, SET_JIGSAW);
                packetIDMap.put(PacketTypeClasses.Play.Client.STRUCT, STRUCT);
                packetIDMap.put(PacketTypeClasses.Play.Client.UPDATE_SIGN, UPDATE_SIGN);
                packetIDMap.put(PacketTypeClasses.Play.Client.ARM_ANIMATION, ARM_ANIMATION);
                packetIDMap.put(PacketTypeClasses.Play.Client.SPECTATE, SPECTATE);
                packetIDMap.put(PacketTypeClasses.Play.Client.USE_ITEM, USE_ITEM);
                packetIDMap.put(PacketTypeClasses.Play.Client.BLOCK_PLACE, BLOCK_PLACE);
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
                    return version.isNewerThan(ServerVersion.v_1_8_8) ?
                            packetID == PacketType.Play.Client.USE_ITEM
                            : packetID == PacketType.Play.Client.BLOCK_PLACE;
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
            public static final byte SPAWN_ENTITY = -67, SPAWN_ENTITY_EXPERIENCE_ORB = -66, SPAWN_ENTITY_WEATHER = -65, SPAWN_ENTITY_LIVING = -64,
                    SPAWN_ENTITY_PAINTING = -63, SPAWN_ENTITY_SPAWN = -62, ANIMATION = -61, STATISTIC = -60,
                    BLOCK_BREAK = -59, BLOCK_BREAK_ANIMATION = -58, TILE_ENTITY_DATA = -57, BLOCK_ACTION = -56,
                    BLOCK_CHANGE = -55, BOSS = -54, SERVER_DIFFICULTY = -53, CHAT = -52, MULTI_BLOCK_CHANGE = -51,
                    TAB_COMPLETE = -50, COMMANDS = -49, TRANSACTION = -48, CLOSE_WINDOW = -47,
                    WINDOW_ITEMS = -46, WINDOW_DATA = -45, SET_SLOT = -44, SET_COOLDOWN = -43,
                    CUSTOM_PAYLOAD = -42, CUSTOM_SOUND_EFFECT = -41, KICK_DISCONNECT = -40, ENTITY_STATUS = -39,
                    EXPLOSION = -38, UNLOAD_CHUNK = -37, GAME_STATE_CHANGE = -36, OPEN_WINDOW_HORSE = -35,
                    KEEP_ALIVE = -34, MAP_CHUNK = -33, WORLD_EVENT = -32, WORLD_PARTICLES = -31,
                    LIGHT_UPDATE = -30, LOGIN = -29, MAP = -28, OPEN_WINDOW_MERCHANT = -27,
                    REL_ENTITY_MOVE = -26, REL_ENTITY_MOVE_LOOK = -25, ENTITY_LOOK = -24, ENTITY = -23,
                    VEHICLE_MOVE = -22, OPEN_BOOK = -21, OPEN_WINDOW = -20, OPEN_SIGN_EDITOR = -19,
                    AUTO_RECIPE = -18, ABILITIES = -17, COMBAT_EVENT = -16, PLAYER_INFO = -15,
                    LOOK_AT = -14, POSITION = -13, RECIPES = -12, ENTITY_DESTROY = -11,
                    REMOVE_ENTITY_EFFECT = -10, RESOURCE_PACK_SEND = -9, RESPAWN = -8, ENTITY_HEAD_ROTATION = -7,
                    SELECT_ADVANCEMENT_TAB = -6, WORLD_BORDER = -5, CAMERA = -4, HELD_ITEM_SLOT = -3,
                    VIEW_CENTRE = -2, VIEW_DISTANCE = -1, SCOREBOARD_DISPLAY_OBJECTIVE = 0, ENTITY_METADATA = 1,
                    ATTACH_ENTITY = 2, ENTITY_VELOCITY = 3, ENTITY_EQUIPMENT = 4, EXPERIENCE = 5,
                    UPDATE_HEALTH = 6, SCOREBOARD_OBJECTIVE = 7, MOUNT = 8, SCOREBOARD_TEAM = 9,
                    SCOREBOARD_SCORE = 10, SPAWN_POSITION = 11, UPDATE_TIME = 12, TITLE = 13,
                    ENTITY_SOUND = 14, NAMED_SOUND_EFFECT = 15, STOP_SOUND = 16, PLAYER_LIST_HEADER_FOOTER = 17,
                    NBT_QUERY = 18, COLLECT = 19, ENTITY_TELEPORT = 20, ADVANCEMENTS = 21, UPDATE_ATTRIBUTES = 22,
                    ENTITY_EFFECT = 23, RECIPE_UPDATE = 24, TAGS = 25, MAP_CHUNK_BULK = 26, NAMED_ENTITY_SPAWN = 27;

            private static void load() {
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY, SPAWN_ENTITY);
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB, SPAWN_ENTITY_EXPERIENCE_ORB);
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_WEATHER, SPAWN_ENTITY_WEATHER);
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_LIVING, SPAWN_ENTITY_LIVING);
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_PAINTING, SPAWN_ENTITY_PAINTING);
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY_SPAWN, SPAWN_ENTITY_SPAWN);
                packetIDMap.put(PacketTypeClasses.Play.Server.ANIMATION, ANIMATION);
                packetIDMap.put(PacketTypeClasses.Play.Server.STATISTIC, STATISTIC);
                packetIDMap.put(PacketTypeClasses.Play.Server.BLOCK_BREAK, BLOCK_BREAK);
                packetIDMap.put(PacketTypeClasses.Play.Server.BLOCK_BREAK_ANIMATION, BLOCK_BREAK_ANIMATION);
                packetIDMap.put(PacketTypeClasses.Play.Server.TILE_ENTITY_DATA, TILE_ENTITY_DATA);
                packetIDMap.put(PacketTypeClasses.Play.Server.BLOCK_ACTION, BLOCK_ACTION);
                packetIDMap.put(PacketTypeClasses.Play.Server.BLOCK_CHANGE, BLOCK_CHANGE);
                packetIDMap.put(PacketTypeClasses.Play.Server.BOSS, BOSS);
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_ENTITY, SPAWN_ENTITY);
                packetIDMap.put(PacketTypeClasses.Play.Server.SERVER_DIFFICULTY, SERVER_DIFFICULTY);
                packetIDMap.put(PacketTypeClasses.Play.Server.CHAT, CHAT);
                packetIDMap.put(PacketTypeClasses.Play.Server.MULTI_BLOCK_CHANGE, MULTI_BLOCK_CHANGE);
                packetIDMap.put(PacketTypeClasses.Play.Server.TAB_COMPLETE, TAB_COMPLETE);
                packetIDMap.put(PacketTypeClasses.Play.Server.COMMANDS, COMMANDS);
                packetIDMap.put(PacketTypeClasses.Play.Server.TRANSACTION, TRANSACTION);
                packetIDMap.put(PacketTypeClasses.Play.Server.CLOSE_WINDOW, CLOSE_WINDOW);
                packetIDMap.put(PacketTypeClasses.Play.Server.WINDOW_ITEMS, WINDOW_ITEMS);
                packetIDMap.put(PacketTypeClasses.Play.Server.WINDOW_DATA, WINDOW_DATA);
                packetIDMap.put(PacketTypeClasses.Play.Server.SET_SLOT, SET_SLOT);
                packetIDMap.put(PacketTypeClasses.Play.Server.SET_COOLDOWN, SET_COOLDOWN);
                packetIDMap.put(PacketTypeClasses.Play.Server.CUSTOM_PAYLOAD, CUSTOM_PAYLOAD);
                packetIDMap.put(PacketTypeClasses.Play.Server.CUSTOM_SOUND_EFFECT, CUSTOM_SOUND_EFFECT);
                packetIDMap.put(PacketTypeClasses.Play.Server.KICK_DISCONNECT, KICK_DISCONNECT);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_STATUS, ENTITY_STATUS);
                packetIDMap.put(PacketTypeClasses.Play.Server.EXPLOSION, EXPLOSION);
                packetIDMap.put(PacketTypeClasses.Play.Server.UNLOAD_CHUNK, UNLOAD_CHUNK);
                packetIDMap.put(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, GAME_STATE_CHANGE);
                packetIDMap.put(PacketTypeClasses.Play.Server.OPEN_WINDOW_HORSE, OPEN_WINDOW_HORSE);
                packetIDMap.put(PacketTypeClasses.Play.Server.KEEP_ALIVE, KEEP_ALIVE);
                packetIDMap.put(PacketTypeClasses.Play.Server.MAP_CHUNK, MAP_CHUNK);
                packetIDMap.put(PacketTypeClasses.Play.Server.WORLD_EVENT, WORLD_EVENT);
                packetIDMap.put(PacketTypeClasses.Play.Server.WORLD_EVENT, SPAWN_ENTITY);
                packetIDMap.put(PacketTypeClasses.Play.Server.WORLD_PARTICLES, WORLD_PARTICLES);
                packetIDMap.put(PacketTypeClasses.Play.Server.LIGHT_UPDATE, LIGHT_UPDATE);
                packetIDMap.put(PacketTypeClasses.Play.Server.LOGIN, LOGIN);
                packetIDMap.put(PacketTypeClasses.Play.Server.MAP, MAP);
                packetIDMap.put(PacketTypeClasses.Play.Server.OPEN_WINDOW_MERCHANT, OPEN_WINDOW_MERCHANT);
                packetIDMap.put(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE, REL_ENTITY_MOVE);
                packetIDMap.put(PacketTypeClasses.Play.Server.REL_ENTITY_MOVE_LOOK, REL_ENTITY_MOVE_LOOK);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_LOOK, ENTITY_LOOK);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY, ENTITY);
                packetIDMap.put(PacketTypeClasses.Play.Server.VEHICLE_MOVE, VEHICLE_MOVE);
                packetIDMap.put(PacketTypeClasses.Play.Server.OPEN_BOOK, OPEN_BOOK);
                packetIDMap.put(PacketTypeClasses.Play.Server.OPEN_WINDOW, OPEN_WINDOW);
                packetIDMap.put(PacketTypeClasses.Play.Server.OPEN_SIGN_EDITOR, OPEN_SIGN_EDITOR);
                packetIDMap.put(PacketTypeClasses.Play.Server.AUTO_RECIPE, AUTO_RECIPE);
                packetIDMap.put(PacketTypeClasses.Play.Server.ABILITIES, ABILITIES);
                packetIDMap.put(PacketTypeClasses.Play.Server.COMBAT_EVENT, COMBAT_EVENT);
                packetIDMap.put(PacketTypeClasses.Play.Server.PLAYER_INFO, PLAYER_INFO);
                packetIDMap.put(PacketTypeClasses.Play.Server.LOOK_AT, LOOK_AT);
                packetIDMap.put(PacketTypeClasses.Play.Server.POSITION, POSITION);
                packetIDMap.put(PacketTypeClasses.Play.Server.RECIPES, RECIPES);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_DESTROY, ENTITY_DESTROY);
                packetIDMap.put(PacketTypeClasses.Play.Server.REMOVE_ENTITY_EFFECT, REMOVE_ENTITY_EFFECT);
                packetIDMap.put(PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND, RESOURCE_PACK_SEND);
                packetIDMap.put(PacketTypeClasses.Play.Server.RESPAWN, RESPAWN);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION, ENTITY_HEAD_ROTATION);
                packetIDMap.put(PacketTypeClasses.Play.Server.SELECT_ADVANCEMENT_TAB, SELECT_ADVANCEMENT_TAB);
                packetIDMap.put(PacketTypeClasses.Play.Server.WORLD_BORDER, WORLD_BORDER);
                packetIDMap.put(PacketTypeClasses.Play.Server.CAMERA, CAMERA);
                packetIDMap.put(PacketTypeClasses.Play.Server.HELD_ITEM_SLOT, HELD_ITEM_SLOT);
                packetIDMap.put(PacketTypeClasses.Play.Server.VIEW_CENTRE, VIEW_CENTRE);
                packetIDMap.put(PacketTypeClasses.Play.Server.VIEW_DISTANCE, VIEW_DISTANCE);
                packetIDMap.put(PacketTypeClasses.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE, SCOREBOARD_DISPLAY_OBJECTIVE);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_METADATA, ENTITY_METADATA);
                packetIDMap.put(PacketTypeClasses.Play.Server.ATTACH_ENTITY, ATTACH_ENTITY);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_VELOCITY, ENTITY_VELOCITY);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_EQUIPMENT, ENTITY_EQUIPMENT);
                packetIDMap.put(PacketTypeClasses.Play.Server.EXPERIENCE, EXPERIENCE);
                packetIDMap.put(PacketTypeClasses.Play.Server.UPDATE_HEALTH, UPDATE_HEALTH);
                packetIDMap.put(PacketTypeClasses.Play.Server.SCOREBOARD_OBJECTIVE, SCOREBOARD_OBJECTIVE);
                packetIDMap.put(PacketTypeClasses.Play.Server.MOUNT, MOUNT);
                packetIDMap.put(PacketTypeClasses.Play.Server.SCOREBOARD_TEAM, SCOREBOARD_TEAM);
                packetIDMap.put(PacketTypeClasses.Play.Server.SCOREBOARD_SCORE, SCOREBOARD_SCORE);
                packetIDMap.put(PacketTypeClasses.Play.Server.SPAWN_POSITION, SPAWN_POSITION);
                packetIDMap.put(PacketTypeClasses.Play.Server.UPDATE_TIME, UPDATE_TIME);
                packetIDMap.put(PacketTypeClasses.Play.Server.TITLE, TITLE);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_SOUND, ENTITY_SOUND);
                packetIDMap.put(PacketTypeClasses.Play.Server.NAMED_SOUND_EFFECT, NAMED_SOUND_EFFECT);
                packetIDMap.put(PacketTypeClasses.Play.Server.STOP_SOUND, STOP_SOUND);
                packetIDMap.put(PacketTypeClasses.Play.Server.PLAYER_LIST_HEADER_FOOTER, PLAYER_LIST_HEADER_FOOTER);
                packetIDMap.put(PacketTypeClasses.Play.Server.NBT_QUERY, NBT_QUERY);
                packetIDMap.put(PacketTypeClasses.Play.Server.COLLECT, COLLECT);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_TELEPORT, ENTITY_TELEPORT);
                packetIDMap.put(PacketTypeClasses.Play.Server.ADVANCEMENTS, ADVANCEMENTS);
                packetIDMap.put(PacketTypeClasses.Play.Server.UPDATE_ATTRIBUTES, UPDATE_ATTRIBUTES);
                packetIDMap.put(PacketTypeClasses.Play.Server.ENTITY_EFFECT, ENTITY_EFFECT);
                packetIDMap.put(PacketTypeClasses.Play.Server.RECIPE_UPDATE, RECIPE_UPDATE);
                packetIDMap.put(PacketTypeClasses.Play.Server.TAGS, TAGS);
                packetIDMap.put(PacketTypeClasses.Play.Server.MAP_CHUNK_BULK, MAP_CHUNK_BULK);
                packetIDMap.put(PacketTypeClasses.Play.Server.NAMED_ENTITY_SPAWN, NAMED_ENTITY_SPAWN);
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
