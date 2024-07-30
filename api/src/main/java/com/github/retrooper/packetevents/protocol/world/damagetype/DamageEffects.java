package com.github.retrooper.packetevents.protocol.world.damagetype;

import net.kyori.adventure.util.Index;

public enum DamageEffects {
    HURT("hurt"),
    THORNS("thorns"),
    DROWNING("drowning"),
    BURNING("burning"),
    POKING("poking"),
    FREEZING("freezing");

    public static final Index<String, DamageEffects> ID_INDEX = Index.create(DamageEffects.class, DamageEffects::getId);

    private final String id;

    DamageEffects(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
