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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.util.Filterable;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.List;

public class WrittenBookContent {

    private final Filterable<String> title;
    private final String author;
    private final int generation;
    private final List<Filterable<Component>> pages;
    private final boolean resolved;

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
}
