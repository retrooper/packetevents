package io.github.retrooper.packetevents.packetwrappers.login;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.tinyprotocol.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.security.PublicKey;


public final class WrappedPacketLoginOutEncryptionBegin extends WrappedPacket {
    private String encodedString;
    private PublicKey encodedKey;
    private byte[] byteArray;

    public WrappedPacketLoginOutEncryptionBegin(final Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        //string - a (encodedstring)
        //public key - b (encoded)
        //byte array - c(byte array)
        this.encodedString = fields[0].get(packet).toString();
        this.encodedKey = (PublicKey) fields[1].get(packet);
        this.byteArray = (byte[]) fields[2].get(packet);
    }

    public String getEncodedString() {
        return encodedString;
    }

    public PublicKey getEncodedKey() {
        return encodedKey;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    private static Class<?> packetClass;

    private static final Reflection.FieldAccessor<?>[] fields = new Reflection.FieldAccessor[3];

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketLoginOutEncryptionBegin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fields[0] = Reflection.getField(packetClass, String.class, 0);
        fields[1] = Reflection.getField(packetClass, PublicKey.class, 1);
        fields[2] = Reflection.getField(packetClass, byte[].class, 2);
    }


}
