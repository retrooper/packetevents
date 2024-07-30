package com.github.retrooper.packetevents.protocol.world.damagetype;

import net.kyori.adventure.util.Index;

public enum DamageScaling {
    NEVER("never"),
    WHEN_CAUSED_BY_LIVING_NON_PLAYER("when_caused_by_living_non_player"),
    ALWAYS("always");

    public static final Index<String, DamageScaling> ID_INDEX = Index.create(DamageScaling.class, DamageScaling::getId);

    private final String id;

    DamageScaling(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
