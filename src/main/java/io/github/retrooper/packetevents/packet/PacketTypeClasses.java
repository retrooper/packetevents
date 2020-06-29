package io.github.retrooper.packetevents.packet;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.utils.NMSUtils;

public class PacketTypeClasses {
    public static class Client {
        private static String c = "PacketPlayIn";

        @Nullable
        public static final Class<?> FLYING = NMSUtils.getNMSClassWithoutException(c + "Flying");

        public static Class<?> POSITION, POSITION_LOOK, LOOK, CLIENT_COMMAND,
                TRANSACTION, BLOCK_DIG, ENTITY_ACTION, USE_ENTITY,
                WINDOW_CLICK, STEER_VEHICLE, CUSTOM_PAYLOAD, ARM_ANIMATION,
                BLOCK_PLACE, ABILITIES, HELD_ITEM_SLOT, CLOSE_WINDOW,
                TAB_COMPLETE, CHAT, SET_CREATIVE_SLOT, KEEP_ALIVE;

        static {
            try {
                POSITION = NMSUtils.getNMSClass(c + "Position");
                POSITION_LOOK = NMSUtils.getNMSClass(c + "PositionLook");
                LOOK = NMSUtils.getNMSClass(c + "Look");

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
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //Block place
            try {
                BLOCK_PLACE = NMSUtils.getNMSClass(c + "BlockPlace");
            }
            catch(ClassNotFoundException e) {
                //They are just on a newer version
                try {
                    BLOCK_PLACE = NMSUtils.getNMSClass(c + "UseItem");
                }
                catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
