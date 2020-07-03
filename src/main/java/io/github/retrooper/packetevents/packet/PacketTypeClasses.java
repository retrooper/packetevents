package io.github.retrooper.packetevents.packet;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public class PacketTypeClasses {
    public static class Client {
        public static Class<?> POSITION, POSITION_LOOK, LOOK, CLIENT_COMMAND,
                TRANSACTION, BLOCK_DIG, ENTITY_ACTION, USE_ENTITY,
                WINDOW_CLICK, STEER_VEHICLE, CUSTOM_PAYLOAD, ARM_ANIMATION,
                BLOCK_PLACE, USE_ITEM, ABILITIES, HELD_ITEM_SLOT,
                CLOSE_WINDOW, TAB_COMPLETE, CHAT, SET_CREATIVE_SLOT,
                KEEP_ALIVE,  SETTINGS , ENCHANT_ITEM, TELEPORT_ACCEPT,
                TILE_NBT_QUERY, DIFFICULTY_CHANGE, B_EDIT, ENTITY_NBT_QUERY,
                JIGSAW_GENERATE, DIFFICULTY_LOCK, VEHICLE_MOVE, BOAT_MOVE,PICK_ITEM,
                AUTO_RECIPE, RECIPE_DISPLAYED, ITEM_NAME, RESOURCE_PACK_STATUS,
                ADVANCEMENTS, TR_SEL, BEACON, SET_COMMAND_BLOCK,
                SET_COMMAND_MINECART, SET_JIGSAW, STRUCT, UPDATE_SIGN, SPECTATE;
        private static final String c = "PacketPlayIn";
        @Nullable
        public static final Class<?> FLYING = NMSUtils.getNMSClassWithoutException(c + "Flying");

        static {
            try {POSITION = NMSUtils.getNMSClass(c + "Position");
                POSITION_LOOK = NMSUtils.getNMSClass(c + "PositionLook");
                LOOK = NMSUtils.getNMSClass(c + "Look");
            } catch (ClassNotFoundException e) {
                POSITION = Reflection.getSubClass(FLYING, c + "Position");
                POSITION_LOOK = Reflection.getSubClass(FLYING, c + "PositionLook");
                LOOK = Reflection.getSubClass(FLYING,  c +"Look");
            }

            try {
                SETTINGS = NMSUtils.getNMSClass(c +"Settings");
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
            //Block place
            try {
                BLOCK_PLACE = NMSUtils.getNMSClass(c + "BlockPlace");
            } catch (ClassNotFoundException e) {
                //They are just on a newer version
                try {
                    USE_ITEM = NMSUtils.getNMSClass(c + "UseItem");
                } catch (ClassNotFoundException e2) {
                    e.printStackTrace();
                    e2.printStackTrace();
                }
            }


            PacketType.Client.init();
        }
    }

}
