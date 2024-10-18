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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayId;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class WrapperPlayServerRecipeBookRemove extends PacketWrapper<WrapperPlayServerRecipeBookRemove> {

    private List<RecipeDisplayId> recipeIds;

    public WrapperPlayServerRecipeBookRemove(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRecipeBookRemove(List<RecipeDisplayId> recipeIds) {
        super(PacketType.Play.Server.RECIPE_BOOK_REMOVE);
        this.recipeIds = recipeIds;
    }

    @Override
    public void read() {
        this.recipeIds = this.readList(RecipeDisplayId::read);
    }

    @Override
    public void write() {
        this.writeList(this.recipeIds, RecipeDisplayId::write);
    }

    @Override
    public void copy(WrapperPlayServerRecipeBookRemove wrapper) {
        this.recipeIds = wrapper.recipeIds;
    }

    public List<RecipeDisplayId> getRecipeIds() {
        return this.recipeIds;
    }

    public void setRecipeIds(List<RecipeDisplayId> recipeIds) {
        this.recipeIds = recipeIds;
    }
}
