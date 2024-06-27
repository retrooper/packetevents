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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class Recipe<T extends RecipeData> {

    private final ResourceLocation key;
    private final RecipeSerializer<T> serializer;
    private final T data;

    @SuppressWarnings("unchecked") // backwards compat
    @Deprecated
    public Recipe(RecipeType serializer, String key, RecipeData data) {
        this(
                new ResourceLocation(key),
                (RecipeSerializer<T>) serializer.getSerializer(),
                (T) data
        );
    }

    public Recipe(ResourceLocation key, RecipeSerializer<T> serializer, T data) {
        this.key = key;
        this.serializer = serializer;
        this.data = data;
    }

    public static Recipe<?> read(PacketWrapper<?> wrapper) {
        ResourceLocation key;
        RecipeSerializer<?> serializer;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            key = wrapper.readIdentifier();
            serializer = wrapper.readMappedEntity(RecipeSerializers::getById);
        } else {
            serializer = RecipeSerializers.getByName(wrapper.readIdentifier().toString());
            key = wrapper.readIdentifier();
        }
        return read(wrapper, key, serializer);
    }

    private static <T extends RecipeData> Recipe<T> read(
            PacketWrapper<?> wrapper,
            ResourceLocation key,
            RecipeSerializer<T> serializer
    ) {
        T data = serializer.read(wrapper);
        return new Recipe<>(key, serializer, data);
    }

    public static <T extends RecipeData> void write(PacketWrapper<?> wrapper, Recipe<T> recipe) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            wrapper.writeIdentifier(recipe.key);
            wrapper.writeMappedEntity(recipe.serializer);
        } else {
            wrapper.writeIdentifier(recipe.serializer.getName());
            wrapper.writeIdentifier(recipe.key);
        }
        recipe.serializer.write(wrapper, recipe.data);
    }

    @Deprecated
    public RecipeType getType() {
        return this.serializer.getLegacyType();
    }

    public String getIdentifier() {
        return this.key.toString();
    }

    public ResourceLocation getKey() {
        return this.key;
    }

    public RecipeSerializer<T> getSerializer() {
        return this.serializer;
    }

    public T getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "Recipe{key=" + this.key + ", serializer=" + this.serializer + ", data=" + this.data + '}';
    }
}
