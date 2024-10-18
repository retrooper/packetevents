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

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class SmithingTrimSlotDisplay extends SlotDisplay<SmithingTrimSlotDisplay> {

    private SlotDisplay<?> base;
    private SlotDisplay<?> material;
    private SlotDisplay<?> pattern;

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
        SlotDisplay<?> pattern = SlotDisplay.read(wrapper);
        return new SmithingTrimSlotDisplay(base, material, pattern);
    }

    public static void write(PacketWrapper<?> wrapper, SmithingTrimSlotDisplay display) {
        SlotDisplay.write(wrapper, display.base);
        SlotDisplay.write(wrapper, display.material);
        SlotDisplay.write(wrapper, display.pattern);
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

    public SlotDisplay<?> getPattern() {
        return this.pattern;
    }

    public void setPattern(SlotDisplay<?> pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SmithingTrimSlotDisplay)) return false;
        SmithingTrimSlotDisplay that = (SmithingTrimSlotDisplay) obj;
        if (!this.base.equals(that.base)) return false;
        if (!this.material.equals(that.material)) return false;
        return this.pattern.equals(that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.base, this.material, this.pattern);
    }

    @Override
    public String toString() {
        return "SmithingTrimSlotDisplay{base=" + this.base + ", material=" + this.material + ", pattern=" + this.pattern + '}';
    }
}
