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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayId;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

public class WrapperPlayClientSetDisplayedRecipe extends PacketWrapper<WrapperPlayClientSetDisplayedRecipe> {

    /**
     * Replaced in 1.21.2 with {@link #recipeId}
     */
    @ApiStatus.Obsolete
    private ResourceLocation recipe;

    private RecipeDisplayId recipeId;

    public WrapperPlayClientSetDisplayedRecipe(PacketReceiveEvent event) {
        super(event);
    }

    /**
     * Replaced in 1.21.2 with {@link #WrapperPlayClientSetDisplayedRecipe(RecipeDisplayId)}
     */
    @ApiStatus.Obsolete
    public WrapperPlayClientSetDisplayedRecipe(ResourceLocation recipe) {
        super(PacketType.Play.Client.SET_DISPLAYED_RECIPE);
        this.recipe = recipe;
    }

    public WrapperPlayClientSetDisplayedRecipe(RecipeDisplayId recipeId) {
        super(PacketType.Play.Client.SET_DISPLAYED_RECIPE);
        this.recipeId = recipeId;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.recipeId = RecipeDisplayId.read(this);
        } else {
            this.recipe = this.readIdentifier();
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            RecipeDisplayId.write(this, this.recipeId);
        } else {
            this.writeIdentifier(this.recipe);
        }
    }

    @Override
    public void copy(WrapperPlayClientSetDisplayedRecipe packet) {
        this.recipe = packet.recipe;
        this.recipeId = packet.recipeId;
    }

    /**
     * Replaced in 1.21.2 with {@link #getRecipeId()}
     */
    @ApiStatus.Obsolete
    public ResourceLocation getRecipe() {
        return this.recipe;
    }

    /**
     * Replaced in 1.21.2 with {@link #setRecipeId(RecipeDisplayId)}
     */
    @ApiStatus.Obsolete
    public void setRecipe(ResourceLocation recipe) {
        this.recipe = recipe;
    }

    public RecipeDisplayId getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(RecipeDisplayId recipeId) {
        this.recipeId = recipeId;
    }
}
