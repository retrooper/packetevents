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

package com.github.retrooper.packetevents.protocol.data.chat.component;

import com.github.retrooper.packetevents.protocol.data.chat.ClickEvent;
import com.github.retrooper.packetevents.protocol.data.chat.Color;

public class TextComponent extends BaseComponent {
    private String text;

    public TextComponent() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseComponent.Builder {
        private final TextComponent component = new TextComponent();

        public Builder text(String text) {
            this.component.setText(text);
            return this;
        }

        public Builder color(Color color) {
            this.component.setColor(color);
            return this;
        }

        public Builder font(String font) {
            this.component.setFont(font);
            return this;
        }

        public Builder bold(boolean bold) {
            this.component.setBold(bold);
            return this;
        }

        public Builder italic(boolean italic) {
            this.component.setItalic(italic);
            return this;
        }

        public Builder underlined(boolean underlined) {
            this.component.setUnderlined(underlined);
            return this;
        }

        public Builder strikeThrough(boolean strikeThrough) {
            this.component.setStrikeThrough(strikeThrough);
            return this;
        }

        public Builder obfuscated(boolean obfuscated) {
            this.component.setObfuscated(obfuscated);
            return this;
        }

        public Builder insertion(String insertion) {
            this.component.setInsertion(insertion);
            return this;
        }

        public Builder openURLClickEvent(String value) {
            this.component.setOpenURLClickEvent(new ClickEvent(ClickEvent.ClickType.OPEN_URL, value));
            return this;
        }

        public Builder openFileClickEvent(String value) {
            this.component.setOpenFileClickEvent(new ClickEvent(ClickEvent.ClickType.OPEN_FILE, value));
            return this;
        }

        public Builder runCommandClickEvent(String value) {
            this.component.setRunCommandClickEvent(new ClickEvent(ClickEvent.ClickType.RUN_COMMAND, value));
            return this;
        }

        public Builder suggestCommandClickEvent(String value) {
            this.component.setSuggestCommandClickEvent(new ClickEvent(ClickEvent.ClickType.SUGGEST_COMMAND, value));
            return this;
        }

        public Builder changePageClickEvent(String value) {
            this.component.setChangePageClickEvent(new ClickEvent(ClickEvent.ClickType.CHANGE_PAGE, value));
            return this;
        }

        public Builder copyToClipboardClickEvent(String value) {
            this.component.setCopyToClipboardClickEvent(new ClickEvent(ClickEvent.ClickType.COPY_TO_CLIPBOARD, value));
            return this;
        }
    }
}
