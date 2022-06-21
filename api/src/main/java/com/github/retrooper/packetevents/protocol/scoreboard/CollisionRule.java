package com.github.retrooper.packetevents.protocol.scoreboard;

import org.jetbrains.annotations.Nullable;

public enum CollisionRule {
    ALWAYS("always"),
    NEVER("never"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam");

    private static final CollisionRule[] VALUES = CollisionRule.values();
    private final String id;

    CollisionRule(String id) {
        this.id = id;
    }

    @Nullable
    public static CollisionRule getById(String id) {
        for (CollisionRule value : VALUES) {
            if (value.id.equalsIgnoreCase(id)) {
                return value;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }
}