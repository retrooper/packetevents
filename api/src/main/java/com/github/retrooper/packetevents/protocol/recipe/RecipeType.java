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

package com.github.retrooper.packetevents.protocol.recipe;

import org.jetbrains.annotations.Nullable;

// From MCProtocolLib
@Deprecated
public enum RecipeType {

    CRAFTING_SHAPELESS(RecipeSerializers.CRAFTING_SHAPELESS),
    CRAFTING_SHAPED(RecipeSerializers.CRAFTING_SHAPED),
    CRAFTING_SPECIAL_ARMORDYE(RecipeSerializers.CRAFTING_SPECIAL_ARMORDYE),
    CRAFTING_SPECIAL_BOOKCLONING(RecipeSerializers.CRAFTING_SPECIAL_BOOKCLONING),
    CRAFTING_SPECIAL_MAPCLONING(RecipeSerializers.CRAFTING_SPECIAL_MAPCLONING),
    CRAFTING_SPECIAL_MAPEXTENDING(RecipeSerializers.CRAFTING_SPECIAL_MAPEXTENDING),
    CRAFTING_SPECIAL_FIREWORK_ROCKET(RecipeSerializers.CRAFTING_SPECIAL_FIREWORK_ROCKET),
    CRAFTING_SPECIAL_FIREWORK_STAR(RecipeSerializers.CRAFTING_SPECIAL_FIREWORK_STAR),
    CRAFTING_SPECIAL_FIREWORK_STAR_FADE(RecipeSerializers.CRAFTING_SPECIAL_FIREWORK_STAR_FADE),
    CRAFTING_SPECIAL_REPAIRITEM(RecipeSerializers.CRAFTING_SPECIAL_REPAIRITEM),
    CRAFTING_SPECIAL_TIPPEDARROW(RecipeSerializers.CRAFTING_SPECIAL_TIPPEDARROW),
    CRAFTING_SPECIAL_BANNERDUPLICATE(RecipeSerializers.CRAFTING_SPECIAL_BANNERDUPLICATE),
    CRAFTING_SPECIAL_BANNERADDPATTERN(null), // I didn't find this anywhere
    CRAFTING_SPECIAL_SHIELDDECORATION(RecipeSerializers.CRAFTING_SPECIAL_SHIELDDECORATION),
    CRAFTING_SPECIAL_SHULKERBOXCOLORING(RecipeSerializers.CRAFTING_SPECIAL_SHULKERBOXCOLORING),
    CRAFTING_SPECIAL_SUSPICIOUSSTEW(RecipeSerializers.CRAFTING_SPECIAL_SUSPICIOUSSTEW),
    SMELTING(RecipeSerializers.SMELTING),
    BLASTING(RecipeSerializers.BLASTING),
    SMOKING(RecipeSerializers.SMOKING),
    CAMPFIRE_COOKING(RecipeSerializers.CAMPFIRE_COOKING),
    STONECUTTING(RecipeSerializers.STONECUTTING),
    SMITHING(RecipeSerializers.SMITHING_TRANSFORM);

    private final @Nullable RecipeSerializer<?> serializer;

    RecipeType(@Nullable RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public RecipeSerializer<?> getSerializer() {
        if (this.serializer == null) {
            throw new UnsupportedOperationException("No serializer found for legacy recipe type " + this);
        }
        return this.serializer;
    }
}
