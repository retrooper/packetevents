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

public class FurnaceRecipeDisplay extends RecipeDisplay<FurnaceRecipeDisplay> {

    private SlotDisplay<?> ingredient;
    private SlotDisplay<?> fuel;
    private SlotDisplay<?> result;
    private SlotDisplay<?> craftingStation;
    private int duration;
    private float experience;

    public FurnaceRecipeDisplay(
            SlotDisplay<?> ingredient, SlotDisplay<?> fuel, SlotDisplay<?> result,
            SlotDisplay<?> craftingStation, int duration, float experience
    ) {
        super(RecipeDisplayTypes.FURNACE);
        this.ingredient = ingredient;
        this.fuel = fuel;
        this.result = result;
        this.craftingStation = craftingStation;
        this.duration = duration;
        this.experience = experience;
    }

    public static FurnaceRecipeDisplay read(PacketWrapper<?> wrapper) {
        SlotDisplay<?> ingredient = SlotDisplay.read(wrapper);
        SlotDisplay<?> fuel = SlotDisplay.read(wrapper);
        SlotDisplay<?> result = SlotDisplay.read(wrapper);
        SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
        int duration = wrapper.readVarInt();
        float experience = wrapper.readFloat();
        return new FurnaceRecipeDisplay(ingredient, fuel, result,
                craftingStation, duration, experience);
    }

    public static void write(PacketWrapper<?> wrapper, FurnaceRecipeDisplay display) {
        SlotDisplay.write(wrapper, display.ingredient);
        SlotDisplay.write(wrapper, display.fuel);
        SlotDisplay.write(wrapper, display.result);
        SlotDisplay.write(wrapper, display.craftingStation);
        wrapper.writeVarInt(display.duration);
        wrapper.writeFloat(display.experience);
    }

    public SlotDisplay<?> getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(SlotDisplay<?> ingredient) {
        this.ingredient = ingredient;
    }

    public SlotDisplay<?> getFuel() {
        return this.fuel;
    }

    public void setFuel(SlotDisplay<?> fuel) {
        this.fuel = fuel;
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

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getExperience() {
        return this.experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FurnaceRecipeDisplay)) return false;
        FurnaceRecipeDisplay that = (FurnaceRecipeDisplay) obj;
        if (this.duration != that.duration) return false;
        if (Float.compare(that.experience, this.experience) != 0) return false;
        if (!this.ingredient.equals(that.ingredient)) return false;
        if (!this.fuel.equals(that.fuel)) return false;
        if (!this.result.equals(that.result)) return false;
        return this.craftingStation.equals(that.craftingStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ingredient, this.fuel, this.result, this.craftingStation, this.duration, this.experience);
    }

    @Override
    public String toString() {
        return "FurnaceRecipeDisplay{ingredient=" + this.ingredient + ", fuel=" + this.fuel + ", result=" + this.result + ", craftingStation=" + this.craftingStation + ", duration=" + this.duration + ", experience=" + this.experience + '}';
    }
}
