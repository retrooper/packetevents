/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData.RegistryElement;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents vanilla's dimension types.
 */
public final class Dimension {

    private static final ResourceLocation OVERWORLD_DIM_KEY = ResourceLocation.minecraft("overworld");
    private static final ResourceLocation OVERWORLD_CAVES_DIM_KEY = ResourceLocation.minecraft("overworld_caves");

    private final ResourceLocation name;
    private final int id;

    private final int minWorldHeight;
    private final int totalWorldHeight;

    private final @Nullable NBTCompound data;

    public Dimension(ResourceLocation name, int id, NBTCompound data) {
        this(name, id,
                data.getNumberTagOrNull("min_y"),
                data.getNumberTagOrNull("height"),
                data);
    }

    public Dimension(
            ResourceLocation name, int id,
            int minWorldHeight, int totalWorldHeight
    ) {
        this(name, id, minWorldHeight, totalWorldHeight, null);
    }

    public Dimension(
            ResourceLocation name, int id,
            @Nullable NBTNumber minWorldHeight,
            @Nullable NBTNumber totalWorldHeight,
            @Nullable NBTCompound data
    ) {
        this(name, id,
                minWorldHeight == null ? 0 : minWorldHeight.getAsInt(),
                totalWorldHeight == null ? 256 : totalWorldHeight.getAsInt(),
                data);
    }

    public Dimension(
            ResourceLocation name, int id,
            int minWorldHeight, int totalWorldHeight,
            @Nullable NBTCompound data
    ) {
        this.name = name;
        this.id = id;
        this.minWorldHeight = minWorldHeight;
        this.totalWorldHeight = totalWorldHeight;
        this.data = data;
    }

    @Deprecated
    public Dimension(DimensionType type) {
        this(new ResourceLocation(type.getName()), type.getId(), new NBTCompound());
    }

    public Dimension(int id) {
        // FIXME naming
        this(new ResourceLocation(DimensionType.getById(id).getName()),
                id, null);
    }

    @Deprecated
    public Dimension(NBTCompound data) {
        this(OVERWORLD_DIM_KEY, 0, data);
    }

    public static Dimension fromRegistryElement(
            RegistryElement element, int id, ClientVersion version) {
        if (element.getData() == null) {
            boolean extended = version.isNewerThanOrEquals(ClientVersion.V_1_18)
                    && (element.getId().equals(OVERWORLD_DIM_KEY) || element.getId().equals(OVERWORLD_CAVES_DIM_KEY));
            return new Dimension(element.getId(), id,
                    extended ? -64 : 0,
                    extended ? 256 + 128 : 256);
        }
        return new Dimension(element.getId(), id,
                ((NBTCompound) element.getData()));
    }

    @Deprecated
    public String getDimensionName() {
        return this.name.toString();
    }

    @Deprecated
    public void setDimensionName(String name) {
        throw new UnsupportedOperationException("Can't replace data for "
                + this.id + " / " + this.name);
    }

    @Deprecated
    public void setId(int id) {
        throw new UnsupportedOperationException("Can't replace data for "
                + this.id + " / " + this.name);
    }

    @Deprecated
    public DimensionType getType() {
        return DimensionType.getById(this.id);
    }

    @Deprecated
    public void setType(DimensionType type) {
        throw new UnsupportedOperationException("Can't replace data for "
                + this.id + " / " + this.name);
    }

    @Deprecated
    public NBTCompound getAttributes() {
        return this.data;
    }

    @Deprecated
    public void setAttributes(NBTCompound attributes) {
        throw new UnsupportedOperationException("Can't replace data for "
                + this.id + " / " + this.name);
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public Dimension withId(int id) {
        return new Dimension(this.name, id,
                this.minWorldHeight, this.totalWorldHeight, this.data);
    }

    public int getMinWorldHeight() {
        return this.minWorldHeight;
    }

    public int getTotalWorldHeight() {
        return this.totalWorldHeight;
    }

    public @Nullable NBTCompound getData() {
        return this.data;
    }
}
