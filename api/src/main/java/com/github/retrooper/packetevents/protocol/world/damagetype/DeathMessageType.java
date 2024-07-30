package com.github.retrooper.packetevents.protocol.world.damagetype;

import net.kyori.adventure.util.Index;

public enum DeathMessageType {
    DEFAULT("default"),
    FALL_VARIANTS("fall_variants"),
    INTENTIONAL_GAME_DESIGN("intentional_game_design");

    public static final Index<String, DeathMessageType> ID_INDEX = Index.create(DeathMessageType.class,
            DeathMessageType::getId);

    private final String id;

    DeathMessageType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
