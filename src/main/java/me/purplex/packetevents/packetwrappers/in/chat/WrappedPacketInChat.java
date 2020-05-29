package me.purplex.packetevents.packetwrappers.in.chat;

import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.utils.NMSUtils;

import java.lang.reflect.Field;

public class WrappedPacketInChat extends WrappedPacket {
    private String message;

    public WrappedPacketInChat(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() throws IllegalAccessException {
        final Object obj = field.get(packet);
        this.message = obj.toString();
    }

    public String getMessage() {
        return message;
    }

    private static Class<?> chatPacketClass;

    private static Field field;

    static {

        try {
            chatPacketClass = NMSUtils.getNMSClass("PacketPlayInChat");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            field = chatPacketClass.getDeclaredField(getChatFieldName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        field.setAccessible(true);
    }

    private static String getChatFieldName() {
        if (version == ServerVersion.v_1_7_10) {
            return "message";
        }
        return "a";
    }
}
