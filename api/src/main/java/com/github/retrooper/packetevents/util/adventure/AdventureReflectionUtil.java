package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.util.Index;
import net.kyori.option.OptionState;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Function;

public class AdventureReflectionUtil {

    static boolean IS_LEGACY_ADVENTURE;
    static boolean IS_4_15_0_OR_NEWER = false;
    static TypeAdapter<Key> KEY_SERIALIZER_INSTANCE;
    static Function<Gson, Object> COMPONENT_SERIALIZER_CREATE;
    static TypeAdapter<ClickEvent.Action> CLICK_EVENT_ACTION_SERIALIZER_INSTANCE;
    static TypeAdapter<HoverEvent.Action<?>> HOVER_EVENT_ACTION_SERIALIZER_INSTANCE;
    static Function<Gson, Object> SHOW_ITEM_SERIALIZER_CREATE;
    static Function<Gson, Object> SHOW_ENTITY_SERIALIZER_CREATE;
    static TypeAdapter<TextColor> TEXT_COLOR_SERIALIZER_INSTANCE;
    static TypeAdapter<TextColor> TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE;
    static TypeAdapter<TextDecoration> TEXT_DECORATION_SERIALIZER_INSTANCE;
    static TypeAdapter<BlockNBTComponent.Pos> BLOCK_NBT_POS_SERIALIZER_INSTANCE;
    static TypeAdapter<UUID> UUID_SERIALIZER_INSTANCE;
    static Function<Gson, Object> TRANSLATION_ARGUMENT_SERIALIZER_CREATE;

    static {
        IS_LEGACY_ADVENTURE = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.SerializerFactory") == null;
        Class<?> KEY_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.KeySerializer");
        Field KEY_SERIALIZER_INSTANCE_FIELD = Reflection.getField(KEY_SERIALIZER, "INSTANCE");
        KEY_SERIALIZER_INSTANCE = (TypeAdapter<Key>) getSafe(KEY_SERIALIZER_INSTANCE_FIELD);

        Class<?> COMPONENT_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.ComponentSerializerImpl");
        Class<?> SHOW_ITEM_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.ShowItemSerializer");
        Class<?> SHOW_ENTITY_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.ShowEntitySerializer");

        if (IS_LEGACY_ADVENTURE) {
            Constructor<?> COMPONENT_SERIALIZER_CONSTRUCTOR = Reflection.getConstructor(COMPONENT_SERIALIZER, 0);
            COMPONENT_SERIALIZER_CREATE = gson -> invokeSafe(COMPONENT_SERIALIZER_CONSTRUCTOR);

            Class<?> INDEXED_SERIALIZER_CLASS = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.IndexedSerializer");
            Method INDEXED_SERIALIZER_CREATE_METHOD = Reflection.getMethod(INDEXED_SERIALIZER_CLASS, "of", String.class, Index.class);

            CLICK_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter<ClickEvent.Action>) invokeSafe(INDEXED_SERIALIZER_CREATE_METHOD, "click action", ClickEvent.Action.NAMES);
            HOVER_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter<HoverEvent.Action<?>>) invokeSafe(INDEXED_SERIALIZER_CREATE_METHOD, "hover action", HoverEvent.Action.NAMES);
            TEXT_DECORATION_SERIALIZER_INSTANCE = (TypeAdapter<TextDecoration>) invokeSafe(INDEXED_SERIALIZER_CREATE_METHOD, "text decoration", TextDecoration.NAMES);

            Constructor<?> SHOW_ITEM_SERIALIZER_CONSTRUCTOR = Reflection.getConstructor(SHOW_ITEM_SERIALIZER, 0);
            SHOW_ITEM_SERIALIZER_CREATE = gson -> invokeSafe(SHOW_ITEM_SERIALIZER_CONSTRUCTOR);

