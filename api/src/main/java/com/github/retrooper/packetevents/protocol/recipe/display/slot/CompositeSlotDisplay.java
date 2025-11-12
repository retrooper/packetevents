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

package com.github.retrooper.packetevents.protocol.recipe.display.slot;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class CompositeSlotDisplay extends SlotDisplay<CompositeSlotDisplay> {

    private List<SlotDisplay<?>> contents;

    public CompositeSlotDisplay(List<SlotDisplay<?>> contents) {
        super(SlotDisplayTypes.COMPOSITE);
        this.contents = contents;
    }

    public static CompositeSlotDisplay read(PacketWrapper<?> wrapper) {
        List<SlotDisplay<?>> contents = wrapper.readList(SlotDisplay::read);
        return new CompositeSlotDisplay(contents);
    }

    public static void write(PacketWrapper<?> wrapper, CompositeSlotDisplay display) {
        wrapper.writeList(display.contents, SlotDisplay::write);
    }

    public List<SlotDisplay<?>> getContents() {
        return this.contents;
    }

    public void setContents(List<SlotDisplay<?>> contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CompositeSlotDisplay)) return false;
        CompositeSlotDisplay that = (CompositeSlotDisplay) obj;
        return this.contents.equals(that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.contents);
    }

    @Override
    public String toString() {
        return "CompositeSlotDisplay{contents=" + this.contents + '}';
    }
}
