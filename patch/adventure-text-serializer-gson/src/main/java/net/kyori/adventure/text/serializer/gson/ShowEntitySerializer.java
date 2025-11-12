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
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_TYPE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_UUID;

final class ShowEntitySerializer extends TypeAdapter<HoverEvent.ShowEntity> {
    static TypeAdapter<HoverEvent.ShowEntity> create(final Gson gson, final OptionState opt) {
        return new ShowEntitySerializer(gson, opt.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_KEY_AS_TYPE_AND_UUID_AS_ID)).nullSafe();
    }

    private final Gson gson;
    private final boolean emitKeyAsTypeAndUuidAsId;

    private ShowEntitySerializer(final Gson gson, final boolean emitKeyAsTypeAndUuidAsId) {
        this.gson = gson;
        this.emitKeyAsTypeAndUuidAsId = emitKeyAsTypeAndUuidAsId;
    }

    @Override
    public HoverEvent.ShowEntity read(final JsonReader in) throws IOException {
        in.beginObject();

        Key type = null;
        UUID id = null;
        @Nullable Component name = null;

        while (in.hasNext()) {
            final String fieldName = in.nextName();

            switch (fieldName) {
                case SHOW_ENTITY_ID:
                    if (in.peek() == JsonToken.BEGIN_ARRAY) {
                        // If it is an array, we know this is a UUID encoded.
                        id = this.gson.fromJson(in, UUID.class);
                    } else {
                        // If it is a string, it might be a key or a UUID.
                        final String string = in.nextString();

                        // We can use some "hints" here.
                        if (string.contains(Key.DEFAULT_SEPARATOR + "")) {
                            type = Key.key(string);
                        }

                        // Otherwise, let's try and parse it as a UUID.
                        try {
                            id = UUID.fromString(string);
                        } catch (final IllegalArgumentException ignored) {
                            try {
                                type = Key.key(string);
                            } catch (final InvalidKeyException ignored2) {
                                // Skip value.
                            }
                        }
                    }
                    break;
                case SHOW_ENTITY_TYPE:
                    type = this.gson.fromJson(in, Key.class);
                    break;
                case SHOW_ENTITY_UUID:
                    id = this.gson.fromJson(in, UUID.class);
                    break;
                case SHOW_ENTITY_NAME:
                    name = this.gson.fromJson(in, SerializerFactory.COMPONENT_TYPE);
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }

        if (type == null || id == null) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        in.endObject();

        return BackwardCompatUtil.createShowEntity(type, id, name); // packetevents patch
    }

    @Override
    public void write(final JsonWriter out, final HoverEvent.ShowEntity value) throws IOException {
        out.beginObject();

        out.name(this.emitKeyAsTypeAndUuidAsId ? SHOW_ENTITY_TYPE : SHOW_ENTITY_ID);
        this.gson.toJson(value.type(), SerializerFactory.KEY_TYPE, out);

        out.name(this.emitKeyAsTypeAndUuidAsId ? SHOW_ENTITY_ID : SHOW_ENTITY_UUID);
        this.gson.toJson(value.id(), SerializerFactory.UUID_TYPE, out);

        final @Nullable Component name = value.name();
        if (name != null) {
            out.name(SHOW_ENTITY_NAME);
            this.gson.toJson(name, SerializerFactory.COMPONENT_TYPE, out);
        }

        out.endObject();
    }
}
