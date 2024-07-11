package com.github.retrooper.packetevents.protocol.player.storage;

public abstract class StorageIdBase {

    private final int id;

    StorageIdBase(int id) {
        this.id = id;
    }

    int id() {
        return this.id;
    }
}