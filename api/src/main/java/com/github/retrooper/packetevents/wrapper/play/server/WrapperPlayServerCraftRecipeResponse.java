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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerCraftRecipeResponse extends PacketWrapper<WrapperPlayServerCraftRecipeResponse> {
    private int windowId;
    private int recipeLegacy;
    private @Nullable String recipeModern;

    public WrapperPlayServerCraftRecipeResponse(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerCraftRecipeResponse(int windowId, int recipeLegacy) {
        super(PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
        this.windowId = windowId;
        this.recipeLegacy = recipeLegacy;
    }

    public WrapperPlayServerCraftRecipeResponse(int windowId, @Nullable String recipeModern) {
        super(PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
        this.windowId = windowId;
        this.recipeModern = recipeModern;
    }

    @Override
    public void read() {
        this.windowId = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readContainerId() : this.readByte();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeModern = readString();
        } else {
            this.recipeLegacy = readVarInt();
        }
    }

    @Override
    public void write() {
        this.writeContainerId(this.windowId);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            writeString(this.recipeModern);
        } else {
            writeVarInt(this.recipeLegacy);
        }
    }

    @Override
    public void copy(WrapperPlayServerCraftRecipeResponse wrapper) {
        this.windowId = wrapper.windowId;
        this.recipeLegacy = wrapper.recipeLegacy;
        this.recipeModern = wrapper.recipeModern;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public <T> T getRecipe() {
        return (T) (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? recipeModern : recipeLegacy);
    }

    public <T> void setRecipe(T recipe) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeModern = (String) recipe;
        } else {
            this.recipeLegacy = (Integer) recipe;
        }
    }
}
