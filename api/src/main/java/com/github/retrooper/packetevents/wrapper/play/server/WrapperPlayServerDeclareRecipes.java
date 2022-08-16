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
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import com.github.retrooper.packetevents.protocol.recipe.Recipe;
import com.github.retrooper.packetevents.protocol.recipe.RecipeType;
import com.github.retrooper.packetevents.protocol.recipe.data.*;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Locale;

// Copy and pasted from MCProtocolLib
public class WrapperPlayServerDeclareRecipes extends PacketWrapper<WrapperPlayServerDeclareRecipes> {
    private Recipe[] recipes;

    public WrapperPlayServerDeclareRecipes(PacketSendEvent event) {
        super(event);
    }

    public Recipe[] getRecipes() {
        return this.recipes;
    }

    public void setRecipes(Recipe[] recipes) {
        this.recipes = recipes;
    }

    @Override
    public void read() {
        this.recipes = new Recipe[readVarInt()];
        for (int i = 0; i < this.recipes.length; i++) {
            RecipeType type = RecipeType.valueOf(readString().replace("minecraft:", "").toUpperCase(Locale.ROOT));
            String identifier = readString();
            RecipeData data = null;
            switch (type) {
                case CRAFTING_SHAPELESS: {
                    String group = readString();
                    Ingredient[] ingredients = new Ingredient[readVarInt()];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = this.readIngredient();
                    }

                    ItemStack result = readItemStack();

                    data = new ShapelessRecipeData(group, ingredients, result);
                    break;
                }
                case CRAFTING_SHAPED: {
                    int width = readVarInt();
                    int height = readVarInt();
                    String group = readString();
                    Ingredient[] ingredients = new Ingredient[width * height];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = this.readIngredient();
                    }

                    ItemStack result = readItemStack();

                    data = new ShapedRecipeData(width, height, group, ingredients, result);
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    String group = readString();
                    Ingredient ingredient = this.readIngredient();
                    ItemStack result = readItemStack();
                    float experience = readFloat();
                    int cookingTime = readVarInt();

                    data = new CookedRecipeData(group, ingredient, result, experience, cookingTime);
                    break;
                }
                case STONECUTTING: {
                    String group = readString();
                    Ingredient ingredient = this.readIngredient();
                    ItemStack result = readItemStack();

                    data = new StoneCuttingRecipeData(group, ingredient, result);
                    break;
                }
                case SMITHING: {
                    Ingredient base = this.readIngredient();
                    Ingredient addition = this.readIngredient();
                    ItemStack result = readItemStack();

                    data = new SmithingRecipeData(base, addition, result);
                    break;
                }
                default:
                    break;
            }

            this.recipes[i] = new Recipe(type, identifier, data);
        }
    }

    private Ingredient readIngredient() {
        ItemStack[] options = new ItemStack[readVarInt()];
        for (int i = 0; i < options.length; i++) {
            options[i] = readItemStack();
        }

        return new Ingredient(options);
    }

    private void writeIngredient(Ingredient ingredient) {
        writeVarInt(ingredient.getOptions().length);
        for (ItemStack option : ingredient.getOptions()) {
            writeItemStack(option);
        }
    }

    public void copy(WrapperPlayServerDeclareRecipes wrapper) {
        this.recipes = wrapper.recipes;
    }

    @Override
    public void write() {
        writeVarInt(this.recipes.length);
        for (Recipe recipe : this.recipes) {
            writeString("minecraft:" + recipe.getType().toString().toLowerCase(Locale.ROOT));
            writeString(recipe.getIdentifier());
            switch (recipe.getType()) {
                case CRAFTING_SHAPELESS: {
                    ShapelessRecipeData data = (ShapelessRecipeData) recipe.getData();

                    writeString(data.getGroup());
                    writeVarInt(data.getIngredients().length);
                    for (Ingredient ingredient : data.getIngredients()) {
                        this.writeIngredient(ingredient);
                    }

                    writeItemStack(data.getResult());
                    break;
                }
                case CRAFTING_SHAPED: {
                    ShapedRecipeData data = (ShapedRecipeData) recipe.getData();
                    if (data.getIngredients().length != data.getWidth() * data.getHeight()) {
                        throw new IllegalStateException("Shaped recipe must have ingredient count equal to width * height.");
                    }

                    writeVarInt(data.getWidth());
                    writeVarInt(data.getHeight());
                    writeString(data.getGroup());
                    for (Ingredient ingredient : data.getIngredients()) {
                        this.writeIngredient(ingredient);
                    }

                    writeItemStack(data.getResult());
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    CookedRecipeData data = (CookedRecipeData) recipe.getData();

                    writeString(data.getGroup());
                    this.writeIngredient(data.getIngredient());
                    writeItemStack(data.getResult());
                    writeFloat(data.getExperience());
                    writeVarInt(data.getCookingTime());
                    break;
                }
                case STONECUTTING: {
                    StoneCuttingRecipeData data = (StoneCuttingRecipeData) recipe.getData();

                    writeString(data.getGroup());
                    this.writeIngredient(data.getIngredient());
                    writeItemStack(data.getResult());
                    break;
                }
                case SMITHING: {
                    SmithingRecipeData data = (SmithingRecipeData) recipe.getData();

                    this.writeIngredient(data.getBase());
                    this.writeIngredient(data.getAddition());
                    writeItemStack(data.getResult());
                    break;
                }
                default:
                    break;
            }
        }
    }
}
