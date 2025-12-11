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
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ResourceLocation implements Keyed {

    public static final NbtCodec<ResourceLocation> CODEC = NbtCodecs.STRING
            .apply(ResourceLocation::new, ResourceLocation::toString);

    public static final String VANILLA_NAMESPACE = Key.MINECRAFT_NAMESPACE;

    private final Key key;

    public ResourceLocation(Key key) {
        this.key = key;
    }

    public ResourceLocation(@KeyPattern.Namespace String namespace, @KeyPattern.Value String key) {
        this.key = Key.key(namespace, key);
    }

    public ResourceLocation(String location) {
        this.key = Key.key(location);
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
        return key;
    }

    public String getNamespace() {
        return key.namespace();
    }

    public String getKey() {
        return key.value();
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourceLocation) {
            ResourceLocation other = (ResourceLocation) obj;
            return other.key.equals(this.key);
        }
        return false;
    }

    @Override
    public String toString() {
        return key.asString();
    }

    public static ResourceLocation minecraft(@KeyPattern.Value String key) {
        return new ResourceLocation(VANILLA_NAMESPACE, key);
    }
}
