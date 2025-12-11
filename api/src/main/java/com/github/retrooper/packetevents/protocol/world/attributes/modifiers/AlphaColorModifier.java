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

import com.github.retrooper.packetevents.protocol.color.AlphaColor;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public interface AlphaColorModifier<A> extends AttributeModifier<AlphaColor, A> {

    AlphaColorModifier<AlphaColor> ALPHA_BLEND = (ArgbModifier) Color::blendWith;
    AlphaColorModifier<Color> ADD = (RgbModifier) AlphaColor::plus;
    AlphaColorModifier<Color> SUBTRACT = (RgbModifier) AlphaColor::minus;
    AlphaColorModifier<AlphaColor> MULTIPLY = (ArgbModifier) AlphaColor::times;
    AlphaColorModifier<BlendToGrayArgument> BLEND_TO_GRAY = new AlphaColorModifier<BlendToGrayArgument>() {
        @Override
        public AlphaColor apply(AlphaColor value, BlendToGrayArgument arg) {
            return arg.blend(value);
        }

        @Override
        public NbtCodec<BlendToGrayArgument> argumentCodec(EnvironmentAttribute<AlphaColor> attribute) {
            return BlendToGrayArgument.CODEC;
        }
    };

    @FunctionalInterface
    interface ArgbModifier extends AlphaColorModifier<AlphaColor> {

        @Override
        default NbtCodec<AlphaColor> argumentCodec(EnvironmentAttribute<AlphaColor> attribute) {
            return NbtCodecs.ARGB_COLOR;
        }
    }

    @FunctionalInterface
    interface RgbModifier extends AlphaColorModifier<Color> {

        @Override
        default NbtCodec<Color> argumentCodec(EnvironmentAttribute<AlphaColor> attribute) {
            return NbtCodecs.RGB_COLOR;
        }
    }
}
