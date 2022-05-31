/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import org.jetbrains.annotations.NotNull;

// From MCProtocolLib
public class Recipe {
    private final @NotNull RecipeType type;
    private final @NotNull String identifier;
    private final RecipeData data;

    public Recipe(@NotNull RecipeType type, @NotNull String identifier, RecipeData data) {
        this.type = type;
        this.identifier = identifier;
        this.data = data;
    }

    public @NotNull RecipeType getType() {
        return type;
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public @NotNull RecipeData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "type=" + type +
                ", identifier='" + identifier + '\'' +
                ", data=" + data +
                '}';
    }
}