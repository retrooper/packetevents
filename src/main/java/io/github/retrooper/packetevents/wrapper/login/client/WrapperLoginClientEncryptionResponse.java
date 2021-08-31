package io.github.retrooper.packetevents.wrapper.login.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper {
    private final byte[] sharedSecret;
    private final byte[] verifyToken;

    public WrapperLoginClientEncryptionResponse(PacketReceiveEvent event) {
        super(event);
        if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_10)) {
            this.sharedSecret = readByteArray(byteBuf.readableBytes());
            this.verifyToken = readByteArray(byteBuf.readableBytes());
        } else {
            int sharedSecretLength = readVarInt();
            int verifyTokenLength = readVarInt();
            this.sharedSecret = readByteArray(sharedSecretLength);
            this.verifyToken = readByteArray(verifyTokenLength);
        }
    }

    public byte[] getSharedSecret() {
        return this.sharedSecret;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }
}
