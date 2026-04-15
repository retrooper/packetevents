package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Method;

@NullMarked
@ApiStatus.Internal
public final class AdventureSupportUtil {

    public static final boolean HAS_TRANSLATION_FALLBACK = Reflection.getMethodExact(TranslatableComponent.class, "fallback", String.class) != null;
    public static final boolean HAS_TRANSLATION_ARGUMENTS = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.TranslationArgument") != null;
    public static final boolean HAS_DATA_COMPONENTS = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.event.DataComponentValue") != null;
    public static final boolean HAS_SHADOW_COLOR = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.format.ShadowColor") != null;
    public static final boolean HAS_PAYLOAD = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.event.ClickEvent$Payload") != null;
    public static final boolean HAS_OBJECT_COMPONENT = Reflection.getClassByNameWithoutException("net.kyori.adventure.text.ObjectComponent") != null;

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
