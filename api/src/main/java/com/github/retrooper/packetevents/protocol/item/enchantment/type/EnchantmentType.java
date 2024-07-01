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

package com.github.retrooper.packetevents.protocol.item.enchantment.type;

import com.github.retrooper.packetevents.protocol.component.IComponentMap;
import com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import com.github.retrooper.packetevents.protocol.item.enchantment.EnchantmentDefinition;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface EnchantmentType extends MappedEntity, CopyableEntity<EnchantmentType> {

    Component getDescription();

    EnchantmentDefinition getDefinition();

    MappedEntitySet<EnchantmentType> getExclusiveSet();

    IComponentMap getEffects();

    static EnchantmentType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        Component description = AdventureSerializer.fromNbt(compound.getTagOrThrow("description"));
        EnchantmentDefinition definition = EnchantmentDefinition.decode(compound, version, data);
        MappedEntitySet<EnchantmentType> exclusiveSet = Optional.ofNullable(compound.getTagOrNull("exclusive_set"))
                .map(tag -> MappedEntitySet.decode(tag, version, EnchantmentTypes.getRegistry())) // TODO use user registry
                .orElseGet(MappedEntitySet::createEmpty);
        IComponentMap effects = StaticComponentMap.EMPTY;//IComponentMap.decode(compound.getTagOrThrow("effects"), version, ); FIXME (OPTIONAL)
        return new StaticEnchantmentType(data, description, definition, exclusiveSet, effects);
    }

    static NBT encode(EnchantmentType type, ClientVersion version) {
        NBTCompound compound = (NBTCompound) EnchantmentDefinition.encode(type.getDefinition(), version);
        compound.setTag("description", AdventureSerializer.toNbt(type.getDescription()));
        if (!type.getExclusiveSet().isEmpty()) {
            compound.setTag("exclusive_set", MappedEntitySet.encode(type.getExclusiveSet(), version));
        }
        // compound.setTag("effects", IComponentMap.encode(type.getEffects(), version)); FIXME (OPTIONAL)
        return compound;
    }
}
