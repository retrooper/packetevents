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

package com.github.retrooper.packetevents.protocol.mapper;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Either a key to a specific tag or a list of possible entities.
 */
public class MappedEntitySet<T> {

    private final @Nullable ResourceLocation tagKey;
    private final @Nullable List<T> entities;

    public MappedEntitySet(ResourceLocation tagKey) {
        this(tagKey, null);
    }

    public MappedEntitySet(List<T> entities) {
        this(null, entities);
    }

    public MappedEntitySet(
            @Nullable ResourceLocation tagKey,
            @Nullable List<T> entities
    ) {
        if (tagKey == null && entities == null) {
            throw new IllegalArgumentException("Illegal generic holder set: either tag key or holder ids have to be set");
        }
        this.tagKey = tagKey;
        this.entities = entities;
    }

    public static <Z extends MappedEntity> MappedEntitySet<Z> createEmpty() {
        return new MappedEntitySet<>(new ArrayList<>(0));
    }

    public static <Z extends MappedEntity> MappedEntitySet<Z> read(
            PacketWrapper<?> wrapper, BiFunction<ClientVersion, Integer, Z> getter) {
        int count = wrapper.readVarInt() - 1;
        if (count == -1) {
            return new MappedEntitySet<>(wrapper.readIdentifier(), null);
        }
        List<Z> entities = new ArrayList<>(Math.min(count, 65536));
        for (int i = 0; i < count; i++) {
            entities.add(wrapper.readMappedEntity(getter));
        }
        return new MappedEntitySet<>(null, entities);
    }

    public static <Z extends MappedEntity> void write(PacketWrapper<?> wrapper, MappedEntitySet<Z> set) {
        if (set.tagKey != null) {
            wrapper.writeByte(0);
            wrapper.writeIdentifier(set.tagKey);
            return;
        }

        assert set.entities != null; // can't be null, verified in ctor
        wrapper.writeVarInt(set.entities.size() + 1);
        for (Z entity : set.entities) {
            wrapper.writeMappedEntity(entity);
        }
    }

    public static <Z extends MappedEntity> MappedEntitySet<Z> decode(
            NBT nbt, ClientVersion version, IRegistry<Z> registry) {
        if (nbt instanceof NBTString) {
            String tagName = ((NBTString) nbt).getValue();
            ResourceLocation tagKey = new ResourceLocation(tagName);
            return new MappedEntitySet<>(tagKey);
        }
        NBTList<?> listTag = (NBTList<?>) nbt;
        List<Z> list = new ArrayList<>(listTag.size());
        for (NBT tag : listTag.getTags()) {
            ResourceLocation tagKey = new ResourceLocation(((NBTString) tag).getValue());
            list.add(registry.getByName(tagKey));
        }
        return new MappedEntitySet<>(list);
    }

    public static <Z extends MappedEntity> NBT encode(MappedEntitySet<Z> set, ClientVersion version) {
        if (set.tagKey != null) {
            return new NBTString(set.tagKey.toString());
        }

        assert set.entities != null; // can't be null, verified in ctor
        NBTList<NBTString> listTag = NBTList.createStringList();
        for (Z entity : set.entities) {
            listTag.addTag(new NBTString(entity.getName().toString()));
        }
        return listTag;
    }

    public boolean isEmpty() {
        return this.entities != null && this.entities.isEmpty();
    }

    public @Nullable ResourceLocation getTagKey() {
        return this.tagKey;
    }

    public @Nullable List<T> getEntities() {
        return this.entities;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MappedEntitySet)) return false;
        MappedEntitySet<?> that = (MappedEntitySet<?>) obj;
        if (!Objects.equals(this.tagKey, that.tagKey)) return false;
        return Objects.equals(this.entities, that.entities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tagKey, this.entities);
    }

    @Override
    public String toString() {
        return "MappedEntitySet{tagKey=" + this.tagKey + ", entities=" + this.entities + '}';
    }
}
