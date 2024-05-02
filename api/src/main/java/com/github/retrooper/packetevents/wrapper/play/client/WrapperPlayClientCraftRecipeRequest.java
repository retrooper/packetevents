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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public class WrapperPlayClientCraftRecipeRequest extends PacketWrapper<WrapperPlayClientCraftRecipeRequest> {
    private int windowId;
    private @Nullable String recipe;
    private boolean makeAll;

    public WrapperPlayClientCraftRecipeRequest(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientCraftRecipeRequest(int windowId, boolean makeAll) {
        super(PacketType.Play.Client.CRAFT_RECIPE_REQUEST);
        this.windowId = windowId;
        this.makeAll = makeAll;
    }

    public WrapperPlayClientCraftRecipeRequest(int windowId, @Nullable String recipe, boolean makeAll) {
        super(PacketType.Play.Client.CRAFT_RECIPE_REQUEST);
        this.windowId = windowId;
        this.recipe = recipe;
        this.makeAll = makeAll;
    }

    @Override
    public void read() {
        this.windowId = readByte();
        this.recipe = readString();

        this.makeAll = readBoolean();
    }

    @Override
    public void write() {
        writeByte(this.windowId);
        writeString(this.recipe);
        writeBoolean(this.makeAll);
    }

    @Override
    public void copy(WrapperPlayClientCraftRecipeRequest wrapper) {
        this.windowId = wrapper.windowId;
        this.recipe = wrapper.recipe;
        this.makeAll = wrapper.makeAll;
    }

    public int getWindowId() {
        return windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public <T> T getRecipe() {
        return (T) (recipe);
    }

    public <T> void setRecipe(T recipe) {
        this.recipe = (String) recipe;
    }

    public boolean isMakeAll() {
        return makeAll;
    }

    public void setMakeAll(boolean makeAll) {
        this.makeAll = makeAll;
    }
}
