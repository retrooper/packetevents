package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import org.jetbrains.annotations.NotNull;

// From MCProtocolLib
public class Ingredient {
    private final @NotNull ItemStack[] options;

    public Ingredient(@NotNull ItemStack... options) {
        this.options = options;
    }

    public @NotNull ItemStack[] getOptions() {
        return options;
    }
}