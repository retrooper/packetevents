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

import com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class FoodProperties {

    private final int nutrition;
    private final float saturation;
    private final boolean canAlwaysEat;
    private final float eatSeconds;
    private final List<PossibleEffect> effects;

    public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat, float eatSeconds, List<PossibleEffect> effects) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.canAlwaysEat = canAlwaysEat;
        this.eatSeconds = eatSeconds;
        this.effects = effects;
    }

    public static FoodProperties read(PacketWrapper<?> wrapper) {
        int nutrition = wrapper.readVarInt();
        float saturation = wrapper.readFloat();
        boolean canAlwaysEat = wrapper.readBoolean();
        float eatSeconds = wrapper.readFloat();
        List<PossibleEffect> effects = wrapper.readList(PossibleEffect::read);
        return new FoodProperties(nutrition, saturation, canAlwaysEat, eatSeconds, effects);
    }

    public static void write(PacketWrapper<?> wrapper, FoodProperties props) {
        wrapper.writeVarInt(props.nutrition);
        wrapper.writeFloat(props.saturation);
        wrapper.writeBoolean(props.canAlwaysEat);
        wrapper.writeFloat(props.eatSeconds);
        wrapper.writeList(props.effects, PossibleEffect::write);
    }

    public static class PossibleEffect {

        private final PotionEffect effect;
        private final float probability;

        public PossibleEffect(PotionEffect effect, float probability) {
            this.effect = effect;
            this.probability = probability;
        }

        public static PossibleEffect read(PacketWrapper<?> wrapper) {
            PotionEffect effect = PotionEffect.read(wrapper);
            float probability = wrapper.readFloat();
            return new PossibleEffect(effect, probability);
        }

        public static void write(PacketWrapper<?> wrapper, PossibleEffect effect) {
            PotionEffect.write(wrapper, effect.effect);
            wrapper.writeFloat(effect.probability);
        }
    }
}


