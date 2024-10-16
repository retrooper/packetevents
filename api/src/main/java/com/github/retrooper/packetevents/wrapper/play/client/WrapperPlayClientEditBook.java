/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayClientEditBook extends PacketWrapper<WrapperPlayClientEditBook> {

    @Deprecated
    public static final int MAX_BYTES_PER_CHAR = 4;

    private static final int TITLE_MAX_CHARS_LEGACY = 128;
    private static final int TITLE_MAX_CHARS = 32;
    private static final int PAGE_MAX_CHARS_LEGACY = 8192;
    private static final int PAGE_MAX_CHARS = 1024;
    private static final int MAX_PAGES_LEGACY = 200;
    private static final int MAX_PAGES = 100;

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
        boolean modernLimits = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2);
        int pageLimit = modernLimits ? MAX_PAGES : MAX_PAGES_LEGACY;
        int pageCharLimit = modernLimits ? PAGE_MAX_CHARS : PAGE_MAX_CHARS_LEGACY;

        this.slot = this.readVarInt();
        int pageCount = this.readVarInt();
        if (pageCount > pageLimit) {
            throw new IllegalStateException("Page count " + pageCount + " is larger than limit of " + pageLimit);
        }
        this.pages = new ArrayList<>(pageCount);
        for (int i = 0; i < pageCount; i++) {
            this.pages.add(this.readString(pageCharLimit));
        }
        this.title = this.readOptional(reader -> {
            int titleLimit = modernLimits ? TITLE_MAX_CHARS : TITLE_MAX_CHARS_LEGACY;
            return reader.readString(titleLimit);
        });
    }

    @Override
    public void write() {
        boolean modernLimits = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2);
        int pageCharLimit = modernLimits ? PAGE_MAX_CHARS : PAGE_MAX_CHARS_LEGACY;

        this.writeVarInt(this.slot);
        this.writeVarInt(this.pages.size());
        for (String page : this.pages) {
            this.writeString(page, pageCharLimit);
        }
        this.writeOptional(this.title, (writer, innerTitle) -> {
            int titleLimit = modernLimits ? TITLE_MAX_CHARS : TITLE_MAX_CHARS_LEGACY;
            writer.writeString(innerTitle, titleLimit);
        });
    }

    @Override
    public void copy(WrapperPlayClientEditBook wrapper) {
        this.slot = wrapper.slot;
        this.pages = wrapper.pages;
        this.title = wrapper.title;
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
