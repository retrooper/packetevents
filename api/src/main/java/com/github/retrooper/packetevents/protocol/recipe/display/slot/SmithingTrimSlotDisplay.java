/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.recipe.display.slot;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public class SmithingTrimSlotDisplay extends SlotDisplay<SmithingTrimSlotDisplay> {

    private SlotDisplay<?> base;
    private SlotDisplay<?> material;
    /**
     * Added with 1.21.5
     */
    private TrimPattern trimPattern;
    /**
     * Removed with 1.21.5, replaced with {@link #trimPattern}
     */
    @ApiStatus.Obsolete
    private SlotDisplay<?> pattern;

    /**
     * Added with 1.21.5
     */
    public SmithingTrimSlotDisplay(
            SlotDisplay<?> base,
            SlotDisplay<?> material,
            TrimPattern trimPattern
    ) {
        super(SlotDisplayTypes.SMITHING_TRIM);
        this.base = base;
        this.material = material;
        this.trimPattern = trimPattern;
    }

    /**
     * Removed with 1.21.5, {@link #pattern} has been replaced with {@link #trimPattern}
     */
    @ApiStatus.Obsolete
    public SmithingTrimSlotDisplay(
            SlotDisplay<?> base,
            SlotDisplay<?> material,
            SlotDisplay<?> pattern
    ) {
        super(SlotDisplayTypes.SMITHING_TRIM);
        this.base = base;
        this.material = material;
        this.pattern = pattern;
    }

    public static SmithingTrimSlotDisplay read(PacketWrapper<?> wrapper) {
        SlotDisplay<?> base = SlotDisplay.read(wrapper);
        SlotDisplay<?> material = SlotDisplay.read(wrapper);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            TrimPattern trimPattern = TrimPattern.read(wrapper);
            return new SmithingTrimSlotDisplay(base, material, trimPattern);
        } else {
            SlotDisplay<?> pattern = SlotDisplay.read(wrapper);
            return new SmithingTrimSlotDisplay(base, material, pattern);
        }
    }

    public static void write(PacketWrapper<?> wrapper, SmithingTrimSlotDisplay display) {
        SlotDisplay.write(wrapper, display.base);
        SlotDisplay.write(wrapper, display.material);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            TrimPattern.write(wrapper, display.trimPattern);
        } else {
            SlotDisplay.write(wrapper, display.pattern);
        }
    }

    public SlotDisplay<?> getBase() {
        return this.base;
    }

    public void setBase(SlotDisplay<?> base) {
        this.base = base;
    }

    public SlotDisplay<?> getMaterial() {
        return this.material;
    }

    public void setMaterial(SlotDisplay<?> material) {
        this.material = material;
    }

    /**
     * Removed with 1.21.5, replaced with {@link #getTrimPattern()}
     */
    @ApiStatus.Obsolete
    public SlotDisplay<?> getPattern() {
        return this.pattern;
    }

    /**
     * Removed with 1.21.5, replaced with {@link #setTrimPattern(TrimPattern)}
     */
    @ApiStatus.Obsolete
    public void setPattern(SlotDisplay<?> pattern) {
        this.pattern = pattern;
    }

    /**
     * Added with 1.21.5
     */
    public TrimPattern getTrimPattern() {
        return this.trimPattern;
    }

    /**
     * Added with 1.21.5
     */
    public void setTrimPattern(TrimPattern trimPattern) {
        this.trimPattern = trimPattern;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SmithingTrimSlotDisplay)) return false;
        SmithingTrimSlotDisplay that = (SmithingTrimSlotDisplay) obj;
        if (!this.base.equals(that.base)) return false;
        if (!this.material.equals(that.material)) return false;
        if (!Objects.equals(this.pattern, that.pattern)) return false;
        return Objects.equals(this.trimPattern, that.trimPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.base, this.material, this.pattern, this.trimPattern);
    }

    @Override
    public String toString() {
        return "SmithingTrimSlotDisplay{base=" + this.base + ", material=" + this.material + ", trimPattern=" + this.trimPattern + ", pattern=" + this.pattern + '}';
    }
}
