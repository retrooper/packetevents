package io.github.retrooper.packetevents.packet;

import java.util.HashMap;
import java.util.Map;

public class PacketType {


    public static class Client {
        public static final Map<Class<?>, Integer> packetIds = new HashMap<Class<?>, Integer>();

        public static final int TELEPORT_ACCEPT = 0,
                TILE_NBT_QUERY = 1,
                DIFFICULTY_CHANGE = 2,
                CHAT = 3,
                CLIENT_COMMAND = 4,
                SETTINGS = 5,
        TAB_COMPLETE = 6, TRANSACTION = 7, ENCHANT_ITEM =8,
                WINDOW_CLICK = 9, CLOSE_WINDOW =10, CUSTOM_PAYLOAD = 11,
                B_EDIT = 12, ENTITY_NBT_QUERY = 13, USE_ENTITY = 14, JIGSAW_GENERATE = 15, KEEP_ALIVE= 16, DIFFICULTY_LOCK = 17,
        POSITION = 18, POSITION_LOOK = 19, LOOK = 20, FLYING = 21, VEHICLE_MOVE = 22, BOAT_MOVE = 23, PICK_ITEM = 24, AUTO_RECIPE = 25, ABILITIES = 26, BLOCK_DIG = 27, ENTITY_ACTION = 28, STEER_VEHICLE = 29, RECIPE_DISPLAYED = 30, ITEM_NAME = 31, RESOURCE_PACK_STATUS = 32, ADVANCEMENTS = 33, TR_SEL = 34, BEACON = 35, HELD_ITEM_SLOT = 36, SET_COMMAND_BLOCK = 37,
                SET_COMMAND_MINECART = 38, SET_CREATIVE_SLOT = 39, SET_JIGSAW = 40, STRUCT = 41, UPDATE_SIGN = 42, ARM_ANIMATION = 43, SPECTATE = 44, USE_ITEM = 45, BLOCK_PLACE = 46;

        static {
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
    }
}
