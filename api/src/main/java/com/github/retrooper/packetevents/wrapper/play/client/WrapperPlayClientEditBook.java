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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WrapperPlayClientEditBook extends PacketWrapper<WrapperPlayClientEditBook> {
    private int slot;
    private List<String> pages;
    private @Nullable String title;

    public WrapperPlayClientEditBook(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientEditBook(int slot, List<String> pages, @Nullable String title) {
        super(PacketType.Play.Client.EDIT_BOOK);
        this.slot = slot;
        this.pages = pages;
        this.title = title;
    }

    @Override
    public void read() {
        slot = readVarInt();

        int size = readVarInt();
        for (int i = 0; i < size; i++) {
            pages.add(readString());
        }

        boolean hasTitle = readBoolean();
        if (hasTitle) {
            title = readString();
        } else {
            title = null;
        }
    }

    @Override
    public void copy(WrapperPlayClientEditBook wrapper) {
        this.slot = wrapper.slot;
        this.pages = wrapper.pages;
        this.title = wrapper.title;
    }

    @Override
    public void write() {
        writeVarInt(slot);
        writeVarInt(pages.size());
        for (String page : pages) {
            writeString(page);
        }
        writeBoolean(title != null);
        if (title != null) {
            writeString(title);
        }
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public @Nullable String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }
}
