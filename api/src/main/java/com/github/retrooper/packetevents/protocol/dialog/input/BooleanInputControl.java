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

import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BooleanInputControl implements InputControl {

    private final Component label;
    private final boolean initial;
    private final String onTrue;
    private final String onFalse;

    public BooleanInputControl(Component label, boolean initial, String onTrue, String onFalse) {
        this.label = label;
        this.initial = initial;
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    public static BooleanInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        Component label = compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
        boolean initial = compound.getBoolean("initial");
        String onTrue = compound.getStringTagValueOrDefault("on_true", "true");
        String onFalse = compound.getStringTagValueOrDefault("on_false", "false");
        return new BooleanInputControl(label, initial, onTrue, onFalse);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, BooleanInputControl control) {
        compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
        if (control.initial) {
            compound.setTag("initial", new NBTByte(true));
        }
        if (!"true".equals(control.onTrue)) {
            compound.setTag("on_true", new NBTString(control.onTrue));
        }
        if (!"false".equals(control.onFalse)) {
            compound.setTag("on_false", new NBTString(control.onFalse));
        }
    }

    @Override
    public InputControlType<?> getType() {
        return InputControlTypes.BOOLEAN;
    }

    public Component getLabel() {
        return this.label;
    }

    public boolean isInitial() {
        return this.initial;
    }

    public String getOnTrue() {
        return this.onTrue;
    }

    public String getOnFalse() {
        return this.onFalse;
    }
}
