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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@NullMarked
public class PatchableComponentMap implements IComponentMap {

    public static final PatchableComponentMap EMPTY = new PatchableComponentMap(
            Collections.emptyMap(), Collections.emptyMap());

    private final Map<ComponentType<?>, ?> base;
    private final Map<ComponentType<?>, Optional<?>> patches;
    private final IRegistryHolder registries;

    public PatchableComponentMap(StaticComponentMap base) {
        this(base.delegate, new HashMap<>(), base.registries);
    }

    @Deprecated
    public PatchableComponentMap(Map<ComponentType<?>, ?> base) {
        this(base, new HashMap<>(), GlobalRegistryHolder.INSTANCE);
    }

    public PatchableComponentMap(
            StaticComponentMap base,
            Map<ComponentType<?>, Optional<?>> patches
    ) {
        this(base.delegate, patches, base.registries);
    }

    @Deprecated
    public PatchableComponentMap(
            Map<ComponentType<?>, ?> base,
            Map<ComponentType<?>, Optional<?>> patches
    ) {
        this(base, patches, GlobalRegistryHolder.INSTANCE);
    }

    public PatchableComponentMap(StaticComponentMap base, IRegistryHolder registries) {
        this(base.delegate, new HashMap<>(), registries);
    }

    public PatchableComponentMap(Map<ComponentType<?>, ?> base, IRegistryHolder registries) {
        this(base, new HashMap<>(), registries);
    }

    public PatchableComponentMap(
            StaticComponentMap base,
            Map<ComponentType<?>, Optional<?>> patches,
            IRegistryHolder registries
    ) {
        this(base.delegate, patches, registries);
    }

    public PatchableComponentMap(
            Map<ComponentType<?>, ?> base,
            Map<ComponentType<?>, Optional<?>> patches,
            IRegistryHolder registries
    ) {
        this.base = Collections.unmodifiableMap(new HashMap<>(base));
        this.patches = patches;
        this.registries = registries;
    }

    public static PatchableComponentMap read(PacketWrapper<?> wrapper, ItemType item, boolean lengthPrefixed) {
        return read(wrapper, item.getComponents(wrapper.getServerVersion().toClientVersion()), lengthPrefixed);
    }

    public static PatchableComponentMap read(PacketWrapper<?> wrapper, StaticComponentMap base, boolean lengthPrefixed) {
        // read component patch counts
        int presentCount = wrapper.readVarInt();
        int absentCount = wrapper.readVarInt();
        if (presentCount == 0 && absentCount == 0) {
            return new PatchableComponentMap(base);
        }

        PatchableComponentMap components = new PatchableComponentMap(base,
                new HashMap<>(presentCount + absentCount),
                wrapper.getRegistryHolder());
        for (int i = 0; i < presentCount; i++) {
            ComponentType<?> type = wrapper.readMappedEntity(ComponentTypes.getRegistry());
            // this is not 1:1 how vanilla decodes the length prefix, vanilla slices the buffer and
            // restricts reading to only the slice; packetevents just verifies the length isn't too large
            // and throws an error if we actually read more/less than expected
            int expectedReaderIndex;
            if (lengthPrefixed) {
                int size = wrapper.readVarInt();
                if (size > ByteBufHelper.readableBytes(wrapper.buffer)) {
                    throw new RuntimeException("Component size " + size + " for " + type.getName() + " out of bounds");
                }
                expectedReaderIndex = ByteBufHelper.readerIndex(wrapper.buffer) + size;
            } else {
                expectedReaderIndex = -1;
            }
            // read component value
            Object value = type.read(wrapper);
            // if this component is length-prefixed, verify the reader index changed to the expected value
            if (expectedReaderIndex != -1) {
                int readerIndex = ByteBufHelper.readerIndex(wrapper.buffer);
                if (readerIndex != expectedReaderIndex) {
                    throw new RuntimeException("Invalid component read for " + type.getName() + "; expected reader index "
                            + expectedReaderIndex + ", got reader index " + readerIndex);
                }
            }
            // set component value in component patch-map
            components.set((ComponentType<Object>) type, value);
        }
        for (int i = 0; i < absentCount; i++) {
            components.unset(wrapper.readMappedEntity(ComponentTypes.getRegistry()));
        }

        return components;
    }

