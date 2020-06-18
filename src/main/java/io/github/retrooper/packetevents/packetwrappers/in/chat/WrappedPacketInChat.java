package io.github.retrooper.packetevents.packetwrappers.in.chat;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Field;

public final class WrappedPacketInChat extends WrappedPacket {
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

    private String message;

    public WrappedPacketInChat(Object packet) {
        super(packet);
    }

    private static String getChatFieldName() {
        if (version == ServerVersion.v_1_7_10) {
            return "message";
        }
        return "a";
    }

    @Override
    protected void setup() {
        try {
            final Object obj = field.get(packet);
            this.message = obj.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }
}
