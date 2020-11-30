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

import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;

public class PacketTypeClasses {
    public static class Status {
        public static void load() {
            Client.START = NMSUtils.getNMSClassWithoutException("PacketStatusInStart");
            Client.PING = NMSUtils.getNMSClassWithoutException("PacketStatusInPing");
            Server.PONG = NMSUtils.getNMSClassWithoutException("PacketStatusOutPong");
            Server.SERVER_INFO = NMSUtils.getNMSClassWithoutException("PacketStatusOutServerInfo");
            PacketType.Status.init();
        }

        public static class Client {
            public static Class<?> START, PING;
        }

        public static class Server {
            public static Class<?> PONG, SERVER_INFO;
        }
    }

    public static class Login {
        public static void load() {
            Client.HANDSHAKE = NMSUtils.getNMSClassWithoutException("PacketHandshakingInSetProtocol");
            //In and Out custom payload login packets have been here since AROUND 1.13.2.
            Client.CUSTOM_PAYLOAD = NMSUtils.getNMSClassWithoutException("PacketLoginInCustomPayload");
            Client.START = NMSUtils.getNMSClassWithoutException("PacketLoginInStart");
            Client.ENCRYPTION_BEGIN = NMSUtils.getNMSClassWithoutException("PacketLoginInEncryptionBegin");

            Server.CUSTOM_PAYLOAD = NMSUtils.getNMSClassWithoutException("PacketLoginOutCustomPayload");
            Server.DISCONNECT = NMSUtils.getNMSClassWithoutException("PacketLoginOutDisconnect");
            Server.ENCRYPTION_BEGIN = NMSUtils.getNMSClassWithoutException("PacketLoginOutEncryptionBegin");
            Server.SUCCESS = NMSUtils.getNMSClassWithoutException("PacketLoginOutSuccess");
            Server.SET_COMPRESSION = NMSUtils.getNMSClassWithoutException("PacketLoginOutSetCompression");
            PacketType.Login.init();
        }

        public static class Client {
            public static Class<?> HANDSHAKE, CUSTOM_PAYLOAD, START, ENCRYPTION_BEGIN;
        }

        public static class Server {
            public static Class<?> CUSTOM_PAYLOAD, DISCONNECT, ENCRYPTION_BEGIN, SUCCESS, SET_COMPRESSION;
        }
    }

    public static class Client {
        private static final String c = "PacketPlayIn";
        public static Class<?> FLYING, POSITION, POSITION_LOOK, LOOK, CLIENT_COMMAND,
                TRANSACTION, BLOCK_DIG, ENTITY_ACTION, USE_ENTITY,
                WINDOW_CLICK, STEER_VEHICLE, CUSTOM_PAYLOAD, ARM_ANIMATION,
                BLOCK_PLACE, USE_ITEM, ABILITIES, HELD_ITEM_SLOT,
                CLOSE_WINDOW, TAB_COMPLETE, CHAT, SET_CREATIVE_SLOT,
                KEEP_ALIVE, SETTINGS, ENCHANT_ITEM, TELEPORT_ACCEPT,
                TILE_NBT_QUERY, DIFFICULTY_CHANGE, B_EDIT, ENTITY_NBT_QUERY,
                JIGSAW_GENERATE, DIFFICULTY_LOCK, VEHICLE_MOVE, BOAT_MOVE, PICK_ITEM,
                AUTO_RECIPE, RECIPE_DISPLAYED, ITEM_NAME, RESOURCE_PACK_STATUS,
                ADVANCEMENTS, TR_SEL, BEACON, SET_COMMAND_BLOCK,
                SET_COMMAND_MINECART, SET_JIGSAW, STRUCT, UPDATE_SIGN, SPECTATE;

