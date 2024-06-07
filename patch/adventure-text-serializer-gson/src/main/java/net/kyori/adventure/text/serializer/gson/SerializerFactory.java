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
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.Nullable;

final class SerializerFactory implements TypeAdapterFactory {
    static final Class<Key> KEY_TYPE = Key.class;
    static final Class<Component> COMPONENT_TYPE = Component.class;
    static final Class<Style> STYLE_TYPE = Style.class;
    static final Class<ClickEvent.Action> CLICK_ACTION_TYPE = ClickEvent.Action.class;
    static final Class<HoverEvent.Action> HOVER_ACTION_TYPE = HoverEvent.Action.class;
    static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
    static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
    static final Class<String> STRING_TYPE = String.class;
    static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE = TextColorWrapper.class;
    static final Class<TextColor> COLOR_TYPE = TextColor.class;
    static final Class<TextDecoration> TEXT_DECORATION_TYPE = TextDecoration.class;
    static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
    static final Class<UUID> UUID_TYPE = UUID.class;
    // packetevents patch start
    static final Class<?> TRANSLATION_ARGUMENT_TYPE;

    static {
        if (BackwardCompatUtil.IS_4_15_0_OR_NEWER) {
            TRANSLATION_ARGUMENT_TYPE = TranslationArgument.class;
        } else {
            TRANSLATION_ARGUMENT_TYPE = null;
        }
    }
    // packetevents patch end

    private final OptionState features;
    private final net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
    // packetevents patch start
    private final BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement;
    // packetevents patch end

    // packetevents patch start
    SerializerFactory(
            final OptionState features,
            final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHoverSerializer,
            final @Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement) {
        this.features = features;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.compatShowAchievement = compatShowAchievement;
    }
    // packetevents patch end

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> rawType = type.getRawType();
        if (COMPONENT_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ComponentSerializerImpl.create(this.features, gson);
        } else if (KEY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) KeySerializer.INSTANCE;
        } else if (STYLE_TYPE.isAssignableFrom(rawType)) {
            // packetevents patch start
            return (TypeAdapter<T>) StyleSerializer.create(this.legacyHoverSerializer, this.compatShowAchievement, this.features, gson);
            // packetevents patch end
        } else if (CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ClickEventActionSerializer.INSTANCE;
        } else if (HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) HoverEventActionSerializer.INSTANCE;
        } else if (SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ShowItemSerializer.create(gson, this.features);
        } else if (SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ShowEntitySerializer.create(gson);
        } else if (COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TextColorWrapper.Serializer.INSTANCE;
        } else if (COLOR_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) (this.features.value(JSONOptions.EMIT_RGB) ? TextColorSerializer.INSTANCE : TextColorSerializer.DOWNSAMPLE_COLOR);
        } else if (TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TextDecorationSerializer.INSTANCE;
        } else if (BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) BlockNBTComponentPosSerializer.INSTANCE;
        }
        // packetevents patch start
        else if (BackwardCompatUtil.IS_4_15_0_OR_NEWER) {
            if (UUID_TYPE.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) UUIDSerializer.uuidSerializer(this.features);
            } else if (TRANSLATION_ARGUMENT_TYPE.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) TranslationArgumentSerializer.create(gson);
            }
        }
        // packetevents patch end

        return null;
    }
}