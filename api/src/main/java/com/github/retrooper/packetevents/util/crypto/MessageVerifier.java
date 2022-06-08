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

package com.github.retrooper.packetevents.util.crypto;

import com.github.retrooper.packetevents.util.AdventureSerializer;
import net.kyori.adventure.text.Component;

import java.nio.ByteBuffer;
import java.security.*;
import java.util.UUID;

public class MessageVerifier {
    private static byte[] byteArray(long value) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xffL);
            value >>= 8;
        }
        return result;
    }

    public static boolean verify(UUID uuid, MessageSignData signData, PublicKey publicKey, Component component)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return verify(uuid, signData, publicKey, AdventureSerializer.toJson(component));
    }

    public static boolean verify(UUID uuid, MessageSignData signData, PublicKey publicKey, String jsonMessage)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //Create signature
        Signature signature = Signature.getInstance("SHA256withDSA");
        //Initialize it with public key
        signature.initVerify(publicKey);
        //Adding data to be verified (Salt, UUID, Timestamp, Message)

        //Verify salt
        signature.update(byteArray(signData.getSaltSignature().getSalt()));

        //Verify UUID
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        signature.update(bb.array());

        //Verify timestamp
        signature.update(byteArray(signData.getTimestamp()));

        //Verify message content
        signature.update(jsonMessage.getBytes());

        //Verifying the signature
        return signature.verify(signData.getSaltSignature().getSignature());
    }
}
