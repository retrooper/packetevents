package com.github.retrooper.packetevents.protocol.resourcepack;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;

public enum ResourcePacketResult {
    SUCCESSFULLY_LOADED,
    DECLINED,
    FAILED_DOWNLOAD,
    ACCEPTED;

    private static final ResourcePacketResult[] VALUES = values();

    public static ResourcePacketResult getById(int index) {
        return VALUES[index];
    }
}