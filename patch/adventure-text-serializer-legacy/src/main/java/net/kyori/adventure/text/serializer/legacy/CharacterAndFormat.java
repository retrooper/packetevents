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
package net.kyori.adventure.text.serializer.legacy;

import java.util.List;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A combination of a {@code character}, a {@link TextFormat}, and if the character is {@link #caseInsensitive()}.
 *
 * @since 4.14.0
 */
@ApiStatus.NonExtendable
public interface CharacterAndFormat extends Examinable {
    /**
     * Character and format pair representing {@link NamedTextColor#BLACK}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat BLACK = characterAndFormat('0', NamedTextColor.BLACK, true);
    /**
     * Character and format pair representing {@link NamedTextColor#DARK_BLUE}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat DARK_BLUE = characterAndFormat('1', NamedTextColor.DARK_BLUE, true);
    /**
     * Character and format pair representing {@link NamedTextColor#DARK_GREEN}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat DARK_GREEN = characterAndFormat('2', NamedTextColor.DARK_GREEN, true);
    /**
     * Character and format pair representing {@link NamedTextColor#DARK_AQUA}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat DARK_AQUA = characterAndFormat('3', NamedTextColor.DARK_AQUA, true);
    /**
     * Character and format pair representing {@link NamedTextColor#DARK_RED}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat DARK_RED = characterAndFormat('4', NamedTextColor.DARK_RED, true);
    /**
     * Character and format pair representing {@link NamedTextColor#DARK_PURPLE}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat DARK_PURPLE = characterAndFormat('5', NamedTextColor.DARK_PURPLE, true);
    /**
     * Character and format pair representing {@link NamedTextColor#GOLD}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat GOLD = characterAndFormat('6', NamedTextColor.GOLD, true);
    /**
     * Character and format pair representing {@link NamedTextColor#GRAY}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat GRAY = characterAndFormat('7', NamedTextColor.GRAY, true);
    /**
     * Character and format pair representing {@link NamedTextColor#DARK_GRAY}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat DARK_GRAY = characterAndFormat('8', NamedTextColor.DARK_GRAY, true);
    /**
     * Character and format pair representing {@link NamedTextColor#BLUE}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat BLUE = characterAndFormat('9', NamedTextColor.BLUE, true);
    /**
     * Character and format pair representing {@link NamedTextColor#GREEN}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat GREEN = characterAndFormat('a', NamedTextColor.GREEN, true);
    /**
     * Character and format pair representing {@link NamedTextColor#AQUA}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat AQUA = characterAndFormat('b', NamedTextColor.AQUA, true);
    /**
     * Character and format pair representing {@link NamedTextColor#RED}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat RED = characterAndFormat('c', NamedTextColor.RED, true);
    /**
     * Character and format pair representing {@link NamedTextColor#LIGHT_PURPLE}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat LIGHT_PURPLE = characterAndFormat('d', NamedTextColor.LIGHT_PURPLE, true);
    /**
     * Character and format pair representing {@link NamedTextColor#YELLOW}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat YELLOW = characterAndFormat('e', NamedTextColor.YELLOW, true);
    /**
     * Character and format pair representing {@link NamedTextColor#WHITE}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat WHITE = characterAndFormat('f', NamedTextColor.WHITE, true);

    /**
     * Character and format pair representing {@link TextDecoration#OBFUSCATED}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat OBFUSCATED = characterAndFormat('k', TextDecoration.OBFUSCATED, true);
    /**
     * Character and format pair representing {@link TextDecoration#BOLD}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat BOLD = characterAndFormat('l', TextDecoration.BOLD, true);
    /**
     * Character and format pair representing {@link TextDecoration#STRIKETHROUGH}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat STRIKETHROUGH = characterAndFormat('m', TextDecoration.STRIKETHROUGH, true);
    /**
     * Character and format pair representing {@link TextDecoration#UNDERLINED}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat UNDERLINED = characterAndFormat('n', TextDecoration.UNDERLINED, true);
    /**
     * Character and format pair representing {@link TextDecoration#ITALIC}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat ITALIC = characterAndFormat('o', TextDecoration.ITALIC, true);

    /**
     * Character and format pair representing {@link Reset#INSTANCE}.
     *
     * @since 4.14.0
     */
    CharacterAndFormat RESET = characterAndFormat('r', true); // packetevents patch

    /**
     * Creates a new combination of a case-sensitive {@code character} and a {@link TextFormat}.
     *
     * @param character the character
     * @param format the format
     * @return a new character and format instance.
     * @since 4.14.0
     */
    static @NotNull CharacterAndFormat characterAndFormat(final char character, final @NotNull TextFormat format) {
        return characterAndFormat(character, format, false);
    }

    /**
     * Creates a new combination of a {@code character} and a {@link TextFormat}.
     *
     * @param character the character
     * @param format the format
     * @param caseInsensitive if the character is case-insensitive
     * @return a new character and format instance.
     * @since 4.17.0
     */
    static @NotNull CharacterAndFormat characterAndFormat(final char character, final @NotNull TextFormat format, final boolean caseInsensitive) {
        return new CharacterAndFormatImpl(character, format, caseInsensitive);
    }

    // packetevents patch start
    /**
     * Creates a new combination of a {@code character} and a reset.
     *
     * @param character the character
     * @param caseInsensitive if the character is case-insensitive
     * @return a new character and format instance.
     * @since 4.17.0
     */
    static @NotNull CharacterAndFormat characterAndFormat(final char character, final boolean caseInsensitive) {
        return new CharacterAndFormatImpl(character, null, caseInsensitive);
    }
    // packetevents patch end

    /**
     * Gets an unmodifiable list of character and format instances containing all default vanilla formats.
     *
     * @return an unmodifiable list of character and format instances containing all default vanilla formats
     * @since 4.14.0
     */
    @Unmodifiable
    static @NotNull List<CharacterAndFormat> defaults() {
        return CharacterAndFormatImpl.Defaults.DEFAULTS;
    }

    /**
     * Gets the character.
     *
     * @return the character
     * @since 4.14.0
     */
    char character();

    /**
     * Gets the format.
     *
     * @return the format
     * @since 4.14.0
     */
    @Nullable TextFormat format(); // packetevents patch

    /**
     * If the {@link #character()} is case-insensitive.
     *
     * @return if the character is case-insensitive
     * @since 4.17.0
     */
    boolean caseInsensitive();

    @Override
    default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("character", this.character()),
                ExaminableProperty.of("format", this.format()),
                ExaminableProperty.of("caseInsensitive", this.caseInsensitive())
        );
    }
}
