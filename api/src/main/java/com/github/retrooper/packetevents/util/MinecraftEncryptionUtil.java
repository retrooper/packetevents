package com.github.retrooper.packetevents.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

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
}
