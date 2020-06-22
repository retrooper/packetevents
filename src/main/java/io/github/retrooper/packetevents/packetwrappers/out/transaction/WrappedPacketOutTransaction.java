package io.github.retrooper.packetevents.packetwrappers.out.transaction;

import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutTransaction extends WrappedPacket implements Sendable {
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
        this.windowId = windowIdAccessor.get(packet);
        this.actionNumber = actionNumberAccessor.get(packet);
        this.accepted = acceptedAccessor.get(packet);
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

    private static Class<?> packetClass;

    private static Constructor<?> packetConstructor;

    private static final Reflection.FieldAccessor<Integer> windowIdAccessor;
    private static final Reflection.FieldAccessor<Short> actionNumberAccessor;
    private static final Reflection.FieldAccessor<Boolean> acceptedAccessor;

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

        windowIdAccessor = Reflection.getField(packetClass, int.class, 0);
        actionNumberAccessor = Reflection.getField(packetClass, short.class, 0);
        acceptedAccessor= Reflection.getField(packetClass, boolean.class, 0);
    }

}
