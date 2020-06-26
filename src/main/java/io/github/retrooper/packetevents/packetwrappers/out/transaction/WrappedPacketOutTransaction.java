package io.github.retrooper.packetevents.packetwrappers.out.transaction;

import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutTransaction extends WrappedPacket implements Sendable {
    private static Class<?> packetClass;
    private static Constructor<?> packetConstructor;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayOutTransaction");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            packetConstructor = packetClass.getConstructor(int.class, short.class, boolean.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private int windowId;
    private short actionNumber;
    private boolean accepted;

    public WrappedPacketOutTransaction(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutTransaction(final int windowId, final short actionNumber, final boolean accepted) {
        super(null);
        this.windowId = windowId;
        this.actionNumber = actionNumber;
        this.accepted = accepted;
    }

    @Override
    protected void setup() {
        try {
            this.windowId = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            this.actionNumber = Reflection.getField(packetClass, short.class, 0).getShort(packet);
            this.accepted = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public int getWindowId() {
        return windowId;
    }

    public short getActionNumber() {
        return actionNumber;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(windowId, actionNumber, accepted);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
