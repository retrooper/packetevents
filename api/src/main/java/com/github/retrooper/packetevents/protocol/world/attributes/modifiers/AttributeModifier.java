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
import com.github.retrooper.packetevents.protocol.util.CodecNameable;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import com.github.retrooper.packetevents.util.MapUtil;
import org.jspecify.annotations.NullMarked;

import java.util.AbstractMap;
import java.util.Map;

/**
 * @versions 1.21.11+
 */
@NullMarked
public interface AttributeModifier<T, A> {

    Map<Operation, AttributeModifier<Boolean, ?>> BOOLEAN_LIBRARY = MapUtil.createMap(
            new AbstractMap.SimpleEntry<>(Operation.AND, BooleanModifier.AND),
            new AbstractMap.SimpleEntry<>(Operation.NAND, BooleanModifier.NAND),
            new AbstractMap.SimpleEntry<>(Operation.OR, BooleanModifier.OR),
            new AbstractMap.SimpleEntry<>(Operation.NOR, BooleanModifier.NOR),
            new AbstractMap.SimpleEntry<>(Operation.XOR, BooleanModifier.XOR),
            new AbstractMap.SimpleEntry<>(Operation.XNOR, BooleanModifier.XNOR)
    );
    Map<Operation, AttributeModifier<Float, ?>> FLOAT_LIBRARY = MapUtil.createMap(
            new AbstractMap.SimpleEntry<>(Operation.ALPHA_BLEND, FloatModifier.ALPHA_BLEND),
            new AbstractMap.SimpleEntry<>(Operation.ADD, FloatModifier.ADD),
            new AbstractMap.SimpleEntry<>(Operation.SUBTRACT, FloatModifier.SUBTRACT),
            new AbstractMap.SimpleEntry<>(Operation.MULTIPLY, FloatModifier.MULTIPLY),
            new AbstractMap.SimpleEntry<>(Operation.MINIMUM, FloatModifier.MINIMUM),
            new AbstractMap.SimpleEntry<>(Operation.MAXIMUM, FloatModifier.MAXIMUM)
    );
    Map<Operation, AttributeModifier<Color, ?>> RGB_COLOR_LIBRARY = MapUtil.createMap(
            new AbstractMap.SimpleEntry<>(Operation.ALPHA_BLEND, ColorModifier.ALPHA_BLEND),
            new AbstractMap.SimpleEntry<>(Operation.ADD, ColorModifier.ADD),
            new AbstractMap.SimpleEntry<>(Operation.SUBTRACT, ColorModifier.SUBTRACT),
            new AbstractMap.SimpleEntry<>(Operation.MULTIPLY, ColorModifier.MULTIPLY),
            new AbstractMap.SimpleEntry<>(Operation.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY)
    );
    Map<Operation, AttributeModifier<AlphaColor, ?>> ARGB_COLOR_LIBRARY = MapUtil.createMap(
            new AbstractMap.SimpleEntry<>(Operation.ALPHA_BLEND, AlphaColorModifier.ALPHA_BLEND),
            new AbstractMap.SimpleEntry<>(Operation.ADD, AlphaColorModifier.ADD),
            new AbstractMap.SimpleEntry<>(Operation.SUBTRACT, AlphaColorModifier.SUBTRACT),
            new AbstractMap.SimpleEntry<>(Operation.MULTIPLY, AlphaColorModifier.MULTIPLY),
            new AbstractMap.SimpleEntry<>(Operation.BLEND_TO_GRAY, AlphaColorModifier.BLEND_TO_GRAY)
    );
    /**
     * @versions 26.1+
     */
    Map<Operation, AttributeModifier<Integer, ?>> INTEGER_LIBRARY = MapUtil.createMap(
            new AbstractMap.SimpleEntry<>(Operation.ADD, IntegerModifier.ADD),
            new AbstractMap.SimpleEntry<>(Operation.SUBTRACT, IntegerModifier.SUBTRACT),
            new AbstractMap.SimpleEntry<>(Operation.MULTIPLY, IntegerModifier.MULTIPLY),
            new AbstractMap.SimpleEntry<>(Operation.MINIMUM, IntegerModifier.MINIMUM),
            new AbstractMap.SimpleEntry<>(Operation.MAXIMUM, IntegerModifier.MAXIMUM)
    );

    @SuppressWarnings("unchecked") // types don't matter
    static <T> AttributeModifier<T, T> override() {
        return (AttributeModifier<T, T>) OverrideModifier.INSTANCE;
    }

    T apply(T value, A arg);

    NbtCodec<A> argumentCodec(EnvironmentAttribute<T> attribute);

    enum Operation implements CodecNameable {

        OVERRIDE("override"),
        ALPHA_BLEND("alpha_blend"),
        ADD("add"),
        SUBTRACT("subtract"),
        MULTIPLY("multiply"),
        BLEND_TO_GRAY("blend_to_gray"),
        MINIMUM("minimum"),
        MAXIMUM("maximum"),
        AND("and"),
        NAND("nand"),
        OR("or"),
        NOR("nor"),
        XOR("xor"),
        XNOR("xnor"),
        ;

        public static final NbtCodec<Operation> CODEC = NbtCodecs.forEnum(values());

        private final String name;

        Operation(String name) {
            this.name = name;
        }

        @Override
        public String getCodecName() {
            return this.name;
        }
    }

    final class OverrideModifier<T> implements AttributeModifier<T, T> {

        private static final OverrideModifier<?> INSTANCE = new OverrideModifier<>();

        @Override
        public T apply(T value, T arg) {
            return arg;
        }

        @Override
        public NbtCodec<T> argumentCodec(EnvironmentAttribute<T> attribute) {
            return attribute.getType().getValueCodec();
        }
    }
}
