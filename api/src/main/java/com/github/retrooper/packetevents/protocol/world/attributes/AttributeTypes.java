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

package com.github.retrooper.packetevents.protocol.world.attributes;

import com.github.retrooper.packetevents.protocol.color.AlphaColor;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects.ParticleSettings;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @versions 1.21.11+
 */
@NullMarked
public final class AttributeTypes {

    private static final VersionedRegistry<AttributeType<?>> REGISTRY = new VersionedRegistry<>("attribute_type");

    private AttributeTypes() {
    }

    @ApiStatus.Internal
    public static <T> AttributeType<T> defineUnsynced(String name) {
        return define(name, null, Collections.emptyMap());
    }

    @ApiStatus.Internal
    public static <T> AttributeType<T> define(String name, NbtCodec<T> codec) {
        return define(name, codec, Collections.emptyMap());
    }

    @ApiStatus.Internal
    public static <T> AttributeType<T> define(
            String name, @Nullable NbtCodec<T> codec,
            Map<AttributeModifier.Operation, AttributeModifier<T, ?>> modifiers
    ) {
        return REGISTRY.define(name, data ->
                new StaticAttributeType<>(data, codec, createModifierCodec(modifiers)));
    }

    @ApiStatus.Internal
    public static <T> NbtCodec<AttributeModifier<T, ?>> createModifierCodec(
            Map<AttributeModifier.Operation, AttributeModifier<T, ?>> modifiers
    ) {
        // build full forward map
        Map<AttributeModifier.Operation, AttributeModifier<T, ?>> allModifiers = new HashMap<>(modifiers.size() + 1);
        allModifiers.put(AttributeModifier.Operation.OVERRIDE, AttributeModifier.override());
        allModifiers.putAll(modifiers);
        // build inverse map
        Map<AttributeModifier<T, ?>, AttributeModifier.Operation> allInverseModifiers = new HashMap<>(modifiers.size() + 1);
        for (Map.Entry<AttributeModifier.Operation, AttributeModifier<T, ?>> entry : allModifiers.entrySet()) {
            allInverseModifiers.put(entry.getValue(), entry.getKey());
        }

        return new NbtCodec<AttributeModifier<T, ?>>() {
            @Override
            public AttributeModifier<T, ?> decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
                AttributeModifier.Operation op = AttributeModifier.Operation.CODEC.decode(nbt, wrapper);
                AttributeModifier<T, ?> modifier = allModifiers.get(op);
                if (modifier == null) {
                    throw new NbtCodecException("Unsupported operation " + op + " for " + modifiers);
                }
                return modifier;
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, AttributeModifier<T, ?> value) throws NbtCodecException {
                AttributeModifier.Operation op = allInverseModifiers.get(value);
                if (op == null) {
                    throw new NbtCodecException("Unsupported modifier " + value + " for " + modifiers);
                }
                return AttributeModifier.Operation.CODEC.encode(wrapper, op);
            }
        };
    }

    public static VersionedRegistry<AttributeType<?>> getRegistry() {
        return REGISTRY;
    }

    public static final AttributeType<Boolean> BOOLEAN = define("boolean", NbtCodecs.BOOLEAN, AttributeModifier.BOOLEAN_LIBRARY);
    /**
     * Not synced via network, not usable via packetevents.
     */
    @ApiStatus.Obsolete
    public static final AttributeType<TriState> TRI_STATE = defineUnsynced("tri_state");
    public static final AttributeType<Float> FLOAT = define("float", NbtCodecs.FLOAT, AttributeModifier.FLOAT_LIBRARY);
    public static final AttributeType<Float> ANGLE_DEGREES = define("angle_degrees", NbtCodecs.FLOAT, AttributeModifier.FLOAT_LIBRARY);
    public static final AttributeType<Color> RGB_COLOR = define("rgb_color", NbtCodecs.RGB_COLOR, AttributeModifier.RGB_COLOR_LIBRARY);
    public static final AttributeType<AlphaColor> ARGB_COLOR = define("argb_color", NbtCodecs.ARGB_COLOR, AttributeModifier.ARGB_COLOR_LIBRARY);
    /**
     * @versions 26.1+
     */
    public static final AttributeType<Integer> INTEGER = define("integer", NbtCodecs.INT, AttributeModifier.INTEGER_LIBRARY);
    public static final AttributeType<MoonPhase> MOON_PHASE = define("moon_phase", MoonPhase.CODEC);
    /**
     * Not synced via network, not usable via packetevents.
     */
    @ApiStatus.Obsolete
    public static final AttributeType<?> ACTIVITY = defineUnsynced("activity");
    /**
     * Not synced via network, not usable via packetevents.
     */
    @ApiStatus.Obsolete
    public static final AttributeType<?> BED_RULE = defineUnsynced("bed_rule");
    public static final AttributeType<Particle<?>> PARTICLE = define("particle", Particle.CODEC);
    public static final AttributeType<List<ParticleSettings>> AMBIENT_PARTICLES = define("ambient_particles", ParticleSettings.CODEC.applyList());
    public static final AttributeType<BackgroundMusic> BACKGROUND_MUSIC = define("background_music", BackgroundMusic.CODEC);
    public static final AttributeType<AmbientSounds> AMBIENT_SOUNDS = define("ambient_sounds", AmbientSounds.CODEC);

    static {
        REGISTRY.unloadMappings();
    }
}
