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

public class WrapperPlayClientCraftRecipeRequest extends PacketWrapper<WrapperPlayClientCraftRecipeRequest> {
    private int windowId;
    /**
     * Exists from 1.13 to 1.21.1
     */
    @ApiStatus.Obsolete
    private ResourceLocation recipeKey;
    /**
     * Exists below 1.13 and since 1.12.2
     */
    private RecipeDisplayId recipeId;
    private boolean makeAll;

    public WrapperPlayClientCraftRecipeRequest(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientCraftRecipeRequest(int windowId, int recipeId, boolean makeAll) {
        this(windowId, new RecipeDisplayId(recipeId), makeAll);
    }

    public WrapperPlayClientCraftRecipeRequest(int windowId, RecipeDisplayId recipeId, boolean makeAll) {
        super(PacketType.Play.Client.CRAFT_RECIPE_REQUEST);
        this.windowId = windowId;
        this.recipeId = recipeId;
        this.makeAll = makeAll;
    }

    public WrapperPlayClientCraftRecipeRequest(int windowId, String recipeKey, boolean makeAll) {
        this(windowId, new ResourceLocation(recipeKey), makeAll);
    }

    public WrapperPlayClientCraftRecipeRequest(int windowId, ResourceLocation recipeKey, boolean makeAll) {
        super(PacketType.Play.Client.CRAFT_RECIPE_REQUEST);
        this.windowId = windowId;
        this.recipeKey = recipeKey;
        this.makeAll = makeAll;
    }

    @Override
    public void read() {
        this.windowId = this.readContainerId();
        if (this.serverVersion.isOlderThan(ServerVersion.V_1_21_2)
                && this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeKey = this.readIdentifier();
        } else {
            this.recipeId = RecipeDisplayId.read(this);
        }
        this.makeAll = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeContainerId(this.windowId);
        if (this.serverVersion.isOlderThan(ServerVersion.V_1_21_2)
                && this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.writeIdentifier(this.recipeKey);
        } else {
            RecipeDisplayId.write(this, this.recipeId);
        }
        this.writeBoolean(this.makeAll);
    }

    @Override
    public void copy(WrapperPlayClientCraftRecipeRequest wrapper) {
        this.windowId = wrapper.windowId;
        this.recipeId = wrapper.recipeId;
        this.recipeKey = wrapper.recipeKey;
        this.makeAll = wrapper.makeAll;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    /**
     * @deprecated Unsafe api, don't use this
     */
    @Deprecated
    public <T> T getRecipe() {
        return (T) (this.serverVersion.isOlderThan(ServerVersion.V_1_21_2)
                && this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)
                ? this.recipeKey : this.recipeId.getId());
    }

    /**
     * @deprecated Unsafe api, don't use this
     */
    @Deprecated
    public <T> void setRecipe(T recipe) {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeKey = new ResourceLocation((String) recipe);
        } else {
            this.recipeId = new RecipeDisplayId((Integer) recipe);
        }
    }

    public ResourceLocation getRecipeKey() {
        return this.recipeKey;
    }

    public void setRecipeKey(ResourceLocation recipeKey) {
        this.recipeKey = recipeKey;
    }

    public RecipeDisplayId getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(RecipeDisplayId recipeId) {
        this.recipeId = recipeId;
    }

    public boolean isMakeAll() {
        return this.makeAll;
    }

    public void setMakeAll(boolean makeAll) {
        this.makeAll = makeAll;
    }
}
