/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.util.Codec;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_COMMAND;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAGE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PATH;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAYLOAD;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_SNAKE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_URL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.COLOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.FONT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CONTENTS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_SNAKE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.INSERTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHADOW_COLOR;

final class StyleSerializer extends TypeAdapter<Style> {
    @SuppressWarnings("checkstyle:NoWhitespaceAfter")
    private static final TextDecoration[] DECORATIONS = {
            // The order here is important -- Minecraft does string comparisons of some
            // serialized components so we have to make sure our order matches Vanilla
            TextDecoration.BOLD,
            TextDecoration.ITALIC,
            TextDecoration.UNDERLINED,
            TextDecoration.STRIKETHROUGH,
            TextDecoration.OBFUSCATED
    };

    private static final String FALLBACK_URL_PROTOCOL = "https://";

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

    static TypeAdapter<Style> create(final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHover, final @Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement, final OptionState features, final Gson gson) { // packetevents patch
        final JSONOptions.HoverEventValueMode hoverMode = features.value(JSONOptions.EMIT_HOVER_EVENT_TYPE);
        final JSONOptions.ClickEventValueMode clickMode = features.value(JSONOptions.EMIT_CLICK_EVENT_TYPE);
        return new StyleSerializer(
                legacyHover,
                compatShowAchievement, // packetevents patch
                hoverMode == JSONOptions.HoverEventValueMode.VALUE_FIELD || hoverMode == JSONOptions.HoverEventValueMode.ALL,
                hoverMode == JSONOptions.HoverEventValueMode.CAMEL_CASE || hoverMode == JSONOptions.HoverEventValueMode.ALL,
                hoverMode == JSONOptions.HoverEventValueMode.SNAKE_CASE || hoverMode == JSONOptions.HoverEventValueMode.ALL,
                clickMode == JSONOptions.ClickEventValueMode.CAMEL_CASE || clickMode == JSONOptions.ClickEventValueMode.BOTH,
                clickMode == JSONOptions.ClickEventValueMode.SNAKE_CASE || clickMode == JSONOptions.ClickEventValueMode.BOTH,
                features.value(JSONOptions.VALIDATE_STRICT_EVENTS),
                features.value(JSONOptions.SHADOW_COLOR_MODE) != JSONOptions.ShadowColorEmitMode.NONE,
                features.value(JSONOptions.EMIT_CHANGE_PAGE_CLICK_EVENT_PAGE_AS_STRING),
                features.value(JSONOptions.EMIT_CLICK_URL_HTTPS),
                gson
        ).nullSafe();
    }

    private final net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHover;
    private final BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement; // packetevents patch
    private final boolean emitValueFieldHover;
    private final boolean emitCamelCaseHover;
    private final boolean emitSnakeCaseHover;
    private final boolean emitCamelCaseClick;
    private final boolean emitSnakeCaseClick;
    private final boolean strictEventValues;
    private final boolean emitShadowColor;
    private final boolean emitStringPage;
    private final boolean emitClickUrlHttps;
    private final Gson gson;

    private StyleSerializer(
            final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHover,
            final @Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement, // packetevents patch
            final boolean emitValueFieldHover,
            final boolean emitCamelCaseHover,
            final boolean emitSnakeCaseHover,
            final boolean emitCamelCaseClick,
            final boolean emitSnakeCaseClick,
            final boolean strictEventValues,
            final boolean emitShadowColor,
            final boolean emitStringPage,
            final boolean emitClickUrlHttps,
            final Gson gson
    ) {
        this.legacyHover = legacyHover;
        this.compatShowAchievement = compatShowAchievement; // packetevents patch
        this.emitValueFieldHover = emitValueFieldHover;
        this.emitCamelCaseHover = emitCamelCaseHover;
        this.emitSnakeCaseHover = emitSnakeCaseHover;
        this.emitCamelCaseClick = emitCamelCaseClick;
        this.emitSnakeCaseClick = emitSnakeCaseClick;
        this.strictEventValues = strictEventValues;
        this.emitShadowColor = emitShadowColor;
        this.emitStringPage = emitStringPage;
        this.emitClickUrlHttps = emitClickUrlHttps;
        this.gson = gson;
    }

