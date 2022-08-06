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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SerializerFactory implements TypeAdapterFactory {

    static Method COMPONENT_SERIALIZER_CREATE_METHOD;
    static TypeAdapter<Key> KEY_SERIALIZER_INSTANCE;
    static TypeAdapter<ClickEvent.Action> CLICK_EVENT_ACTION_SERIALIZER_INSTANCE;
    static TypeAdapter<HoverEvent.Action<?>> HOVER_EVENT_ACTION_SERIALIZER_INSTANCE;
    static Method SHOW_ITEM_SERIALIZER_CREATE_METHOD;
    static Method SHOW_ENTITY_SERIALIZER_CREATE_METHOD;
    static TypeAdapter<TextColor> TEXT_COLOR_SERIALIZER_INSTANCE;
    static TypeAdapter<TextColor> TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE;
    static TypeAdapter<TextDecoration> TEXT_DECORATION_SERIALIZER_INSTANCE;
    static TypeAdapter<BlockNBTComponent.Pos> BLOCK_NBT_POS_SERIALIZER_INSTANCE;

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
            return (TypeAdapter<T>) this.invokeSafe(COMPONENT_SERIALIZER_CREATE_METHOD, gson);
        } else if (Key.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) KEY_SERIALIZER_INSTANCE;
        } else if (Style.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) StyleSerializerExtended.create(this.emitLegacyHover, gson);
        } else if (ClickEvent.Action.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) CLICK_EVENT_ACTION_SERIALIZER_INSTANCE;
        } else if (HoverEvent.Action.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) HOVER_EVENT_ACTION_SERIALIZER_INSTANCE;
        } else if (HoverEvent.ShowItem.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) this.invokeSafe(SHOW_ITEM_SERIALIZER_CREATE_METHOD, gson);
        } else if (HoverEvent.ShowEntity.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) this.invokeSafe(SHOW_ENTITY_SERIALIZER_CREATE_METHOD, gson);
        } else if (TextColorWrapper.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TextColorWrapper.Serializer.INSTANCE;
        } else if (TextColor.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) (this.downsampleColors ? TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE : TEXT_COLOR_SERIALIZER_INSTANCE);
        } else if (TextDecoration.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TEXT_DECORATION_SERIALIZER_INSTANCE;
        } else if (BlockNBTComponent.Pos.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) BLOCK_NBT_POS_SERIALIZER_INSTANCE;
        } else {
            return null;
        }
    }

    private Object invokeSafe(Method method, Object... params) {
        try {
            return method.invoke(null, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        try {
            Class<?> COMPONENT_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.ComponentSerializerImpl");
            COMPONENT_SERIALIZER_CREATE_METHOD = COMPONENT_SERIALIZER.getDeclaredMethod("create", Gson.class);
            COMPONENT_SERIALIZER_CREATE_METHOD.setAccessible(true);

            Class<?> KEY_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.KeySerializer");
            Field KEY_SERIALIZER_INSTANCE_FIELD = KEY_SERIALIZER.getDeclaredField("INSTANCE");
            KEY_SERIALIZER_INSTANCE_FIELD.setAccessible(true);
            KEY_SERIALIZER_INSTANCE = (TypeAdapter<Key>) KEY_SERIALIZER_INSTANCE_FIELD.get(null);

            Class<?> CLICK_EVENT_ACTION_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.ClickEventActionSerializer");
            Field CLICK_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD = CLICK_EVENT_ACTION_SERIALIZER.getDeclaredField("INSTANCE");
            CLICK_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD.setAccessible(true);
            CLICK_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter<ClickEvent.Action>) CLICK_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD.get(null);

            Class<?> HOVER_EVENT_ACTION_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.HoverEventActionSerializer");
            Field HOVER_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD = HOVER_EVENT_ACTION_SERIALIZER.getDeclaredField("INSTANCE");
            HOVER_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD.setAccessible(true);
            HOVER_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter<HoverEvent.Action<?>>) HOVER_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD.get(null);

            Class<?> SHOW_ITEM_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.ShowItemSerializer");
            SHOW_ITEM_SERIALIZER_CREATE_METHOD = SHOW_ITEM_SERIALIZER.getDeclaredMethod("create", Gson.class);
            SHOW_ITEM_SERIALIZER_CREATE_METHOD.setAccessible(true);

            Class<?> SHOW_ENTITY_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.ShowEntitySerializer");
            SHOW_ENTITY_SERIALIZER_CREATE_METHOD = SHOW_ENTITY_SERIALIZER.getDeclaredMethod("create", Gson.class);
            SHOW_ENTITY_SERIALIZER_CREATE_METHOD.setAccessible(true);

            Class<?> TEXT_COLOR_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.TextColorSerializer");
            Field TEXT_COLOR_SERIALIZER_INSTANCE_FIELD = TEXT_COLOR_SERIALIZER.getDeclaredField("INSTANCE");
            TEXT_COLOR_SERIALIZER_INSTANCE_FIELD.setAccessible(true);
            TEXT_COLOR_SERIALIZER_INSTANCE = (TypeAdapter<TextColor>) TEXT_COLOR_SERIALIZER_INSTANCE_FIELD.get(null);

            Class<?> TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR = Class.forName("net.kyori.adventure.text.serializer.gson.TextColorSerializer");
            Field TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD = TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR.getDeclaredField("DOWNSAMPLE_COLOR");
            TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD.setAccessible(true);
            TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE = (TypeAdapter<TextColor>) TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD.get(null);

            Class<?> TEXT_DECORATION_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.TextDecorationSerializer");
            Field TEXT_DECORATION_SERIALIZER_INSTANCE_FIELD = TEXT_DECORATION_SERIALIZER.getDeclaredField("INSTANCE");
            TEXT_DECORATION_SERIALIZER_INSTANCE_FIELD.setAccessible(true);
            TEXT_DECORATION_SERIALIZER_INSTANCE = (TypeAdapter<TextDecoration>) TEXT_DECORATION_SERIALIZER_INSTANCE_FIELD.get(null);

            Class<?> BLOCK_NBT_COMPONENT_POS_SERIALIZER = Class.forName("net.kyori.adventure.text.serializer.gson.BlockNBTComponentPosSerializer");
            Field BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD = BLOCK_NBT_COMPONENT_POS_SERIALIZER.getDeclaredField("INSTANCE");
            BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD.setAccessible(true);
            BLOCK_NBT_POS_SERIALIZER_INSTANCE = (TypeAdapter<BlockNBTComponent.Pos>) BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD.get(null);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
