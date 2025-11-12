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

package com.github.retrooper.packetevents.util.mappings;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public interface IRegistryHolder {

    default <T extends MappedEntity> IRegistry<T> getRegistryOr(IRegistry<T> fallbackRegistry) {
        return this.getRegistryOr(fallbackRegistry, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    @SuppressWarnings("unchecked") // should be fine
    default <T extends MappedEntity> IRegistry<T> getRegistryOr(IRegistry<T> fallbackRegistry, ClientVersion version) {
        IRegistry<?> replacedRegistry = this.getRegistry(fallbackRegistry.getRegistryKey(), version);
        return replacedRegistry != null ? (IRegistry<T>) replacedRegistry : fallbackRegistry;
    }

    default @Nullable IRegistry<?> getRegistry(ResourceLocation registryKey) {
        return this.getRegistry(registryKey, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    @Nullable IRegistry<?> getRegistry(ResourceLocation registryKey, ClientVersion version);
}
