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

import java.util.List;

@NullMarked
public class SingleOptionInputControl implements InputControl {

    private final int width;
    private final List<Entry> options;
    private final Component label;
    private final boolean labelVisible;

    public SingleOptionInputControl(int width, List<Entry> options, Component label, boolean labelVisible) {
        boolean initial = false;
        for (Entry entry : options) {
            if (entry.initial) {
                if (initial) {
                    throw new IllegalArgumentException("Multiple initial values");
                }
                initial = true;
            }
        }
        this.width = width;
        this.options = options;
        this.label = label;
        this.labelVisible = labelVisible;
    }

    public static SingleOptionInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
        List<Entry> options = compound.getListOrThrow("options", Entry::decode, wrapper);
        Component label = compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
        boolean labelVisible = compound.getBooleanOr("label_visible", true);
        return new SingleOptionInputControl(width, options, label, labelVisible);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, SingleOptionInputControl control) {
        if (control.width != 200) {
            compound.setTag("width", new NBTInt(control.width));
        }
        compound.setList("options", control.options, Entry::encode, wrapper);
        compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
        if (!control.labelVisible) {
            compound.setTag("label_visible", new NBTByte(false));
        }
    }

    @Override
    public InputControlType<?> getType() {
        return InputControlTypes.SINGLE_OPTION;
    }

    public int getWidth() {
        return this.width;
    }

    public List<Entry> getOptions() {
        return this.options;
    }

    public Component getLabel() {
        return this.label;
    }

    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    public static final class Entry {

        private final String id;
        private final @Nullable Component display;
        private final boolean initial;

        public Entry(String id, @Nullable Component display, boolean initial) {
            this.id = id;
            this.display = display;
            this.initial = initial;
        }

        public static Entry decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTString) {
                return new Entry(((NBTString) nbt).getValue(), null, false);
            }
            NBTCompound compound = (NBTCompound) nbt;
            String id = compound.getStringTagValueOrThrow("id");
            Component display = compound.getOrNull("display", AdventureSerializer.serializer(wrapper), wrapper);
            boolean initial = compound.getBooleanOr("initial", false);
            return new Entry(id, display, initial);
        }

        public static NBT encode(PacketWrapper<?> wrapper, Entry entry) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("id", new NBTString(entry.id));
            if (entry.display != null) {
                compound.set("display", entry.display, AdventureSerializer.serializer(wrapper), wrapper);
            }
            if (entry.initial) {
                compound.setTag("initial", new NBTByte(true));
            }
            return compound;
        }

        public String getId() {
            return this.id;
        }

        public @Nullable Component getDisplay() {
            return this.display;
        }

        public boolean isInitial() {
            return this.initial;
        }
    }
}
