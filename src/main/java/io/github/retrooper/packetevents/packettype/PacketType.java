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
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Field;
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
     * If a Packet Type could not be resolved, it will be set to the current value of this constant.
     * This value may change over the versions so it is important to use the variable and not hard code its value.
     */
    public static final byte INVALID = -128;
    public static Map<Class<?>, Byte> packetIDMap = new HashMap<>();

    public static void load() {
        for (Class<?> cls : PacketType.class.getDeclaredClasses()) {
            Class<?> packetTypeClassesSubclass = SubclassUtil.getSubClass(PacketTypeClasses.class, cls.getSimpleName());
            for (Class<?> boundCls : cls.getDeclaredClasses()) {
                Class<?> packetTypeClassesSubclassBoundClass = SubclassUtil.getSubClass(packetTypeClassesSubclass, boundCls.getSimpleName());
                for (Field f : boundCls.getFields()) {
                    String fieldName = f.getName();
                    Class<?> fieldType = f.getType();
                    if (fieldType.equals(byte.class)) {
                        Field packetTypeClassesSubclassBoundField = null;
                        try {
                            packetTypeClassesSubclassBoundField = packetTypeClassesSubclassBoundClass.getDeclaredField(fieldName);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }

                        try {
                            byte byteID = f.getByte(null);
                            packetIDMap.put((Class<?>) packetTypeClassesSubclassBoundField.get(null), byteID);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


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
