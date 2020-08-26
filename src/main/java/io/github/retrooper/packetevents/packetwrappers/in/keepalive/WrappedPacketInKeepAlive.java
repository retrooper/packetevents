package io.github.retrooper.packetevents.packetwrappers.in.keepalive;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketInKeepAlive extends WrappedPacket {
    private static Class<?> packetClass;
    private static boolean integerPresentInIndex0;
    private long id;

    public WrappedPacketInKeepAlive(final Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.KEEP_ALIVE;
        integerPresentInIndex0 = Reflection.getField(packetClass, int.class, 0) != null;
    }

    @Override
    protected void setup() {
        if (!integerPresentInIndex0) {
            try {
                this.id = Reflection.getField(packetClass, long.class, 0).getLong(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.id = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Optionally Cast this long to an integer if you are using 1.7.10->1.12.2!
     * In 1.13.2->1.16.1 a long is sent
     *
     * @return response is
     */
    public long getId() {
        return id;
    }
}
