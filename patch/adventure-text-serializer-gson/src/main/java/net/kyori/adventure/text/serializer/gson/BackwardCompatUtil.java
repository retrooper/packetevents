package net.kyori.adventure.text.serializer.gson;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.UUID;

public final class BackwardCompatUtil {

    static boolean IS_4_15_0_OR_NEWER = false;

    static {
        try {
            TranslationArgument.class.getName();
            IS_4_15_0_OR_NEWER = true;
        } catch (Throwable ignored) {
        }
    }

    private BackwardCompatUtil() {
    }

    public interface ShowAchievementToComponent {

        @NotNull
        Component convert(@NotNull String input);

    }

    public static HoverEvent.ShowItem createShowItem(final @NotNull Key item, final @Range(from = 0, to = Integer.MAX_VALUE) int count, final @Nullable BinaryTagHolder nbt) {
        try {
            return HoverEvent.ShowItem.showItem(item, count, nbt);
        } catch (final NoSuchMethodError ignored) {
            return HoverEvent.ShowItem.of(item, count, nbt);
        }
    }

    public static HoverEvent.ShowEntity createShowEntity(final @NotNull Key type, final @NotNull UUID id, final @Nullable Component name) {
        try {
            return HoverEvent.ShowEntity.showEntity(type, id, name);
        } catch (final NoSuchMethodError ignored) {
            return HoverEvent.ShowEntity.of(type, id, name);
        }
    }

    public static <D, E, DX extends Throwable, EX extends Throwable> @NotNull Codec<D, E, DX, EX> createCodec(final @NotNull Codec.Decoder<D, E, DX> decoder, final @NotNull Codec.Encoder<D, E, EX> encoder) {
        try {
            return Codec.codec(decoder, encoder);
        } catch (final NoSuchMethodError ignored) {
            return Codec.of(decoder, encoder);
        }
    }

}
