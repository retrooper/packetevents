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
import com.github.retrooper.packetevents.util.VersionRange;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TypesBuilderData {

    protected final ResourceLocation name;
    protected final int[] ids;

    protected final TypesBuilder typesBuilder;
    protected final VersionRange versions;

    @Deprecated
    public TypesBuilderData(ResourceLocation name, int[] ids) {
        this(name, ids, new TypesBuilder("", true), VersionRange.ALL_VERSIONS);
    }

    @ApiStatus.Internal
    public TypesBuilderData(
            ResourceLocation name, int[] ids,
            TypesBuilder typesBuilder, VersionRange versions
    ) {
        this.name = name;
        this.ids = ids;
        this.typesBuilder = typesBuilder;
        this.versions = versions;
    }

    public int getId(ClientVersion version) {
        return this.ids[this.typesBuilder.getDataIndex(version)];
    }

    public ResourceLocation getName() {
        return this.name;
    }

    @Deprecated
    public int[] getData() {
        return this.ids;
    }

    public VersionRange getVersions() {
        return this.versions;
    }
}
