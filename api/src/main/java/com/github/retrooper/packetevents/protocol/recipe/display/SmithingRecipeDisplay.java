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

public class SmithingRecipeDisplay extends RecipeDisplay<SmithingRecipeDisplay> {

    private SlotDisplay<?> template;
    private SlotDisplay<?> base;
    private SlotDisplay<?> addition;
    private SlotDisplay<?> result;
    private SlotDisplay<?> craftingStation;

    public SmithingRecipeDisplay(
            SlotDisplay<?> template,
            SlotDisplay<?> base,
            SlotDisplay<?> addition,
            SlotDisplay<?> result,
            SlotDisplay<?> craftingStation
    ) {
        super(RecipeDisplayTypes.SMITHING);
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
        this.craftingStation = craftingStation;
    }

    public static SmithingRecipeDisplay read(PacketWrapper<?> wrapper) {
        SlotDisplay<?> template = SlotDisplay.read(wrapper);
        SlotDisplay<?> base = SlotDisplay.read(wrapper);
        SlotDisplay<?> addition = SlotDisplay.read(wrapper);
        SlotDisplay<?> result = SlotDisplay.read(wrapper);
        SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
        return new SmithingRecipeDisplay(template, base, addition, result, craftingStation);
    }

    public static void write(PacketWrapper<?> wrapper, SmithingRecipeDisplay display) {
        SlotDisplay.write(wrapper, display.template);
        SlotDisplay.write(wrapper, display.base);
        SlotDisplay.write(wrapper, display.addition);
        SlotDisplay.write(wrapper, display.result);
        SlotDisplay.write(wrapper, display.craftingStation);
    }

    public SlotDisplay<?> getTemplate() {
        return this.template;
    }

    public void setTemplate(SlotDisplay<?> template) {
        this.template = template;
    }

    public SlotDisplay<?> getBase() {
        return this.base;
    }

    public void setBase(SlotDisplay<?> base) {
        this.base = base;
    }

    public SlotDisplay<?> getAddition() {
        return this.addition;
    }

    public void setAddition(SlotDisplay<?> addition) {
        this.addition = addition;
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
        if (!(obj instanceof SmithingRecipeDisplay)) return false;
        SmithingRecipeDisplay that = (SmithingRecipeDisplay) obj;
        if (!this.template.equals(that.template)) return false;
        if (!this.base.equals(that.base)) return false;
        if (!this.addition.equals(that.addition)) return false;
        if (!this.result.equals(that.result)) return false;
        return this.craftingStation.equals(that.craftingStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.template, this.base, this.addition, this.result, this.craftingStation);
    }

    @Override
    public String toString() {
        return "SmithingRecipeDisplay{template=" + this.template + ", base=" + this.base + ", addition=" + this.addition + ", result=" + this.result + ", craftingStation=" + this.craftingStation + '}';
    }
}
