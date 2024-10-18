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

package com.github.retrooper.packetevents.protocol.recipe.display;

import com.github.retrooper.packetevents.protocol.recipe.display.slot.SlotDisplay;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class StonecutterRecipeDisplay extends RecipeDisplay<StonecutterRecipeDisplay> {

    private SlotDisplay<?> input;
    private SlotDisplay<?> result;
    private SlotDisplay<?> craftingStation;

    public StonecutterRecipeDisplay(
            SlotDisplay<?> input,
            SlotDisplay<?> result,
            SlotDisplay<?> craftingStation
    ) {
        super(RecipeDisplayTypes.STONECUTTER);
        this.input = input;
        this.result = result;
        this.craftingStation = craftingStation;
    }

    public static StonecutterRecipeDisplay read(PacketWrapper<?> wrapper) {
        SlotDisplay<?> input = SlotDisplay.read(wrapper);
        SlotDisplay<?> result = SlotDisplay.read(wrapper);
        SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
        return new StonecutterRecipeDisplay(input, result, craftingStation);
    }

    public static void write(PacketWrapper<?> wrapper, StonecutterRecipeDisplay display) {
        SlotDisplay.write(wrapper, display.input);
        SlotDisplay.write(wrapper, display.result);
        SlotDisplay.write(wrapper, display.craftingStation);
    }

    public SlotDisplay<?> getInput() {
        return this.input;
    }

    public void setInput(SlotDisplay<?> input) {
        this.input = input;
    }

    public SlotDisplay<?> getResult() {
        return this.result;
    }

    public void setResult(SlotDisplay<?> result) {
        this.result = result;
    }

    public SlotDisplay<?> getCraftingStation() {
        return this.craftingStation;
    }

    public void setCraftingStation(SlotDisplay<?> craftingStation) {
        this.craftingStation = craftingStation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StonecutterRecipeDisplay)) return false;
        StonecutterRecipeDisplay that = (StonecutterRecipeDisplay) obj;
        if (!this.input.equals(that.input)) return false;
        if (!this.result.equals(that.result)) return false;
        return this.craftingStation.equals(that.craftingStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.input, this.result, this.craftingStation);
    }

    @Override
    public String toString() {
        return "StonecutterRecipeDisplay{input=" + this.input + ", result=" + this.result + ", craftingStation=" + this.craftingStation + '}';
    }
}
