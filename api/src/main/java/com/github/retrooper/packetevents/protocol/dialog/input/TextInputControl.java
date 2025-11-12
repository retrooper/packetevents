/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.dialog.input;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class TextInputControl implements InputControl {

    private final int width;
    private final Component label;
    private final boolean labelVisible;
    private final String initial;
    private final int maxLength;
    private final @Nullable MultilineOptions multiline;

    public TextInputControl(int width, Component label, boolean labelVisible, String initial, int maxLength, @Nullable MultilineOptions multiline) {
        if (initial.length() > maxLength) {
            throw new IllegalArgumentException("Default text length exceeds allowed size");
        }
        this.width = width;
        this.label = label;
        this.labelVisible = labelVisible;
        this.initial = initial;
        this.maxLength = maxLength;
        this.multiline = multiline;
    }

    public static TextInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
        Component label = compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
        boolean labelVisible = compound.getBooleanOr("label_visible", true);
        String initial = compound.getStringTagValueOrDefault("initial", "");
        int maxLength = compound.getNumberTagValueOrDefault("max_length", 32).intValue();
        MultilineOptions multiline = compound.getOrNull("multiline", MultilineOptions::decode, wrapper);
        return new TextInputControl(width, label, labelVisible, initial, maxLength, multiline);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, TextInputControl control) {
        if (control.width != 200) {
            compound.setTag("width", new NBTInt(control.width));
        }
        compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
        if (!control.labelVisible) {
            compound.setTag("label_visible", new NBTByte(false));
        }
        if (!control.initial.isEmpty()) {
            compound.setTag("initial", new NBTString(control.initial));
        }
        if (control.maxLength != 32) {
            compound.setTag("max_length", new NBTInt(control.maxLength));
        }
        if (control.multiline != null) {
            compound.set("multiline", control.multiline, MultilineOptions::encode, wrapper);
        }
    }

    @Override
    public InputControlType<?> getType() {
        return InputControlTypes.TEXT;
    }

    public int getWidth() {
        return this.width;
    }

    public Component getLabel() {
        return this.label;
    }

    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    public String getInitial() {
        return this.initial;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public @Nullable MultilineOptions getMultiline() {
        return this.multiline;
    }

    public static final class MultilineOptions {

        private final @Nullable Integer maxLines;
        private final @Nullable Integer height;

        public MultilineOptions(@Nullable Integer maxLines, @Nullable Integer height) {
            this.maxLines = maxLines;
            this.height = height;
        }

        public static MultilineOptions decode(NBT nbt, PacketWrapper<?> wrapper) {
            NBTCompound compound = (NBTCompound) nbt;
            Number maxLines = compound.getNumberTagValueOrNull("max_lines");
            Number height = compound.getNumberTagValueOrNull("height");
            return new MultilineOptions(
                    maxLines != null ? maxLines.intValue() : null,
                    height != null ? height.intValue() : null
            );
        }

        public static NBT encode(PacketWrapper<?> wrapper, MultilineOptions options) {
            NBTCompound compound = new NBTCompound();
            if (options.maxLines != null) {
                compound.setTag("max_lines", new NBTInt(options.maxLines));
            }
            if (options.height != null) {
                compound.setTag("height", new NBTInt(options.height));
            }
            return compound;
        }

        public @Nullable Integer getMaxLines() {
            return this.maxLines;
        }

        public @Nullable Integer getHeight() {
            return this.height;
        }
    }
}
