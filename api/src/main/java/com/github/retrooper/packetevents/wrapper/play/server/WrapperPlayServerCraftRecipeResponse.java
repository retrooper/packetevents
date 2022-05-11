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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
import java.util.OptionalInt;

public class WrapperPlayServerCraftRecipeResponse extends PacketWrapper<WrapperPlayServerCraftRecipeResponse> {
  private int windowId;
  private OptionalInt recipeLegacy;
  private Optional<String> recipeModern;

  public WrapperPlayServerCraftRecipeResponse(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerCraftRecipeResponse(int windowId, OptionalInt recipeLegacy) {
    super(PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
    this.windowId = windowId;
    this.recipeLegacy = recipeLegacy;
  }

  public WrapperPlayServerCraftRecipeResponse(int windowId, Optional<String> recipeModern) {
    super(PacketType.Play.Server.CRAFT_RECIPE_RESPONSE);
    this.windowId = windowId;
    this.recipeModern = recipeModern;
  }

  @Override
  public void read() {
    this.recipeLegacy = OptionalInt.empty();
    this.recipeModern = Optional.empty();
    this.windowId = readByte();
    if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
      this.recipeLegacy = OptionalInt.of(readVarInt());
    } else {
      this.recipeModern = Optional.of(readString());
    }
  }

  @Override
  public void write() {
    writeByte(this.windowId);
    if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
      writeVarInt(this.recipeLegacy.getAsInt());
    } else {
      writeString(this.recipeModern.get());
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

  public OptionalInt getRecipeLegacy() {
    return recipeLegacy;
  }

  public void setRecipeLegacy(OptionalInt recipeLegacy) {
    this.recipeLegacy = recipeLegacy;
  }

  public Optional<String> getRecipeModern() {
    return recipeModern;
  }

  public void setRecipeModern(Optional<String> recipeModern) {
    this.recipeModern = recipeModern;
  }
}
