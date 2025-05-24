/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SHOW_ITEM_COMPONENTS;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SHOW_ITEM_COUNT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SHOW_ITEM_ID;

final class ShowItemSerializer extends TypeAdapter<HoverEvent.ShowItem> {
    @SuppressWarnings("deprecation")
    private static final String LEGACY_SHOW_ITEM_TAG = net.kyori.adventure.text.serializer.json.JSONComponentConstants.SHOW_ITEM_TAG;

    private final Gson gson;
    private final boolean emitDefaultQuantity;
    private final JSONOptions.ShowItemHoverDataMode itemDataMode;

    static TypeAdapter<HoverEvent.ShowItem> create(final Gson gson, final OptionState opt) {
        return new ShowItemSerializer(gson, opt.value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY), opt.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE)).nullSafe();
    }

    private ShowItemSerializer(final Gson gson, final boolean emitDefaultQuantity, final JSONOptions.ShowItemHoverDataMode itemDataMode) {
        this.gson = gson;
        this.emitDefaultQuantity = emitDefaultQuantity;
        this.itemDataMode = itemDataMode;
    }

    @Override
    @SuppressWarnings("deprecation")
    public HoverEvent.ShowItem read(final JsonReader in) throws IOException {
        in.beginObject();

        Key key = null;
        int count = 1;
        @Nullable BinaryTagHolder nbt = null;
        @Nullable Map<Key, DataComponentValue> dataComponents = null;

        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals(SHOW_ITEM_ID)) {
                key = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
            } else if (fieldName.equals(SHOW_ITEM_COUNT)) {
                count = in.nextInt();
            } else if (fieldName.equals(LEGACY_SHOW_ITEM_TAG)) {
                final JsonToken token = in.peek();
                if (token == JsonToken.STRING || token == JsonToken.NUMBER) {
                    // packetevents patch start
                    if (BackwardCompatUtil.IS_4_10_0_OR_NEWER) {
                        nbt = BinaryTagHolder.binaryTagHolder(in.nextString());
                    } else {
                        nbt = BinaryTagHolder.of(in.nextString());
                    }
                    // packetevents patch end
                } else if (token == JsonToken.BOOLEAN) {
                    // packetevents patch start
                    if (BackwardCompatUtil.IS_4_10_0_OR_NEWER) {
                        nbt = BinaryTagHolder.binaryTagHolder(String.valueOf(in.nextBoolean()));
                    } else {
                        nbt = BinaryTagHolder.of(String.valueOf(in.nextBoolean()));
                    }
                    // packetevents patch end
                } else if (token == JsonToken.NULL) {
                    in.nextNull();
                } else {
                    throw new JsonParseException("Expected " + LEGACY_SHOW_ITEM_TAG + " to be a string");
                }
            } else if (fieldName.equals(SHOW_ITEM_COMPONENTS)) {
                in.beginObject();
                while (in.peek() != JsonToken.END_OBJECT) {
                    final Key id = Key.key(in.nextName());
                    final JsonElement tree = this.gson.fromJson(in, JsonElement.class);
                    if (dataComponents == null) {
                        dataComponents = new HashMap<>();
                    }
                    dataComponents.put(id, GsonDataComponentValue.gsonDataComponentValue(tree));
                }
                in.endObject();
            } else {
                in.skipValue();
            }
        }

        if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        }
        in.endObject();

        if (dataComponents != null) {
            return HoverEvent.ShowItem.showItem(key, count, dataComponents);
        } else {
            // packetevents patch start
            return BackwardCompatUtil.createShowItem(key, count, nbt);
            // packetevents patch end
        }
    }

    @Override
    public void write(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
        out.beginObject();

        out.name(SHOW_ITEM_ID);
        this.gson.toJson(value.item(), SerializerFactory.KEY_TYPE, out);

        final int count = value.count();
        if (count != 1 || this.emitDefaultQuantity) {
            out.name(SHOW_ITEM_COUNT);
            out.value(count);
        }

        final @NotNull Map<Key, DataComponentValue> dataComponents = !BackwardCompatUtil.IS_4_17_0_OR_NEWER
                ? Collections.emptyMap() : value.dataComponents();
        if (!dataComponents.isEmpty() && this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_LEGACY_NBT) {
            out.name(SHOW_ITEM_COMPONENTS);
            out.beginObject();
            for (final Map.Entry<Key, GsonDataComponentValue> entry : value.dataComponentsAs(GsonDataComponentValue.class).entrySet()) {
                out.name(entry.getKey().asString());
                this.gson.toJson(entry.getValue().element(), out);
            }
            out.endObject();
        } else if (this.itemDataMode != JSONOptions.ShowItemHoverDataMode.EMIT_DATA_COMPONENTS) {
            maybeWriteLegacy(out, value);
        }

        out.endObject();
    }

    @SuppressWarnings("deprecation")
    private static void maybeWriteLegacy(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
        final @Nullable BinaryTagHolder nbt = value.nbt();
        if (nbt != null) {
            out.name(LEGACY_SHOW_ITEM_TAG);
            out.value(nbt.string());
        }
    }
}
