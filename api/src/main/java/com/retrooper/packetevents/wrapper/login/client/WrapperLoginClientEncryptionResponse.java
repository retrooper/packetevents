package com.retrooper.packetevents.wrapper.login.client;

import com.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.retrooper.packetevents.protocol.packettype.PacketType;
import com.retrooper.packetevents.protocol.data.player.ClientVersion;
import com.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper<WrapperLoginClientEncryptionResponse> {
    private byte[] sharedSecret;
    private byte[] verifyToken;

    public WrapperLoginClientEncryptionResponse(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperLoginClientEncryptionResponse(ClientVersion clientVersion, byte[] sharedSecret, byte[] verifyToken) {
        super(PacketType.Login.Client.ENCRYPTION_RESPONSE.getID(), clientVersion);
        this.sharedSecret = sharedSecret;
        this.verifyToken = verifyToken;
    }

    @Override
    public void readData() {
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

    @Override
    public void readData(WrapperLoginClientEncryptionResponse wrapper) {
        this.sharedSecret = wrapper.sharedSecret;
        this.verifyToken = wrapper.verifyToken;
    }

    @Override
    public void writeData() {
        if (clientVersion.isOlderThan(ClientVersion.v_1_10)) {
            writeVarInt(sharedSecret.length);
            writeVarInt(verifyToken.length);
        }
        writeByteArray(sharedSecret);
        writeByteArray(verifyToken);
    }

    public byte[] getSharedSecret() {
        return this.sharedSecret;
    }

    public void setSharedSecret(byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    public void setVerifyToken(byte[] verifyToken) {
        this.verifyToken = verifyToken;
    }
}
