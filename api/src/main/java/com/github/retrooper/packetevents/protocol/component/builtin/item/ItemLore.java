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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemLore {

    public static final ItemLore EMPTY = new ItemLore(Collections.emptyList());

    private List<Component> lines;

    public ItemLore(List<Component> lines) {
        this.lines = lines;
    }

    public static ItemLore read(PacketWrapper<?> wrapper) {
        List<Component> lines = wrapper.readList(PacketWrapper::readComponent);
        return new ItemLore(lines);
    }

    public static void write(PacketWrapper<?> wrapper, ItemLore lore) {
        wrapper.writeList(lore.lines, PacketWrapper::writeComponent);
    }

    public void addLine(Component line) {
        this.lines.add(line);
    }

    public List<Component> getLines() {
        return this.lines;
    }

    public void setLines(List<Component> lines) {
        this.lines = lines;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemLore)) return false;
        ItemLore itemLore = (ItemLore) obj;
        return this.lines.equals(itemLore.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lines);
    }

    @Override
    public String toString() {
        return "ItemLore{lines=" + this.lines + '}';
    }
}
