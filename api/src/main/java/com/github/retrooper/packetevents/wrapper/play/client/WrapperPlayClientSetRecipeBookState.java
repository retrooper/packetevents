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
import com.github.retrooper.packetevents.protocol.item.book.BookType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSetRecipeBookState extends PacketWrapper<WrapperPlayClientSetRecipeBookState> {
    private BookType bookType;
    private boolean bookOpen;
    private boolean filterActive;

    public WrapperPlayClientSetRecipeBookState(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSetRecipeBookState(BookType bookType, boolean bookOpen, boolean filterActive) {
        super(PacketType.Play.Client.SET_RECIPE_BOOK_STATE);
        this.bookType = bookType;
        this.bookOpen = bookOpen;
        this.filterActive = filterActive;
    }

    @Override
    public void read() {
        this.bookType = BookType.getById(readVarInt());
        this.bookOpen = readBoolean();
        this.filterActive = readBoolean();
    }

    @Override
    public void write() {
        writeVarInt(this.bookType.getId());
        writeBoolean(this.bookOpen);
        writeBoolean(this.filterActive);
    }

    @Override
    public void copy(WrapperPlayClientSetRecipeBookState wrapper) {
        this.bookType = wrapper.bookType;
        this.bookOpen = wrapper.bookOpen;
        this.filterActive = wrapper.filterActive;
    }

    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    public boolean isBookOpen() {
        return bookOpen;
    }

    public void setBookOpen(boolean bookOpen) {
        this.bookOpen = bookOpen;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
    }
}
