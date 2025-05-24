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

package com.github.retrooper.packetevents.protocol.item;

import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.HashedComponentPatchMap;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class HashedStack {

    private final ItemType item;
    private final int count;
    private final HashedComponentPatchMap components;

    public HashedStack(ItemType item, int count, HashedComponentPatchMap components) {
        this.item = item;
        this.count = count;
        this.components = components;
    }

    public static Optional<HashedStack> read(PacketWrapper<?> wrapper) {
        if (!wrapper.readBoolean()) {
            return Optional.empty();
        }
        ItemType item = wrapper.readMappedEntity(ItemTypes.getRegistry());
        int count = wrapper.readVarInt();
        HashedComponentPatchMap components = HashedComponentPatchMap.read(wrapper);
        return Optional.of(new HashedStack(item, count, components));
    }

    public static void write(PacketWrapper<?> wrapper, Optional<HashedStack> optStack) {
        if (optStack == null || !optStack.isPresent()) {
            wrapper.writeBoolean(false);
        } else {
            wrapper.writeBoolean(true);

            HashedStack stack = optStack.get();
            wrapper.writeMappedEntity(stack.item);
            wrapper.writeVarInt(stack.count);
            HashedComponentPatchMap.write(wrapper, stack.components);
        }
    }

    public static Optional<HashedStack> fromItemStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }
        // extract component info from item stack
        Map<ComponentType<?>, Optional<?>> patches = stack.getComponents().getPatches();
        Map<ComponentType<?>, Integer> addedComponents = new HashMap<>(patches.size());
        Set<ComponentType<?>> removedComponents = new HashSet<>(patches.size());
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : patches.entrySet()) {
            if (patch.getValue().isPresent()) {
                // TODO how to hash? are we even able to do this without rewriting everything?
                addedComponents.put(patch.getKey(), 0);
            } else {
                removedComponents.add(patch.getKey());
            }
        }
        // construct hashed item stack structure
        HashedComponentPatchMap map = new HashedComponentPatchMap(addedComponents, removedComponents);
        return Optional.of(new HashedStack(stack.getType(), stack.getAmount(), map));
    }

    /**
     * <strong>WARNING</strong>: We can't fully reconstruct the ItemStack which was hashed,
     * as all added component values have been hashed
     */
    public ItemStack asItemStack() {
        ItemStack stack = ItemStack.builder().type(this.item).amount(this.count).build();
        // we can't re-construct the hashed added components, but we
        // can apply the removed components
        for (ComponentType<?> component : this.components.getRemovedComponents()) {
            stack.unsetComponent(component);
        }
        return stack;
    }

    public ItemType getItem() {
        return this.item;
    }

    public int getCount() {
        return this.count;
    }

    public HashedComponentPatchMap getComponents() {
        return this.components;
    }
}
