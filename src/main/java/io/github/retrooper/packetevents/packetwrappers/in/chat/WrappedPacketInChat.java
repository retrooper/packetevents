package io.github.retrooper.packetevents.packetwrappers.in.chat;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketInChat extends WrappedPacket {
    private static Class<?> chatPacketClass;

    static {
        try {
            chatPacketClass = NMSUtils.getNMSClass("PacketPlayInChat");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String message;

    public WrappedPacketInChat(Object packet) {
        super(packet);
    }


    @Override
    protected void setup() {
        try {
            final Object obj = Reflection.getField(chatPacketClass, String.class, 0).get(packet);
            this.message = obj.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }
}
