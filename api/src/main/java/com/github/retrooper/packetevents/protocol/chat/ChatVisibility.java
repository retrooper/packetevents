package com.github.retrooper.packetevents.protocol.chat;

public enum ChatVisibility {
    FULL,
    SYSTEM,
    HIDDEN;

    private static final ChatVisibility[] VALUES = values();

    public static ChatVisibility getById(int index) {
        return VALUES[index];
    }
}
