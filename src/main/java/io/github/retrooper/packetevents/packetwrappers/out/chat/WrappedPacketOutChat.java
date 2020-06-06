package io.github.retrooper.packetevents.packetwrappers.out.chat;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WrappedPacketOutChat extends WrappedPacket implements Sendable {
    private String message;

    public WrappedPacketOutChat(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutChat(final String message) {
        super(null);
        this.message = message;
    }

    @Override
    protected void setup() {

    }

    @Override
    public Object asNMSPacket() {
        Object iChatBaseComponentObj = null;
        try {
            iChatBaseComponentObj = serialize.invoke(null, this.message);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            return chatClassConstructor.newInstance(iChatBaseComponentObj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getMessage() {
        return this.message;
    }

    private static Class<?> chatClass;

    private static Constructor<?> chatClassConstructor;

    private static Class<?> iChatBaseComponentClass;

    private static Class<?> chatSerializer;

    private static Method serialize;

    static {
        try {
            chatClass = NMSUtils.getNMSClass("PacketPlayOutChat");
            final String subClass = version.isHigherThan(ServerVersion.v_1_8) ? "IChatBaseComponent$" : "";
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
            chatSerializer = NMSUtils.getNMSClass(subClass + "ChatSerializer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            chatClassConstructor = chatClass.getConstructor(iChatBaseComponentClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            serialize = chatSerializer.getMethod("a", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
