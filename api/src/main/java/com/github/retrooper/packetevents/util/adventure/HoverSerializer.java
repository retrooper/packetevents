/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.protocol.stats.Statistics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HoverSerializer {

    private static final TagStringIO SNBT_IO = TagStringIO.get();
    private static final Codec<CompoundBinaryTag, String, IOException, IOException> SNBT_CODEC = Codec.codec(SNBT_IO::asCompound, SNBT_IO::asString);

    static final String ITEM_TYPE = "id";
    static final String ITEM_COUNT = "Count";
    static final String ITEM_TAG = "tag";

    static final String ENTITY_NAME = "name";
    static final String ENTITY_TYPE = "type";
    static final String ENTITY_ID = "id";
    static final Pattern LEGACY_NAME_PATTERN = Pattern.compile("([A-Z][a-z]+)([A-Z][a-z]+)?");

    private final Gson gson;

    public HoverSerializer(Gson gson) {
        this.gson = gson;
    }

    public HoverEvent.ShowItem deserializeShowItem(final JsonElement input, boolean legacy) throws IOException {
        if (legacy) {
            Component component = this.gson.fromJson(input, Component.class);
            assertTextComponent(component);
            final CompoundBinaryTag contents = SNBT_CODEC.decode(((TextComponent) component).content());
            final CompoundBinaryTag tag = contents.getCompound(ITEM_TAG);
            return HoverEvent.ShowItem.of(
                    Key.key(contents.getString(ITEM_TYPE)),
                    contents.getByte(ITEM_COUNT, (byte) 1),
                    tag == CompoundBinaryTag.empty() ? null : BinaryTagHolder.encode(tag, SNBT_CODEC)
            );
        } else {
            return this.gson.fromJson(input, HoverEvent.ShowItem.class);
        }
    }

    public HoverEvent.ShowEntity deserializeShowEntity(final JsonElement input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec, boolean legacy) throws IOException {
        if (legacy) {
            Component component = this.gson.fromJson(input, Component.class);
            assertTextComponent(component);
            final CompoundBinaryTag contents = SNBT_CODEC.decode(((TextComponent) component).content());
            String type = contents.getString(ENTITY_TYPE);
            Matcher matcher = LEGACY_NAME_PATTERN.matcher(type);
            if (matcher.matches()) {
                StringJoiner joiner = new StringJoiner("_");
                joiner.add(matcher.group(1));
                if (matcher.group(2) != null) {
                    joiner.add(matcher.group(2));
                }
                type = joiner.toString().toLowerCase(Locale.ROOT);
            }
            return HoverEvent.ShowEntity.of(
                    Key.key(type),
                    UUID.fromString(contents.getString(ENTITY_ID)),
                    componentCodec.decode(contents.getString(ENTITY_NAME))
            );
        } else {
            return this.gson.fromJson(input, HoverEvent.ShowEntity.class);
        }
    }

    public Component deserializeShowAchievement(final JsonElement input) throws IOException {
        assertStringValue(input);
        return Statistics.getById(input.getAsString()).display();
    }

    private static void assertStringValue(final JsonElement element) {
        if (!(element.isJsonPrimitive() && ((JsonPrimitive) element).isString())) {
            throw new IllegalArgumentException("Legacy events must be single Component instances");
        }
    }

    private static void assertTextComponent(final Component component) {
        if (!(component instanceof TextComponent) || !component.children().isEmpty()) {
            throw new IllegalArgumentException("Legacy events must be single Component instances");
        }
    }

    public @NotNull Component serializeShowItem(final HoverEvent.@NotNull ShowItem input) throws IOException {
        final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(ITEM_TYPE, input.item().asString())
                .putByte(ITEM_COUNT, (byte) input.count());
        final BinaryTagHolder nbt = input.nbt();
        if (nbt != null) {
            builder.put(ITEM_TAG, nbt.get(SNBT_CODEC));
        }
        return Component.text(SNBT_CODEC.encode(builder.build()));
    }

    public @NotNull Component serializeShowEntity(final HoverEvent.@NotNull ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
        final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putString(ENTITY_ID, input.id().toString())
                .putString(ENTITY_TYPE, input.type().asString());
        final Component name = input.name();
        if (name != null) {
            builder.putString(ENTITY_NAME, componentCodec.encode(name));
        }
        return Component.text(SNBT_CODEC.encode(builder.build()));
    }

}