    @Override
    public Style read(final JsonReader in) throws IOException {
        in.beginObject();
        final Style.Builder style = Style.style();

        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals(FONT)) {
                style.font(this.gson.fromJson(in, SerializerFactory.KEY_TYPE));
            } else if (fieldName.equals(COLOR)) {
                final TextColorWrapper color = this.gson.fromJson(in, SerializerFactory.COLOR_WRAPPER_TYPE);
                if (color.color != null) {
                    style.color(color.color);
                } else if (color.decoration != null) {
                    style.decoration(color.decoration, TextDecoration.State.TRUE);
                }
            } else if (BackwardCompatUtil.IS_4_18_0_OR_NEWER && fieldName.equals(SHADOW_COLOR)) { // packetevents patch
                style.shadowColor(this.gson.fromJson(in, SerializerFactory.SHADOW_COLOR_TYPE));
            } else if (TextDecoration.NAMES.keys().contains(fieldName)) {
                style.decoration(TextDecoration.NAMES.value(fieldName), GsonHacks.readBoolean(in));
            } else if (fieldName.equals(INSERTION)) {
                style.insertion(in.nextString());
            } else if (fieldName.equals(CLICK_EVENT_SNAKE) || fieldName.equals(CLICK_EVENT_CAMEL)) {
                in.beginObject();
                ClickEvent.Action action = null;
                String value = null;
                Key key = null;
                Integer page = null;
                while (in.hasNext()) {
                    final String clickEventField = in.nextName();
                    if (clickEventField.equals(CLICK_EVENT_ACTION)) {
                        action = this.gson.fromJson(in, SerializerFactory.CLICK_ACTION_TYPE);
                    } else if (clickEventField.equals(CLICK_EVENT_PAGE)) {
                        if (in.peek() == JsonToken.NUMBER) {
                            page = in.nextInt();
                        } else if (in.peek() == JsonToken.STRING) {
                            page = Integer.parseInt(in.nextString());
                        } else if (in.peek() == JsonToken.NULL) {
                            throw ComponentSerializerImpl.notSureHowToDeserialize(clickEventField);
                        } else {
                            in.skipValue();
                        }
                    } else if (clickEventField.equals(CLICK_EVENT_VALUE) || clickEventField.equals(CLICK_EVENT_URL) || clickEventField.equals(CLICK_EVENT_PATH) || clickEventField.equals(CLICK_EVENT_COMMAND) || clickEventField.equals(CLICK_EVENT_PAYLOAD)) {
                        if (in.peek() == JsonToken.NULL) {
                            if (this.strictEventValues) {
                                throw ComponentSerializerImpl.notSureHowToDeserialize(clickEventField);
                            }
                            in.nextNull();
                        } else {
                            value = in.nextString();
                        }
                    } else if (clickEventField.equals(CLICK_EVENT_ID)) {
                        key = Key.key(in.nextString());
                    } else {
                        in.skipValue();
                    }
                }
                if (action != null && action.readable()) {
                    switch (action) {
                        case OPEN_URL:
                            if (value != null) style.clickEvent(ClickEvent.openUrl(value));
                            break;
                        case RUN_COMMAND:
                            if (value != null) style.clickEvent(ClickEvent.runCommand(value));
                            break;
                        case SUGGEST_COMMAND:
                            if (value != null) style.clickEvent(ClickEvent.suggestCommand(value));
                            break;
                        case CHANGE_PAGE:
                            if (page != null) style.clickEvent(ClickEvent.changePage(page));
                            break;
                        case COPY_TO_CLIPBOARD:
                            if (value != null) style.clickEvent(ClickEvent.copyToClipboard(value));
                            break;
                        case CUSTOM:
                            if (key != null && value != null) style.clickEvent(ClickEvent.custom(key, value));
                            break;
                        // Not readable.
                        case SHOW_DIALOG:
                        case OPEN_FILE:
                            break;
                    }
                }
                in.endObject();
            } else if (fieldName.equals(HOVER_EVENT_SNAKE) || fieldName.equals(HOVER_EVENT_CAMEL)) {
                final JsonObject hoverEventObject = this.gson.fromJson(in, JsonObject.class);
                if (hoverEventObject != null) {
                    final JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive(HOVER_EVENT_ACTION);
                    if (serializedAction == null) {
                        continue;
                    }

                    // packetevents patch begin
                    final String actionString = this.gson.fromJson(serializedAction, SerializerFactory.STRING_TYPE);
                    boolean isShowAchievement = false;
                    @SuppressWarnings("rawtypes")
                    HoverEvent.Action action;
                    if (actionString.equals("show_achievement")) {
                        try {
                            action = HoverEvent.Action.SHOW_ACHIEVEMENT;
                        } catch (final NoSuchFieldError e) {
                            action = HoverEvent.Action.SHOW_TEXT;
                        }
                        isShowAchievement = true;
                    } else {
                        action = this.gson.fromJson(serializedAction, SerializerFactory.HOVER_ACTION_TYPE);
                    }
                    // packetevents patch end
                    if (action.readable()) {
                        final @Nullable Object value;
                        final Class<?> actionType = action.type();
                        if (hoverEventObject.has(HOVER_EVENT_CONTENTS)) {
                            final @Nullable JsonElement rawValue = hoverEventObject.get(HOVER_EVENT_CONTENTS);
                            if (GsonHacks.isNullOrEmpty(rawValue)) {
                                if (this.strictEventValues) {
                                    throw ComponentSerializerImpl.notSureHowToDeserialize(rawValue);
                                }
                                value = null;
                            } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                                value = this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
                            } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                                value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ITEM_TYPE);
                            } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                                value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ENTITY_TYPE);
                            } else {
                                value = null;
                            }
                        } else if (hoverEventObject.has(HOVER_EVENT_VALUE)) {
                            final JsonElement element = hoverEventObject.get(HOVER_EVENT_VALUE);
                            if (GsonHacks.isNullOrEmpty(element)) {
                                if (this.strictEventValues) {
                                    throw ComponentSerializerImpl.notSureHowToDeserialize(element);
                                }
                                value = null;
                            } else if (Component.class.isAssignableFrom(actionType)) {
                                // packetevents patch begin
                                if (isShowAchievement && this.compatShowAchievement != null) {
                                    final String id = this.gson.fromJson(element, SerializerFactory.STRING_TYPE);
                                    value = this.compatShowAchievement.convert(id);
                                } else {
                                    final Component rawValue = this.gson.fromJson(element, SerializerFactory.COMPONENT_TYPE);
                                    value = this.legacyHoverEventContents(action, rawValue);
                                }
                                // packetevents patch end
                            } else if (String.class.isAssignableFrom(actionType)) {
                                value = this.gson.fromJson(element, SerializerFactory.STRING_TYPE);
                            } else {
                                value = null;
                            }
                        } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                            value = this.gson.fromJson(hoverEventObject, SerializerFactory.SHOW_ITEM_TYPE);
                        } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                            value = this.gson.fromJson(hoverEventObject, SerializerFactory.SHOW_ENTITY_TYPE);
                        } else {
                            if (this.strictEventValues) {
                                throw ComponentSerializerImpl.notSureHowToDeserialize(hoverEventObject);
                            }
                            value = null;
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

    private Object legacyHoverEventContents(final HoverEvent.Action<?> action, final Component rawValue) {
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return rawValue; // Passthrough -- no serialization needed
        } else if (this.legacyHover != null) {
            try {
                if (action == HoverEvent.Action.SHOW_ENTITY) {
                    return this.legacyHover.deserializeShowEntity(rawValue, this.decoder());
                } else if (action == HoverEvent.Action.SHOW_ITEM) {
                    return this.legacyHover.deserializeShowItem(rawValue);
                }
            } catch (final IOException ex) {
                throw new JsonParseException(ex);
            }
        }
        // if we can't handle
        throw new UnsupportedOperationException();
    }

    private Codec.Decoder<Component, String, JsonParseException> decoder() {
        return string -> this.gson.fromJson(string, SerializerFactory.COMPONENT_TYPE);
    }

    private Codec.Encoder<Component, String, JsonParseException> encoder() {
        return component -> this.gson.toJson(component, SerializerFactory.COMPONENT_TYPE);
    }

    @Override
    public void write(final JsonWriter out, final Style value) throws IOException {
        out.beginObject();

        for (int i = 0, length = DECORATIONS.length; i < length; i++) {
            final TextDecoration decoration = DECORATIONS[i];
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
            this.gson.toJson(color, SerializerFactory.COLOR_TYPE, out);
        }

        // packetevents patch start
        if (BackwardCompatUtil.IS_4_18_0_OR_NEWER) {
            final @Nullable ShadowColor shadowColor = value.shadowColor();
            if (shadowColor != null && this.emitShadowColor) {
                out.name(SHADOW_COLOR);
                this.gson.toJson(shadowColor, SerializerFactory.SHADOW_COLOR_TYPE, out);
            }
        }
        // packetevents patch end

        final @Nullable String insertion = value.insertion();
        if (insertion != null) {
            out.name(INSERTION);
            out.value(insertion);
        }

        final @Nullable ClickEvent clickEvent = value.clickEvent();
        if (clickEvent != null) {
            final ClickEvent.Action action = clickEvent.action();

            if (this.emitSnakeCaseClick) {
                out.name(CLICK_EVENT_SNAKE);
                out.beginObject();
                out.name(CLICK_EVENT_ACTION);
                this.gson.toJson(action, SerializerFactory.CLICK_ACTION_TYPE, out);

                if (action.readable()) {
                    final Object payload = BackwardCompatUtil.IS_4_22_0_OR_NEWER ? clickEvent.payload() : clickEvent.value(); // packetevents patch

                    if (payload instanceof String || payload instanceof ClickEvent.Payload.Text) { // packetevents patch
                        switch (action) {
                            case OPEN_URL:
                                out.name(CLICK_EVENT_URL);
                                break;
                            case RUN_COMMAND:
                            case SUGGEST_COMMAND:
                                out.name(CLICK_EVENT_COMMAND);
                                break;
                            case COPY_TO_CLIPBOARD:
                                out.name(CLICK_EVENT_VALUE);
                                break;
                        }
                        String payloadValue = payload instanceof String ? (String) payload : ((ClickEvent.Payload.Text) payload).value(); // packetevents patch
                        if (action == ClickEvent.Action.OPEN_URL && this.emitClickUrlHttps && !StyleSerializer.isValidUrlScheme(payloadValue)) {
                            payloadValue = StyleSerializer.FALLBACK_URL_PROTOCOL + payloadValue;
                        }
                        out.value(payloadValue);
                    } else if (payload instanceof ClickEvent.Payload.Custom) {
                        final ClickEvent.Payload.Custom customPayload = (ClickEvent.Payload.Custom) payload;
                        out.name(CLICK_EVENT_ID);
                        this.gson.toJson(customPayload.key(), SerializerFactory.KEY_TYPE, out);
                        out.name(CLICK_EVENT_PAYLOAD);
                        out.value(customPayload.data());
                    } else if (payload instanceof ClickEvent.Payload.Int) {
                        final ClickEvent.Payload.Int intPayload = (ClickEvent.Payload.Int) payload;
                        out.name(CLICK_EVENT_PAGE);
                        if (this.emitStringPage) {
                            out.value(String.valueOf(intPayload.integer()));
                        } else {
                            out.value(intPayload.integer());
                        }
                    }
                }

                out.endObject();
            }

            if (this.emitCamelCaseClick && (BackwardCompatUtil.IS_4_22_0_OR_NEWER || action.payloadType() == ClickEvent.Payload.Text.class)) { // packetevents patch
                out.name(CLICK_EVENT_CAMEL);
                out.beginObject();
                out.name(CLICK_EVENT_ACTION);
                this.gson.toJson(action, SerializerFactory.CLICK_ACTION_TYPE, out);
                out.name(CLICK_EVENT_VALUE);
                String payloadValue = clickEvent.value();
                if (action == ClickEvent.Action.OPEN_URL && this.emitClickUrlHttps && !StyleSerializer.isValidUrlScheme(payloadValue)) {
                    payloadValue = StyleSerializer.FALLBACK_URL_PROTOCOL + payloadValue;
                }
                out.value(payloadValue);
                out.endObject();
            }
        }

        final @Nullable HoverEvent<?> hoverEvent = value.hoverEvent();
        if (hoverEvent != null && (((this.emitSnakeCaseHover || this.emitCamelCaseHover) && !hoverEvent.action().toString().equals("show_achievement")) || this.emitValueFieldHover)) { // packetevents patch
            final HoverEvent.Action<?> action = hoverEvent.action();

            if (this.emitSnakeCaseHover && !action.toString().equals("show_achievement")) { // packetevents patch
                out.name(HOVER_EVENT_SNAKE);
                out.beginObject();

                out.name(HOVER_EVENT_ACTION);
                this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);

                if (action == HoverEvent.Action.SHOW_ITEM) {
                    for (final Map.Entry<String, JsonElement> entry : this.gson.toJsonTree(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE).getAsJsonObject().entrySet()) {
                        out.name(entry.getKey());
                        this.gson.toJson(entry.getValue(), out);
                    }
                } else if (action == HoverEvent.Action.SHOW_ENTITY) {
                    for (final Map.Entry<String, JsonElement> entry : this.gson.toJsonTree(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE).getAsJsonObject().entrySet()) {
                        out.name(entry.getKey());
                        this.gson.toJson(entry.getValue(), out);
                    }
                } else if (action == HoverEvent.Action.SHOW_TEXT) {
                    out.name(HOVER_EVENT_VALUE);
                    this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
                } else {
                    throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
                }

                out.endObject();
            }

            if (this.emitCamelCaseHover || this.emitValueFieldHover) {
                out.name(HOVER_EVENT_CAMEL);
                out.beginObject();

                out.name(HOVER_EVENT_ACTION);
                this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);

                if (this.emitCamelCaseHover && !action.toString().equals("show_achievement")) { // legacy action has no modern contents value // packetevents patch
                    out.name(HOVER_EVENT_CONTENTS);
                    if (action == HoverEvent.Action.SHOW_ITEM) {
                        this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE, out);
                    } else if (action == HoverEvent.Action.SHOW_ENTITY) {
                        this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE, out);
                    } else if (action == HoverEvent.Action.SHOW_TEXT) {
                        this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
                    } else {
                        throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
                    }
                }

                if (this.emitValueFieldHover) {
                    out.name(HOVER_EVENT_VALUE);
                    this.serializeLegacyHoverEvent(hoverEvent, out);
                }

                out.endObject();
            }
        }

        final @Nullable Key font = value.font();
        if (font != null) {
            out.name(FONT);
            this.gson.toJson(font, SerializerFactory.KEY_TYPE, out);
        }

        out.endObject();
    }

    private void serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonWriter out) throws IOException {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) { // serialization is the same
            this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
        } else if (hoverEvent.action().toString().equals("show_achievement")) { // packetevents patch
            this.gson.toJson(hoverEvent.value(), SerializerFactory.STRING_TYPE, out);
        } else if (this.legacyHover != null) { // for data formats that require knowledge of SNBT
            Component serialized = null;
            try {
                if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                    serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity) hoverEvent.value(), this.encoder());
                } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                    serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem) hoverEvent.value());
                }
            } catch (final IOException ex) {
                throw new JsonSyntaxException(ex);
            }
            if (serialized != null) {
                this.gson.toJson(serialized, SerializerFactory.COMPONENT_TYPE, out);
            } else {
                out.nullValue();
            }
        } else {
            out.nullValue();
        }
    }

    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "HttpUrlsUsage"})
    private static boolean isValidUrlScheme(final String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
