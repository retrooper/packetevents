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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSetDisplayedRecipe extends PacketWrapper<WrapperPlayClientSetDisplayedRecipe> {
    private ResourceLocation recipe;

    public WrapperPlayClientSetDisplayedRecipe(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSetDisplayedRecipe(ResourceLocation recipe) {
        super(PacketType.Play.Client.SET_DISPLAYED_RECIPE);
        this.recipe = recipe;
    }

    @Override
    public void read() {
        this.recipe = readIdentifier();
    }

    @Override
    public void write() {
        writeIdentifier(this.recipe);
    }

    @Override
    public void copy(WrapperPlayClientSetDisplayedRecipe packet) {
        this.recipe = packet.recipe;
    }

    public ResourceLocation getRecipe() {
        return recipe;
    }

    public void setRecipe(ResourceLocation recipe) {
        this.recipe = recipe;
    }
}
