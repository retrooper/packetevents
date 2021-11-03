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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;

import java.util.Optional;

public class Dimension {
    private DimensionType type;
    private Optional<NBTCompound> attributes;

    public Dimension(DimensionType type) {
        this.type = type;
        this.attributes = Optional.empty();
    }

    public Dimension(DimensionType type, NBTCompound attributes) {
        this.type = type;
        setAttributes(attributes);
    }

    public DimensionType getType() {
        return type;
    }

    public void setType(DimensionType type) {
        this.type = type;
    }

    public Optional<NBTCompound> getAttributes() {
        return attributes;
    }

    public void setAttributes(NBTCompound attributes) {
        if (attributes == null) {
            this.attributes = Optional.empty();
            return;
        }
        this.attributes = Optional.of(attributes);
    }
}
