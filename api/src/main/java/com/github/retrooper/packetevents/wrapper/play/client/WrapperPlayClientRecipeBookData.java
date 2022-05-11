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
import com.github.retrooper.packetevents.protocol.recipe.RecipeState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayClientRecipeBookData extends PacketWrapper<WrapperPlayClientRecipeBookData> {
  private RecipeState state;
  private Optional<String> recipeId;
  private boolean craftingRecipeBookOpen;
  private boolean craftingRecipeBookFilterActive;
  private boolean smeltingRecipeBookOpen;
  private boolean smeltingRecipeBookFilterActive;

  public WrapperPlayClientRecipeBookData(PacketReceiveEvent event) {
    super(event);
  }

  public WrapperPlayClientRecipeBookData(RecipeState state, Optional<String> recipeId, boolean craftingRecipeBookOpen, boolean craftingRecipeBookFilterActive, boolean smeltingRecipeBookOpen, boolean smeltingRecipeBookFilterActive) {
    super(PacketType.Play.Client.RECIPE_BOOK_DATA);
    this.state = state;
    this.recipeId = recipeId;
    this.craftingRecipeBookOpen = craftingRecipeBookOpen;
    this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
    this.smeltingRecipeBookOpen = smeltingRecipeBookOpen;
    this.smeltingRecipeBookFilterActive = smeltingRecipeBookFilterActive;
  }

  @Override
  public void read() {
    this.recipeId = Optional.empty();
    this.state = RecipeState.VALUES[readVarInt()];
    if (this.state == RecipeState.DISPLAYED_RECIPE) {
      this.recipeId = Optional.of(readString());
    } else {
      this.craftingRecipeBookOpen = readBoolean();
      this.craftingRecipeBookFilterActive = readBoolean();
      this.smeltingRecipeBookOpen = readBoolean();
      this.smeltingRecipeBookFilterActive = readBoolean();
    }
  }

  @Override
  public void write() {
    writeVarInt(this.state.ordinal());
    if (this.state == RecipeState.DISPLAYED_RECIPE) {
      writeString(this.recipeId.get());
    } else {
      writeBoolean(this.craftingRecipeBookOpen);
      writeBoolean(this.craftingRecipeBookFilterActive);
      writeBoolean(this.smeltingRecipeBookOpen);
      writeBoolean(this.smeltingRecipeBookFilterActive);
    }
  }

  @Override
  public void copy(WrapperPlayClientRecipeBookData wrapper) {
    this.state = wrapper.state;
    this.recipeId = wrapper.recipeId;
    this.craftingRecipeBookOpen = wrapper.craftingRecipeBookOpen;
    this.craftingRecipeBookFilterActive = wrapper.craftingRecipeBookFilterActive;
    this.smeltingRecipeBookOpen = wrapper.smeltingRecipeBookOpen;
    this.smeltingRecipeBookFilterActive = wrapper.smeltingRecipeBookFilterActive;
  }

  public RecipeState getState() {
    return state;
  }

  public void setState(RecipeState state) {
    this.state = state;
  }

  public Optional<String> getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(Optional<String> recipeId) {
    this.recipeId = recipeId;
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
}
