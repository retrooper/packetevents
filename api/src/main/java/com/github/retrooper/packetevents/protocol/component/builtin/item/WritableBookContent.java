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

import com.github.retrooper.packetevents.util.Filterable;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class WritableBookContent {

    private List<Filterable<String>> pages;

    public WritableBookContent(List<Filterable<String>> pages) {
        this.pages = pages;
    }

    public static WritableBookContent read(PacketWrapper<?> wrapper) {
        List<Filterable<String>> pages = wrapper.readList(ew -> Filterable.read(
                ew, eew -> eew.readString(1024)));
        return new WritableBookContent(pages);
    }

    public static void write(PacketWrapper<?> wrapper, WritableBookContent content) {
        wrapper.writeList(content.pages, (ew, page) -> Filterable.write(
                ew, page, (eew, text) -> eew.writeString(text, 1024)));
    }

    public @Nullable Filterable<String> getPage(int index) {
        return index >= 0 && index < this.pages.size() ? this.pages.get(index) : null;
    }

    public void addPage(Filterable<String> page) {
        this.pages.add(page);
    }

    public List<Filterable<String>> getPages() {
        return this.pages;
    }

    public void setPages(List<Filterable<String>> pages) {
        this.pages = pages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WritableBookContent)) return false;
        WritableBookContent that = (WritableBookContent) obj;
        return this.pages.equals(that.pages);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.pages);
    }
}
