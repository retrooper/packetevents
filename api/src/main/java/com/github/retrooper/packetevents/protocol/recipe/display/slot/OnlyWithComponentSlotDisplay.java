/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.recipe.display.slot;

import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 26.1+
 */
@NullMarked
public class OnlyWithComponentSlotDisplay extends SlotDisplay<OnlyWithComponentSlotDisplay> {

    private final SlotDisplay<?> source;
    private final ComponentType<?> component;

    public OnlyWithComponentSlotDisplay(SlotDisplay<?> source, ComponentType<?> component) {
        super(SlotDisplayTypes.ONLY_WITH_COMPONENT);
        this.source = source;
        this.component = component;
    }

    public static OnlyWithComponentSlotDisplay read(PacketWrapper<?> wrapper) {
        SlotDisplay<?> source = SlotDisplay.read(wrapper);
        ComponentType<?> component = wrapper.readMappedEntity(ComponentTypes.getRegistry());
        return new OnlyWithComponentSlotDisplay(source, component);
    }

    public static void write(PacketWrapper<?> wrapper, OnlyWithComponentSlotDisplay display) {
        SlotDisplay.write(wrapper, display.source);
        wrapper.writeMappedEntity(display.component);
    }

    public SlotDisplay<?> getSource() {
        return this.source;
    }

    public ComponentType<?> getComponent() {
        return this.component;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OnlyWithComponentSlotDisplay)) return false;
        OnlyWithComponentSlotDisplay that = (OnlyWithComponentSlotDisplay) obj;
        if (!this.source.equals(that.source)) return false;
        return this.component.equals(that.component);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source, this.component);
    }

    @Override
    public String toString() {
        return "OnlyWithComponentSlotDisplay{source=" + this.source + ", component=" + this.component + '}';
    }
}
