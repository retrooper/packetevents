package io.github.retrooper.packetevents.wrapper.login.client;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper {
    private final int SHARED_SECRET_LENGTH;
    private final int VERIFY_TOKEN_LENGTH;
    private final byte[] sharedSecret;
    private final byte[] verifyToken;

    public WrapperLoginClientEncryptionResponse(ByteBufAbstract byteBuf) {
        super(byteBuf);
        this.SHARED_SECRET_LENGTH = readVarInt();
        this.VERIFY_TOKEN_LENGTH = readVarInt();

        this.sharedSecret = readByteArray(SHARED_SECRET_LENGTH);
        this.verifyToken = readByteArray(VERIFY_TOKEN_LENGTH);
    }

    public byte[] getSharedSecret(){
        return this.sharedSecret;
    }

    public byte[] getVerifyToken(){
        return this.verifyToken;
    }
}
