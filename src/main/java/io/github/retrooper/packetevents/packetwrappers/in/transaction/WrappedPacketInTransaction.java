package io.github.retrooper.packetevents.packetwrappers.in.transaction;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketInTransaction extends WrappedPacket {
    private static Class<?> packetClass;

    public static void load() {
        packetClass = PacketTypeClasses.Client.TRANSACTION;
    }

    private int windowId;
    private short actionNumber;
    private boolean accepted;

    public WrappedPacketInTransaction(final Object packet) {
        super(packet);
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
}
