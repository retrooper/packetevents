package me.purplex.packetevents.packet;


public abstract class Packet {
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
        public static final String BLOCK_PLACE = c + "BlockPlace";
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
    }

    public static boolean isInstanceOf(String fatherPacket, String childPacket) {
        if (fatherPacket.equals(Client.FLYING) && childPacket.equals(Client.POSITION) || childPacket.equals(Client.POSITION_LOOK) || childPacket.equals(Client.LOOK)) {
            return true;
        }
        return fatherPacket.equals(childPacket);
    }


}
