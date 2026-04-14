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

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import com.github.retrooper.packetevents.util.Either;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @versions 1.21.11++
 */
@NullMarked
public class EnvironmentAttributeMap {

    public static final NbtCodec<EnvironmentAttributeMap> CODEC = new NbtMapCodec<EnvironmentAttributeMap>() {
        @Override
        public EnvironmentAttributeMap decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            VersionedRegistry<EnvironmentAttribute<?>> registry = EnvironmentAttributes.getRegistry();
            ClientVersion version = wrapper.getServerVersion().toClientVersion();
            Map<EnvironmentAttribute<?>, Entry<?, ?>> entries = new HashMap<>();
            for (String tag : compound.getTagNames()) {
                EnvironmentAttribute<?> attribute = registry.getByNameOrThrow(version, tag);
                if (attribute.isSynced()) {
                    entries.put(attribute, compound.getOrThrow(tag, Entry.codec(attribute), wrapper));
                }
            }
            return new EnvironmentAttributeMap(entries);
        }

        @Override
        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, EnvironmentAttributeMap value) throws NbtCodecException {
            for (Map.Entry<EnvironmentAttribute<?>, Entry<?, ?>> entry : value.entries.entrySet()) {
                if (entry.getKey().isSynced()) {
                    this.encode0(compound, entry.getKey(), entry.getValue(), wrapper);
                }
            }
        }

        private <T> void encode0(NBTCompound compound, EnvironmentAttribute<?> attribute, Entry<T, ?> entry, PacketWrapper<?> wrapper) {
            @SuppressWarnings("unchecked")
            EnvironmentAttribute<T> castAttribute = (EnvironmentAttribute<T>) attribute;
            compound.set(attribute.getName().toString(), entry, Entry.codec(castAttribute), wrapper);
        }
    }.codec();

    public static final EnvironmentAttributeMap EMPTY = new EnvironmentAttributeMap(Collections.emptyMap());

    private final Map<EnvironmentAttribute<?>, Entry<?, ?>> entries;

    private EnvironmentAttributeMap(Map<EnvironmentAttribute<?>, Entry<?, ?>> entries) {
        this.entries = entries;
    }

    public static EnvironmentAttributeMap create() {
        return new EnvironmentAttributeMap(new HashMap<>());
    }

    public EnvironmentAttributeMap copyImmutable() {
        Map<EnvironmentAttribute<?>, Entry<?, ?>> entries = new HashMap<>(this.entries);
        return new EnvironmentAttributeMap(Collections.unmodifiableMap(entries));
    }

    public EnvironmentAttributeMap copyMutable() {
        return new EnvironmentAttributeMap(new HashMap<>(this.entries));
    }

    public <T> EnvironmentAttributeMap set(EnvironmentAttribute<T> attribute, T value) {
        this.set(attribute, value, AttributeModifier.override());
        return this;
    }

    public <T, A> EnvironmentAttributeMap set(EnvironmentAttribute<T> attribute, A value, AttributeModifier<T, A> modifier) {
        this.entries.put(attribute, new Entry<>(value, modifier));
        return this;
    }

    public void setAll(EnvironmentAttributeMap map) {
        this.entries.putAll(map.entries);
    }

    public <T> T getOrDefault(EnvironmentAttribute<T> attribute) {
        return this.apply(attribute, attribute.getDefaultValue());
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable Entry<T, ?> get(EnvironmentAttribute<T> attribute) {
        return (Entry<T, ?>) this.entries.get(attribute);
    }

    public <T> T apply(EnvironmentAttribute<T> attribute, T base) {
        Entry<T, ?> entry = this.get(attribute);
        return entry != null ? entry.getValue(base) : base;
    }

    public boolean contains(EnvironmentAttribute<?> attribute) {
        return this.entries.containsKey(attribute);
    }

    public Set<EnvironmentAttribute<?>> keySet() {
        return this.entries.keySet();
    }

    public int size() {
        return this.entries.size();
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EnvironmentAttributeMap)) return false;
        return this.entries.equals(((EnvironmentAttributeMap) obj).entries);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.entries);
    }

    public static final class Entry<T, A> {

        private final A argument;
        private final AttributeModifier<T, A> modifier;

        public Entry(A argument, AttributeModifier<T, A> modifier) {
            this.argument = argument;
            this.modifier = modifier;
        }

        public static <T> Entry<T, T> createOverride(T value) {
            return new Entry<>(value, AttributeModifier.override());
        }

        public static <T> NbtCodec<Entry<T, ?>> codec(EnvironmentAttribute<T> attribute) {
            NbtCodec<T> valueCodec = attribute.getType().getValueCodec();
            NbtCodec<AttributeModifier<T, ?>> modifierCodec = attribute.getType().getModifierCodec();
            return NbtCodecs.either(
                    valueCodec,
                    new NbtMapCodec<Entry<T, ?>>() {
                        @Override
                        public Entry<T, ?> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                            AttributeModifier<T, ?> modifier = compound.getOrThrow("modifier", modifierCodec, wrapper);
                            Object arg = compound.getOrThrow("argument", modifier.argumentCodec(attribute), wrapper);
                            @SuppressWarnings("unchecked")
                            Entry<T, Object> entry = new Entry<>(arg, (AttributeModifier<T, ? super Object>) modifier);
                            return entry;
                        }

                        @Override
                        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Entry<T, ?> value) throws NbtCodecException {
                            this.encode0(compound, wrapper, value);
                        }

                        private <A> void encode0(NBTCompound compound, PacketWrapper<?> wrapper, Entry<T, A> value) throws NbtCodecException {
                            compound.set("modifier", value.modifier, modifierCodec, wrapper);
                            compound.set("argument", value.argument, value.modifier.argumentCodec(attribute), wrapper);
                        }
                    }.codec()
            ).apply(
                    e -> e.map(Entry::createOverride, Function.identity()),
                    e -> {
                        if (e.isOverride()) {
                            @SuppressWarnings("unchecked")
                            T arg = (T) e.argument;
                            return Either.createLeft(arg);
                        }
                        return Either.createRight(e);
                    }
            );
        }

        public boolean isOverride() {
            return this.modifier == AttributeModifier.override();
        }

        public T getValue(T base) {
            return this.modifier.apply(base, this.argument);
        }

        public A getArgument() {
            return this.argument;
        }

        public AttributeModifier<T, A> getModifier() {
            return this.modifier;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || this.getClass() != obj.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) obj;
            if (this.modifier != entry.modifier) return false;
            return this.argument.equals(entry.argument);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.argument, this.modifier);
        }
    }
}
