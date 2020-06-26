package io.github.retrooper.packetevents.packetwrappers.out.kickdisconnect;

import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutKickDisconnect extends WrappedPacket implements Sendable {
    private static Class<?> packetClass, iChatBaseComponentClass;
    private static Constructor<?> kickDisconnectConstructor;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayOutKickDisconnect");
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            kickDisconnectConstructor = packetClass.getConstructor(iChatBaseComponentClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private String kickMessage;

    public WrappedPacketOutKickDisconnect(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutKickDisconnect(final String kickMessage) {
        super(null);
        this.kickMessage = kickMessage;
    }

    @Override
    protected void setup() {
        try {
            this.kickMessage = WrappedPacketOutChat.toStringFromIChatBaseComponent(Reflection.getField(packetClass, iChatBaseComponentClass, 0).get(packet));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getKickMessage() {
        return kickMessage;
    }

    @Override
    public Object asNMSPacket() {
        try {
            return kickDisconnectConstructor.newInstance(WrappedPacketOutChat.toIChatBaseComponent("{\"text\": \"" + kickMessage + "\"}"));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
