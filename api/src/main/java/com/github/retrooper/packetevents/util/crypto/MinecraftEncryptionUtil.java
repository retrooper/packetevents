/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

import org.jetbrains.annotations.ApiStatus;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class MinecraftEncryptionUtil {
    /**
     * This decrypts the specified byte data using RSA PKCS#1 padding.
     *
     * @param privateKey Private key from the server key pair
     * @param data       Encrypted data
     * @return Decrypted data
     */
    public static byte[] decryptRSA(PrivateKey privateKey, byte[] data) {
        return decrypt("RSA/ECB/PKCS1Padding", privateKey, data);
    }

    /**
     * This encrypts the specified byte data using RSA PKCS#1 padding.
     *
     * @param publicKey Public key from the server key pair
     * @param data      Decrypted data
     * @return Encrypted data
     */
    public static byte[] encryptRSA(PublicKey publicKey, byte[] data) {
        return encrypt("RSA/ECB/PKCS1Padding", publicKey, data);
    }

    public static byte[] decrypt(Cipher cipher, byte[] data) {
        try {
            return cipher.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] decrypt(String algorithm, PrivateKey privateKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] encrypt(String algorithm, PublicKey publicKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] encrypt(Cipher cipher, byte[] data) {
        //This is on purpose, they work the same way
        return decrypt(cipher, data);
    }

    public static PublicKey publicKey(byte[] bytes) {
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
