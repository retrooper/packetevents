package io.github.retrooper.packetevents.packetwrappers.login;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.tinyprotocol.Reflection;
import io.github.retrooper.packetevents.tinyprotocol.Reflection.FieldAccessor;
import io.github.retrooper.packetevents.utils.NMSUtils;


public final class WrappedPacketLoginHandshake extends WrappedPacket {
    private static Class<?> loginHandshakeClass;
    private static final FieldAccessor<?>[] fields = new FieldAccessor[3];

    static {
        try {
            loginHandshakeClass = NMSUtils.getNMSClass("PacketHandshakingInSetProtocol");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fields[0] = Reflection.getField(loginHandshakeClass, int.class, 0);
        fields[1] = Reflection.getField(loginHandshakeClass, String.class, 0);
        fields[2] = Reflection.getField(loginHandshakeClass, int.class, 1);
    }

    private int protocolVersion, port;
    private String hostname;

    public WrappedPacketLoginHandshake(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        final Object protocolVersionObj = fields[0].get(packet);
        final Object hostNameObj = fields[1].get(packet);
        final Object portObj = fields[2].get(packet);

        this.protocolVersion = (int) protocolVersionObj;
        this.hostname = hostNameObj.toString();
        this.port = (int) portObj;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
