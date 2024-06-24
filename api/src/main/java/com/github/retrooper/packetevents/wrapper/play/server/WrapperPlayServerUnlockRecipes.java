package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.recipe.RecipeBookAction;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUnlockRecipes extends PacketWrapper<WrapperPlayServerUnlockRecipes> {


    public WrapperPlayServerUnlockRecipes(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUnlockRecipes(PacketTypeCommon packetType) {
        super(packetType);
    }

    RecipeBookAction action;
    boolean craftingRecipeBookOpen;
    boolean craftingRecipeBookFilterActive;
    boolean smeltingRecipeBookOpen;
    boolean smeltingRecipeBookFilterActive;
    boolean blastFurnaceRecipeBookOpen;
    boolean blastFurnaceRecipeBookFilterActive;
    boolean smokerRecipeBookOpen;
    boolean smokerRecipeBookFilterActive;
    String[] recipeIds;
    String[] hightlightRecipes;

    @Override
    public void read() {
        action = RecipeBookAction.getById(readVarInt());

        craftingRecipeBookOpen = readBoolean();
        craftingRecipeBookFilterActive = readBoolean();
        smeltingRecipeBookOpen = readBoolean();
        smeltingRecipeBookFilterActive = readBoolean();
        blastFurnaceRecipeBookOpen = readBoolean();
        blastFurnaceRecipeBookFilterActive = readBoolean();
        smokerRecipeBookOpen = readBoolean();
        smokerRecipeBookFilterActive = readBoolean();

        recipeIds = new String[readVarInt()];
        for (int i = 0; i < recipeIds.length; i++) {
            recipeIds[i] = readString();
        }

        if(action == RecipeBookAction.INIT) {
            hightlightRecipes = new String[readVarInt()];
            for (int i = 0; i < hightlightRecipes.length; i++) {
                hightlightRecipes[i] = readString();
            }
        }
    }

    @Override
    public void write() {
        writeVarInt(action.getId());

        writeBoolean(craftingRecipeBookOpen);
        writeBoolean(craftingRecipeBookFilterActive);
        writeBoolean(smeltingRecipeBookOpen);
        writeBoolean(smeltingRecipeBookFilterActive);
        writeBoolean(blastFurnaceRecipeBookOpen);
        writeBoolean(blastFurnaceRecipeBookFilterActive);
        writeBoolean(smokerRecipeBookOpen);
        writeBoolean(smokerRecipeBookFilterActive);

        writeVarInt(recipeIds.length);
        for (String recipeId : recipeIds) {
            writeString(recipeId);
        }

        if(action == RecipeBookAction.INIT) {
            writeVarInt(hightlightRecipes.length);
            for (String hightlightRecipe : hightlightRecipes) {
                writeString(hightlightRecipe);
            }
        }
    }

    public RecipeBookAction getAction() {
        return action;
    }

    public void setAction(RecipeBookAction action) {
        this.action = action;
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

    public String[] getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(String[] recipeIds) {
        this.recipeIds = recipeIds;
    }

    public String[] getHightlightRecipes() {
        return hightlightRecipes;
    }

    public void setHightlightRecipes(String[] hightlightRecipes) {
        this.hightlightRecipes = hightlightRecipes;
    }
}
