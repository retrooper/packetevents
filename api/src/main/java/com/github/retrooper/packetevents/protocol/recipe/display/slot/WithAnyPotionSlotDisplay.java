/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 26.1+
 */
@NullMarked
public class WithAnyPotionSlotDisplay extends SlotDisplay<WithAnyPotionSlotDisplay> {

    private final SlotDisplay<?> display;

    public WithAnyPotionSlotDisplay(SlotDisplay<?> display) {
        super(SlotDisplayTypes.WITH_ANY_POTION);
        this.display = display;
    }

    public static WithAnyPotionSlotDisplay read(PacketWrapper<?> wrapper) {
        return new WithAnyPotionSlotDisplay(SlotDisplay.read(wrapper));
    }

    public static void write(PacketWrapper<?> wrapper, WithAnyPotionSlotDisplay display) {
        SlotDisplay.write(wrapper, display.display);
    }

    public SlotDisplay<?> getDisplay() {
        return this.display;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WithAnyPotionSlotDisplay)) return false;
        WithAnyPotionSlotDisplay that = (WithAnyPotionSlotDisplay) obj;
        return this.display.equals(that.display);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.display);
    }

    @Override
    public String toString() {
        return "WithAnyPotionSlotDisplay{display=" + this.display + '}';
    }
}
