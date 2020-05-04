package me.purplex.packetevents.packetevent.packet;

public abstract class Packet {
    public static class Client {
        private static final String CLIENT = "PacketPlayIn";
        public static final String KEEP_ALIVE = CLIENT + "KeepAlive";

        @Deprecated
        public static final String FLYING = CLIENT + "Flying";
        public static final String POSITION = CLIENT + "Position";
        public static final String POSITION_LOOK = CLIENT + "PositionLook";
        public static final String LOOK = CLIENT + "Look";
        @Deprecated
        public static final String LEGACY_POSITION = FLYING + "$" + CLIENT + "Position";
        @Deprecated
        public static final String LEGACY_POSITION_LOOK = FLYING + "$" + CLIENT + "PositionLook";
        @Deprecated
        public static final String LEGACY_LOOK = FLYING + "$" + CLIENT + "Look";
        public static final String TRANSACTION = CLIENT + "Transaction";
        public static final String BLOCK_DIG = CLIENT + "BlockDig";
        public static final String ENTITY_ACTION = CLIENT + "EntityAction";
        public static final String USE_ENTITY = CLIENT + "UseEntity";
        public static final String WINDOW_CLICK = CLIENT + "WindowClick";
        public static final String STEER_VEHICLE = CLIENT + "SteerVehicle";
        public static final String CUSTOM_PAYLOAD = CLIENT + "CustomPayload";
        public static final String ARM_ANIMATION = CLIENT + "ArmAnimation";
        public static final String BLOCK_PLACE_1_9 = CLIENT + "BlockPlace1_9";
        public static final String BLOCK_PLACE = CLIENT + "BlockPlace";
        public static final String ABILITIES = CLIENT + "Abilities";
        public static final String HELD_ITEM_SLOT = CLIENT + "HeldItemSlot";
        public static final String CLOSE_WINDOW = CLIENT + "CloseWindow";
        public static final String TAB_COMPLETE = CLIENT + "TabComplete";
        public static final String CHAT = CLIENT + "Chat";
        public static final String CREATIVE_SLOT = CLIENT + "SetCreativeSlot";
        public static final String CLIENT_COMMAND = CLIENT + "ClientCommand";
    }

    public static class Server {
        private static final String SERVER = "PacketPlayOut";

        public static final String ANIMATION = SERVER + "Animation";
        public static final String KEEP_ALIVE = SERVER + "KeepAlive";
        public static final String CHAT = SERVER + "Chat";
        public static final String POSITION = SERVER + "Position";
        public static final String TRANSACTION = SERVER + "Transaction";
        public static final String NAMED_ENTITY_SPAWN = SERVER + "NamedEntitySpawn";
        public static final String SPAWN_ENTITY_LIVING = SERVER + "SpawnEntityLiving";
        public static final String SPAWN_ENTITY = SERVER + "SpawnEntity";
        public static final String CUSTOM_PAYLOAD = SERVER + "CustomPayload";
        public static final String ABILITIES = SERVER + "Abilities";
        public static final String ENTITY_METADATA = SERVER + "EntityMetadata";
        public static final String ENTITY_VELOCITY = SERVER + "EntityVelocity";
        public static final String ENTITY_DESTROY = SERVER + "EntityDestroy";
        public static final String ENTITY_HEAD_ROTATION = SERVER + "EntityHeadRotation";
        public static final String BLOCK_CHANGE = SERVER + "BlockChange";
        public static final String CLOSE_WINDOW = SERVER + "CloseWindow";
        public static final String HELD_ITEM = SERVER + "HeldItemSlot";
        public static final String TAB_COMPLETE = SERVER + "TabComplete";
        public static final String RESPAWN = SERVER + "Respawn";
        public static final String WORLD_PARTICLE = SERVER + "WorldParticles";
        public static final String COMMANDS = SERVER + "Commands";
        public static final String OPEN_WINDOW = SERVER + "OpenWindow";
        public static final String LOGIN = SERVER + "Login";
        public static final String SERVER_DIFFICULTY = SERVER + "ServerDifficulty";
    }

    public static class Login {
        public static final String HANDSHAKE = "PacketHandshakingInSetProtocol";
        public static final String PING = "PacketStatusInPing";
        public static final String START = "PacketStatusInStart";
        public static final String SUCCESS = "PacketLoginOutSuccess";
    }
}
