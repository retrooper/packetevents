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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.Recipe;
import com.github.retrooper.packetevents.protocol.recipe.RecipePropertySet;
import com.github.retrooper.packetevents.protocol.recipe.SingleInputOptionDisplay;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;

/**
 * Note: The entire recipe system has been rewritten with 1.21.2.
 */
public class WrapperPlayServerDeclareRecipes extends PacketWrapper<WrapperPlayServerDeclareRecipes> {

    /**
     * Removed with 1.21.2
     */
    @ApiStatus.Obsolete
    private Recipe<?>[] recipes;

    /**
     * Added with 1.21.2
     */
    private Map<ResourceLocation, RecipePropertySet> itemSets;
    /**
     * Added with 1.21.2
     */
    private List<SingleInputOptionDisplay> stonecutterRecipes;

    public WrapperPlayServerDeclareRecipes(PacketSendEvent event) {
        super(event);
    }

    /**
     * Removed with 1.21.2
     */
    @ApiStatus.Obsolete
    public WrapperPlayServerDeclareRecipes(Recipe<?>[] recipes) {
        super(PacketType.Play.Server.DECLARE_RECIPES);
        this.recipes = recipes;
    }

    public WrapperPlayServerDeclareRecipes(
            Map<ResourceLocation, RecipePropertySet> itemSets,
            List<SingleInputOptionDisplay> stonecutterRecipes
    ) {
        super(PacketType.Play.Server.DECLARE_RECIPES);
        this.itemSets = itemSets;
        this.stonecutterRecipes = stonecutterRecipes;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.itemSets = this.readMap(
                    PacketWrapper::readIdentifier,
                    RecipePropertySet::read);
            this.stonecutterRecipes = this.readList(
                    SingleInputOptionDisplay::read);
        } else {
            this.recipes = this.readArray(Recipe::read, Recipe.class);
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeMap(this.itemSets,
                    PacketWrapper::writeIdentifier,
                    RecipePropertySet::write);
            this.writeList(this.stonecutterRecipes, SingleInputOptionDisplay::write);
        } else {
            this.writeArray(this.recipes, Recipe::write);
        }
    }

    @Override
    public void copy(WrapperPlayServerDeclareRecipes wrapper) {
        this.recipes = wrapper.recipes;
        this.itemSets = wrapper.itemSets;
        this.stonecutterRecipes = wrapper.stonecutterRecipes;
    }

    /**
     * Removed with 1.21.2
     */
    @ApiStatus.Obsolete
    public Recipe<?>[] getRecipes() {
        return this.recipes;
    }

    /**
     * Removed with 1.21.2
     */
    @ApiStatus.Obsolete
    public void setRecipes(Recipe<?>[] recipes) {
        this.recipes = recipes;
    }

    public Map<ResourceLocation, RecipePropertySet> getItemSets() {
        return this.itemSets;
    }

    public void setItemSets(Map<ResourceLocation, RecipePropertySet> itemSets) {
        this.itemSets = itemSets;
    }

    public List<SingleInputOptionDisplay> getStonecutterRecipes() {
        return this.stonecutterRecipes;
    }

    public void setStonecutterRecipes(List<SingleInputOptionDisplay> stonecutterRecipes) {
        this.stonecutterRecipes = stonecutterRecipes;
    }
}
