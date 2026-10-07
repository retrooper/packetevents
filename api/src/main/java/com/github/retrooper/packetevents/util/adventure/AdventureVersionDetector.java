package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecorationAndState;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Consumer;

@NullMarked
@ApiStatus.Internal
final class AdventureVersionDetector {

    private AdventureVersionDetector() {
    }

    public static @Nullable String detectAdventureVersion() {
        // check whether adventure is actually installed
        try {
            Class.forName("net.kyori.adventure.text.Component");
        } catch (NoClassDefFoundError | ClassNotFoundException ignored) {
            return null;
        }
        return detectAdventureVersion0();
    }

    /**
     * Looks at which methods are available to determine the current version of adventure.
     * I don't know if there is a better way to do this. I hope there is.
     */
    @SuppressWarnings("deprecation")
    private static String detectAdventureVersion0() {
        // 5.x.x
        if (Reflection.getMethodExact(ClickEvent.class, "value", String.class) == null) {
            return "5.0.0";
        }

        // 4.x.x
        if (Reflection.getMethodExact(HoverEvent.class, "showItem", HoverEvent.class, Keyed.class, int.class) == null) {
            return "4.5.1";
        } else if (Reflection.getMethodExact(TranslationRegistry.class, "contains", boolean.class, String.class) == null) {
            return "4.6.0";
        } else if (Reflection.getMethodExact(SelectorComponent.class, "separator", Component.class) == null) {
            return "4.7.0";
        } else if (Reflection.getMethodExact(Audience.class, "forEachAudience", void.class, Consumer.class) == null) {
            return "4.8.1";
        } else if (Reflection.getMethodExact(TextDecoration.class, "withState", TextDecorationAndState.class, boolean.class) == null) {
            return "4.9.3";
        } else if (Reflection.getMethodExact(Index.class, "keyOrThrow", Object.class, Object.class) == null) {
            return "4.10.1";
        } else if (Reflection.getMethodExact(Sound.class, "seed", OptionalLong.class) == null) {
            return "4.11.0";
        } else if (Reflection.getMethodExact(TranslatableComponent.class, "fallback", String.class) == null) {
            return "4.12.0";
        } else if (Reflection.getMethod(BossBar.class, "viewers", 0) == null) {
            return "4.13.1";
        } else if (Reflection.getMethodExact(TranslatableComponent.class, "arguments", List.class) == null) {
            return "4.14.0";
        } else if (Reflection.getMethodExact(Audience.class, "removeResourcePacks", void.class, Iterable.class) == null) {
            return "4.15.0";
        } else if (Reflection.getMethodExact(HoverEvent.class, "showItem", HoverEvent.class, Keyed.class, int.class, Map.class) == null) {
            return "4.16.0";
        } else if (Reflection.getMethod(Component.class, "virtual", 0) == null) {
            return "4.17.0";
        } else if (Reflection.getMethod(TextReplacementConfig.Builder.class, "replaceInsideHoverEvents", 0) == null) {
            return "4.18.0";
        } else if (Reflection.getMethod(Translator.class, "canTranslate", 0) == null) {
            return "4.19.0";
        } else if (Reflection.getField(JSONOptions.HoverEventValueMode.class, "ALL") == null) {
            return "4.20.0";
        } else if (Reflection.getMethod(ClickEvent.class, "payload", 0) == null) {
            return "4.21.0";
        } else if (Reflection.getMethodExact(ClickEvent.class, "custom", ClickEvent.class, Key.class, BinaryTagHolder.class) == null) {
            return "4.22.0";
        } else if (Reflection.getMethodExact(Audience.class, "closeDialog", void.class) == null) {
            return "4.23.0";
        } else if (Reflection.getMethod(Component.class, "object", 0) == null) {
            return "4.24.0";
        } else if (Reflection.getMethodExact(Component.class, "toBuilder", ComponentBuilder.class) == null) {
            return "4.25.0";
        } else {
            return "4.26.1";
        }
    }
}
