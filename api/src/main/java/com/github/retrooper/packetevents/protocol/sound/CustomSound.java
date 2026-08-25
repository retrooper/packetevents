package com.github.retrooper.packetevents.protocol.sound;

import org.jetbrains.annotations.NotNull;

public class CustomSound implements Sound{
    private final @NotNull String name;

    public CustomSound(@NotNull String name){
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
