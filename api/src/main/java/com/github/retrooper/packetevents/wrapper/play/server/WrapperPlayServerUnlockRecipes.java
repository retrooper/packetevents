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
import com.github.retrooper.packetevents.protocol.recipe.UnlockRecipesType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

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
    private int @Nullable [] recipeIdsLegacy;
    private @Nullable String[] recipeIdsModern;
    private int elementsInit;
    private int @Nullable [] recipeIdsLegacyInit;
    private @Nullable String[] recipeIdsModernInit;

    public WrapperPlayServerUnlockRecipes(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUnlockRecipes(UnlockRecipesType type, boolean craftingRecipeBookOpen, boolean craftingRecipeBookFilterActive, int elements,
                                          int @Nullable [] recipeIdsLegacy, int elementsInit, int @Nullable [] recipeIdsLegacyInit) {
        super(PacketType.Play.Server.UNLOCK_RECIPES);
        this.type = type;
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
        this.elements = elements;
        this.recipeIdsLegacy = recipeIdsLegacy;
        this.elementsInit = elementsInit;
        this.recipeIdsLegacyInit = recipeIdsLegacyInit;
    }

    public WrapperPlayServerUnlockRecipes(UnlockRecipesType type, boolean craftingRecipeBookOpen, boolean craftingRecipeBookFilterActive, boolean smeltingRecipeBookOpen,
                                          boolean smeltingRecipeBookFilterActive, int elements, String[] recipeIdsModern, int elementsInit,
                                          String[] recipeIdsModernInit) {
        super(PacketType.Play.Server.UNLOCK_RECIPES);
        this.type = type;
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
        this.smeltingRecipeBookOpen = smeltingRecipeBookOpen;
        this.smeltingRecipeBookFilterActive = smeltingRecipeBookFilterActive;
        this.elements = elements;
        this.recipeIdsModern = recipeIdsModern;
        this.elementsInit = elementsInit;
        this.recipeIdsModernInit = recipeIdsModernInit;
    }

    public WrapperPlayServerUnlockRecipes(UnlockRecipesType type, boolean craftingRecipeBookOpen, boolean craftingRecipeBookFilterActive, boolean smeltingRecipeBookOpen,
                                          boolean smeltingRecipeBookFilterActive, boolean blastFurnaceRecipeBookOpen, boolean blastFurnaceRecipeBookFilterActive,
                                          boolean smokerRecipeBookOpen, boolean smokerRecipeBookFilterActive, int elements, String[] recipeIdsModern,
                                          int elementsInit, String[] recipeIdsModernInit) {
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
        this.recipeIdsModern = recipeIdsModern;
        this.elementsInit = elementsInit;
        this.recipeIdsModernInit = recipeIdsModernInit;
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
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeIdsModern = new String[this.elements];
            for (int i = 0; i < this.elements; i++) {
                this.recipeIdsModern[i] = readString();
            }
        } else {
            this.recipeIdsLegacy = new int[this.elements];
            for (int i = 0; i < this.elements; i++) {
                this.recipeIdsLegacy[i] = readVarInt();
            }
        }

        if (type == UnlockRecipesType.INIT) {
            this.elementsInit = readVarInt();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                this.recipeIdsModernInit = new String[this.elementsInit];
                for (int i = 0; i < this.elementsInit; i++) {
                    this.recipeIdsModernInit[i] = readString();
                }
            } else {
                this.recipeIdsLegacyInit = new int[this.elementsInit];
                for (int i = 0; i < this.elementsInit; i++) {
                    this.recipeIdsLegacyInit[i] = readVarInt();
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
            Arrays.stream(this.recipeIdsModern).forEach(this::writeString);
        } else {
            Arrays.stream(this.recipeIdsLegacy).forEach(this::writeVarInt);
        }
        if (type == UnlockRecipesType.INIT) {
            writeVarInt(this.elementsInit);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                Arrays.stream(this.recipeIdsModernInit).forEach(this::writeString);
            } else {
                Arrays.stream(this.recipeIdsLegacyInit).forEach(this::writeVarInt);
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
        this.recipeIdsModern = wrapper.recipeIdsModern;
        this.recipeIdsLegacy = wrapper.recipeIdsLegacy;
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

    public <T> T getRecipeIds() {
        return (T) (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? recipeIdsModern : recipeIdsLegacy);
    }

    public <T> void setRecipeIds(T recipeIds) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeIdsModern = (String[]) recipeIds;
        } else {
            this.recipeIdsLegacy = (int[]) recipeIds;
        }
    }

    public int getElementsInit() {
        return elementsInit;
    }

    public void setElementsInit(int elementsInit) {
        this.elementsInit = elementsInit;
    }

    public <T> T getRecipeIdsInit() {
        return (T) (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? recipeIdsModernInit : recipeIdsLegacyInit);
    }

    public <T> void setRecipeIdsInit(T recipeIds) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.recipeIdsModernInit = (String[]) recipeIds;
        } else {
            this.recipeIdsLegacyInit = (int[]) recipeIds;
        }
    }
}
