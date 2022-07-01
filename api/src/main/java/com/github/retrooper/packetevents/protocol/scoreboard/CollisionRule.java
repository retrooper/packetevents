package com.github.retrooper.packetevents.protocol.scoreboard;

import org.jetbrains.annotations.Nullable;

public enum CollisionRule {
    ALWAYS("always"),
    NEVER("never"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam");

    private static final CollisionRule[] VALUES = CollisionRule.values();
    private final String name;

    CollisionRule(String name) {
        this.name = name;
    }

    @Nullable
    public static CollisionRule getByName(String name) {
        for (CollisionRule value : VALUES) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}