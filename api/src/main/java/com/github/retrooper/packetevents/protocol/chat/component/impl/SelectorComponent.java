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

package com.github.retrooper.packetevents.protocol.chat.component.impl;

import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.serializer.ComponentSerializer;
import com.google.gson.JsonObject;

import java.util.Optional;

public class SelectorComponent extends BaseComponent {
    private String selector;
    private Optional<BaseComponent> separator;

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public Optional<BaseComponent> getSeparator() {
        return separator;
    }

    public void setSeparator(Optional<BaseComponent> separator) {
        this.separator = separator;
    }

    @Override
    public void parseJson(JsonObject jsonObject, Color defaultColor) {
        if (jsonObject.has("selector")) {
            selector = jsonObject.get("selector").getAsString();
        }
        else {
            selector = "";
        }

        if (jsonObject.has("separator")) {
            JsonObject separatorJsonObject = jsonObject.get("separator").getAsJsonObject();
            separator = Optional.of(ComponentSerializer.parseJsonComponent(separatorJsonObject));
        }
        else {
            separator = Optional.empty();
        }
        super.parseJson(jsonObject, defaultColor);
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        jsonObject.addProperty("selector", selector);
        separator.ifPresent(baseComponent -> jsonObject.add("separator", baseComponent.buildJson()));
        return jsonObject;
    }

    public static SelectorComponent.Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseComponent.Builder<Builder> {
        public Builder() {
            super(new SelectorComponent());
        }

        public Builder selector(String selector) {
            ((SelectorComponent)component).setSelector(selector);
            return this;
        }

        public Builder separator(Optional<BaseComponent> separator) {
            ((SelectorComponent)component).setSeparator(separator);
            return this;
        }


        public SelectorComponent build() {
            return (SelectorComponent) component;
        }
    }
}
