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

public class WithRemainderSlotDisplay extends SlotDisplay<WithRemainderSlotDisplay> {

    private SlotDisplay<?> input;
    private SlotDisplay<?> remainder;

    public WithRemainderSlotDisplay(SlotDisplay<?> input, SlotDisplay<?> remainder) {
        super(SlotDisplayTypes.WITH_REMAINDER);
        this.input = input;
        this.remainder = remainder;
    }

    public static WithRemainderSlotDisplay read(PacketWrapper<?> wrapper) {
        SlotDisplay<?> input = SlotDisplay.read(wrapper);
        SlotDisplay<?> remainder = SlotDisplay.read(wrapper);
        return new WithRemainderSlotDisplay(input, remainder);
    }

    public static void write(PacketWrapper<?> wrapper, WithRemainderSlotDisplay display) {
        SlotDisplay.write(wrapper, display.input);
        SlotDisplay.write(wrapper, display.remainder);
    }

    public SlotDisplay<?> getInput() {
        return this.input;
    }

    public void setInput(SlotDisplay<?> input) {
        this.input = input;
    }

    public SlotDisplay<?> getRemainder() {
        return this.remainder;
    }

    public void setRemainder(SlotDisplay<?> remainder) {
        this.remainder = remainder;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WithRemainderSlotDisplay)) return false;
        WithRemainderSlotDisplay that = (WithRemainderSlotDisplay) obj;
        if (!this.input.equals(that.input)) return false;
        return this.remainder.equals(that.remainder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.input, this.remainder);
    }

    @Override
    public String toString() {
        return "WithRemainderSlotDisplay{input=" + this.input + ", remainder=" + this.remainder + '}';
    }
}
