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
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SerializerFactory implements TypeAdapterFactory {

    private final boolean downsampleColors;
    private final boolean emitLegacyHover;

    SerializerFactory(final boolean downsampleColors, final boolean emitLegacyHover) {
        this.downsampleColors = downsampleColors;
        this.emitLegacyHover = emitLegacyHover;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final Class<? super T> rawType = type.getRawType();
        if (Component.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.COMPONENT_SERIALIZER_CREATE.apply(gson);
        } else if (Key.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.KEY_SERIALIZER_INSTANCE;
        } else if (Style.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) StyleSerializerExtended.create(this.emitLegacyHover, gson);
        } else if (ClickEvent.Action.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.CLICK_EVENT_ACTION_SERIALIZER_INSTANCE;
        } else if (HoverEvent.Action.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.HOVER_EVENT_ACTION_SERIALIZER_INSTANCE;
        } else if (HoverEvent.ShowItem.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.SHOW_ITEM_SERIALIZER_CREATE.apply(gson);
        } else if (HoverEvent.ShowEntity.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.SHOW_ENTITY_SERIALIZER_CREATE.apply(gson);
        } else if (TextColorWrapper.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TextColorWrapper.Serializer.INSTANCE;
        } else if (TextColor.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) (this.downsampleColors ? AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE : AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_INSTANCE);
        } else if (TextDecoration.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.TEXT_DECORATION_SERIALIZER_INSTANCE;
        } else if (BlockNBTComponent.Pos.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) AdventureReflectionUtil.BLOCK_NBT_POS_SERIALIZER_INSTANCE;
        } else {
            return null;
        }
    }

}
