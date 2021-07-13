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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public class PacketTypeClasses {
    public static void load() {
        //STATUS
        PacketTypeClasses.Status.Client.load();
        PacketTypeClasses.Status.Server.load();
        //HANDSHAKING
        PacketTypeClasses.Handshaking.Client.load();
        //LOGIN
        PacketTypeClasses.Login.Client.load();
        PacketTypeClasses.Login.Server.load();
        //PLAY
        PacketTypeClasses.Play.Client.load();
        PacketTypeClasses.Play.Server.load();
    }

    public static class Status {
        public static class Client {
            private static String PREFIX;
            public static Class<?> START, PING;

            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    PREFIX  = "net.minecraft.network.protocol.status.";
                }
                else {
                    PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                Client.START = Reflection.getClassByNameWithoutException(PREFIX + "PacketStatusInStart");
                Client.PING = Reflection.getClassByNameWithoutException(PREFIX + "PacketStatusInPing");
            }
        }

        public static class Server {
            private static String PREFIX;
            public static Class<?> PONG, SERVER_INFO;

            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    PREFIX  = "net.minecraft.network.protocol.status.";
                }
                else {
                    PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                Server.PONG = Reflection.getClassByNameWithoutException(PREFIX + "PacketStatusOutPong");
                Server.SERVER_INFO = Reflection.getClassByNameWithoutException(PREFIX + "PacketStatusOutServerInfo");
            }
        }
    }

    public static class Handshaking {
        public static class Client {
            private static String PREFIX;
            public static Class<?> SET_PROTOCOL;

            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    PREFIX  = "net.minecraft.network.protocol.handshake.";
                }
                else {
                    PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                Handshaking.Client.SET_PROTOCOL = Reflection.getClassByNameWithoutException(PREFIX + "PacketHandshakingInSetProtocol");
            }
        }
    }

    public static class Login {
        public static class Client {
            private static String PREFIX;
            public static Class<?> CUSTOM_PAYLOAD, START, ENCRYPTION_BEGIN;

            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    PREFIX  = "net.minecraft.network.protocol.login.";
                }
                else {
                    PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                //In and Out custom payload login packets have been here since 1.13
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
                    Client.CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginInCustomPayload");
                }
                Client.START = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginInStart");
                Client.ENCRYPTION_BEGIN = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginInEncryptionBegin");
            }
        }

        public static class Server {
            private static String PREFIX;
            public static Class<?> CUSTOM_PAYLOAD, DISCONNECT, ENCRYPTION_BEGIN, SUCCESS, SET_COMPRESSION;

            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    PREFIX  = "net.minecraft.network.protocol.login.";
                }
                else {
                    PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                //In and Out custom payload login packets have been here since 1.13
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
                    Server.CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginOutCustomPayload");
                }
                Server.DISCONNECT = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginOutDisconnect");
                Server.ENCRYPTION_BEGIN = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginOutEncryptionBegin");
                Server.SUCCESS = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginOutSuccess");
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_7_10)) {
                    Server.SET_COMPRESSION = Reflection.getClassByNameWithoutException(PREFIX + "PacketLoginOutSetCompression");
                }
            }
        }
    }

    public static class Play {
        public static class Client {
            private static String COMMON_PREFIX;
            private static String PREFIX;
            public static Class<?> FLYING, POSITION, POSITION_LOOK, LOOK, GROUND, CLIENT_COMMAND,
                    TRANSACTION, BLOCK_DIG, ENTITY_ACTION, USE_ENTITY,
                    WINDOW_CLICK, STEER_VEHICLE, CUSTOM_PAYLOAD, ARM_ANIMATION,
                    BLOCK_PLACE, USE_ITEM, ABILITIES, HELD_ITEM_SLOT,
                    CLOSE_WINDOW, TAB_COMPLETE, CHAT, SET_CREATIVE_SLOT,
                    KEEP_ALIVE, SETTINGS, ENCHANT_ITEM, TELEPORT_ACCEPT,
                    TILE_NBT_QUERY, DIFFICULTY_CHANGE, B_EDIT, ENTITY_NBT_QUERY,
                    JIGSAW_GENERATE, DIFFICULTY_LOCK, VEHICLE_MOVE, BOAT_MOVE, PICK_ITEM,
                    AUTO_RECIPE, RECIPE_DISPLAYED, ITEM_NAME, RESOURCE_PACK_STATUS,
                    ADVANCEMENTS, TR_SEL, BEACON, SET_COMMAND_BLOCK,
                    SET_COMMAND_MINECART, SET_JIGSAW, STRUCT, UPDATE_SIGN, SPECTATE, PONG;

            /**
             * Initiate all server-bound play packet classes.
             */
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    PREFIX  = "net.minecraft.network.protocol.game.";
                }
                else {
                    PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                COMMON_PREFIX  = PREFIX + "PacketPlayIn";
                FLYING = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Flying");
                try {
                    POSITION = Class.forName(COMMON_PREFIX + "Position");
                    POSITION_LOOK = Class.forName(COMMON_PREFIX + "PositionLook");
                    LOOK = Class.forName(COMMON_PREFIX + "Look");
                } catch (ClassNotFoundException ex) {
                    POSITION = SubclassUtil.getSubClass(FLYING,"PacketPlayInPosition");
                    POSITION_LOOK = SubclassUtil.getSubClass(FLYING, "PacketPlayInPositionLook");
                    LOOK = SubclassUtil.getSubClass(FLYING, "PacketPlayInLook");
                }
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    GROUND = SubclassUtil.getSubClass(FLYING, "d");
                }
                else {
                    GROUND = FLYING;
                }
                //This packet does not exist in the 1.17+ protocol
                TRANSACTION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Transaction");
                //This packet was added in 1.17 protocol
                PONG = Reflection.getClassByNameWithoutException(PREFIX + "ServerboundPongPacket");
                try {
                    SETTINGS = Class.forName(COMMON_PREFIX + "Settings");
                    ENCHANT_ITEM = Class.forName(COMMON_PREFIX + "EnchantItem");

                    CLIENT_COMMAND = Class.forName(COMMON_PREFIX + "ClientCommand");
                    BLOCK_DIG = Class.forName(COMMON_PREFIX + "BlockDig");
                    ENTITY_ACTION = Class.forName(COMMON_PREFIX + "EntityAction");
                    USE_ENTITY = Class.forName(COMMON_PREFIX + "UseEntity");
                    WINDOW_CLICK = Class.forName(COMMON_PREFIX + "WindowClick");
                    STEER_VEHICLE = Class.forName(COMMON_PREFIX + "SteerVehicle");
                    CUSTOM_PAYLOAD = Class.forName(COMMON_PREFIX + "CustomPayload");
                    ARM_ANIMATION = Class.forName(COMMON_PREFIX + "ArmAnimation");
                    ABILITIES = Class.forName(COMMON_PREFIX + "Abilities");
                    HELD_ITEM_SLOT = Class.forName(COMMON_PREFIX + "HeldItemSlot");
                    CLOSE_WINDOW = Class.forName(COMMON_PREFIX + "CloseWindow");
                    TAB_COMPLETE = Class.forName(COMMON_PREFIX + "TabComplete");
                    CHAT = Class.forName(COMMON_PREFIX + "Chat");
                    SET_CREATIVE_SLOT = Class.forName(COMMON_PREFIX + "SetCreativeSlot");
                    KEEP_ALIVE = Class.forName(COMMON_PREFIX + "KeepAlive");
                    UPDATE_SIGN = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "UpdateSign");

                    TELEPORT_ACCEPT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "TeleportAccept");
                    TILE_NBT_QUERY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "TileNBTQuery");
                    DIFFICULTY_CHANGE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "DifficultyChange");
                    B_EDIT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "BEdit");
                    ENTITY_NBT_QUERY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityNBTQuery");
                    JIGSAW_GENERATE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "JigsawGenerate");
                    DIFFICULTY_LOCK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "DifficultyLock");
                    VEHICLE_MOVE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "VehicleMove");
                    BOAT_MOVE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "BoatMove");
                    PICK_ITEM = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "PickItem");
                    AUTO_RECIPE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "AutoRecipe");
                    RECIPE_DISPLAYED = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "RecipeDisplayed");
                    ITEM_NAME = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ItemName");
                    //1.8+
                    RESOURCE_PACK_STATUS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ResourcePackStatus");

                    ADVANCEMENTS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Advancements");
                    TR_SEL = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "TrSel");
                    BEACON = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Beacon");
                    SET_COMMAND_BLOCK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SetCommandBlock");
                    SET_COMMAND_MINECART = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SetCommandMinecart");
                    SET_JIGSAW = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SetJigsaw");
                    STRUCT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Struct");
                    SPECTATE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Spectate");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    BLOCK_PLACE = Class.forName(COMMON_PREFIX + "BlockPlace");
                    USE_ITEM = Class.forName(COMMON_PREFIX + "UseItem");
                } catch (Exception ignored) {
                }

            }
        }

        public static class Server {
            private static String COMMON_PREFIX, PREFIX;
            public static Class<?> SPAWN_ENTITY, SPAWN_ENTITY_EXPERIENCE_ORB, SPAWN_ENTITY_WEATHER, SPAWN_ENTITY_LIVING,
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
                    ENTITY_EFFECT, RECIPE_UPDATE, TAGS, MAP_CHUNK_BULK, NAMED_ENTITY_SPAWN, PING, ADD_VIBRATION_SIGNAL,
                    CLEAR_TITLES, INITIALIZE_BORDER, PLAYER_COMBAT_END, PLAYER_COMBAT_ENTER, PLAYER_COMBAT_KILL,
                    SET_ACTIONBAR_TEXT, SET_BORDER_CENTER, SET_BORDER_LERP_SIZE, SET_BORDER_SIZE, SET_BORDER_WARNING_DELAY,
                    SET_BORDER_WARNING_DISTANCE, SET_SUBTITLE_TEXT, SET_TITLES_ANIMATION, SET_TITLE_TEXT;

            /**
             * Initiate all client-bound packet classes.
             */
            public static void load() {
                if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                    PREFIX  = "net.minecraft.network.protocol.game.";
                }
                else {
                    PREFIX = ServerVersion.getNMSDirectory() + ".";
                }
                COMMON_PREFIX  = PREFIX + "PacketPlayOut";
                SPAWN_ENTITY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SpawnEntity");
                SPAWN_ENTITY_EXPERIENCE_ORB = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SpawnEntityExperienceOrb");
                SPAWN_ENTITY_WEATHER = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SpawnEntityWeather");
                SPAWN_ENTITY_LIVING = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SpawnEntityLiving");
                SPAWN_ENTITY_PAINTING = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SpawnEntityPainting");
                SPAWN_ENTITY_SPAWN = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SpawnEntitySpawn");
                ANIMATION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Animation");
                STATISTIC = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Statistic");
                BLOCK_BREAK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "BlockBreak");
                BLOCK_BREAK_ANIMATION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "BlockBreakAnimation");
                TILE_ENTITY_DATA = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "TileEntityData");
                BLOCK_ACTION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "BlockAction");
                BLOCK_CHANGE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "BlockChange");
                BOSS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Boss");
                SERVER_DIFFICULTY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ServerDifficulty");
                CHAT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Chat");
                MULTI_BLOCK_CHANGE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "MultiBlockChange");
                TAB_COMPLETE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "TabComplete");
                COMMANDS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Commands");
                TRANSACTION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Transaction");
                CLOSE_WINDOW = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "CloseWindow");
                WINDOW_ITEMS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "WindowItems");
                WINDOW_DATA = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "WindowData");
                SET_SLOT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SetSlot");
                SET_COOLDOWN = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SetCooldown");
                CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "CustomPayload");
                CUSTOM_SOUND_EFFECT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "CustomSoundEffect");
                KICK_DISCONNECT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "KickDisconnect");
                ENTITY_STATUS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityStatus");
                EXPLOSION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Explosion");
                UNLOAD_CHUNK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "UnloadChunk");
                GAME_STATE_CHANGE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "GameStateChange");
                OPEN_WINDOW_HORSE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "OpenWindowHorse");
                KEEP_ALIVE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "KeepAlive");
                MAP_CHUNK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "MapChunk");
                WORLD_EVENT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "WorldEvent");
                WORLD_PARTICLES = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "WorldParticles");
                LIGHT_UPDATE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "LightUpdate");
                LOGIN = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Login");
                MAP = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Map");
                OPEN_WINDOW_MERCHANT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "OpenWindowMerchant");
                ENTITY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Entity");
                REL_ENTITY_MOVE = SubclassUtil.getSubClass(ENTITY, "PacketPlayOutRelEntityMove");
                REL_ENTITY_MOVE_LOOK = SubclassUtil.getSubClass(ENTITY, "PacketPlayOutRelEntityMoveLook");
                ENTITY_LOOK = SubclassUtil.getSubClass(ENTITY, "PacketPlayOutEntityLook");
                if (REL_ENTITY_MOVE == null) {
                    //is not a subclass and should be accessed normally
                    REL_ENTITY_MOVE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "RelEntityMove");
                    REL_ENTITY_MOVE_LOOK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "RelEntityMoveLook");
                    ENTITY_LOOK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "RelEntityLook");
                }
                VEHICLE_MOVE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "VehicleMove");
                OPEN_BOOK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "OpenBook");
                OPEN_WINDOW = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "OpenWindow");
                OPEN_SIGN_EDITOR = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "OpenSignEditor");
                AUTO_RECIPE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "AutoRecipe");
                ABILITIES = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Abilities");
                COMBAT_EVENT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "CombatEvent");
                PLAYER_INFO = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "PlayerInfo");
                LOOK_AT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "LookAt");
                POSITION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Position");
                RECIPES = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Recipes");
                ENTITY_DESTROY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityDestroy");
                REMOVE_ENTITY_EFFECT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "RemoveEntityEffect");
                RESOURCE_PACK_SEND = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ResourcePackSend");
                RESPAWN = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Respawn");
                ENTITY_HEAD_ROTATION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityHeadRotation");
                SELECT_ADVANCEMENT_TAB = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SelectAdvancementTab");
                WORLD_BORDER = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "WorldBorder");
                CAMERA = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Camera");
                HELD_ITEM_SLOT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "HeldItemSlot");
                VIEW_CENTRE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ViewCentre");
                VIEW_DISTANCE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ViewDistance");
                SCOREBOARD_DISPLAY_OBJECTIVE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ScoreboardDisplayObjective");
                ENTITY_METADATA = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityMetadata");
                ATTACH_ENTITY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "AttachEntity");
                ENTITY_VELOCITY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityVelocity");
                ENTITY_EQUIPMENT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityEquipment");
                EXPERIENCE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Experience");
                UPDATE_HEALTH = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "UpdateHealth");
                SCOREBOARD_OBJECTIVE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ScoreboardObjective");
                MOUNT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Mount");
                SCOREBOARD_TEAM = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ScoreboardTeam");
                SCOREBOARD_SCORE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "ScoreboardScore");
                SPAWN_POSITION = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "SpawnPosition");
                UPDATE_TIME = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "UpdateTime");
                TITLE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Title");
                ENTITY_SOUND = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntitySound");
                NAMED_SOUND_EFFECT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "NamedSoundEffect");
                STOP_SOUND = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "StopSound");
                PLAYER_LIST_HEADER_FOOTER = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "PlayerListHeaderFooter");
                NBT_QUERY = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "NBTQuery");
                COLLECT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Collect");
                ENTITY_TELEPORT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityTeleport");
                ADVANCEMENTS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Advancements");
                UPDATE_ATTRIBUTES = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "UpdateAttributes");
                ENTITY_EFFECT = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "EntityEffect");
                RECIPE_UPDATE = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "RecipeUpdate");
                TAGS = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "Tags");
                MAP_CHUNK_BULK = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "MapChunkBulk");
                NAMED_ENTITY_SPAWN = Reflection.getClassByNameWithoutException(COMMON_PREFIX + "NamedEntitySpawn");

                //These packets were added in 1.17
                PING = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundPingPacket");
                ADD_VIBRATION_SIGNAL = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundAddVibrationSignalPacket");
                CLEAR_TITLES = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundClearTitlesPacket");
                INITIALIZE_BORDER = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundInitializeBorderPacket");
                PLAYER_COMBAT_END = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundPlayerCombatEndPacket");
                PLAYER_COMBAT_ENTER = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundPlayerCombatEnterPacket");
                PLAYER_COMBAT_KILL = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundPlayerCombatKillPacket");
                SET_ACTIONBAR_TEXT = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetActionBarTextPacket");
                SET_BORDER_CENTER = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetBorderCenterPacket");
                SET_BORDER_LERP_SIZE = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetBorderLerpSizePacket");
                SET_BORDER_SIZE = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetBorderSizePacket");
                SET_BORDER_WARNING_DELAY = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetBorderWarningDelayPacket");
                SET_BORDER_WARNING_DISTANCE = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetBorderWarningDistancePacket");
                SET_SUBTITLE_TEXT = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetSubtitleTextPacket");
                SET_TITLES_ANIMATION = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetTitlesAnimationPacket");
                SET_TITLE_TEXT = Reflection.getClassByNameWithoutException(PREFIX + "ClientboundSetTitleTextPacket");
            }
        }
    }

}
