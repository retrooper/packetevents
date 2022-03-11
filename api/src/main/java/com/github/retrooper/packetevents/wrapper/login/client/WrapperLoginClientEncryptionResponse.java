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

package com.github.retrooper.packetevents.wrapper.login.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.MinecraftEncryptionUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper<WrapperLoginClientEncryptionResponse> {
    private byte[] encryptedSharedSecret;
    private byte[] encryptedVerifyToken;

    public WrapperLoginClientEncryptionResponse(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperLoginClientEncryptionResponse(ClientVersion clientVersion, byte[] encryptedSharedSecret, byte[] encryptedVerifyToken) {
        super(PacketType.Login.Client.ENCRYPTION_RESPONSE.getId(), clientVersion);
        this.encryptedSharedSecret = encryptedSharedSecret;
        this.encryptedVerifyToken = encryptedVerifyToken;
    }

    @Override
    public void read() {
        this.encryptedSharedSecret = readByteArray(ByteBufHelper.readableBytes(buffer));
        this.encryptedVerifyToken = readByteArray(ByteBufHelper.readableBytes(buffer));
    }

    @Override
    public void copy(WrapperLoginClientEncryptionResponse wrapper) {
        this.encryptedSharedSecret = wrapper.encryptedSharedSecret;
        this.encryptedVerifyToken = wrapper.encryptedVerifyToken;
    }

    @Override
    public void write() {
        writeByteArray(encryptedSharedSecret);
        writeByteArray(encryptedVerifyToken);
    }

    public byte[] getEncryptedSharedSecret() {
        return this.encryptedSharedSecret;
    }

    public void setEncryptedSharedSecret(byte[] encryptedSharedSecret) {
        this.encryptedSharedSecret = encryptedSharedSecret;
    }

    public SecretKey getSecretKey(PrivateKey key) {
        byte[] data = getEncryptedSharedSecret();
        byte[] decryptedData = MinecraftEncryptionUtil.decrypt(key.getAlgorithm(), key, data);
        if (decryptedData != null) {
            return new SecretKeySpec(decryptedData, "AES");
        }
        else {
            return null;
        }
    }

    //TODO Confirm is this is correct
    public void setSharedKey(SecretKey key, PublicKey publicKey) {
        this.encryptedSharedSecret = MinecraftEncryptionUtil.encrypt(publicKey.getAlgorithm(), publicKey, key.getEncoded());
    }


    public byte[] getEncryptedVerifyToken() {
        return this.encryptedVerifyToken;
    }

    public void setEncryptedVerifyToken(byte[] encryptedVerifyToken) {
        this.encryptedVerifyToken = encryptedVerifyToken;
    }
}