            Constructor<?> SHOW_ENTITY_SERIALIZER_CONSTRUCTOR = Reflection.getConstructor(SHOW_ENTITY_SERIALIZER, 0);
            SHOW_ENTITY_SERIALIZER_CREATE = gson -> invokeSafe(SHOW_ENTITY_SERIALIZER_CONSTRUCTOR);
        } else {
            IS_4_15_0_OR_NEWER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.TranslationArgumentSerializer") != null;

            final Object optionState;
            if (IS_4_15_0_OR_NEWER) {
                // adventure >= v4.15; add option state for serializer construction
                optionState = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16)
                        ? JSONOptions.byDataVersion() : JSONOptions.byDataVersion().at(2525);
                Method COMPONENT_SERIALIZER_CREATE_METHOD = Reflection.getMethod(COMPONENT_SERIALIZER, "create", OptionState.class, Gson.class);
                COMPONENT_SERIALIZER_CREATE = gson -> invokeSafe(COMPONENT_SERIALIZER_CREATE_METHOD, optionState, gson);
            } else {
                // adventure < 4.15; no option states available
                optionState = null;
                Method COMPONENT_SERIALIZER_CREATE_METHOD = Reflection.getMethod(COMPONENT_SERIALIZER, "create", Gson.class);
                COMPONENT_SERIALIZER_CREATE = gson -> invokeSafe(COMPONENT_SERIALIZER_CREATE_METHOD, gson);
            }

            Class<?> CLICK_EVENT_ACTION_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.ClickEventActionSerializer");
            Field CLICK_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD = Reflection.getField(CLICK_EVENT_ACTION_SERIALIZER, "INSTANCE");
            CLICK_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter<ClickEvent.Action>) getSafe(CLICK_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD);

            Class<?> HOVER_EVENT_ACTION_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.HoverEventActionSerializer");
            Field HOVER_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD = Reflection.getField(HOVER_EVENT_ACTION_SERIALIZER, "INSTANCE");
            HOVER_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter<HoverEvent.Action<?>>) getSafe(HOVER_EVENT_ACTION_SERIALIZER_INSTANCE_FIELD);

            Class<?> TEXT_DECORATION_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.TextDecorationSerializer");
            Field TEXT_DECORATION_SERIALIZER_INSTANCE_FIELD = Reflection.getField(TEXT_DECORATION_SERIALIZER, "INSTANCE");
            TEXT_DECORATION_SERIALIZER_INSTANCE = (TypeAdapter<TextDecoration>) getSafe(TEXT_DECORATION_SERIALIZER_INSTANCE_FIELD);

            Method SHOW_ITEM_SERIALIZER_CREATE_METHOD = Reflection.getMethod(SHOW_ITEM_SERIALIZER, "create", Gson.class);
            SHOW_ITEM_SERIALIZER_CREATE = gson -> invokeSafe(SHOW_ITEM_SERIALIZER_CREATE_METHOD, gson);

            Method SHOW_ENTITY_SERIALIZER_CREATE_METHOD = Reflection.getMethod(SHOW_ENTITY_SERIALIZER, "create", Gson.class);
            SHOW_ENTITY_SERIALIZER_CREATE = gson -> invokeSafe(SHOW_ENTITY_SERIALIZER_CREATE_METHOD, gson);

            if (IS_4_15_0_OR_NEWER) {
                Class<?> UUID_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.UUIDSerializer");
                Method UUID_SERIALIZER_CREATE = Reflection.getMethod(UUID_SERIALIZER, "uuidSerializer", OptionState.class);
                UUID_SERIALIZER_INSTANCE = (TypeAdapter<UUID>) invokeSafe(UUID_SERIALIZER_CREATE, optionState);

                Class<?> TRANSLATION_ARGUMENT_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.TranslationArgumentSerializer");
                Method TRANSLATION_ARGUMENT_SERIALIZER_CREATE_METHOD = Reflection.getMethod(TRANSLATION_ARGUMENT_SERIALIZER, "create", Gson.class);
                TRANSLATION_ARGUMENT_SERIALIZER_CREATE = gson -> invokeSafe(TRANSLATION_ARGUMENT_SERIALIZER_CREATE_METHOD, gson);
            }
        }

        Class<?> TEXT_COLOR_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.TextColorSerializer");
        Field TEXT_COLOR_SERIALIZER_INSTANCE_FIELD = Reflection.getField(TEXT_COLOR_SERIALIZER, "INSTANCE");
        TEXT_COLOR_SERIALIZER_INSTANCE = (TypeAdapter<TextColor>) getSafe(TEXT_COLOR_SERIALIZER_INSTANCE_FIELD);

        Field TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD = Reflection.getField(TEXT_COLOR_SERIALIZER, "DOWNSAMPLE_COLOR");
        TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE = (TypeAdapter<TextColor>) getSafe(TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD);

        Class<?> BLOCK_NBT_COMPONENT_POS_SERIALIZER = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.serializer.gson.BlockNBTComponentPosSerializer");
        Field BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD = Reflection.getField(BLOCK_NBT_COMPONENT_POS_SERIALIZER, "INSTANCE");
        BLOCK_NBT_POS_SERIALIZER_INSTANCE = (TypeAdapter<BlockNBTComponent.Pos>) getSafe(BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD);
    }

    static Object getSafe(Field field) {
        return getSafe(field, null);
    }

    static Object getSafe(Field field, Object instance) {
        if (field == null) return null;

        try {
            return field.get(instance);
        } catch (Exception e) {
            return null;
        }
    }

    static Object invokeSafe(Method method, Object... params) {
        return invokeSafe(null, method, params);
    }

    static Object invokeSafe(Object instance, Method method, Object... params) {
        if (method == null) return null;
        try {
            return method.invoke(instance, params);
        } catch (Exception e) {
            return null;
        }
    }

    static Object invokeSafe(Constructor<?> constructor, Object... params) {
        if (constructor == null) return null;
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            return null;
        }
    }

}