    public static void write(PacketWrapper<?> wrapper, ItemStack stack, boolean lengthPrefixed) {
        if (!stack.hasComponentPatches()) {
            wrapper.writeShort(0);
        } else {
            write(wrapper, stack.getComponents(), lengthPrefixed);
        }
    }

    public static void write(PacketWrapper<?> wrapper, PatchableComponentMap components, boolean lengthPrefixed) {
        // write component patch counts
        Map<ComponentType<?>, Optional<?>> allPatches = components.getPatches();
        int presentCount = 0, absentCount = 0;
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (patch.getValue().isPresent()) {
                presentCount++;
            } else {
                absentCount++;
            }
        }
        wrapper.writeVarInt(presentCount);
        wrapper.writeVarInt(absentCount);

        // write present patches
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (patch.getValue().isPresent()) {
                wrapper.writeVarInt(patch.getKey().getId(wrapper.getServerVersion().toClientVersion()));
                if (lengthPrefixed) {
                    // easiest solution is to just temporarily replace the buffer
                    Object originalBuffer = wrapper.buffer;
                    wrapper.buffer = ByteBufHelper.allocateNewBuffer(originalBuffer);
                    ((ComponentType<Object>) patch.getKey()).write(wrapper, patch.getValue().get());
                    Object componentBuffer = wrapper.buffer;
                    wrapper.buffer = originalBuffer;
                    // after writing to new buffer, write length of newly written buffer to original buffer
                    wrapper.writeVarInt(ByteBufHelper.readableBytes(componentBuffer));
                    // copy component buffer bytes to original buffer
                    ByteBufHelper.writeBytes(wrapper.buffer, componentBuffer);
                    // release component buffer
                    ByteBufHelper.release(componentBuffer);
                } else {
                    ((ComponentType<Object>) patch.getKey()).write(wrapper, patch.getValue().get());
                }
            }
        }

        // write absent patches
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (!patch.getValue().isPresent()) {
                wrapper.writeVarInt(patch.getKey().getId(wrapper.getServerVersion().toClientVersion()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T get(ComponentType<T> type) {
        Optional<?> patched = this.patches.get(type);
        Object v = patched != null
                ? (T) patched.orElse(null)
                : (T) this.base.get(type);
        if (v instanceof ComponentValueRef) {
            v = ((ComponentValueRef<?>) v).resolve(this.registries);
        }
        return (T) v;
    }

    @Override
    public <T> void set(ComponentType<T> type, Optional<T> value) {
        Object baseVal = this.base.get(type);
        if (baseVal instanceof ComponentValueRef) {
            baseVal = ((ComponentValueRef<?>) baseVal).resolve(this.registries);
        }

        T newVal = value.orElse(null);
        if (Objects.equals(baseVal, newVal)) {
            this.patches.remove(type); // fallback to base
        } else {
            this.patches.put(type, value);
        }
    }

    @Override
    public boolean has(ComponentType<?> type) {
        Optional<?> patched = this.patches.get(type);
        return patched != null ? patched.isPresent() : this.base.containsKey(type);
    }

    @Override
    public PatchableComponentMap withRegistries(IRegistryHolder registries) {
        if (this.registries != registries) {
            return new PatchableComponentMap(this.base, this.patches, this.registries);
        }
        return this;
    }

    public PatchableComponentMap copy() {
        return new PatchableComponentMap(this.base, new HashMap<>(this.patches), this.registries);
    }

    @Deprecated
    public Map<ComponentType<?>, ?> getBase() {
        return this.base;
    }

    @Deprecated
    public Map<ComponentType<?>, Optional<?>> getPatches() {
        return this.patches;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PatchableComponentMap)) return false;
        PatchableComponentMap that = (PatchableComponentMap) obj;
        if (!this.base.equals(that.base)) return false;
        return this.patches.equals(that.patches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.base, this.patches);
    }

    @Override
    public String toString() {
        return "PatchableComponentMap{base=" + this.base + ", patches=" + this.patches + '}';
    }
}
