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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.protocol.mapper.GenericMappedEntity;
import com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// TODO: GenericMappedEntity -> minecraft:potion registry
public class ItemPotionContents {

    private final @Nullable GenericMappedEntity potion;
    private final @Nullable Integer customColor;
    private final List<PotionEffect> customEffects;

    public ItemPotionContents(
            @Nullable GenericMappedEntity potion,
            @Nullable Integer customColor,
            List<PotionEffect> customEffects
    ) {
        this.potion = potion;
        this.customColor = customColor;
        this.customEffects = customEffects;
    }

    public static ItemPotionContents read(PacketWrapper<?> wrapper) {
        GenericMappedEntity potionId = wrapper.readOptional(GenericMappedEntity::read);
        Integer customColor = wrapper.readOptional(PacketWrapper::readInt);
        List<PotionEffect> customEffects = wrapper.readList(PotionEffect::read);
        return new ItemPotionContents(potionId, customColor, customEffects);
    }

    public static void write(PacketWrapper<?> wrapper, ItemPotionContents contents) {
        wrapper.writeOptional(contents.potion, PacketWrapper::writeMappedEntity);
        wrapper.writeOptional(contents.customColor, PacketWrapper::writeInt);
        wrapper.writeList(contents.customEffects, PotionEffect::write);
    }
}
