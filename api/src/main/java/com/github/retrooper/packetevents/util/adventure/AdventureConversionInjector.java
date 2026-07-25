/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import net.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.serializer.gson.GsonDataComponentValue.gsonDataComponentValue;

/**
 * Adventure has their own static registry for type conversion between data component values;
 * as we can't properly register something in this registry, we have to use a shitton of reflection
 * to inject our own converter, as otherwise components which use our NBT holder couldn't be used
 * with adventure's GSON serializer
 *
 * @see <a href="https://github.com/retrooper/packetevents/issues/1448">#1448</a>
 * @see <a href="https://github.com/retrooper/packetevents/issues/1345">#1345</a>
 */
@NullMarked
@ApiStatus.Internal
public final class AdventureConversionInjector {

    private AdventureConversionInjector() {
    }

    public static void inject() {
        if (!AdventureSupportUtil.HAS_DATA_COMPONENTS) {
            return;
        }
        try {
            Class<?> conversionImpl = Class.forName("net.kyori.adventure.text.event.DataComponentValueConversionImpl");
            Class<?> registeredConversion = Class.forName("net.kyori.adventure.text.event.DataComponentValueConverterRegistry$RegisteredConversion");
            Class<?> conversionCache = Class.forName("net.kyori.adventure.text.event.DataComponentValueConverterRegistry$ConversionCache");

            // this is the actual converter...
            BiFunction<Key, NbtTagHolder, GsonDataComponentValue> converter = (key, holder) ->
                    gsonDataComponentValue(NBTCodec.nbtToJson(holder.getTag(), false));

            Constructor<?> conversionImplCtor = conversionImpl.getDeclaredConstructor(Class.class, Class.class, BiFunction.class);
            conversionImplCtor.setAccessible(true);
            @SuppressWarnings("unchecked")
            DataComponentValueConverterRegistry.Conversion<NbtTagHolder, GsonDataComponentValue> conversionImplInst =
                    (DataComponentValueConverterRegistry.Conversion<NbtTagHolder, GsonDataComponentValue>)
                            conversionImplCtor.newInstance(NbtTagHolder.class, GsonDataComponentValue.class, converter);

            Constructor<?> registeredConversionCtor = registeredConversion.getDeclaredConstructor(
                    Key.class, DataComponentValueConverterRegistry.Conversion.class);
            registeredConversionCtor.setAccessible(true);
            Key providerKey = Key.key("packetevents:hack/nbt2gson");
            Object registeredConversionInst = registeredConversionCtor.newInstance(providerKey, conversionImplInst);

            Map<Class<?>, Object> vals = new ConcurrentHashMap<>(1);
            vals.put(GsonDataComponentValue.class, registeredConversionInst);

            // just injecting into the cache is enough to make
            // everything work as there is no cache invalidation
            @SuppressWarnings("unchecked")
            Map<Object, Object> cache = (Map<Object, Object>) Reflection.getField(conversionCache, "CACHE").get(null);
            cache.put(NbtTagHolder.class, vals);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Failed to inject into conversion cache", exception);
        }
    }

    public static void uninject() {
        if (!AdventureSupportUtil.HAS_DATA_COMPONENTS) {
            return;
        }
        try {
            Class<?> conversionCache = Class.forName("net.kyori.adventure.text.event.DataComponentValueConverterRegistry$ConversionCache");
            @SuppressWarnings("unchecked")
            Map<Object, Object> cache = (Map<Object, Object>) Reflection.getField(conversionCache, "CACHE").get(null);
            cache.remove(NbtTagHolder.class);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Failed to uninject from conversion cache", exception);
        }
    }
}
