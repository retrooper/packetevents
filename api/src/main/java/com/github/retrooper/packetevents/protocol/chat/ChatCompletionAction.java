package com.github.retrooper.packetevents.protocol.chat;

public enum ChatCompletionAction {
    ADD,
    REMOVE,
    SET;

    private static final ChatCompletionAction[] VALUES = values();

    public static ChatCompletionAction fromId(int id) {
        return VALUES[id];
    }
}
