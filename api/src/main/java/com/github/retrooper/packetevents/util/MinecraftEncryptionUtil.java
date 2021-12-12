package com.github.retrooper.packetevents.util;

public class MinecraftEncryptionUtil {
    /**
     * This decrypts the specified byte data using RSA PKCS#1 padding.
     *
     * @param privateKey Private key from the server key pair
     * @param data       Encrypted data
     * @return Decrypted data
     */
    public static byte[] decryptRSA(PrivateKey privateKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * This encrypts the specified byte data using RSA PKCS#1 padding.
     *
     * @param publicKey Public key from the server key pair
     * @param data      Decrypted data
     * @return Encrypted data
     */
    public static byte[] encryptRSA(PublicKey publicKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * This decrypts the specified byte data using AES-CFB8-128 without padding.
     *
     * @param initialVector Used as the IV and Secret Key Spec in the cipher
     * @param data          Encrypted data
     * @return Decrypted data
     */
    public static byte[] decryptAES(byte[] initialVector, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, initialVector, initialVector);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * This encrypts the specified byte data using AES-CFB8-128 without padding.
     *
     * @param initialVector Used as the IV and Secret Key Spec in the cipher
     * @param data          Decrypted data
     * @return Encrypted data
     */
    public static void encryptAES(byte[] initialVector, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, initialVector, initalVector);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
