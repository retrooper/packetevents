package io.github.retrooper.packetevents.utils.gameprofile;

import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;

import java.security.*;

public class WrappedProperty {
    private final String name;
    private final String value;
    private final String signature;
    public WrappedProperty(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public WrappedProperty(String name, String value) {
        this(name, value, null);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public boolean hasSignature() {
        return this.signature != null;
    }

    public boolean isSignatureValid(PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(this.value.getBytes());
            return signature.verify(Base64.decodeBase64(this.signature));
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
        } catch (InvalidKeyException var4) {
            var4.printStackTrace();
        } catch (SignatureException var5) {
            var5.printStackTrace();
        }

        return false;
    }
}
