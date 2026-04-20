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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class CharacterAndFormatImpl implements CharacterAndFormat {
  private final char character;
  private final TextFormat format;
  private final boolean caseInsensitive;

  CharacterAndFormatImpl(final char character, final @NotNull TextFormat format, final boolean caseInsensitive) {
    this.character = character;
    this.format = requireNonNull(format, "format");
    this.caseInsensitive = caseInsensitive;
  }

  @Override
  public char character() {
    return this.character;
  }

  @Override
  public @NotNull TextFormat format() {
    return this.format;
  }

  @Override
  public boolean caseInsensitive() {
    return this.caseInsensitive;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof CharacterAndFormatImpl)) return false;
    final CharacterAndFormatImpl that = (CharacterAndFormatImpl) other;
    return this.character == that.character
      && this.format.equals(that.format)
      && this.caseInsensitive == that.caseInsensitive;
  }

  @Override
  public int hashCode() {
    int result = this.character;
    result = 31 * result + this.format.hashCode();
    result = 31 * result + Boolean.hashCode(this.caseInsensitive);
    return result;
  }

  @Override
  public @NotNull String toString() {
    return Internals.toString(this);
  }

  static final class Defaults {
    static final List<CharacterAndFormat> DEFAULTS = createDefaults();

    private Defaults() {
    }

    @SuppressWarnings("DuplicatedCode")
    static List<CharacterAndFormat> createDefaults() {
      final List<CharacterAndFormat> formats = new ArrayList<>(16 + 5 + 1); // colours + decorations + reset

      formats.add(BLACK);
      formats.add(DARK_BLUE);
      formats.add(DARK_GREEN);
      formats.add(DARK_AQUA);
      formats.add(DARK_RED);
      formats.add(DARK_PURPLE);
      formats.add(GOLD);
      formats.add(GRAY);
      formats.add(DARK_GRAY);
      formats.add(BLUE);
      formats.add(GREEN);
      formats.add(AQUA);
      formats.add(RED);
      formats.add(LIGHT_PURPLE);
      formats.add(YELLOW);
      formats.add(WHITE);

      formats.add(OBFUSCATED);
      formats.add(BOLD);
      formats.add(STRIKETHROUGH);
      formats.add(UNDERLINED);
      formats.add(ITALIC);

      formats.add(RESET);

      return Collections.unmodifiableList(formats);
    }
  }
}
