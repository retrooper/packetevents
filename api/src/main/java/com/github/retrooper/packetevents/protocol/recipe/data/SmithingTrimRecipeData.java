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

package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class SmithingTrimRecipeData implements RecipeData {

    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    public SmithingTrimRecipeData(Ingredient template, Ingredient base, Ingredient addition) {
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public static SmithingTrimRecipeData read(PacketWrapper<?> wrapper) {
        Ingredient template = Ingredient.read(wrapper);
        Ingredient base = Ingredient.read(wrapper);
        Ingredient addition = Ingredient.read(wrapper);
        return new SmithingTrimRecipeData(template, base, addition);
    }

    public static void write(PacketWrapper<?> wrapper, SmithingTrimRecipeData data) {
        Ingredient.write(wrapper, data.template);
        Ingredient.write(wrapper, data.base);
        Ingredient.write(wrapper, data.addition);
    }

    public Ingredient getTemplate() {
        return this.template;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getAddition() {
        return this.addition;
    }
}
