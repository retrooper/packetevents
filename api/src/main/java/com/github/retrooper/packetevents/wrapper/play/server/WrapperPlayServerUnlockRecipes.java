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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.RecipeId;
import com.github.retrooper.packetevents.protocol.recipe.UnlockRecipesType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WrapperPlayServerUnlockRecipes extends PacketWrapper<WrapperPlayServerUnlockRecipes> {
    private UnlockRecipesType type;
    private boolean craftingRecipeBookOpen;
    private boolean craftingRecipeBookFilterActive;
    private boolean smeltingRecipeBookOpen;
    private boolean smeltingRecipeBookFilterActive;
    private boolean blastFurnaceRecipeBookOpen;
    private boolean blastFurnaceRecipeBookFilterActive;
    private boolean smokerRecipeBookOpen;
    private boolean smokerRecipeBookFilterActive;
    private int elements;
    private @Nullable List<RecipeId<?>> recipeIds;
    private int elementsInit;
    private @Nullable List<RecipeId<?>> recipeIdsInit;

    public WrapperPlayServerUnlockRecipes(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUnlockRecipes(UnlockRecipesType type, boolean craftingRecipeBookOpen, boolean craftingRecipeBookFilterActive, int elements,
                                          @Nullable List<RecipeId<?>> recipeIds, int elementsInit, @Nullable List<RecipeId<?>> recipeIdsInit) {
        super(PacketType.Play.Server.UNLOCK_RECIPES);
        this.type = type;
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
        this.elements = elements;
        this.recipeIds = recipeIds;
        this.elementsInit = elementsInit;
        this.recipeIdsInit = recipeIdsInit;
    }

    public WrapperPlayServerUnlockRecipes(UnlockRecipesType type, boolean craftingRecipeBookOpen, boolean craftingRecipeBookFilterActive, boolean smeltingRecipeBookOpen,
                                          boolean smeltingRecipeBookFilterActive, boolean blastFurnaceRecipeBookOpen, boolean blastFurnaceRecipeBookFilterActive,
                                          boolean smokerRecipeBookOpen, boolean smokerRecipeBookFilterActive, int elements, @Nullable List<RecipeId<?>> recipeIds,
                                          int elementsInit, @Nullable List<RecipeId<?>> recipeIdsInit) {
        super(PacketType.Play.Server.UNLOCK_RECIPES);
        this.type = type;
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
        this.smeltingRecipeBookOpen = smeltingRecipeBookOpen;
        this.smeltingRecipeBookFilterActive = smeltingRecipeBookFilterActive;
        this.blastFurnaceRecipeBookOpen = blastFurnaceRecipeBookOpen;
        this.blastFurnaceRecipeBookFilterActive = blastFurnaceRecipeBookFilterActive;
        this.smokerRecipeBookOpen = smokerRecipeBookOpen;
        this.smokerRecipeBookFilterActive = smokerRecipeBookFilterActive;
        this.elements = elements;
        this.recipeIds = recipeIds;
        this.elementsInit = elementsInit;
        this.recipeIdsInit = recipeIdsInit;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12)) {
            this.type = UnlockRecipesType.getById(readVarInt());
        } else {
            this.type = UnlockRecipesType.getById(readShort());
        }
        this.craftingRecipeBookOpen = readBoolean();
        this.craftingRecipeBookFilterActive = readBoolean();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.smeltingRecipeBookOpen = readBoolean();
            this.smeltingRecipeBookFilterActive = readBoolean();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
            this.blastFurnaceRecipeBookOpen = readBoolean();
            this.blastFurnaceRecipeBookFilterActive = readBoolean();
            this.smokerRecipeBookOpen = readBoolean();
            this.smokerRecipeBookFilterActive = readBoolean();
        }
        this.elements = readVarInt();
        for (int i = 0; i < this.elements; i++) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                this.recipeIds.add(new RecipeId<>(readIdentifier()));
            } else {
                this.recipeIds.add(new RecipeId<>(readVarInt()));
            }
        }


        if (type == UnlockRecipesType.INIT) {
            this.elementsInit = readVarInt();
            for (int i = 0; i < this.elementsInit; i++) {
                if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                    this.recipeIdsInit.add(new RecipeId<>(readIdentifier()));
                } else {
                    this.recipeIdsInit.add(new RecipeId<>(readVarInt()));
                }
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12)) {
            writeVarInt(this.type.ordinal());
        } else {
            writeShort(this.type.ordinal());
        }
        writeBoolean(this.craftingRecipeBookOpen);
        writeBoolean(this.craftingRecipeBookFilterActive);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            writeBoolean(this.smeltingRecipeBookOpen);
            writeBoolean(this.smeltingRecipeBookFilterActive);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
            writeBoolean(this.blastFurnaceRecipeBookOpen);
            writeBoolean(this.blastFurnaceRecipeBookFilterActive);
            writeBoolean(this.smokerRecipeBookOpen);
            writeBoolean(this.smokerRecipeBookFilterActive);
        }
        writeVarInt(this.elements);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeIds.forEach(recipeId -> writeIdentifier(new ResourceLocation(recipeId.getId().toString())));
        } else {
            this.recipeIds.forEach(recipeId -> writeVarInt((int) recipeId.getId()));
        }
        if (type == UnlockRecipesType.INIT) {
            writeVarInt(this.elementsInit);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                this.recipeIdsInit.forEach(recipeId -> writeIdentifier(new ResourceLocation(recipeId.getId().toString())));
            } else {
                this.recipeIdsInit.forEach(recipeId -> writeVarInt((int) recipeId.getId()));
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerUnlockRecipes wrapper) {
        this.type = wrapper.type;
        this.craftingRecipeBookOpen = wrapper.craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = wrapper.craftingRecipeBookFilterActive;
        this.smeltingRecipeBookOpen = wrapper.smeltingRecipeBookOpen;
        this.smeltingRecipeBookFilterActive = wrapper.smeltingRecipeBookFilterActive;
        this.blastFurnaceRecipeBookOpen = wrapper.blastFurnaceRecipeBookOpen;
        this.blastFurnaceRecipeBookFilterActive = wrapper.blastFurnaceRecipeBookFilterActive;
        this.smokerRecipeBookOpen = wrapper.smokerRecipeBookOpen;
        this.smokerRecipeBookFilterActive = wrapper.smokerRecipeBookFilterActive;
        this.elements = wrapper.elements;
        this.recipeIds = wrapper.recipeIds;
        this.recipeIdsInit = wrapper.recipeIdsInit;
        this.elementsInit = wrapper.elementsInit;
    }

    public UnlockRecipesType getType() {
        return type;
    }

    public void setType(UnlockRecipesType type) {
        this.type = type;
    }

    public boolean isCraftingRecipeBookOpen() {
        return craftingRecipeBookOpen;
    }

    public void setCraftingRecipeBookOpen(boolean craftingRecipeBookOpen) {
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
    }

    public boolean isCraftingRecipeBookFilterActive() {
        return craftingRecipeBookFilterActive;
    }

    public void setCraftingRecipeBookFilterActive(boolean craftingRecipeBookFilterActive) {
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
    }

    public boolean isSmeltingRecipeBookOpen() {
        return smeltingRecipeBookOpen;
    }

    public void setSmeltingRecipeBookOpen(boolean smeltingRecipeBookOpen) {
        this.smeltingRecipeBookOpen = smeltingRecipeBookOpen;
    }

    public boolean isSmeltingRecipeBookFilterActive() {
        return smeltingRecipeBookFilterActive;
    }

    public void setSmeltingRecipeBookFilterActive(boolean smeltingRecipeBookFilterActive) {
        this.smeltingRecipeBookFilterActive = smeltingRecipeBookFilterActive;
    }

    public boolean isBlastFurnaceRecipeBookOpen() {
        return blastFurnaceRecipeBookOpen;
    }

    public void setBlastFurnaceRecipeBookOpen(boolean blastFurnaceRecipeBookOpen) {
        this.blastFurnaceRecipeBookOpen = blastFurnaceRecipeBookOpen;
    }

    public boolean isBlastFurnaceRecipeBookFilterActive() {
        return blastFurnaceRecipeBookFilterActive;
    }

    public void setBlastFurnaceRecipeBookFilterActive(boolean blastFurnaceRecipeBookFilterActive) {
        this.blastFurnaceRecipeBookFilterActive = blastFurnaceRecipeBookFilterActive;
    }

    public boolean isSmokerRecipeBookOpen() {
        return smokerRecipeBookOpen;
    }

    public void setSmokerRecipeBookOpen(boolean smokerRecipeBookOpen) {
        this.smokerRecipeBookOpen = smokerRecipeBookOpen;
    }

    public boolean isSmokerRecipeBookFilterActive() {
        return smokerRecipeBookFilterActive;
    }

    public void setSmokerRecipeBookFilterActive(boolean smokerRecipeBookFilterActive) {
        this.smokerRecipeBookFilterActive = smokerRecipeBookFilterActive;
    }

    public int getElements() {
        return elements;
    }

    public void setElements(int elements) {
        this.elements = elements;
    }

    /**
     * On version 1.13 and above, the recipe ids are read as ResourceLocation, otherwise they are read as integers.
     *
     * @return The recipe ids
     */
    public List<RecipeId<?>> getRecipeIds() {
        return recipeIds;
    }

    /**
     * On version 1.13 and above, the recipe ids are written as ResourceLocation, otherwise they are written as integers.
     *
     * @param recipeIds The recipe ids
     */
    public void setRecipeIds(List<RecipeId<?>> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public int getElementsInit() {
        return elementsInit;
    }

    public void setElementsInit(int elementsInit) {
        this.elementsInit = elementsInit;
    }

    /**
     * On version 1.13 and above, the recipe ids are read as ResourceLocation, otherwise they are read as integers.
     *
     * @return The recipe ids init
     */
    public List<RecipeId<?>> getRecipeIdsInit() {
        return recipeIdsInit;
    }


    /**
     * On version 1.13 and above, the recipe ids are written as ResourceLocation, otherwise they are written as integers.
     *
     * @param recipeIdsInit The recipe ids init
     */
    public void setRecipeIdsInit(List<RecipeId<?>> recipeIdsInit) {
        this.recipeIdsInit = recipeIdsInit;
    }
}
