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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class LegacyComponentSerializerImpl implements LegacyComponentSerializer {
  static final Pattern DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/([A-Za-z0-9\\-._~!$&'()*+,;=:@/]|%[0-9A-Fa-f]{2})*)?");
  static final Pattern URL_SCHEME_PATTERN = Pattern.compile("^[a-z][a-z0-9+\\-.]*:");
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  private static final char LEGACY_BUNGEE_HEX_CHAR = 'x';

  private static final Optional<Provider> SERVICE = Services.service(Provider.class);
  static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::legacy)
    .orElseGet(() -> builder -> {
      // NOOP
    });

  // We cannot store these fields in LegacyComponentSerializerImpl directly due to class initialisation issues.
  static final class Instances {
    static final LegacyComponentSerializer SECTION = SERVICE
      .map(Provider::legacySection)
      .orElseGet(() -> new LegacyComponentSerializerImpl(SECTION_CHAR, HEX_CHAR, null, false, false, ComponentFlattener.basic(), CharacterAndFormatSet.DEFAULT));
    static final LegacyComponentSerializer AMPERSAND = SERVICE
      .map(Provider::legacyAmpersand)
      .orElseGet(() -> new LegacyComponentSerializerImpl(AMPERSAND_CHAR, HEX_CHAR, null, false, false, ComponentFlattener.basic(), CharacterAndFormatSet.DEFAULT));
  }

  private final char character;
  private final char hexCharacter;
  private final @Nullable TextReplacementConfig urlReplacementConfig;
  private final boolean hexColours;
  private final boolean useTerriblyStupidHexFormat; // (╯°□°)╯︵ ┻━┻
  private final ComponentFlattener flattener;
  private final CharacterAndFormatSet formats;

  LegacyComponentSerializerImpl(final char character, final char hexCharacter, final @Nullable TextReplacementConfig urlReplacementConfig, final boolean hexColours, final boolean useTerriblyStupidHexFormat, final ComponentFlattener flattener, final CharacterAndFormatSet formats) {
    this.character = character;
    this.hexCharacter = hexCharacter;
    this.urlReplacementConfig = urlReplacementConfig;
    this.hexColours = hexColours;
    this.useTerriblyStupidHexFormat = useTerriblyStupidHexFormat;
    this.flattener = flattener;
    this.formats = formats;
  }

  private @Nullable FormatCodeType determineFormatType(final char legacy, final String input, final int pos) {
    if (pos >= 14) {
      // The BungeeCord RGB color format uses a repeating sequence of RGB values, each character formatted
      // as their own color format string, and to make things interesting, all the colors are also valid
      // Mojang colors. To differentiate this, we do a lookback check for &x (or equivalent) for its position
      // in the string if it is indeed a BungeeCord-style RGB color.
      final int expectedCharacterPosition = pos - 14;
      final int expectedIndicatorPosition = pos - 13;
      if (input.charAt(expectedCharacterPosition) == this.character && input.charAt(expectedIndicatorPosition) == LEGACY_BUNGEE_HEX_CHAR) {
        return FormatCodeType.BUNGEECORD_UNUSUAL_HEX;
      }
    }
    if (legacy == this.hexCharacter && input.length() - pos >= 6) {
      return FormatCodeType.KYORI_HEX;
    } else if (this.formats.characters.indexOf(legacy) != -1) {
      return FormatCodeType.MOJANG_LEGACY;
    }
    return null;
  }

  static @Nullable LegacyFormat legacyFormat(final char character) {
    final int index = CharacterAndFormatSet.DEFAULT.characters.indexOf(character);
    if (index != -1) {
      final TextFormat format = CharacterAndFormatSet.DEFAULT.formats.get(index);
      if (format instanceof NamedTextColor) {
        return new LegacyFormat((NamedTextColor) format);
      } else if (format instanceof TextDecoration) {
        return new LegacyFormat((TextDecoration) format);
      } else if (format instanceof Reset) {
        return LegacyFormat.RESET;
      }
    }
    return null;
  }

  private @Nullable DecodedFormat decodeTextFormat(final char legacy, final String input, final int pos) {
    final FormatCodeType foundFormat = this.determineFormatType(legacy, input, pos);
    if (foundFormat == null) {
      return null;
    }
    if (foundFormat == FormatCodeType.KYORI_HEX) {
      final @Nullable TextColor parsed = tryParseHexColor(input.substring(pos, pos + 6));
      if (parsed != null) {
        return new DecodedFormat(foundFormat, parsed);
      }
    } else if (foundFormat == FormatCodeType.MOJANG_LEGACY) {
      return new DecodedFormat(foundFormat, this.formats.formats.get(this.formats.characters.indexOf(legacy)));
    } else if (foundFormat == FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
      final StringBuilder foundHex = new StringBuilder(6);
      for (int i = pos - 1; i >= pos - 11; i -= 2) {
        foundHex.append(input.charAt(i));
      }
      final @Nullable TextColor parsed = tryParseHexColor(foundHex.reverse().toString());
      if (parsed != null) {
        return new DecodedFormat(foundFormat, parsed);
      }
    }
    return null;
  }

  private static @Nullable TextColor tryParseHexColor(final String hexDigits) {
    try {
      final int color = Integer.parseInt(hexDigits, 16);
      return TextColor.color(color);
    } catch (final NumberFormatException ex) {
      return null;
    }
  }

  private static boolean isHexTextColor(final TextFormat format) {
    return format instanceof TextColor && !(format instanceof NamedTextColor);
  }

  private @Nullable String toLegacyCode(TextFormat format) {
    if (isHexTextColor(format)) {
      final TextColor color = (TextColor) format;
      if (this.hexColours) {
        final String hex = String.format("%06x", color.value());
        if (this.useTerriblyStupidHexFormat) {
          // ah yes, wonderful. A 14 digit long completely unreadable string.
          final StringBuilder legacy = new StringBuilder(String.valueOf(LEGACY_BUNGEE_HEX_CHAR));
          for (int i = 0, length = hex.length(); i < length; i++) {
            legacy.append(this.character).append(hex.charAt(i));
          }
          return legacy.toString();
        } else {
          // this is a bit nicer, hey?
          return this.hexCharacter + hex;
        }
      } else {
        if (!(color instanceof NamedTextColor)) {
          // if we are not using hex colours, then convert the hex colour
          // to the "nearest" possible named/standard text colour
          format = TextColor.nearestColorTo(this.formats.colors, color);
        }
      }
    }
    final int index = this.formats.formats.indexOf(format);
    if (index == -1) {
      // this format was removed from the formats list
      return null;
    }
    return Character.toString(this.formats.characters.charAt(index));
  }

  private TextComponent extractUrl(final TextComponent component) {
    if (this.urlReplacementConfig == null) return component;
    final Component newComponent = component.replaceText(this.urlReplacementConfig);
    if (newComponent instanceof TextComponent) return (TextComponent) newComponent;
    return Component.text().append(newComponent).build();
  }

  @Override
  public @NotNull TextComponent deserialize(final @NotNull String input) {
    int next = input.lastIndexOf(this.character, input.length() - 2);
    if (next == -1) {
      return this.extractUrl(Component.text(input));
    }

    final List<TextComponent> parts = new ArrayList<>();

    TextComponent.Builder current = null;
    boolean reset = false;

    int pos = input.length();
    do {
      final DecodedFormat decoded = this.decodeTextFormat(input.charAt(next + 1), input, next + 2);
      if (decoded != null) {
        final int from = next + (decoded.encodedFormat == FormatCodeType.KYORI_HEX ? 8 : 2);
        if (from != pos) {
          if (current != null) {
            if (reset) {
              parts.add(current.build());
              reset = false;
              current = Component.text();
            } else {
              current = Component.text().append(current.build());
            }
          } else {
            current = Component.text();
          }

          current.content(input.substring(from, pos));
        } else if (current == null) {
          current = Component.text();
        }

        if (!reset) {
          reset = applyFormat(current, decoded.format);
        }
        if (decoded.encodedFormat == FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
          // BungeeCord hex characters are a repeating set of characters, all of which are also valid
          // legacy Mojang chat colors. Subtract the number of characters in the format, and only then
          // skip ahead.
          next -= 12;
        }
        pos = next;
      }

      next = input.lastIndexOf(this.character, next - 1);
    } while (next != -1);

    if (current != null) {
      parts.add(current.build());
    }

    final String remaining = pos > 0 ? input.substring(0, pos) : "";
    if (parts.size() == 1 && remaining.isEmpty()) {
      return this.extractUrl(parts.get(0));
    } else {
      Collections.reverse(parts);
      return this.extractUrl(Component.text().content(remaining).append(parts).build());
    }
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    final Cereal state = new Cereal();
    this.flattener.flatten(component, state);
    return state.toString();
  }

  private static boolean applyFormat(final TextComponent.@NotNull Builder builder, final @NotNull TextFormat format) {
    if (format instanceof TextColor) {
      builder.colorIfAbsent((TextColor) format);
      return true;
    } else if (format instanceof TextDecoration) {
      builder.decoration((TextDecoration) format, TextDecoration.State.TRUE);
      return false;
    } else if (format instanceof Reset) {
      return true;
    }
    throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  // Are you hungry?
  private final class Cereal implements FlattenerListener {
    private final StringBuilder sb = new StringBuilder();
    private final StyleState style = new StyleState();
    private @Nullable TextFormat lastWritten;
    private StyleState[] styles = new StyleState[8];
    private int head = -1;

    @Override
    public void pushStyle(final @NotNull Style pushed) {
      final int idx = ++this.head;
      if (idx >= this.styles.length) {
        this.styles = Arrays.copyOf(this.styles, this.styles.length * 2);
      }
      StyleState state = this.styles[idx];

      if (state == null) {
        this.styles[idx] = state = new StyleState();
      }

      if (idx > 0) {
        // https://github.com/KyoriPowered/adventure/issues/287
        // https://github.com/KyoriPowered/adventure/pull/299
        state.set(this.styles[idx - 1]);
      } else {
        state.clear();
      }

      state.apply(pushed);
    }

    @Override
    public void component(final @NotNull String text) {
      if (!text.isEmpty()) {
        if (this.head < 0) throw new IllegalStateException("No style has been pushed!");

        this.styles[this.head].applyFormat();
        this.sb.append(text);
      }
    }

    @Override
    public void popStyle(final @NotNull Style style) {
      if (this.head-- < 0) {
        throw new IllegalStateException("Tried to pop beyond what was pushed!");
      }
    }

    void append(final @NotNull TextFormat format) {
      if (this.lastWritten != format) {
        final String legacyCode = LegacyComponentSerializerImpl.this.toLegacyCode(format);
        if (legacyCode == null) {
          return;
        }
        this.sb.append(LegacyComponentSerializerImpl.this.character).append(legacyCode);
      }
      this.lastWritten = format;
    }

    @Override
    public String toString() {
      return this.sb.toString();
    }

    private final class StyleState {
      private @Nullable TextColor color;
      private final Set<TextDecoration> decorations;
      private boolean needsReset;

      StyleState() {
        this.decorations = EnumSet.noneOf(TextDecoration.class);
      }

      void set(final @NotNull StyleState that) {
        this.color = that.color;
        this.decorations.clear();
        this.decorations.addAll(that.decorations);
      }

      public void clear() {
        this.color = null;
        this.decorations.clear();
      }

      void apply(final @NotNull Style component) {
        final TextColor color = component.color();
        if (color != null) {
          this.color = color;
        }

        for (int i = 0, length = DECORATIONS.length; i < length; i++) {
          final TextDecoration decoration = DECORATIONS[i];
          switch (component.decoration(decoration)) {
            case TRUE:
              this.decorations.add(decoration);
              break;
            case FALSE:
              if (this.decorations.remove(decoration)) {
                this.needsReset = true;
              }
              break;
            default: break; // ignored
          }
        }
      }

      void applyFormat() {
        final boolean colorChanged = this.color != Cereal.this.style.color;
        if (this.needsReset) {
          if (!colorChanged) {
            Cereal.this.append(Reset.INSTANCE);
          }
          this.needsReset = false;
        }

        // If color changes, we need to do a full reset.
        // Additionally, if the last thing to be appended was a reset then we need to re-apply everything.
        if (colorChanged || Cereal.this.lastWritten == Reset.INSTANCE) {
          this.applyFullFormat();
          return;
        }

        // Does current have any decorations we don't have?
        // Since there is no way to undo decorations, we need to reset these cases
        if (!this.decorations.containsAll(Cereal.this.style.decorations)) {
          this.applyFullFormat();
          return;
        }

        // Apply new decorations
        for (final TextDecoration decoration : this.decorations) {
          if (Cereal.this.style.decorations.add(decoration)) {
            Cereal.this.append(decoration);
          }
        }
      }

      private void applyFullFormat() {
        if (this.color != null) {
          Cereal.this.append(this.color);
        } else {
          Cereal.this.append(Reset.INSTANCE);
        }
        Cereal.this.style.color = this.color;

        for (final TextDecoration decoration : this.decorations) {
          Cereal.this.append(decoration);
        }

        Cereal.this.style.decorations.clear();
        Cereal.this.style.decorations.addAll(this.decorations);
      }
    }
  }

  static final class BuilderImpl implements Builder {
    private char character = LegacyComponentSerializer.SECTION_CHAR;
    private char hexCharacter = LegacyComponentSerializer.HEX_CHAR;
    private TextReplacementConfig urlReplacementConfig = null;
    private boolean hexColours = false;
    private boolean useTerriblyStupidHexFormat = false;
    private ComponentFlattener flattener = ComponentFlattener.basic();
    private CharacterAndFormatSet formats = CharacterAndFormatSet.DEFAULT;

    BuilderImpl() {
      BUILDER.accept(this); // let service provider touch the builder before anybody else touches it
    }

    BuilderImpl(final @NotNull LegacyComponentSerializerImpl serializer) {
      this();
      this.character = serializer.character;
      this.hexCharacter = serializer.hexCharacter;
      this.urlReplacementConfig = serializer.urlReplacementConfig;
      this.hexColours = serializer.hexColours;
      this.useTerriblyStupidHexFormat = serializer.useTerriblyStupidHexFormat;
      this.flattener = serializer.flattener;
      this.formats = serializer.formats;
    }

    @Override
    public @NotNull Builder character(final char legacyCharacter) {
      this.character = legacyCharacter;
      return this;
    }

    @Override
    public @NotNull Builder hexCharacter(final char legacyHexCharacter) {
      this.hexCharacter = legacyHexCharacter;
      return this;
    }

    @Override
    public @NotNull Builder extractUrls() {
      return this.extractUrls(DEFAULT_URL_PATTERN, null);
    }

    @Override
    public @NotNull Builder extractUrls(final @NotNull Pattern pattern) {
      return this.extractUrls(pattern, null);
    }

    @Override
    public @NotNull Builder extractUrls(final @Nullable Style style) {
      return this.extractUrls(DEFAULT_URL_PATTERN, style);
    }

    @Override
    public @NotNull Builder extractUrls(final @NotNull Pattern pattern, final @Nullable Style style) {
      requireNonNull(pattern, "pattern");
      this.urlReplacementConfig = TextReplacementConfig.builder()
        .match(pattern)
        .replacement(url -> {
          String clickUrl = url.content();
          if (!URL_SCHEME_PATTERN.matcher(clickUrl).find()) {
            clickUrl = "http://" + clickUrl;
          }

          try {
            final URI ignored = new URI(clickUrl); // just to validate that the uri is valid
            return (style == null ? url : url.style(style)).clickEvent(ClickEvent.openUrl(clickUrl));
          } catch (final URISyntaxException ignored) {
            return url;
          }
        })
        .build();
      return this;
    }

    @Override
    public @NotNull Builder hexColors() {
      this.hexColours = true;
      return this;
    }

    @Override
    public @NotNull Builder useUnusualXRepeatedCharacterHexFormat() {
      this.useTerriblyStupidHexFormat = true; // :(
      return this;
    }

    @Override
    public @NotNull Builder flattener(final @NotNull ComponentFlattener flattener) {
      this.flattener = requireNonNull(flattener, "flattener");
      return this;
    }

    @Override
    public @NotNull Builder formats(final @NotNull List<CharacterAndFormat> formats) {
      this.formats = CharacterAndFormatSet.of(formats);
      return this;
    }

    @Override
    public @NotNull LegacyComponentSerializer build() {
      return new LegacyComponentSerializerImpl(this.character, this.hexCharacter, this.urlReplacementConfig, this.hexColours, this.useTerriblyStupidHexFormat, this.flattener, this.formats);
    }
  }

  enum FormatCodeType {
    MOJANG_LEGACY,
    KYORI_HEX,
    BUNGEECORD_UNUSUAL_HEX;
  }

  static final class DecodedFormat {
    final FormatCodeType encodedFormat;
    final TextFormat format;

    private DecodedFormat(final FormatCodeType encodedFormat, final TextFormat format) {
      if (format == null) {
        throw new IllegalStateException("No format found");
      }
      this.encodedFormat = encodedFormat;
      this.format = format;
    }
  }
}
