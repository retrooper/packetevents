/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.wrapper.login.server;

import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.wrapper.SendablePacketWrapper;
import io.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientEncryptionResponse;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * This packet is sent by the server to the client if the server is in online mode.
 * If the server is in offline mode, this packet won't be sent,
 * encryption won't be enabled and no authentication will be performed.
 *
 * @see WrapperLoginClientEncryptionResponse
 */
public class WrapperLoginServerEncryptionRequest extends SendablePacketWrapper {
    private final String serverID;
    private final PublicKey publicKey;
    private final byte[] verifyToken;

    public WrapperLoginServerEncryptionRequest(ByteBufAbstract byteBuf) {
        super(byteBuf);
        this.serverID = readString(20);
        int publicKeyLength = readVarInt();
        byte[] publicKeyBytes = readByteArray(publicKeyLength);
        this.publicKey = encrypt(publicKeyBytes);
        int verifyTokenLength = readVarInt();
        this.verifyToken = readByteArray(verifyTokenLength);
    }

    public WrapperLoginServerEncryptionRequest(String serverID, byte[] publicKeyBytes, byte[] verifyToken) {
        super(PacketType.Login.Server.ENCRYPTION_REQUEST.getID());
        this.serverID = serverID;
        this.publicKey = encrypt(publicKeyBytes);
        this.verifyToken = verifyToken;
    }

    public WrapperLoginServerEncryptionRequest(String serverID, PublicKey publicKey, byte[] verifyToken) {
        super(PacketType.Login.Server.ENCRYPTION_REQUEST.getID());
        this.serverID = serverID;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    /**
     * This is an empty string on the vanilla client. (1.7.10 and above)
     *
     * @return Server ID
     */
    public String getServerID() {
        return serverID;
    }

    /**
     * The public key is in DER encoding format.
     * More technically, it is in ASN.1 format.
     * The public key will be encrypted by the client and
     * sent to the server via the {@link WrapperLoginClientEncryptionResponse} packet.
     *
     * @return Public key
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * The verify token will be encrypted by the client and
     * sent to the server via the {@link WrapperLoginClientEncryptionResponse} packet.
     *
     * @return Verify token
     */
    public byte[] getVerifyToken() {
        return verifyToken;
    }

    private PublicKey encrypt(byte[] bytes) {
        try {
            EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(encodedKeySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void createPacket() {
        writeString(serverID, 20);
        byte[] encoded = publicKey.getEncoded();
        writeVarInt(encoded.length);
        writeByteArray(encoded);
        writeVarInt(verifyToken.length);
        writeByteArray(verifyToken);
    }
}
