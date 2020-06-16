package io.github.retrooper.packetevents.packetwrappers.in.keepalive;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Field;

public final class WrappedPacketInKeepAlive extends WrappedPacket {
    public WrappedPacketInKeepAlive(final Object packet) {
        super(packet);
    }

    private long id;

    @Override
    protected void setup() {
        try {
            if (version.isHigherThan(ServerVersion.v_1_12)) {
                this.id = idField.getLong(packet);
            } else {
                this.id = idField.getInt(packet);
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

    private static Class<?> keepAliveClass;

    //1.7.10->1.12.2 has int field "a", rest as long field "a"
    private static Field idField;

    static {
        try {
            keepAliveClass = NMSUtils.getNMSClass("PacketPlayInKeepAlive");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            idField = keepAliveClass.getDeclaredField("a");
            idField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
