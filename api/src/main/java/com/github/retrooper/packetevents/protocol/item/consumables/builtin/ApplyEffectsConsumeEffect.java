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
import com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class ApplyEffectsConsumeEffect extends ConsumeEffect<ApplyEffectsConsumeEffect> {

    private final List<PotionEffect> effects;
    private final float probability;

    public ApplyEffectsConsumeEffect(List<PotionEffect> effects, float probability) {
        super(ConsumeEffectTypes.APPLY_EFFECTS);
        this.effects = effects;
        this.probability = probability;
    }

    public static ApplyEffectsConsumeEffect read(PacketWrapper<?> wrapper) {
        List<PotionEffect> effects = wrapper.readList(PotionEffect::read);
        float probability = wrapper.readFloat();
        return new ApplyEffectsConsumeEffect(effects, probability);
    }

    public static void write(PacketWrapper<?> wrapper, ApplyEffectsConsumeEffect effect) {
        wrapper.writeList(effect.effects, PotionEffect::write);
        wrapper.writeFloat(effect.probability);
    }

    public List<PotionEffect> getEffects() {
        return this.effects;
    }

    public float getProbability() {
        return this.probability;
    }
}
