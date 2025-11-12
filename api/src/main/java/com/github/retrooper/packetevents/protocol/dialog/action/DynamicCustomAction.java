/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.dialog.action;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class DynamicCustomAction implements Action {

    private final ResourceLocation id;
    private final @Nullable NBTCompound additions;

    public DynamicCustomAction(ResourceLocation id, @Nullable NBTCompound additions) {
        this.id = id;
        this.additions = additions;
    }

    public static DynamicCustomAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        ResourceLocation id = compound.getOrThrow("id", ResourceLocation::decode, wrapper);
        NBTCompound additions = compound.getCompoundTagOrNull("additions");
        return new DynamicCustomAction(id, additions);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DynamicCustomAction action) {
        compound.set("id", action.id, ResourceLocation::encode, wrapper);
        if (action.additions != null) {
            compound.setTag("additions", action.additions);
        }
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypes.DYNAMIC_CUSTOM;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public @Nullable NBTCompound getAdditions() {
        return this.additions;
    }
}
