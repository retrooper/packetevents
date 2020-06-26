package io.github.retrooper.packetevents.packetwrappers.in.keepalive;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketInKeepAlive extends WrappedPacket {
    private static Class<?> keepAliveClass;

    static {
        try {
            keepAliveClass = NMSUtils.getNMSClass("PacketPlayInKeepAlive");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private long id;

    public WrappedPacketInKeepAlive(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        try {
            if (version.isHigherThan(ServerVersion.v_1_12)) {
                this.id = Reflection.getField(keepAliveClass, long.class, 0).getLong(packet);
            } else {
                this.id = Reflection.getField(keepAliveClass, int.class, 0).getInt(packet);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cast this long to an integer if you are using 1.7.10->1.12.2!
     * In 1.13.2->1.15.2 a long is sent
     *
     * @return
     */
    public long getId() {
        return id;
    }
}
