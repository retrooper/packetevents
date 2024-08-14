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

package com.github.retrooper.packetevents.protocol.mapper;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractMappedEntity implements MappedEntity {

    protected final @Nullable TypesBuilderData data;

    protected AbstractMappedEntity(@Nullable TypesBuilderData data) {
        this.data = data;
    }

    public @Nullable TypesBuilderData getRegistryData() {
        return this.data;
    }

    @Override
    public ResourceLocation getName() {
        if (this.data != null) {
            return this.data.getName();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int getId(ClientVersion version) {
        if (this.data != null) {
            return this.data.getId(version);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered() {
        return this.data != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AbstractMappedEntity)) return false;
        AbstractMappedEntity that = (AbstractMappedEntity) obj;
        if (this.data != null && that.data != null) {
            return this.data.getName().equals(that.data.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.data != null) {
            return Objects.hashCode(this.data.getName());
        }
        return super.hashCode();
    }
}
