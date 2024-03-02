package com.github.retrooper.packetevents.protocol.chat;

public class SignedCommandArgument {
    private String argument;
    private MessageSignature signature;

    public SignedCommandArgument(String argument, MessageSignature signature) {
        this.argument = argument;
        this.signature = signature;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public MessageSignature getSignature() {
        return signature;
    }

    public void setSignature(MessageSignature signature) {
        this.signature = signature;
    }
}
