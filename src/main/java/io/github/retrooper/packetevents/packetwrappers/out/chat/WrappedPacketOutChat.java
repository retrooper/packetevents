package io.github.retrooper.packetevents.packetwrappers.out.chat;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class WrappedPacketOutChat extends WrappedPacket implements Sendable {
    private static Constructor<?> chatClassConstructor;
    private static Class<?> chatClass, iChatBaseComponentClass, chatSerializerClass;
    private static Field iChatBaseField;
    private static Method serialize, getStringOfIChatBase;

    static {
        try {
            chatClass = NMSUtils.getNMSClass("PacketPlayOutChat");
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");

            //In 1.8.3+ the ChatSerializer class is declared in the IChatBaseComponent class, so we have to handle tahat
            if (version.isHigherThan(ServerVersion.v_1_8)) {
                for (final Class<?> cls : iChatBaseComponentClass.getDeclaredClasses()) {
                    if (cls.getSimpleName().equals("ChatSerializer")) {
                        chatSerializerClass = cls;
                        break;
                    }
                }
            } else {
                chatSerializerClass = NMSUtils.getNMSClass("ChatSerializer");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            chatClassConstructor = chatClass.getConstructor(iChatBaseComponentClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            serialize = chatSerializerClass.getMethod("a", String.class);
            getStringOfIChatBase = iChatBaseComponentClass.getMethod(getTextMethodName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            iChatBaseField = chatClass.getDeclaredField("a");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        iChatBaseField.setAccessible(true);

    }

    private String message;

    public WrappedPacketOutChat(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutChat(final String message) {
        super(null);
        this.message = "{\"text\": \"" + message + "\"}";
    }

    public static String toStringFromIChatBaseComponent(Object obj) {
        try {
            return getStringOfIChatBase.invoke(obj).toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toIChatBaseComponent(String msg) {
        try {
            return serialize.invoke(null, msg);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getTextMethodName() {
        //1.7.10
        if (version.isLowerThan(ServerVersion.v_1_8)) {
            return "e"; //or c
        } else {
            return "getText";
        }
    }

    @Override
    protected void setup() {
        //all fields are IChatBaseComponents, and named 'a'

        try {
            Object iChatBaseObj = iChatBaseField.get(packet);

            Object contentString = getStringOfIChatBase.invoke(iChatBaseObj);

            this.message = contentString.toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return chatClassConstructor.newInstance(toIChatBaseComponent(this.message));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getMessage() {
        return this.message;
    }
}
