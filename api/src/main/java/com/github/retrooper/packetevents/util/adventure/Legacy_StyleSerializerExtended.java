package com.github.retrooper.packetevents.util.adventure;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Codec;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.Set;

public class Legacy_StyleSerializerExtended implements JsonDeserializer<Style>, JsonSerializer<Style> {

    private static final TextDecoration[] DECORATIONS = {
            TextDecoration.BOLD,
            TextDecoration.ITALIC,
            TextDecoration.UNDERLINED,
            TextDecoration.STRIKETHROUGH,
            TextDecoration.OBFUSCATED
    };

    static {
        // Ensure coverage of decorations
        final Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
        for (final TextDecoration decoration : DECORATIONS) {
            knownDecorations.remove(decoration);
        }
        if (!knownDecorations.isEmpty()) {
            throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
        }
    }

    static final String FONT = "font";
    static final String COLOR = "color";
    static final String INSERTION = "insertion";
    static final String CLICK_EVENT = "clickEvent";
    static final String CLICK_EVENT_ACTION = "action";
    static final String CLICK_EVENT_VALUE = "value";
    static final String HOVER_EVENT = "hoverEvent";
    static final String HOVER_EVENT_ACTION = "action";
    static final String HOVER_EVENT_CONTENTS = "contents";
    static final @Deprecated String HOVER_EVENT_VALUE = "value";
    private final boolean emitLegacyHover;
    private final HoverSerializer hoverSerializer;

    Legacy_StyleSerializerExtended(final boolean emitLegacyHover) {
        this.emitLegacyHover = emitLegacyHover;
        this.hoverSerializer = new HoverSerializer();
    }

