package io.github.retrooper.packetevents.packetwrappers.login;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.tinyprotocol.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketLoginStatusPing extends WrappedPacket {
    private static final Reflection.FieldAccessor<Long> numberAccessor;
    private static Class<?> packetClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketStatusInPing");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        numberAccessor = Reflection.getField(packetClass, long.class, 0);
    }

    private long number;

    public WrappedPacketLoginStatusPing(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        this.number = numberAccessor.get(packet);
    }

    public long getNumber() {
        return number;
    }

}
