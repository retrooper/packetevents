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

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class StyleSerializerExtended extends TypeAdapter<Style> {

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

    static TypeAdapter<Style> create(final boolean emitLegacyHover, final Gson gson) {
        return new StyleSerializerExtended(emitLegacyHover, gson).nullSafe();
    }

    private final boolean emitLegacyHover;
    private final Gson gson;
    private final HoverSerializer hoverSerializer;

    private StyleSerializerExtended(final boolean emitLegacyHover, final Gson gson) {
        this.emitLegacyHover = emitLegacyHover;
        this.gson = gson;
        this.hoverSerializer = new HoverSerializer(gson);
    }

    @Override
    public Style read(final JsonReader in) throws IOException {
        in.beginObject();
        final Style.Builder style = Style.style();

        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals(FONT)) {
                style.font(this.gson.fromJson(in, Key.class));
            } else if (fieldName.equals(COLOR)) {
                final TextColorWrapper color = this.gson.fromJson(in, TextColorWrapper.class);
                if (color.color != null) {
                    style.color(color.color);
                } else if (color.decoration != null) {
                    style.decoration(color.decoration, TextDecoration.State.TRUE);
                }
            } else if (TextDecoration.NAMES.keys().contains(fieldName)) {
                style.decoration(TextDecoration.NAMES.value(fieldName), this.readBoolean(in));
            } else if (fieldName.equals(INSERTION)) {
                style.insertion(in.nextString());
            } else if (fieldName.equals(CLICK_EVENT)) {
                in.beginObject();
                ClickEvent.Action action = null;
                String value = null;
                while (in.hasNext()) {
                    final String clickEventField = in.nextName();
                    if (clickEventField.equals(CLICK_EVENT_ACTION)) {
                        action = this.gson.fromJson(in, ClickEvent.Action.class);
                    } else if (clickEventField.equals(CLICK_EVENT_VALUE)) {
                        value = in.peek() == JsonToken.NULL ? null : in.nextString();
                    } else {
                        in.skipValue();
                    }
                }
                if (action != null && action.readable() && value != null) {
                    style.clickEvent(ClickEvent.clickEvent(action, value));
                }
                in.endObject();
            } else if (fieldName.equals(HOVER_EVENT)) {
                final JsonObject hoverEventObject = this.gson.fromJson(in, JsonObject.class);
                if (hoverEventObject != null) {
                    final JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive(HOVER_EVENT_ACTION);
                    if (serializedAction == null) {
                        continue;
                    }

                    // Patch for hover event deserialization
                    final JsonElement rawValue;
                    boolean legacy = false;
                    if (hoverEventObject.has(HOVER_EVENT_CONTENTS)) {
                        rawValue = hoverEventObject.get(HOVER_EVENT_CONTENTS);
                    } else if (hoverEventObject.has(HOVER_EVENT_VALUE)) {
                        rawValue = hoverEventObject.get(HOVER_EVENT_VALUE);
                        legacy = true;
                    } else {
                        rawValue = null;
                    }

                    if (rawValue != null) {
                        HoverEvent.Action action = null;
                        Object value = null;
                        switch (serializedAction.getAsString()) {
                            case "show_text":
                                action = HoverEvent.Action.SHOW_TEXT;
                                value = this.gson.fromJson(rawValue, Component.class);;
                                break;
                            case "show_item":
                                action = HoverEvent.Action.SHOW_ITEM;
                                value = this.hoverSerializer.deserializeShowItem(rawValue, legacy);
                                break;
                            case "show_entity":
                                action = HoverEvent.Action.SHOW_ENTITY;
                                value = this.hoverSerializer.deserializeShowEntity(rawValue, this.decoder(), legacy);
                                break;
                            case "show_achievement":
                                action = HoverEvent.Action.SHOW_TEXT;
                                value = this.hoverSerializer.deserializeShowAchievement(rawValue);
                                break;
                        }

                        if (value != null) {
                            style.hoverEvent(HoverEvent.hoverEvent(action, value));
                        }
                    }
                }
            } else {
                in.skipValue();
            }
        }

        in.endObject();
        return style.build();
    }

    private boolean readBoolean(final JsonReader in) throws IOException {
        final JsonToken peek = in.peek();
        if (peek == JsonToken.BOOLEAN) {
            return in.nextBoolean();
        } else if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
            return Boolean.parseBoolean(in.nextString());
        } else {
            throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
        }
    }

    private Codec.Decoder<Component, String, JsonParseException> decoder() {
        return string -> this.gson.fromJson(string, Component.class);
    }

    private Codec.Encoder<Component, String, JsonParseException> encoder() {
        return component -> this.gson.toJson(component, Component.class);
    }

    @Override
    public void write(final JsonWriter out, final Style value) throws IOException {
        out.beginObject();

        for (final TextDecoration decoration : DECORATIONS) {
            final TextDecoration.State state = value.decoration(decoration);
            if (state != TextDecoration.State.NOT_SET) {
                final String name = TextDecoration.NAMES.key(decoration);
                assert name != null; // should never be null
                out.name(name);
                out.value(state == TextDecoration.State.TRUE);
            }
        }

        final @Nullable TextColor color = value.color();
        if (color != null) {
            out.name(COLOR);
            this.gson.toJson(color, TextColor.class, out);
        }

        final @Nullable String insertion = value.insertion();
        if (insertion != null) {
            out.name(INSERTION);
            out.value(insertion);
        }

        final @Nullable ClickEvent clickEvent = value.clickEvent();
        if (clickEvent != null) {
            out.name(CLICK_EVENT);
            out.beginObject();
            out.name(CLICK_EVENT_ACTION);
            this.gson.toJson(clickEvent.action(), ClickEvent.Action.class, out);
            out.name(CLICK_EVENT_VALUE);
            out.value(clickEvent.value());
            out.endObject();
        }

        final @Nullable HoverEvent<?> hoverEvent = value.hoverEvent();
        if (hoverEvent != null) {
            out.name(HOVER_EVENT);
            out.beginObject();
            out.name(HOVER_EVENT_ACTION);
            final HoverEvent.Action<?> action = hoverEvent.action();
            this.gson.toJson(action, HoverEvent.Action.class, out);
            if (this.emitLegacyHover) {
                out.name(HOVER_EVENT_VALUE);
                this.serializeLegacyHoverEvent(hoverEvent, out);
            } else {
                out.name(HOVER_EVENT_CONTENTS);
                if (action == HoverEvent.Action.SHOW_ITEM) {
                    this.gson.toJson(hoverEvent.value(), HoverEvent.ShowItem.class, out);
                } else if (action == HoverEvent.Action.SHOW_ENTITY) {
                    this.gson.toJson(hoverEvent.value(), HoverEvent.ShowEntity.class, out);
                } else if (action == HoverEvent.Action.SHOW_TEXT) {
                    this.gson.toJson(hoverEvent.value(), Component.class, out);
                } else {
                    throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
                }
            }

            out.endObject();
        }

        final @Nullable Key font = value.font();
        if (font != null) {
            out.name(FONT);
            this.gson.toJson(font, Key.class, out);
        }

        out.endObject();
    }

    private void serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonWriter out) throws IOException {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) { // serialization is the same
            this.gson.toJson(hoverEvent.value(), Component.class, out);
            return;
        }

        // Patch for hover event serialization
        Component serialized = null;
        try {
            if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                serialized = this.hoverSerializer.serializeShowEntity((HoverEvent.ShowEntity) hoverEvent.value(), this.encoder());
            } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                serialized = this.hoverSerializer.serializeShowItem((HoverEvent.ShowItem) hoverEvent.value());
            }
        } catch (final IOException ex) {
            throw new JsonSyntaxException(ex);
        }
        if (serialized != null) {
            this.gson.toJson(serialized, Component.class, out);
        } else {
            out.nullValue();
        }
    }

}
