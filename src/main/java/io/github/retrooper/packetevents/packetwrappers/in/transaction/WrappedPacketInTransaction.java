package io.github.retrooper.packetevents.packetwrappers.in.transaction;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketInTransaction extends WrappedPacket {
    private int windowId;
    private short actionNumber;
    private boolean accepted;
    public WrappedPacketInTransaction(final Object packet) {
        super(packet);
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

    private static Class<?> packetClass;

    private static final Reflection.FieldAccessor<Integer> windowIdAccessor;
    private static final Reflection.FieldAccessor<Short> actionNumberAccessor;
    private static final Reflection.FieldAccessor<Boolean> acceptedAccessor;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayInTransaction");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        windowIdAccessor = Reflection.getField(packetClass, int.class, 0);
        actionNumberAccessor = Reflection.getField(packetClass, short.class, 0);
        acceptedAccessor= Reflection.getField(packetClass, boolean.class, 0);
    }
}
