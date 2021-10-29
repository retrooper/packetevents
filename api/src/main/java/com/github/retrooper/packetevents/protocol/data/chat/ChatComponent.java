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

package com.github.retrooper.packetevents.protocol.data.chat;

public class ChatComponent {
    private String text = "";
    private Color color = Color.WHITE;
    private String font = "";
    private String insertion = "";
    private boolean bold = false;
    private boolean italic = false;
    private boolean underlined = false;
    private boolean strikeThrough = false;
    private boolean obfuscated = false;

    public ChatComponent() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getInsertion() {
        return insertion;
    }

    public void setInsertion(String insertion) {
        this.insertion = insertion;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }

    public boolean isStrikeThrough() {
        return strikeThrough;
    }

    public void setStrikeThrough(boolean strikeThrough) {
        this.strikeThrough = strikeThrough;
    }

    public boolean isObfuscated() {
        return obfuscated;
    }

    public void setObfuscated(boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    public static BuilderData generate() {
        return new BuilderData();
    }

    public static class BuilderData {
        private final ChatComponent component = new ChatComponent();

        public BuilderData text(String text) {
            this.component.setText(text);
            return this;
        }

        public BuilderData color(Color color) {
            this.component.setColor(color);
            return this;
        }

        public BuilderData font(String font) {
            this.component.setFont(font);
            return this;
        }

        public BuilderData bold(boolean bold) {
            this.component.setBold(bold);
            return this;
        }

        public BuilderData italic(boolean italic) {
            this.component.setItalic(italic);
            return this;
        }

        public BuilderData underlined(boolean underlined) {
            this.component.setUnderlined(underlined);
            return this;
        }

        public BuilderData strikeThrough(boolean strikeThrough) {
            this.component.setStrikeThrough(strikeThrough);
            return this;
        }

        public BuilderData obfuscated(boolean obfuscated) {
            this.component.setObfuscated(obfuscated);
            return this;
        }

        public BuilderData insertion(String insertion) {
            this.component.setInsertion(insertion);
            return this;
        }

        public ChatComponent build() {
            return component;
        }
    }
}
