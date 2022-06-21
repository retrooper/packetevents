package com.github.retrooper.packetevents.protocol.scoreboard;

import org.jetbrains.annotations.Nullable;

public enum NameTagVisibility {
    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam");

    private static final NameTagVisibility[] VALUES = NameTagVisibility.values();
    private final String id;

    NameTagVisibility(String id) {
        this.id = id;
    }

    @Nullable
    public static NameTagVisibility getById(String id) {
        for (NameTagVisibility value : VALUES) {
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