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

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class WrapperLoginServerEncryptionRequest extends PacketWrapper {
    private final String serverID;
    private final PublicKey publicKey;
    private final byte[] verifyToken;

    public WrapperLoginServerEncryptionRequest(ByteBufAbstract byteBuf) {
        super(byteBuf);
        this.serverID = readString(20);
        int publicKeyLength = readVarInt();
        byte[] publicKeyBytes = readByteArray(publicKeyLength);
        //To decode use PublicKey#getEncoded
        this.publicKey = encrypt(publicKeyBytes);
        int verifyTokenLength = readVarInt();
        this.verifyToken = readByteArray(verifyTokenLength);
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
}
