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
        byte byteID = INVALID + 1;//-127
        for (Class<?> cls : PacketType.class.getDeclaredClasses()) {
            Class<?> packetTypeClassesSubclass = SubclassUtil.getSubClass(PacketTypeClasses.class, cls.getSimpleName());
            for (Class<?> boundCls : cls.getDeclaredClasses()) {
                Class<?> packetTypeClassesSubclassBoundClass = SubclassUtil.getSubClass(packetTypeClassesSubclass, boundCls.getSimpleName());
                for (Field f : boundCls.getFields()) {
                    String fieldName = f.getName();
                    Class<?> fieldType = f.getType();
                    if (fieldType.equals(byte.class)) {
                        try {
                            f.set(null, byteID);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        Field packetTypeClassesSubclassBoundField = null;
                        try {
                            packetTypeClassesSubclassBoundField = packetTypeClassesSubclassBoundClass.getDeclaredField(fieldName);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        try {
                            packetIDMap.put((Class<?>) packetTypeClassesSubclassBoundField.get(null), byteID);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        byteID++;
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
            public static byte START, PING;
        }

        /**
         * Client-bound (server-sided) Status Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Clientbound_2">https://wiki.vg/Protocol#Clientbound_2</a>
         * @since 1.8
         */
        public static class Server {
            public static byte PONG, SERVER_INFO;
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
            public static byte SET_PROTOCOL;
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
            public static byte CUSTOM_PAYLOAD, START, ENCRYPTION_BEGIN;
        }

        /**
         * Client-bound (server-sided) Login Packet IDs.
         *
         * @author retrooper
         * @see <a href="https://wiki.vg/Protocol#Clientbound_3">https://wiki.vg/Protocol#Clientbound_3</a>
         * @since 1.8
         */
        public static class Server {
            public static byte CUSTOM_PAYLOAD, DISCONNECT, ENCRYPTION_BEGIN, SUCCESS, SET_COMPRESSION;
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
            public static byte TELEPORT_ACCEPT,
                    TILE_NBT_QUERY, DIFFICULTY_CHANGE, CHAT, CLIENT_COMMAND,
                    SETTINGS, TAB_COMPLETE, TRANSACTION, ENCHANT_ITEM,
                    WINDOW_CLICK, CLOSE_WINDOW, CUSTOM_PAYLOAD, B_EDIT,
                    ENTITY_NBT_QUERY, USE_ENTITY, JIGSAW_GENERATE, KEEP_ALIVE,
                    DIFFICULTY_LOCK, POSITION, POSITION_LOOK, LOOK,
                    FLYING, VEHICLE_MOVE, BOAT_MOVE, PICK_ITEM,
                    AUTO_RECIPE, ABILITIES, BLOCK_DIG, ENTITY_ACTION,
                    STEER_VEHICLE, RECIPE_DISPLAYED, ITEM_NAME, RESOURCE_PACK_STATUS,
                    ADVANCEMENTS, TR_SEL, BEACON, HELD_ITEM_SLOT,
                    SET_COMMAND_BLOCK, SET_COMMAND_MINECART, SET_CREATIVE_SLOT, SET_JIGSAW,
                    STRUCT, UPDATE_SIGN, ARM_ANIMATION, SPECTATE,
                    USE_ITEM, BLOCK_PLACE;

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
            public static byte SPAWN_ENTITY, SPAWN_ENTITY_EXPERIENCE_ORB, SPAWN_ENTITY_WEATHER, SPAWN_ENTITY_LIVING,
                    SPAWN_ENTITY_PAINTING, SPAWN_ENTITY_SPAWN, ANIMATION, STATISTIC,
                    BLOCK_BREAK, BLOCK_BREAK_ANIMATION, TILE_ENTITY_DATA, BLOCK_ACTION,
                    BLOCK_CHANGE, BOSS, SERVER_DIFFICULTY, CHAT, MULTI_BLOCK_CHANGE,
                    TAB_COMPLETE, COMMANDS, TRANSACTION, CLOSE_WINDOW,
                    WINDOW_ITEMS, WINDOW_DATA, SET_SLOT, SET_COOLDOWN,
                    CUSTOM_PAYLOAD, CUSTOM_SOUND_EFFECT, KICK_DISCONNECT, ENTITY_STATUS,
                    EXPLOSION, UNLOAD_CHUNK, GAME_STATE_CHANGE, OPEN_WINDOW_HORSE,
                    KEEP_ALIVE, MAP_CHUNK, WORLD_EVENT, WORLD_PARTICLES,
                    LIGHT_UPDATE, LOGIN, MAP, OPEN_WINDOW_MERCHANT,
                    REL_ENTITY_MOVE, REL_ENTITY_MOVE_LOOK, ENTITY_LOOK, ENTITY,
                    VEHICLE_MOVE, OPEN_BOOK, OPEN_WINDOW, OPEN_SIGN_EDITOR,
                    AUTO_RECIPE, ABILITIES, COMBAT_EVENT, PLAYER_INFO,
                    LOOK_AT, POSITION, RECIPES, ENTITY_DESTROY,
                    REMOVE_ENTITY_EFFECT, RESOURCE_PACK_SEND, RESPAWN, ENTITY_HEAD_ROTATION,
                    SELECT_ADVANCEMENT_TAB, WORLD_BORDER, CAMERA, HELD_ITEM_SLOT,
                    VIEW_CENTRE, VIEW_DISTANCE, SCOREBOARD_DISPLAY_OBJECTIVE, ENTITY_METADATA,
                    ATTACH_ENTITY, ENTITY_VELOCITY, ENTITY_EQUIPMENT, EXPERIENCE,
                    UPDATE_HEALTH, SCOREBOARD_OBJECTIVE, MOUNT, SCOREBOARD_TEAM,
                    SCOREBOARD_SCORE, SPAWN_POSITION, UPDATE_TIME, TITLE,
                    ENTITY_SOUND, NAMED_SOUND_EFFECT, STOP_SOUND, PLAYER_LIST_HEADER_FOOTER,
                    NBT_QUERY, COLLECT, ENTITY_TELEPORT, ADVANCEMENTS, UPDATE_ATTRIBUTES,
                    ENTITY_EFFECT, RECIPE_UPDATE, TAGS, MAP_CHUNK_BULK, NAMED_ENTITY_SPAWN;

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
