package com.github.retrooper.packetevents.protocol.scoreboard;

import org.jetbrains.annotations.Nullable;

public enum NameTagVisibility {
    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam");

    private static final NameTagVisibility[] VALUES = NameTagVisibility.values();
    private final String name;

    NameTagVisibility(String name) {
        this.name = name;
    }

    @Nullable
    public static NameTagVisibility getByName(String name) {
        for (NameTagVisibility value : VALUES) {
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