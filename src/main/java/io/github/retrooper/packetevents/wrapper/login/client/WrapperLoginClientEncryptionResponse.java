package io.github.retrooper.packetevents.wrapper.login.client;

import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper {
    private final byte[] sharedSecret;
    private final byte[] verifyToken;

    public WrapperLoginClientEncryptionResponse(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        if (version.isNewerThanOrEquals(ClientVersion.v_1_10)) {
            this.sharedSecret = readByteArray(byteBuf.readableBytes());
            this.verifyToken = readByteArray(byteBuf.readableBytes());
        }
        else {
            int sharedSecretLength = readVarInt();
            int verifyTokenLength = readVarInt();
            this.sharedSecret = readByteArray(sharedSecretLength);
            this.verifyToken = readByteArray(verifyTokenLength);
        }
    }

    public byte[] getSharedSecret(){
        return this.sharedSecret;
    }

    public byte[] getVerifyToken(){
        return this.verifyToken;
    }
}
