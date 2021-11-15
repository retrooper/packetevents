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

package com.github.retrooper.packetevents.protocol.chat.component;

import com.github.retrooper.packetevents.protocol.chat.Color;
import com.google.gson.JsonObject;

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

    @Override
    public void parseJSON(JsonObject jsonObject) {
        if (jsonObject.has("text")) {
            text = jsonObject.get("text").getAsString();
        } else {
            text = "";
        }
        super.parseJSON(jsonObject);
    }

    @Override
    public JsonObject buildJSON() {
        JsonObject jsonObject = super.buildJSON();
        jsonObject.addProperty("text", text);
        return jsonObject;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T extends TextComponent> extends BaseComponent.Builder<T> {
        private final TextComponent component = new TextComponent();

        public Builder text(String text) {
            this.component.setText(text);
            return this;
        }

        @Override
        public Builder color(Color color) {
            this.component.setColor(color);
            return this;
        }

        @Override
        public Builder bold(boolean bold) {
            this.component.setBold(bold);
            return this;
        }

        @Override
        public Builder italic(boolean italic) {
            this.component.setItalic(italic);
            return this;
        }

        @Override
        public Builder underlined(boolean underlined) {
            this.component.setUnderlined(underlined);
            return this;
        }

        @Override
        public Builder strikeThrough(boolean strikeThrough) {
            this.component.setStrikeThrough(strikeThrough);
            return this;
        }

        @Override
        public Builder obfuscated(boolean obfuscated) {
            this.component.setObfuscated(obfuscated);
            return this;
        }

        @Override
        public Builder insertion(String insertion) {
            this.component.setInsertion(insertion);
            return this;
        }

        @Override
        public Builder clickEvent(ClickEvent clickEvent) {
            this.component.setClickEvent(clickEvent);
            return this;
        }

        @Override
        public Builder hoverEvent(HoverEvent hoverEvent) {
            this.component.setHoverEvent(hoverEvent);
            return this;
        }

        @Override
        public T build() {
            return (T) component;
        }
    }
}
