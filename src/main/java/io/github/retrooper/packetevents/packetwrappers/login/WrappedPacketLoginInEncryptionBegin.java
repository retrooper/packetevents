package io.github.retrooper.packetevents.packetwrappers.login;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.tinyprotocol.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketLoginInEncryptionBegin extends WrappedPacket {
    private byte[] publicKey, verifyToken;
    public WrappedPacketLoginInEncryptionBegin(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        this.publicKey = fields[0].get(packet);
        this.verifyToken = fields[1].get(packet);
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public byte[] getVerifyToken() {
        return verifyToken;
    }

    private static Class<?> packetClass;

    private static final Reflection.FieldAccessor<byte[]>[] fields = new Reflection.FieldAccessor[2];

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketLoginInEncryptionBegin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fields[0] = Reflection.getField(packetClass, byte[].class, 0);
        fields[1] = Reflection.getField(packetClass, byte[].class, 1);
    }
}
