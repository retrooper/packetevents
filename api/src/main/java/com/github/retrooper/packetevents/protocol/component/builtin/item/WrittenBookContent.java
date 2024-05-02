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
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class WrittenBookContent {

    private Filterable<String> title;
    private String author;
    private int generation;
    private List<Filterable<Component>> pages;
    private boolean resolved;

    public WrittenBookContent(
            Filterable<String> title, String author, int generation,
            List<Filterable<Component>> pages, boolean resolved
    ) {
        this.title = title;
        this.author = author;
        this.generation = generation;
        this.pages = pages;
        this.resolved = resolved;
    }

    public static WrittenBookContent read(PacketWrapper<?> wrapper) {
        Filterable<String> title = Filterable.read(wrapper, ew -> ew.readString(32));
        String author = wrapper.readString();
        int generation = wrapper.readVarInt();
        List<Filterable<Component>> pages = wrapper.readList(ew -> Filterable.read(
                ew, PacketWrapper::readComponent));
        boolean resolved = wrapper.readBoolean();
        return new WrittenBookContent(title, author, generation, pages, resolved);
    }

    public static void write(PacketWrapper<?> wrapper, WrittenBookContent content) {
        Filterable.write(wrapper, content.title, (ew, text) -> ew.writeString(text, 32));
        wrapper.writeString(content.author);
        wrapper.writeVarInt(content.generation);
        wrapper.writeList(content.pages, (ew, page) -> Filterable.write(
                ew, page, PacketWrapper::writeComponent));
        wrapper.writeBoolean(content.resolved);
    }

    public Filterable<String> getTitle() {
        return this.title;
    }

    public void setTitle(Filterable<String> title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getGeneration() {
        return this.generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public @Nullable Filterable<Component> getPage(int index) {
        return index >= 0 && index < this.pages.size() ? this.pages.get(index) : null;
    }

    public void addPage(Filterable<Component> page) {
        this.pages.add(page);
    }

    public List<Filterable<Component>> getPages() {
        return this.pages;
    }

    public void setPages(List<Filterable<Component>> pages) {
        this.pages = pages;
    }

    public boolean isResolved() {
        return this.resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WrittenBookContent)) return false;
        WrittenBookContent that = (WrittenBookContent) obj;
        if (this.generation != that.generation) return false;
        if (this.resolved != that.resolved) return false;
        if (!this.title.equals(that.title)) return false;
        if (!this.author.equals(that.author)) return false;
        return this.pages.equals(that.pages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.author, this.generation, this.pages, this.resolved);
    }
}
