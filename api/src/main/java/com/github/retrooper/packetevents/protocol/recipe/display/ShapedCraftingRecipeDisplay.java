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

import java.util.List;
import java.util.Objects;

public class ShapedCraftingRecipeDisplay extends RecipeDisplay<ShapedCraftingRecipeDisplay> {

    private int width;
    private int height;
    private List<SlotDisplay<?>> ingredients;
    private SlotDisplay<?> result;
    private SlotDisplay<?> craftingStation;

    public ShapedCraftingRecipeDisplay(
            int width, int height,
            List<SlotDisplay<?>> ingredients,
            SlotDisplay<?> result,
            SlotDisplay<?> craftingStation
    ) {
        super(RecipeDisplayTypes.CRAFTING_SHAPED);
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.result = result;
        this.craftingStation = craftingStation;
    }

    public static ShapedCraftingRecipeDisplay read(PacketWrapper<?> wrapper) {
        int width = wrapper.readVarInt();
        int height = wrapper.readVarInt();
        List<SlotDisplay<?>> ingredients = wrapper.readList(SlotDisplay::read);
        SlotDisplay<?> result = SlotDisplay.read(wrapper);
        SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
        return new ShapedCraftingRecipeDisplay(width, height, ingredients, result, craftingStation);
    }

    public static void write(PacketWrapper<?> wrapper, ShapedCraftingRecipeDisplay display) {
        wrapper.writeVarInt(display.width);
        wrapper.writeVarInt(display.height);
        wrapper.writeList(display.ingredients, SlotDisplay::write);
        SlotDisplay.write(wrapper, display.result);
        SlotDisplay.write(wrapper, display.craftingStation);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<SlotDisplay<?>> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(List<SlotDisplay<?>> ingredients) {
        this.ingredients = ingredients;
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
        if (!(obj instanceof ShapedCraftingRecipeDisplay)) return false;
        ShapedCraftingRecipeDisplay that = (ShapedCraftingRecipeDisplay) obj;
        if (this.width != that.width) return false;
        if (this.height != that.height) return false;
        if (!this.ingredients.equals(that.ingredients)) return false;
        if (!this.result.equals(that.result)) return false;
        return this.craftingStation.equals(that.craftingStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.width, this.height, this.ingredients, this.result, this.craftingStation);
    }

    @Override
    public String toString() {
        return "ShapedCraftingRecipeDisplay{width=" + this.width + ", height=" + this.height + ", ingredients=" + this.ingredients + ", result=" + this.result + ", craftingStation=" + this.craftingStation + '}';
    }
}
