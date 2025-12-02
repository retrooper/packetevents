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
import com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayId;
import com.github.retrooper.packetevents.protocol.recipe.display.RecipeDisplay;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

public class WrapperPlayServerCraftRecipeResponse extends PacketWrapper<WrapperPlayServerCraftRecipeResponse> {

    private int windowId;
    /**
     * Removed with 1.13 and replaced with {@link #recipeKey}.
     */
    @ApiStatus.Obsolete
    private RecipeDisplayId recipeId;
    /**
     * Exists since 1.13, removed with 1.21.2 and replaced with {@link #recipeDisplay}.
     */
    @ApiStatus.Obsolete
    private ResourceLocation recipeKey;
    /**
     * Exists since 1.12.2.
     */
    private RecipeDisplay<?> recipeDisplay;

    public WrapperPlayServerCraftRecipeResponse(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerCraftRecipeResponse(int windowId, int recipeId) {
        this(windowId, new RecipeDisplayId(recipeId));
    }

    public WrapperPlayServerCraftRecipeResponse(int windowId, RecipeDisplayId recipeId) {
        super(PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
        this.windowId = windowId;
        this.recipeId = recipeId;
    }

    public WrapperPlayServerCraftRecipeResponse(int windowId, String recipeKey) {
        this(windowId, new ResourceLocation(recipeKey));
    }

    public WrapperPlayServerCraftRecipeResponse(int windowId, ResourceLocation recipeKey) {
        super(PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
        this.windowId = windowId;
        this.recipeKey = recipeKey;
    }

    public WrapperPlayServerCraftRecipeResponse(int windowId, RecipeDisplay<?> recipeDisplay) {
        super(PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
        this.windowId = windowId;
        this.recipeDisplay = recipeDisplay;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.windowId = this.readContainerId();
            this.recipeDisplay = RecipeDisplay.read(this);
        } else {
            this.windowId = this.readByte();
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                this.recipeKey = this.readIdentifier();
            } else {
                this.recipeId = RecipeDisplayId.read(this);
            }
        }
    }

    @Override
    public void write() {
        this.writeContainerId(this.windowId);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            RecipeDisplay.write(this, this.recipeDisplay);
        } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.writeIdentifier(this.recipeKey);
        } else {
            RecipeDisplayId.write(this, this.recipeId);
        }
    }

    @Override
    public void copy(WrapperPlayServerCraftRecipeResponse wrapper) {
        this.windowId = wrapper.windowId;
        this.recipeId = wrapper.recipeId;
        this.recipeKey = wrapper.recipeKey;
        this.recipeDisplay = wrapper.recipeDisplay;
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

    public RecipeDisplay<?> getRecipeDisplay() {
        return this.recipeDisplay;
    }

    public void setRecipeDisplay(RecipeDisplay<?> recipeDisplay) {
        this.recipeDisplay = recipeDisplay;
    }
}
