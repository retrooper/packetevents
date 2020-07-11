package io.github.retrooper.packetevents.enums.minecraft;

public enum Gamemode {
    NOT_SET(-1, ""),
    SURVIVAL(0, "survival"),
    CREATIVE(1, "creative"),
    ADVENTURE(2, "adventure"),
    SPECTATOR(3, "spectator");

    int f;
    String g;

    Gamemode(int var3, String var4) {
        this.f = var3;
        this.g = var4;
    }

    public int getId() {
        return this.f;
    }

    public String b() {
        return this.g;
    }
}
