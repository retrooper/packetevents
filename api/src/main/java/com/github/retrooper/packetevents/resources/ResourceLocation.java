/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.resources;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class ResourceLocation implements Keyed {

    public static final NbtCodec<ResourceLocation> CODEC = NbtCodecs.STRING
            .apply(ResourceLocation::new, ResourceLocation::toString);

    public static final String VANILLA_NAMESPACE = "minecraft";

    protected final String namespace;
    protected final String key;

    public ResourceLocation(Key key) {
        this(key.namespace(), key.value());
    }

    public ResourceLocation(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
    }

    public ResourceLocation(String location) {
        String[] array = new String[]{VANILLA_NAMESPACE, location};
        int index = location.indexOf(":");
        if (index != -1) {
            array[1] = location.substring(index + 1);
            if (index >= 1) {
                array[0] = location.substring(0, index);
            }
        }
        this.namespace = array[0];
        this.key = array[1];
    }

    public static ResourceLocation read(PacketWrapper<?> wrapper) {
        return wrapper.readIdentifier();
    }

    public static void write(PacketWrapper<?> wrapper, ResourceLocation resourceLocation) {
        wrapper.writeIdentifier(resourceLocation);
    }

    public static ResourceLocation decode(NBT nbt, PacketWrapper<?> wrapper) {
        return new ResourceLocation(((NBTString) nbt).getValue());
    }

    public static NBT encode(PacketWrapper<?> wrapper, ResourceLocation resourceLocation) {
        return new NBTString(resourceLocation.toString());
    }

    public static String getNamespace(String location) {
        int namespaceIdx = location.indexOf(':');
        if (namespaceIdx > 0) {
            return location.substring(0, namespaceIdx);
        }
        return VANILLA_NAMESPACE;
    }

    public static String getPath(String location) {
        int namespaceIdx = location.indexOf(':');
        if (namespaceIdx != -1) {
            return location.substring(namespaceIdx + 1);
        }
        return location;
    }

    @Contract("null -> null; !null -> !null")
    public static @Nullable String normString(@Nullable String location) {
        if (location == null) {
            return null;
        }
        int index = location.indexOf(':');
        if (index > 0) {
            return location; // namespace already set
        } else if (index == -1) {
            // prepend namespace and delimiter
            return VANILLA_NAMESPACE + ":" + location;
        } else { // index == 0
            // treat prepending delimiter as no namespace
            return VANILLA_NAMESPACE + location;
        }
    }

    @Override
    public Key key() {
        return Key.key(this.namespace, this.key);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.namespace, this.key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourceLocation) {
            ResourceLocation other = (ResourceLocation) obj;
            return other.namespace.equals(namespace) && other.key.equals(key);
        }
        return false;
    }

    @Override
    public String toString() {
        return namespace + ":" + key;
    }

    public static ResourceLocation minecraft(String key) {
        return new ResourceLocation(VANILLA_NAMESPACE, key);
    }
}