        /**
         * Initiate all server-bound packet classes.
         */
        public static void load() {
            FLYING = NMSUtils.getNMSClassWithoutException(c + "Flying");
            try {
                POSITION = NMSUtils.getNMSClass(c + "Position");
                POSITION_LOOK = NMSUtils.getNMSClass(c + "PositionLook");
                LOOK = NMSUtils.getNMSClass(c + "Look");
            } catch (ClassNotFoundException e) {
                POSITION = SubclassUtil.getSubClass(FLYING, c + "Position");
                POSITION_LOOK = SubclassUtil.getSubClass(FLYING, c + "PositionLook");
                LOOK = SubclassUtil.getSubClass(FLYING, c + "Look");
            }

            try {
                SETTINGS = NMSUtils.getNMSClass(c + "Settings");
                ENCHANT_ITEM = NMSUtils.getNMSClass(c + "EnchantItem");

                CLIENT_COMMAND = NMSUtils.getNMSClass(c + "ClientCommand");
                TRANSACTION = NMSUtils.getNMSClass(c + "Transaction");
                BLOCK_DIG = NMSUtils.getNMSClass(c + "BlockDig");
                ENTITY_ACTION = NMSUtils.getNMSClass(c + "EntityAction");
                USE_ENTITY = NMSUtils.getNMSClass(c + "UseEntity");
                WINDOW_CLICK = NMSUtils.getNMSClass(c + "WindowClick");
                STEER_VEHICLE = NMSUtils.getNMSClass(c + "SteerVehicle");
                CUSTOM_PAYLOAD = NMSUtils.getNMSClass(c + "CustomPayload");
                ARM_ANIMATION = NMSUtils.getNMSClass(c + "ArmAnimation");
                ABILITIES = NMSUtils.getNMSClass(c + "Abilities");
                HELD_ITEM_SLOT = NMSUtils.getNMSClass(c + "HeldItemSlot");
                CLOSE_WINDOW = NMSUtils.getNMSClass(c + "CloseWindow");
                TAB_COMPLETE = NMSUtils.getNMSClass(c + "TabComplete");
                CHAT = NMSUtils.getNMSClass(c + "Chat");
                SET_CREATIVE_SLOT = NMSUtils.getNMSClass(c + "SetCreativeSlot");
                KEEP_ALIVE = NMSUtils.getNMSClass(c + "KeepAlive");
                UPDATE_SIGN = NMSUtils.getNMSClassWithoutException(c + "UpdateSign");

                TELEPORT_ACCEPT = NMSUtils.getNMSClassWithoutException(c + "TeleportAccept");
                TILE_NBT_QUERY = NMSUtils.getNMSClassWithoutException(c + "TileNBTQuery");
                DIFFICULTY_CHANGE = NMSUtils.getNMSClassWithoutException(c + "DifficultyChange");
                B_EDIT = NMSUtils.getNMSClassWithoutException(c + "BEdit");
                ENTITY_NBT_QUERY = NMSUtils.getNMSClassWithoutException(c + "EntityNBTQuery");
                JIGSAW_GENERATE = NMSUtils.getNMSClassWithoutException(c + "JigsawGenerate");
                DIFFICULTY_LOCK = NMSUtils.getNMSClassWithoutException(c + "DifficultyLock");
                VEHICLE_MOVE = NMSUtils.getNMSClassWithoutException(c + "VehicleMove");
                BOAT_MOVE = NMSUtils.getNMSClassWithoutException(c + "BoatMove");
                PICK_ITEM = NMSUtils.getNMSClassWithoutException(c + "PickItem");
                AUTO_RECIPE = NMSUtils.getNMSClassWithoutException(c + "AutoRecipe");
                RECIPE_DISPLAYED = NMSUtils.getNMSClassWithoutException(c + "RecipeDisplayed");
                ITEM_NAME = NMSUtils.getNMSClassWithoutException(c + "ItemName");
                //1.8+
                RESOURCE_PACK_STATUS = NMSUtils.getNMSClassWithoutException(c + "ResourcePackStatus");

                ADVANCEMENTS = NMSUtils.getNMSClassWithoutException(c + "Advancements");
                TR_SEL = NMSUtils.getNMSClassWithoutException(c + "TrSel");
                BEACON = NMSUtils.getNMSClassWithoutException(c + "Beacon");
                SET_COMMAND_BLOCK = NMSUtils.getNMSClassWithoutException(c + "SetCommandBlock");
                SET_COMMAND_MINECART = NMSUtils.getNMSClassWithoutException(c + "SetCommandMinecart");
                SET_JIGSAW = NMSUtils.getNMSClassWithoutException(c + "SetJigsaw");
                STRUCT = NMSUtils.getNMSClassWithoutException(c + "Struct");
                SPECTATE = NMSUtils.getNMSClassWithoutException(c + "Spectate");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                BLOCK_PLACE = NMSUtils.getNMSClass(c + "BlockPlace");
                USE_ITEM = NMSUtils.getNMSClass(c + "UseItem");
            } catch (ClassNotFoundException ignored) {

            }

            PacketType.Client.init();
        }
    }

