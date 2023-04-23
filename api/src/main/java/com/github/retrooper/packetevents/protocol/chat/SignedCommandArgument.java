package com.github.retrooper.packetevents.protocol.chat;

public class SignedCommandArgument {
    private String argument;
    private byte[] signature;

    public SignedCommandArgument(String argument, byte[] signature) {
        this.argument = argument;
        this.signature = signature;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
