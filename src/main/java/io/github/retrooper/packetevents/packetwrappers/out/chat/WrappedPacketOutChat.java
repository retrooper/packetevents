package io.github.retrooper.packetevents.packetwrappers.out.chat;

import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutChat extends WrappedPacket implements Sendable {
    private static Constructor<?> chatClassConstructor;
    private static Class<?> chatClass, iChatBaseComponentClass, chatSerializerClass;

    static {
        try {
            chatClass = NMSUtils.getNMSClass("PacketPlayOutChat");
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //In 1.8.3+ the ChatSerializer class is declared in the IChatBaseComponent class, so we have to handle that
        try {
            chatSerializerClass = NMSUtils.getNMSClass("ChatSerializer");
        }
        catch(ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            chatSerializerClass = Reflection.getSubClass(iChatBaseComponentClass, "ChatSerializer");
        }

        try {
            chatClassConstructor = chatClass.getConstructor(iChatBaseComponentClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


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
            return Reflection.getMethod(iChatBaseComponentClass, String.class, 0).invoke(obj).toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toIChatBaseComponent(String msg) {
        try {
            return Reflection.getMethod(chatSerializerClass, 0, String.class).invoke(null, msg);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void setup() {
        try {
            final Object iChatBaseObj = Reflection.getField(chatClass, iChatBaseComponentClass, 0).get(packet);

            final Object contentString = Reflection.getMethod(iChatBaseComponentClass, String.class, 0).invoke(iChatBaseObj);

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
