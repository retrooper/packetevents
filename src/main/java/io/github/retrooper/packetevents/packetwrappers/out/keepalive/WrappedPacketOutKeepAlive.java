package io.github.retrooper.packetevents.packetwrappers.out.keepalive;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public class WrappedPacketOutKeepAlive extends WrappedPacket {
    private static Class<?> packetClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayOutKeepAlive");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private long id;

    public WrappedPacketOutKeepAlive(Object packet) {
        super(packet);
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

    @Override
    protected void setup() {
        if (Reflection.getField(packetClass, int.class, 0) == null) {
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
}
