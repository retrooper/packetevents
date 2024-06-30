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
import com.github.retrooper.packetevents.protocol.recipe.Recipe;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDeclareRecipes extends PacketWrapper<WrapperPlayServerDeclareRecipes> {

    private Recipe<?>[] recipes;

    public WrapperPlayServerDeclareRecipes(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void read() {
        this.recipes = this.readArray(Recipe::read, Recipe.class);
    }

    @Override
    public void write() {
        this.writeArray(this.recipes, Recipe::write);
    }

    @Override
    public void copy(WrapperPlayServerDeclareRecipes wrapper) {
        this.recipes = wrapper.recipes;
    }

    public Recipe<?>[] getRecipes() {
        return this.recipes;
    }

    public void setRecipes(Recipe<?>[] recipes) {
        this.recipes = recipes;
    }
}
