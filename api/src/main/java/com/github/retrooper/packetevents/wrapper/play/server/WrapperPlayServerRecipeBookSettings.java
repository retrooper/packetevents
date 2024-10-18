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
import com.github.retrooper.packetevents.protocol.recipe.RecipeBookSettings;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerRecipeBookSettings extends PacketWrapper<WrapperPlayServerRecipeBookSettings> {

    private RecipeBookSettings settings;

    public WrapperPlayServerRecipeBookSettings(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRecipeBookSettings(RecipeBookSettings settings) {
        super(PacketType.Play.Server.RECIPE_BOOK_SETTINGS);
        this.settings = settings;
    }

    @Override
    public void read() {
        this.settings = RecipeBookSettings.read(this);
    }

    @Override
    public void write() {
        RecipeBookSettings.write(this, this.settings);
    }

    @Override
    public void copy(WrapperPlayServerRecipeBookSettings wrapper) {
        this.settings = wrapper.settings;
    }

    public RecipeBookSettings getSettings() {
        return this.settings;
    }

    public void setSettings(RecipeBookSettings settings) {
        this.settings = settings;
    }
}
