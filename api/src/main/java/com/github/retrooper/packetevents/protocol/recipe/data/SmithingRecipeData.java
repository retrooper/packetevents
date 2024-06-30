/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.UnknownNullability;

public class SmithingRecipeData implements RecipeData {

    private final @UnknownNullability Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;

    @Deprecated
    public SmithingRecipeData(Ingredient base, Ingredient addition, ItemStack result) {
        this(null, base, addition, result);
    }

    public SmithingRecipeData(
            @UnknownNullability Ingredient template,
            Ingredient base, Ingredient addition, ItemStack result
    ) {
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public static SmithingRecipeData read(PacketWrapper<?> wrapper) {
        return read(wrapper, false);
    }

    public static SmithingRecipeData read(PacketWrapper<?> wrapper, boolean legacy) {
        Ingredient template = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20)
                || (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) && !legacy)
                ? Ingredient.read(wrapper) : null;
        Ingredient base = Ingredient.read(wrapper);
        Ingredient addition = Ingredient.read(wrapper);
        ItemStack result = wrapper.readItemStack();
        return new SmithingRecipeData(template, base, addition, result);
    }

    public static void write(PacketWrapper<?> wrapper, SmithingRecipeData data) {
        write(wrapper, data, false);
    }

    public static void write(PacketWrapper<?> wrapper, SmithingRecipeData data, boolean legacy) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20)
                || (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) && !legacy)) {
            Ingredient.write(wrapper, data.template);
        }
        Ingredient.write(wrapper, data.base);
        Ingredient.write(wrapper, data.addition);
        wrapper.writeItemStack(data.result);
    }

    public @UnknownNullability Ingredient getTemplate() {
        return this.template;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getAddition() {
        return this.addition;
    }

    public ItemStack getResult() {
        return this.result;
    }
}
