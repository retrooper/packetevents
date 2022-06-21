package com.github.retrooper.packetevents.protocol.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

public class TeamInfo {
    private @Nullable Component displayName;
    private @Nullable Component prefix;
    private @Nullable Component suffix;
    private NameTagVisibility tagVisibility;
    private CollisionRule collisionRule;
    private NamedTextColor color;
    private OptionData optionData;

    public TeamInfo(@Nullable Component displayName, @Nullable Component prefix, @Nullable Component suffix, NameTagVisibility tagVisibility,
                    CollisionRule collisionRule, NamedTextColor color, OptionData optionData) {
        this.displayName = displayName;
        if (prefix == null) {
            prefix = Component.empty();
        }
        if (suffix == null) {
            suffix = Component.empty();
        }
        this.prefix = prefix;
        this.suffix = suffix;
        this.tagVisibility = tagVisibility;
        this.collisionRule = collisionRule;
        this.color = color;
        this.optionData = optionData;
    }

    public @Nullable Component getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
    }

    public @Nullable Component getPrefix() {
        return prefix;
    }

    public void setPrefix(@Nullable Component prefix) {
        this.prefix = prefix;
    }

    public @Nullable Component getSuffix() {
        return suffix;
    }

    public void setSuffix(@Nullable Component suffix) {
        this.suffix = suffix;
    }

    public NameTagVisibility getTagVisibility() {
        return tagVisibility;
    }

    public void setTagVisibility(NameTagVisibility tagVisibility) {
        this.tagVisibility = tagVisibility;
    }

    public CollisionRule getCollisionRule() {
        return collisionRule;
    }

    public void setCollisionRule(CollisionRule collisionRule) {
        this.collisionRule = collisionRule;
    }

    public NamedTextColor getColor() {
        return color;
    }

    public void setColor(NamedTextColor color) {
        this.color = color;
    }

    public OptionData getOptionData() {
        return optionData;
    }

    public void setOptionData(OptionData optionData) {
        this.optionData = optionData;
    }
}