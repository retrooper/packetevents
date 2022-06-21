package com.github.retrooper.packetevents.protocol.command;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CommandMatch {
    private String text;
    private @Nullable Component tooltip;

    public CommandMatch(String text) {
        this(text, Component.empty());
    }

    public CommandMatch(String text, @Nullable Component tooltip) {
        this.text = text;
        this.tooltip = tooltip;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Optional<Component> getTooltip() {
        return Optional.ofNullable(tooltip);
    }

    public void setTooltip(@Nullable Component tooltip) {
        this.tooltip = tooltip;
    }
}