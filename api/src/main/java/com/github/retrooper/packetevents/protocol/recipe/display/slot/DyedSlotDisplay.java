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
public class DyedSlotDisplay extends SlotDisplay<DyedSlotDisplay> {

    private final SlotDisplay<?> source;
    private final SlotDisplay<?> target;

    public DyedSlotDisplay(SlotDisplay<?> source, SlotDisplay<?> target) {
        super(SlotDisplayTypes.DYED);
        this.source = source;
        this.target = target;
    }

    public static DyedSlotDisplay read(PacketWrapper<?> wrapper) {
        SlotDisplay<?> source = SlotDisplay.read(wrapper);
        SlotDisplay<?> target = SlotDisplay.read(wrapper);
        return new DyedSlotDisplay(source, target);
    }

    public static void write(PacketWrapper<?> wrapper, DyedSlotDisplay display) {
        SlotDisplay.write(wrapper, display.source);
        SlotDisplay.write(wrapper, display.target);
    }

    public SlotDisplay<?> getSource() {
        return this.source;
    }

    public SlotDisplay<?> getTarget() {
        return this.target;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DyedSlotDisplay)) return false;
        DyedSlotDisplay that = (DyedSlotDisplay) obj;
        if (!this.source.equals(that.source)) return false;
        return this.target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source, this.target);
    }

    @Override
    public String toString() {
        return "DyedSlotDisplay{source=" + this.source + ", target=" + this.target + '}';
    }
}
