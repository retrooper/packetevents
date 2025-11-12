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

package com.github.retrooper.packetevents.util.mappings;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

public class TypesBuilderData {

    protected final TypesBuilder typesBuilder;
    protected final int[] data;
    protected final ResourceLocation name;

    @Deprecated
    public TypesBuilderData(ResourceLocation name, int[] data) {
        this(new TypesBuilder("", true), name, data);
    }

    @ApiStatus.Internal
    public TypesBuilderData(TypesBuilder typesBuilder, ResourceLocation name, int[] data) {
        this.typesBuilder = typesBuilder;
        this.name = name;
        this.data = data;
    }

    public int getId(ClientVersion version) {
        return this.data[this.typesBuilder.getDataIndex(version)];
    }

    @Deprecated
    public int[] getData() {
        return data;
    }

    public ResourceLocation getName() {
        return this.name;
    }
}
