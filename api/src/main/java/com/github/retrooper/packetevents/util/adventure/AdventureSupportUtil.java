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

import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@NullMarked
@ApiStatus.Internal
public final class AdventureSupportUtil {

    public static final boolean HAS_BOSSBAR_VIEWERS = Reflection.getMethod(BossBar.class, "viewers", 0) != null;
    public static final boolean HAS_TRANSLATION_FALLBACK = Reflection.getMethodExact(TranslatableComponent.class, "fallback", String.class) != null;
    public static final boolean HAS_TRANSLATION_ARGUMENTS = Reflection.getMethodExact(TranslatableComponent.class, "arguments", List.class) != null;
    public static final boolean HAS_JSON_SERIALIZER_OPTS = Reflection.getMethod(GsonComponentSerializer.Builder.class, "editOptions", 0) != null;
    public static final boolean HAS_DATA_COMPONENTS = Reflection.getMethodExact(HoverEvent.class, "showItem", HoverEvent.class, Keyed.class, int.class, Map.class) != null;
    public static final boolean HAS_SHADOW_COLOR = Reflection.getMethod(Style.class, "shadowColor", 0) != null;
    public static final boolean HAS_PAYLOAD = Reflection.getMethod(ClickEvent.class, "payload", 0) != null;
    public static final boolean HAS_OBJECT_COMPONENT = Reflection.getMethod(Component.class, "object", 0) != null;

    private static final Method CLICK_EVENT_VALUE = Reflection.getMethodExact(ClickEvent.class, "value", String.class);

    private AdventureSupportUtil() {
    }

    public static String getStringValue(ClickEvent event) {
        if (HAS_PAYLOAD) {
            return ((ClickEvent.Payload.Text) event.payload()).value();
        }
        try {
            return (String) CLICK_EVENT_VALUE.invoke(event);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static int getIntValue(ClickEvent event) {
        if (HAS_PAYLOAD) {
            return ((ClickEvent.Payload.Int) event.payload()).integer();
        }
        try {
            return Integer.parseInt((String) CLICK_EVENT_VALUE.invoke(event));
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
