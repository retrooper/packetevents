package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.gson.BackwardCompatUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Method;

@NullMarked
@ApiStatus.Internal
public class AdventureSupportUtil {

    private static final Method CLICK_EVENT_VALUE = Reflection.getMethod(ClickEvent.class, "value", 0);

    private AdventureSupportUtil() {
    }

    public static String getStringValue(ClickEvent event) {
        if (BackwardCompatUtil.IS_4_22_0_OR_NEWER) {
            return ((ClickEvent.Payload.Text) event.payload()).value();
        }
        try {
            return (String) CLICK_EVENT_VALUE.invoke(event);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static int getIntValue(ClickEvent event) {
        if (BackwardCompatUtil.IS_4_22_0_OR_NEWER) {
            return ((ClickEvent.Payload.Int) event.payload()).integer();
        }
        try {
            return Integer.parseInt((String) CLICK_EVENT_VALUE.invoke(event));
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
