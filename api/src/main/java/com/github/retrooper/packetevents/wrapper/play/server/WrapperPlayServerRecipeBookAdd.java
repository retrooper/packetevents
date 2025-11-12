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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayEntry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class WrapperPlayServerRecipeBookAdd extends PacketWrapper<WrapperPlayServerRecipeBookAdd> {

    private List<AddEntry> entries;
    private boolean replace;

    public WrapperPlayServerRecipeBookAdd(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRecipeBookAdd(List<AddEntry> entries, boolean replace) {
        super(PacketType.Play.Server.RECIPE_BOOK_ADD);
        this.entries = entries;
        this.replace = replace;
    }

    @Override
    public void read() {
        this.entries = this.readList(AddEntry::read);
        this.replace = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeList(this.entries, AddEntry::write);
        this.writeBoolean(this.replace);
    }

    @Override
    public void copy(WrapperPlayServerRecipeBookAdd wrapper) {
        this.entries = wrapper.entries;
        this.replace = wrapper.replace;
    }

    public List<AddEntry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<AddEntry> entries) {
        this.entries = entries;
    }

    public boolean isReplace() {
        return this.replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public static final class AddEntry {

        private static final byte FLAG_NOTIFICATION = 0b01;
        private static final byte FLAG_HIGHLIGHT = 0b10;

        private RecipeDisplayEntry contents;
        private boolean notification;
        private boolean highlight;

        public AddEntry(RecipeDisplayEntry contents, byte flags) {
            this(contents,
                    (flags & FLAG_NOTIFICATION) != 0,
                    (flags & FLAG_HIGHLIGHT) != 0);
        }

        public AddEntry(RecipeDisplayEntry contents, boolean notification, boolean highlight) {
            this.contents = contents;
            this.notification = notification;
            this.highlight = highlight;
        }

        public static AddEntry read(PacketWrapper<?> wrapper) {
            RecipeDisplayEntry contents = RecipeDisplayEntry.read(wrapper);
            byte flags = wrapper.readByte();
            return new AddEntry(contents, flags);
        }

        public static void write(PacketWrapper<?> wrapper, AddEntry entry) {
            RecipeDisplayEntry.write(wrapper, entry.contents);
            wrapper.writeByte(entry.packFlags());
        }

        public RecipeDisplayEntry getContents() {
            return this.contents;
        }

        public void setContents(RecipeDisplayEntry contents) {
            this.contents = contents;
        }

        public boolean isNotification() {
            return this.notification;
        }

        public void setNotification(boolean notification) {
            this.notification = notification;
        }

        public boolean isHighlight() {
            return this.highlight;
        }

        public void setHighlight(boolean highlight) {
            this.highlight = highlight;
        }

        public byte packFlags() {
            return (byte) ((this.notification ? FLAG_NOTIFICATION : 0)
                    | (this.highlight ? FLAG_HIGHLIGHT : 0));
        }
    }
}