    public Style deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return this.deserialize(object, context);
    }

    private Style deserialize(final JsonObject json, final JsonDeserializationContext context) throws JsonParseException {
        final Style.Builder style = Style.style();
        if (json.has(FONT)) {
            style.font(context.deserialize(json.get(FONT), Key.class));
        }

        if (json.has(COLOR)) {
            final TextColorWrapper color = context.deserialize(json.get(COLOR), TextColorWrapper.class);
            if (color.color != null) {
                style.color(color.color);
            } else if (color.decoration != null) {
                style.decoration(color.decoration, true);
            }
        }

        for (final TextDecoration decoration : DECORATIONS) {
            final String value = TextDecoration.NAMES.key(decoration);
            if (json.has(value)) {
                style.decoration(decoration, json.get(value).getAsBoolean());
            }
        }

        if (json.has(INSERTION)) {
            style.insertion(json.get(INSERTION).getAsString());
        }

        if (json.has(CLICK_EVENT)) {
            final JsonObject clickEvent = json.getAsJsonObject(CLICK_EVENT);
            if (clickEvent != null) {
                final ClickEvent.Action action = optionallyDeserialize(clickEvent.getAsJsonPrimitive(CLICK_EVENT_ACTION), context, ClickEvent.Action.class);
                if (action != null && action.readable()) {
                    final JsonPrimitive rawValue = clickEvent.getAsJsonPrimitive(CLICK_EVENT_VALUE);
                    final String value = rawValue == null ? null : rawValue.getAsString();
                    if (value != null) {
                        style.clickEvent(ClickEvent.clickEvent(action, value));
                    }
                }
            }
        }

        if (json.has(HOVER_EVENT)) {
            final JsonObject hoverEventObject = json.getAsJsonObject(HOVER_EVENT);
            if (hoverEventObject != null) {
                // Patch for hover event deserialization
                final JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive(HOVER_EVENT_ACTION);
                if (serializedAction != null) {
                    final JsonElement rawValue;
                    final boolean legacy;
                    if (hoverEventObject.has(HOVER_EVENT_CONTENTS)) {
                        legacy = false;
                        rawValue = hoverEventObject.get(HOVER_EVENT_CONTENTS);
                    } else if (hoverEventObject.has(HOVER_EVENT_VALUE)) {
                        rawValue = hoverEventObject.get(HOVER_EVENT_VALUE);
                        legacy = true;
                    } else {
                        legacy = false;
                        rawValue = null;
                    }

                    if (rawValue != null) {
                        HoverEvent.Action action = null;
                        Object value = null;
                        switch (serializedAction.getAsString()) {
                            case "show_text":
                                action = HoverEvent.Action.SHOW_TEXT;
                                value = context.deserialize(rawValue, Component.class);;
                                break;
                            case "show_item":
                                action = HoverEvent.Action.SHOW_ITEM;
                                value = tryIgnoring(() -> this.hoverSerializer.deserializeShowItem(context::deserialize, rawValue, legacy));
                                break;
                            case "show_entity":
                                action = HoverEvent.Action.SHOW_ENTITY;
                                value = tryIgnoring(() -> this.hoverSerializer.deserializeShowEntity(context::deserialize, rawValue, this.decoder(context), legacy));
                                break;
                            case "show_achievement":
                                action = HoverEvent.Action.SHOW_TEXT;
                                value = tryIgnoring(() -> this.hoverSerializer.deserializeShowAchievement(rawValue));
                                break;
                        }

                        if (value != null) {
                            style.hoverEvent(HoverEvent.hoverEvent(action, value));
                        }
                    }
                }
            }
        }

        if (json.has(FONT)) {
            style.font(context.deserialize(json.get(FONT), Key.class));
        }

        return style.build();
    }

    private <T> T tryIgnoring(final ExceptionalFunction<T> function) {
        try {
            return function.invoke();
        } catch (IOException e) {
            return null;
        }
    }

    @FunctionalInterface
    interface ExceptionalFunction<T> {
        T invoke() throws IOException;
    }

    private static <T> T optionallyDeserialize(final JsonElement json, final JsonDeserializationContext context, final Class<T> type) {
        return json == null ? null : context.deserialize(json, type);
    }

    private Codec.Decoder<Component, String, JsonParseException> decoder(final JsonDeserializationContext ctx) {
        return string -> {
            final JsonReader reader = new JsonReader(new StringReader(string));
            return (Component) ctx.deserialize(Streams.parse(reader), Component.class);
        };
    }

    public JsonElement serialize(final Style src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject json = new JsonObject();

        for (final TextDecoration decoration : DECORATIONS) {
            final TextDecoration.State state = src.decoration(decoration);
            if (state != TextDecoration.State.NOT_SET) {
                final String name = TextDecoration.NAMES.key(decoration);
                assert name != null; // should never be null
                json.addProperty(name, state == TextDecoration.State.TRUE);
            }
        }

        final TextColor color = src.color();
        if (color != null) {
            json.add(COLOR, context.serialize(color));
        }

        final String insertion = src.insertion();
        if (insertion != null) {
            json.addProperty(INSERTION, insertion);
        }

        final ClickEvent clickEvent = src.clickEvent();
        if (clickEvent != null) {
            JsonObject eventJson = new JsonObject();
            eventJson.add(CLICK_EVENT_ACTION, context.serialize(clickEvent.action()));
            eventJson.addProperty(CLICK_EVENT_VALUE, clickEvent.value());
            json.add(CLICK_EVENT, eventJson);
        }

        final HoverEvent<?> hoverEvent = src.hoverEvent();
        if (hoverEvent != null) {
            final JsonObject eventJson = new JsonObject();
            eventJson.add(HOVER_EVENT_ACTION, context.serialize(hoverEvent.action()));
            final JsonElement modernContents = context.serialize(hoverEvent.value());
            eventJson.add(HOVER_EVENT_CONTENTS, modernContents);
            if (this.emitLegacyHover) {
                eventJson.add(HOVER_EVENT_VALUE, this.serializeLegacyHoverEvent(hoverEvent, modernContents, context));
            }

            json.add(HOVER_EVENT, eventJson);
        }

        final Key font = src.font();
        if (font != null) {
            json.add(FONT, context.serialize(font));
        }

        return json;
    }

    private JsonElement serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonElement modernContents, final JsonSerializationContext context) {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) { // serialization is the same
            return modernContents;
        }

        // Patch for hover event serialization
        Component serialized = null;
        try {
            if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                serialized = this.hoverSerializer.serializeShowEntity((HoverEvent.ShowEntity) hoverEvent.value(), this.encoder(context));
            } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                serialized = this.hoverSerializer.serializeShowItem((HoverEvent.ShowItem) hoverEvent.value());
            }
        } catch (IOException var6) {
            throw new JsonSyntaxException(var6);
        }

        return serialized == null ? JsonNull.INSTANCE : context.serialize(serialized);
    }

    private Codec.Encoder<Component, String, RuntimeException> encoder(final JsonSerializationContext ctx) {
        return component -> ctx.serialize(component).toString();
    }

}
