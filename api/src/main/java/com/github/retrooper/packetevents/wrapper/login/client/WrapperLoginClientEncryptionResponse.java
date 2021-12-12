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

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper<WrapperLoginClientEncryptionResponse> {
    private byte[] sharedSecret;
    private byte[] encryptedVerifyToken;

    public WrapperLoginClientEncryptionResponse(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperLoginClientEncryptionResponse(ClientVersion clientVersion, byte[] sharedSecret, byte[] encryptedVerifyToken) {
        super(PacketType.Login.Client.ENCRYPTION_RESPONSE.getId(), clientVersion);
        this.sharedSecret = sharedSecret;
        this.encryptedVerifyToken = encryptedVerifyToken;
    }

    @Override
    public void readData() {
        this.sharedSecret = readByteArray(buffer.readableBytes());
        this.encryptedVerifyToken = readByteArray(buffer.readableBytes());
    }

    @Override
    public void readData(WrapperLoginClientEncryptionResponse wrapper) {
        this.sharedSecret = wrapper.sharedSecret;
        this.encryptedVerifyToken = wrapper.encryptedVerifyToken;
    }

    @Override
    public void writeData() {
        writeByteArray(sharedSecret);
        writeByteArray(encryptedVerifyToken);
    }

    public byte[] getSharedSecret() {
        return this.sharedSecret;
    }

    public void setSharedSecret(byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public byte[] getEncryptedVerifyToken() {
        return this.encryptedVerifyToken;
    }

    public void setEncryptedVerifyToken(byte[] encryptedVerifyToken) {
        this.encryptedVerifyToken = encryptedVerifyToken;
    }


    //PrivateKey should be generated from the server key pair
    public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] encrypt(PublicKey publicKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }



}
