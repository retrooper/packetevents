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

package com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class RemoveEffectsConsumeEffect extends ConsumeEffect<RemoveEffectsConsumeEffect> {

    private final MappedEntitySet<PotionType> effects;

    public RemoveEffectsConsumeEffect(MappedEntitySet<PotionType> effects) {
        super(ConsumeEffectTypes.REMOVE_EFFECTS);
        this.effects = effects;
    }

    public static RemoveEffectsConsumeEffect read(PacketWrapper<?> wrapper) {
        MappedEntitySet<PotionType> effects = MappedEntitySet.read(wrapper, PotionTypes::getById);
        return new RemoveEffectsConsumeEffect(effects);
    }

    public static void write(PacketWrapper<?> wrapper, RemoveEffectsConsumeEffect effect) {
        MappedEntitySet.write(wrapper, effect.effects);
    }

    public MappedEntitySet<PotionType> getEffects() {
        return this.effects;
    }
}
