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

package com.github.retrooper.packetevents.protocol.world.attributes.modifiers;

import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import com.github.retrooper.packetevents.util.AlphaFloat;
import com.github.retrooper.packetevents.util.MathUtil;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public interface FloatModifier<A> extends AttributeModifier<Float, A> {

    FloatModifier<AlphaFloat> ALPHA_BLEND = new FloatModifier<AlphaFloat>() {
        @Override
        public Float apply(Float value, AlphaFloat arg) {
            return MathUtil.lerp(arg.getAlpha(), value, arg.getValue());
        }

        @Override
        public NbtCodec<AlphaFloat> argumentCodec(EnvironmentAttribute<Float> attribute) {
            return AlphaFloat.CODEC;
        }
    };
    FloatModifier<Float> ADD = (Simple) Float::sum;
    FloatModifier<Float> SUBTRACT = (Simple) (a, b) -> a - b;
    FloatModifier<Float> MULTIPLY = (Simple) (a, b) -> a * b;
    FloatModifier<Float> MINIMUM = (Simple) Math::min;
    FloatModifier<Float> MAXIMUM = (Simple) Math::max;

    @FunctionalInterface
    interface Simple extends FloatModifier<Float> {

        @Override
        default NbtCodec<Float> argumentCodec(EnvironmentAttribute<Float> attribute) {
            return NbtCodecs.FLOAT;
        }
    }
}
