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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class HashedComponentPatchMap {

    private final Map<ComponentType<?>, Integer> addedComponents;
    private final Set<ComponentType<?>> removedComponents;

    public HashedComponentPatchMap(Map<ComponentType<?>, Integer> addedComponents, Set<ComponentType<?>> removedComponents) {
        this.addedComponents = addedComponents;
        this.removedComponents = removedComponents;
    }

    public static HashedComponentPatchMap read(PacketWrapper<?> wrapper) {
        Map<ComponentType<?>, Integer> addedComponents = wrapper.readMap(ew ->
                ew.readMappedEntity(ComponentTypes.getRegistry()), PacketWrapper::readInt);
        Set<ComponentType<?>> removedComponents = wrapper.readCollection(HashSet::new, ew ->
                ew.readMappedEntity(ComponentTypes.getRegistry()));
        return new HashedComponentPatchMap(addedComponents, removedComponents);
    }

    public static void write(PacketWrapper<?> wrapper, HashedComponentPatchMap map) {
        wrapper.writeMap(map.addedComponents, PacketWrapper::writeMappedEntity, PacketWrapper::writeInt);
        wrapper.writeCollection(map.removedComponents, PacketWrapper::writeMappedEntity);
    }

    public Map<ComponentType<?>, Integer> getAddedComponents() {
        return this.addedComponents;
    }

    public Set<ComponentType<?>> getRemovedComponents() {
        return this.removedComponents;
    }
}
