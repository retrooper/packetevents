/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.util.adventure;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class TextColorWrapper {

    final @Nullable TextColor color;
    final @Nullable TextDecoration decoration;
    final boolean reset;

    TextColorWrapper(final @Nullable TextColor color, final @Nullable TextDecoration decoration, final boolean reset) {
        this.color = color;
        this.decoration = decoration;
        this.reset = reset;
    }

    static final class Serializer extends TypeAdapter<TextColorWrapper> {
        static final TextColorWrapper.Serializer INSTANCE = new TextColorWrapper.Serializer();

        private Serializer() {
        }

        @Override
        public void write(final JsonWriter out, final TextColorWrapper value) {
            throw new JsonSyntaxException("Cannot write TextColorWrapper instances");
        }

        @Override
        public TextColorWrapper read(final JsonReader in) throws IOException {
            final String input = in.nextString();
            final TextColor color = this.colorFromString(input);
            final TextDecoration decoration = TextDecoration.NAMES.value(input);
            final boolean reset = decoration == null && input.equals("reset");
            if (color == null && decoration == null && !reset) {
                throw new JsonParseException("Don't know how to parse " + input + " at " + in.getPath());
            }
            return new TextColorWrapper(color, decoration, reset);
        }

        TextColor colorFromString(final String value) {
            if (value.startsWith("#")) {
                return TextColor.fromHexString(value);
            } else {
                return NamedTextColor.NAMES.value(value);
            }
        }
    }

}
