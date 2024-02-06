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

package com.github.retrooper.packetevents.util.adventure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

/**
 * Implementing adventures {@link GsonComponentSerializer} to fix jsons for legacy versions.
 */
public class GsonComponentSerializerExtended implements GsonComponentSerializer {

    private final Gson serializer;
    private final UnaryOperator<GsonBuilder> populator;

    public GsonComponentSerializerExtended(final boolean downsampleColor, final boolean emitLegacyHover) {
        if (AdventureReflectionUtil.IS_LEGACY_ADVENTURE) {
            this.populator = builder -> {
                builder.registerTypeHierarchyAdapter(Key.class, AdventureReflectionUtil.KEY_SERIALIZER_INSTANCE);
                builder.registerTypeHierarchyAdapter(Component.class, AdventureReflectionUtil.COMPONENT_SERIALIZER_CREATE.apply(null));
                builder.registerTypeHierarchyAdapter(Style.class, new Legacy_StyleSerializerExtended(emitLegacyHover));
                builder.registerTypeAdapter(ClickEvent.Action.class, AdventureReflectionUtil.CLICK_EVENT_ACTION_SERIALIZER_INSTANCE);
                builder.registerTypeAdapter(HoverEvent.Action.class, AdventureReflectionUtil.HOVER_EVENT_ACTION_SERIALIZER_INSTANCE);
                builder.registerTypeAdapter(HoverEvent.ShowItem.class, AdventureReflectionUtil.SHOW_ITEM_SERIALIZER_CREATE.apply(null));
                builder.registerTypeAdapter(HoverEvent.ShowEntity.class, AdventureReflectionUtil.SHOW_ENTITY_SERIALIZER_CREATE.apply(null));
                builder.registerTypeAdapter(TextColorWrapper.class, TextColorWrapper.Serializer.INSTANCE);
                builder.registerTypeHierarchyAdapter(TextColor.class, downsampleColor ? AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE : AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_INSTANCE);
                builder.registerTypeAdapter(TextDecoration.class, AdventureReflectionUtil.TEXT_DECORATION_SERIALIZER_INSTANCE);
                builder.registerTypeHierarchyAdapter(BlockNBTComponent.Pos.class, AdventureReflectionUtil.BLOCK_NBT_POS_SERIALIZER_INSTANCE);
                return builder;
            };
        } else {
            this.populator = builder -> {
                builder.registerTypeAdapterFactory(new SerializerFactory(downsampleColor, emitLegacyHover));
                return builder;
            };
        }
        this.serializer = this.populator.apply(new GsonBuilder().disableHtmlEscaping()).create();
    }

    @Override
    public @NotNull Gson serializer() {
        return this.serializer;
    }

    @Override
    public @NotNull UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }

    @Override
    public @NotNull Component deserialize(final @NotNull String string) {
        final Component component = this.serializer().fromJson(string, Component.class);
        if (component == null) throw new JsonParseException("Don't know how to turn " + string + " into a Component");
        return component;
    }

    @Override
    public @Nullable Component deserializeOr(final @Nullable String input, final @Nullable Component fallback) {
        if (input == null) return fallback;
        final Component component = this.serializer().fromJson(input, Component.class);
        if (component == null) return fallback;
        return component;
    }

    @Override
    public @NotNull String serialize(final @NotNull Component component) {
        return this.serializer().toJson(component);
    }

    @Override
    public @NotNull Component deserializeFromTree(final @NotNull JsonElement input) {
        final Component component = this.serializer().fromJson(input, Component.class);
        if (component == null) throw new JsonParseException("Don't know how to turn " + input + " into a Component");
        return component;
    }

    @Override
    public @NotNull JsonElement serializeToTree(final @NotNull Component component) {
        return this.serializer().toJsonTree(component);
    }

    @Override
    public @NotNull Builder toBuilder() {
        throw new UnsupportedOperationException("Builder pattern is not supported."); // We don't need to support this
    }

}
