package io.github.retrooper.packetevents.packet;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.utils.NMSUtils;

public abstract class Packet {

    private static Class<?> packetPlayInFlying, packetPlayInPosition, packetPlayInPositionLook, packetPlayInLook;

    static {
        try {
            packetPlayInFlying = NMSUtils.getNMSClass(Client.FLYING);
        } catch (ClassNotFoundException e) {
            //That is fine, they are on 1.9+ so we just initiate the rest of the variables
            try {
                packetPlayInPosition = NMSUtils.getNMSClass(Client.POSITION);
                packetPlayInPositionLook = NMSUtils.getNMSClass(Client.POSITION_LOOK);
                packetPlayInLook = NMSUtils.getNMSClass(Client.LOOK);
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Is this NMS packet an instanceof the "PacketPlayInFlying" packet in the 1.8 protocol?
     * Supports 1.9->1.15.2 too!
     *
     * @param nmsPacket
     * @return If the nms packet provided is an instanceof the "PacketPlayInFlying" packet in the 1.8 protocol!
     */
    public static boolean isInstanceOfFlyingPacket(Object nmsPacket) {
        //1.7->1.8.8
        if (packetPlayInFlying != null) {
            return packetPlayInFlying.isInstance(nmsPacket);
        } else {
            //1.9->1.15.2
            return packetPlayInPosition.isInstance(nmsPacket)
                    || packetPlayInPositionLook.isInstance(nmsPacket)
                    || packetPlayInLook.isInstance(nmsPacket);
        }
    }

    /**
     * Use isInstanceOfFlyingPacket(nmsPacketObject) instead of this, as the flying packet is the only packet that has subclasses (in 1.8 atleast).
     * Will be removed in newer versions of PacketEvents!
     *
     * @param fatherPacket
     * @param childPacket
     * @return isInstanceOf
     */
    @Deprecated
    public static boolean isInstanceOf(String fatherPacket, String childPacket) {
        if (fatherPacket.equals(Client.FLYING) && childPacket.equals(Client.POSITION) || childPacket.equals(Client.POSITION_LOOK) || childPacket.equals(Client.LOOK)) {
            return true;
        }
        return fatherPacket.equals(childPacket);
    }

    public static class Client {
        private static final String c = "PacketPlayIn";

        @Deprecated
        public static final String FLYING = c + "Flying";


        public static final String POSITION = c + "Position";

        public static final String POSITION_LOOK = c + "PositionLook";

        public static final String LOOK = c + "Look";
        public static final String CLIENT_COMMAND = c + "ClientCommand";

        public static final String TRANSACTION = c + "Transaction";
        public static final String BLOCK_DIG = c + "BlockDig";
        public static final String ENTITY_ACTION = c + "EntityAction";
        public static final String USE_ENTITY = c + "UseEntity";
        public static final String WINDOW_CLICK = c + "WindowClick";
        public static final String STEER_VEHICLE = c + "SteerVehicle";
        public static final String CUSTOM_PAYLOAD = c + "CustomPayload";
        public static final String ARM_ANIMATION = c + "ArmAnimation";
        /**
         * The packet name of the block place depending on your server version, "PacketPlayInBlockPlace" or "PacketPlayInUseItem"
         */
        public static final String BLOCK_PLACE = c + ((ServerVersion.getVersion().isHigherThan(ServerVersion.v_1_8_8)) ? "UseItem" : "BlockPlace");

        public static final String ABILITIES = c + "Abilities";
        public static final String HELD_ITEM_SLOT = c + "HeldItemSlot";
        public static final String CLOSE_WINDOW = c + "CloseWindow";
        public static final String TAB_COMPLETE = c + "TabComplete";
        public static final String CHAT = c + "Chat";
        public static final String SET_CREATIVE_SLOT = c + "SetCreativeSlot";

        public static final String KEEP_ALIVE = c + "KeepAlive";

    }

    public static class Server {
        private static final String s = "PacketPlayOut";

        public static final String ANIMATION = s + "Animation";
        public static final String KEEP_ALIVE = s + "KeepAlive";
        public static final String CHAT = s + "Chat";
        public static final String POSITION = s + "Position";
        public static final String TRANSACTION = s + "Transaction";
        public static final String NAMED_ENTITY_SPAWN = s + "NamedEntitySpawn";
        public static final String SPAWN_ENTITY_LIVING = s + "SpawnEntityLiving";
        public static final String SPAWN_ENTITY = s + "SpawnEntity";
        public static final String CUSTOM_PAYLOAD = s + "CustomPayload";
        public static final String ABILITIES = s + "Abilities";
        public static final String ENTITY_METADATA = s + "EntityMetadata";
        public static final String ENTITY_VELOCITY = s + "EntityVelocity";
        public static final String ENTITY_DESTROY = s + "EntityDestroy";
        public static final String ENTITY_HEAD_ROTATION = s + "EntityHeadRotation";
        public static final String BLOCK_CHANGE = s + "BlockChange";
        public static final String CLOSE_WINDOW = s + "CloseWindow";
        public static final String HELD_ITEM_SLOT = s + "HeldItemSlot";
        public static final String TAB_COMPLETE = s + "TabComplete";
        public static final String RESPAWN = s + "Respawn";
        public static final String WORLD_PARTICLES = s + "WorldParticles";
        public static final String COMMANDS = s + "Commands";
        public static final String OPEN_WINDOW = s + "OpenWindow";
        public static final String LOGIN = s + "Login";
        public static final String SERVER_DIFFICULTY = s + "ServerDifficulty";
    }

    public static class Login {
        public static final String HANDSHAKE = "PacketHandshakingInSetProtocol";
        public static final String PING = "PacketStatusInPing";
        public static final String START = "PacketStatusInStart";
        public static final String SUCCESS = "PacketLoginOutSuccess";
        public static final String ENCRYPTION_BEGIN_IN = "PacketLoginInEncryptionBegin";
        public static final String ENCRYPTION_BEGIN_OUT = "PacketLoginOutEncryptionBegin";

        public static final String[] LOGIN_PACKETS = new String[]{"PacketHandshakingInSetProtocol", "PacketLoginInStart", "PacketLoginInEncryptionBegin"};
    }

}
