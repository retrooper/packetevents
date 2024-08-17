/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
package net.kyori.adventure.text.serializer.gson;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collections;
import java.util.UUID;

public final class BackwardCompatUtil {

    public static final boolean IS_4_10_0_OR_NEWER;
    public static final boolean IS_4_13_0_OR_NEWER;
    public static final boolean IS_4_15_0_OR_NEWER;
    public static final boolean IS_4_17_0_OR_NEWER;

    static {
        boolean is4_10_0OrNewer = false;
        try {
            // some methods were renamed in 4.10.0
            BinaryTagHolder.binaryTagHolder("");
            is4_10_0OrNewer = true;
        } catch (Throwable ignored) {
        }
        IS_4_10_0_OR_NEWER = is4_10_0OrNewer;

        boolean is4_13_0OrNewer = false;
        try {
            // translatable fallback support was added in 4.13.0
            Component.translatable().fallback("");
            is4_13_0OrNewer = true;
        } catch (Throwable ignored) {
        }
        IS_4_13_0_OR_NEWER = is4_13_0OrNewer;

        boolean is4_15_0OrNewer = false;
        try {
            Component.translatable().arguments(Component.empty()); // TranslatableComponent#arguments method was added in 4.15.0
            is4_15_0OrNewer = true;
        } catch (Throwable ignored) {
        }
        IS_4_15_0_OR_NEWER = is4_15_0OrNewer;

        boolean is4_17_0OrNewer = false;
        try {
            // data components support was added in 4.17.0
            HoverEvent.ShowItem.showItem(Key.key("air"), 1, Collections.emptyMap());
            is4_17_0OrNewer = true;
        } catch (Throwable ignored) {
        }
        IS_4_17_0_OR_NEWER = is4_17_0OrNewer;
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