    public static class Server {
        private static final String s = "PacketPlayOut";
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
                ENTITY_EFFECT, RECIPE_UPDATE, TAGS, MAP_CHUNK_BULK;

        /**
         * Initiate all client-bound packet classes.
         */
        public static void load() {
            SPAWN_ENTITY = NMSUtils.getNMSClassWithoutException(s + "SpawnEntity");
            SPAWN_ENTITY_EXPERIENCE_ORB = NMSUtils.getNMSClassWithoutException(s + "SpawnEntityExperienceOrb");
            SPAWN_ENTITY_WEATHER = NMSUtils.getNMSClassWithoutException(s + "SpawnEntityWeather");
            SPAWN_ENTITY_LIVING = NMSUtils.getNMSClassWithoutException(s + "SpawnEntityLiving");
            SPAWN_ENTITY_PAINTING = NMSUtils.getNMSClassWithoutException(s + "SpawnEntityPainting");
            SPAWN_ENTITY_SPAWN = NMSUtils.getNMSClassWithoutException(s + "SpawnEntitySpawn");
            ANIMATION = NMSUtils.getNMSClassWithoutException(s + "Animation");
            STATISTIC = NMSUtils.getNMSClassWithoutException(s + "Statistic");
            BLOCK_BREAK = NMSUtils.getNMSClassWithoutException(s + "BlockBreak");
            BLOCK_BREAK_ANIMATION = NMSUtils.getNMSClassWithoutException(s + "BlockBreakAnimation");
            TILE_ENTITY_DATA = NMSUtils.getNMSClassWithoutException(s + "TileEntityData");
            BLOCK_ACTION = NMSUtils.getNMSClassWithoutException(s + "BlockAction");
            BLOCK_CHANGE = NMSUtils.getNMSClassWithoutException(s + "BlockChange");
            BOSS = NMSUtils.getNMSClassWithoutException(s + "Boss");
            SERVER_DIFFICULTY = NMSUtils.getNMSClassWithoutException(s + "ServerDifficulty");
            CHAT = NMSUtils.getNMSClassWithoutException(s + "Chat");
            MULTI_BLOCK_CHANGE = NMSUtils.getNMSClassWithoutException(s + "MultiBlockChange");
            TAB_COMPLETE = NMSUtils.getNMSClassWithoutException(s + "TabComplete");
            COMMANDS = NMSUtils.getNMSClassWithoutException(s + "Commands");
            TRANSACTION = NMSUtils.getNMSClassWithoutException(s + "Transaction");
            CLOSE_WINDOW = NMSUtils.getNMSClassWithoutException(s + "CloseWindow");
            WINDOW_ITEMS = NMSUtils.getNMSClassWithoutException(s + "WindowItems");
            WINDOW_DATA = NMSUtils.getNMSClassWithoutException(s + "WindowData");
            SET_SLOT = NMSUtils.getNMSClassWithoutException(s + "SetSlot");
            SET_COOLDOWN = NMSUtils.getNMSClassWithoutException(s + "SetCooldown");
            CUSTOM_PAYLOAD = NMSUtils.getNMSClassWithoutException(s + "CustomPayload");
            CUSTOM_SOUND_EFFECT = NMSUtils.getNMSClassWithoutException(s + "CustomSoundEffect");
            KICK_DISCONNECT = NMSUtils.getNMSClassWithoutException(s + "KickDisconnect");
            ENTITY_STATUS = NMSUtils.getNMSClassWithoutException(s + "EntityStatus");
            EXPLOSION = NMSUtils.getNMSClassWithoutException(s + "Explosion");
            UNLOAD_CHUNK = NMSUtils.getNMSClassWithoutException(s + "UnloadChunk");
            GAME_STATE_CHANGE = NMSUtils.getNMSClassWithoutException(s + "GameStateChange");
            OPEN_WINDOW_HORSE = NMSUtils.getNMSClassWithoutException(s + "OpenWindowHorse");
            KEEP_ALIVE = NMSUtils.getNMSClassWithoutException(s + "KeepAlive");
            MAP_CHUNK = NMSUtils.getNMSClassWithoutException(s + "MapChunk");
            WORLD_EVENT = NMSUtils.getNMSClassWithoutException(s + "WorldEvent");
            WORLD_PARTICLES = NMSUtils.getNMSClassWithoutException(s + "WorldParticles");
            LIGHT_UPDATE = NMSUtils.getNMSClassWithoutException(s + "LightUpdate");
            LOGIN = NMSUtils.getNMSClassWithoutException(s + "Login");
            MAP = NMSUtils.getNMSClassWithoutException(s + "Map");
            OPEN_WINDOW_MERCHANT = NMSUtils.getNMSClassWithoutException(s + "OpenWindowMerchant");
            ENTITY = NMSUtils.getNMSClassWithoutException(s + "Entity");
            REL_ENTITY_MOVE = SubclassUtil.getSubClass(ENTITY, s + "RelEntityMove");
            REL_ENTITY_MOVE_LOOK = SubclassUtil.getSubClass(ENTITY, s + "RelEntityMoveLook");
            ENTITY_LOOK = SubclassUtil.getSubClass(ENTITY, s + "EntityLook");
            if (REL_ENTITY_MOVE == null) {
                //is not a subclass and should be accessed normally
                REL_ENTITY_MOVE = NMSUtils.getNMSClassWithoutException(s + "RelEntityMove");
                REL_ENTITY_MOVE_LOOK = NMSUtils.getNMSClassWithoutException(s + "RelEntityMoveLook");
                ENTITY_LOOK = NMSUtils.getNMSClassWithoutException(s + "RelEntityLook");
            }
            VEHICLE_MOVE = NMSUtils.getNMSClassWithoutException(s + "VehicleMove");
            OPEN_BOOK = NMSUtils.getNMSClassWithoutException(s + "OpenBook");
            OPEN_WINDOW = NMSUtils.getNMSClassWithoutException(s + "OpenWindow");
            OPEN_SIGN_EDITOR = NMSUtils.getNMSClassWithoutException(s + "OpenSignEditor");
            AUTO_RECIPE = NMSUtils.getNMSClassWithoutException(s + "AutoRecipe");
            ABILITIES = NMSUtils.getNMSClassWithoutException(s + "Abilities");
            COMBAT_EVENT = NMSUtils.getNMSClassWithoutException(s + "CombatEvent");
            PLAYER_INFO = NMSUtils.getNMSClassWithoutException(s + "PlayerInfo");
            LOOK_AT = NMSUtils.getNMSClassWithoutException(s + "LookAt");
            POSITION = NMSUtils.getNMSClassWithoutException(s + "Position");
            RECIPES = NMSUtils.getNMSClassWithoutException(s + "Recipes");
            ENTITY_DESTROY = NMSUtils.getNMSClassWithoutException(s + "EntityDestroy");
            REMOVE_ENTITY_EFFECT = NMSUtils.getNMSClassWithoutException(s + "RemoveEntityEffect");
            RESOURCE_PACK_SEND = NMSUtils.getNMSClassWithoutException(s + "ResourcePackSend");
            RESPAWN = NMSUtils.getNMSClassWithoutException(s + "Respawn");
            ENTITY_HEAD_ROTATION = NMSUtils.getNMSClassWithoutException(s + "EntityHeadRotation");
            SELECT_ADVANCEMENT_TAB = NMSUtils.getNMSClassWithoutException(s + "SelectAdvancementTab");
            WORLD_BORDER = NMSUtils.getNMSClassWithoutException(s + "WorldBorder");
            CAMERA = NMSUtils.getNMSClassWithoutException(s + "Camera");
            HELD_ITEM_SLOT = NMSUtils.getNMSClassWithoutException(s + "HeldItemSlot");
            VIEW_CENTRE = NMSUtils.getNMSClassWithoutException(s + "ViewCentre");
            VIEW_DISTANCE = NMSUtils.getNMSClassWithoutException(s + "ViewDistance");
            SCOREBOARD_DISPLAY_OBJECTIVE = NMSUtils.getNMSClassWithoutException(s + "ScoreboardDisplayObjective");
            ENTITY_METADATA = NMSUtils.getNMSClassWithoutException(s + "EntityMetadata");
            ATTACH_ENTITY = NMSUtils.getNMSClassWithoutException(s + "AttachEntity");
            ENTITY_VELOCITY = NMSUtils.getNMSClassWithoutException(s + "EntityVelocity");
            ENTITY_EQUIPMENT = NMSUtils.getNMSClassWithoutException(s + "EntityEquipment");
            EXPERIENCE = NMSUtils.getNMSClassWithoutException(s + "Experience");
            UPDATE_HEALTH = NMSUtils.getNMSClassWithoutException(s + "UpdateHealth");
            SCOREBOARD_OBJECTIVE = NMSUtils.getNMSClassWithoutException(s + "ScoreboardObjective");
            MOUNT = NMSUtils.getNMSClassWithoutException(s + "Mount");
            SCOREBOARD_TEAM = NMSUtils.getNMSClassWithoutException(s + "ScoreboardTeam");
            SCOREBOARD_SCORE = NMSUtils.getNMSClassWithoutException(s + "ScoreboardScore");
            SPAWN_POSITION = NMSUtils.getNMSClassWithoutException(s + "SpawnPosition");
            UPDATE_TIME = NMSUtils.getNMSClassWithoutException(s + "UpdateTime");
            TITLE = NMSUtils.getNMSClassWithoutException(s + "Title");
            ENTITY_SOUND = NMSUtils.getNMSClassWithoutException(s + "EntitySound");
            NAMED_SOUND_EFFECT = NMSUtils.getNMSClassWithoutException(s + "NamedSoundEffect");
            STOP_SOUND = NMSUtils.getNMSClassWithoutException(s + "StopSound");
            PLAYER_LIST_HEADER_FOOTER = NMSUtils.getNMSClassWithoutException(s + "PlayerListHeaderFooter");
            NBT_QUERY = NMSUtils.getNMSClassWithoutException(s + "NBTQuery");
            COLLECT = NMSUtils.getNMSClassWithoutException(s + "Collect");
            ENTITY_TELEPORT = NMSUtils.getNMSClassWithoutException(s + "EntityTeleport");
            ADVANCEMENTS = NMSUtils.getNMSClassWithoutException(s + "Advancements");
            UPDATE_ATTRIBUTES = NMSUtils.getNMSClassWithoutException(s + "UpdateAttributes");
            ENTITY_EFFECT = NMSUtils.getNMSClassWithoutException(s + "EntityEffect");
            RECIPE_UPDATE = NMSUtils.getNMSClassWithoutException(s + "RecipeUpdate");
            TAGS = NMSUtils.getNMSClassWithoutException(s + "Tags");
            MAP_CHUNK_BULK = NMSUtils.getNMSClassWithoutException(s + "MapChunkBulk");
            PacketType.Server.init();
        }
    }

}
