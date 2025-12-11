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

package com.github.retrooper.packetevents.resources;

import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TagKey {

    public static final NbtCodec<TagKey> CODEC = NbtCodecs.STRING.apply(
            name -> {
                TagKey tagKey = TagKey.tryParse(name);
                if (tagKey == null) {
                    throw new NbtCodecException("Not a tag: " + name);
                }
                return tagKey;
            },
            TagKey::toString
    );

    private final ResourceLocation id;

    public TagKey(ResourceLocation id) {
        this.id = id;
    }

    public static TagKey parse(String name) {
        TagKey tagKey = tryParse(name);
        if (tagKey == null) {
            throw new IllegalArgumentException("Not a tag: " + name);
        }
        return tagKey;
    }

    public static @Nullable TagKey tryParse(String name) {
        if (name.isEmpty() || name.charAt(0) != '#') {
            return null;
        }
        return new TagKey(new ResourceLocation(name.substring(1)));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return '#' + this.id.toString();
    }
}
