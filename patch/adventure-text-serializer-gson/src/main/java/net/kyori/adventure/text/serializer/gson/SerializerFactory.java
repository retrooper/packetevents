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
        if (Component.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ComponentSerializerImpl.create(this.features, gson);
        } else if (Key.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) KeySerializer.INSTANCE;
        } else if (Style.class.isAssignableFrom(rawType)) {
            // packetevents patch start
            return (TypeAdapter<T>) StyleSerializer.create(this.legacyHoverSerializer, this.compatShowAchievement, this.features, gson);
            // packetevents patch end
        } else if (ClickEvent.Action.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ClickEventActionSerializer.INSTANCE;
        } else if (HoverEvent.Action.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) HoverEventActionSerializer.INSTANCE;
        } else if (HoverEvent.ShowItem.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ShowItemSerializer.create(gson);
        } else if (HoverEvent.ShowEntity.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ShowEntitySerializer.create(gson);
        } else if (TextColorWrapper.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TextColorWrapper.Serializer.INSTANCE;
        } else if (TextColor.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) (this.features.value(JSONOptions.EMIT_RGB) ? TextColorSerializer.INSTANCE : TextColorSerializer.DOWNSAMPLE_COLOR);
        } else if (TextDecoration.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TextDecorationSerializer.INSTANCE;
        } else if (BlockNBTComponent.Pos.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) BlockNBTComponentPosSerializer.INSTANCE;
        }
        // packetevents patch start
        else if (BackwardCompatUtil.IS_4_15_0_OR_NEWER) {
            if (UUID.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) UUIDSerializer.uuidSerializer(this.features);
            } else if (TranslationArgument.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) TranslationArgumentSerializer.create(gson);
            }
        }
        // packetevents patch end

        return null;
    }
}