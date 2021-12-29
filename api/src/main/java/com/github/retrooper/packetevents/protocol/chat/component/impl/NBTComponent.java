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

public class NBTComponent extends BaseComponent {
    private String nbtPath;
    private Optional<BaseComponent> separator;

    protected NBTComponent() {
    }

    @Override
    public void parseJson(JsonObject jsonObject, Color defaultColor) {
        super.parseJson(jsonObject, defaultColor);
        if (jsonObject.has("nbt")) {
            nbtPath = jsonObject.get("nbt").getAsString();
        }
        else {
            nbtPath = "";
        }

        if (jsonObject.has("separator")) {
            JsonObject separatorJsonObject = jsonObject.get("separator").getAsJsonObject();
            separator = Optional.of(ComponentSerializer.parseJsonComponent(separatorJsonObject));
        }
        else {
            separator = Optional.empty();
        }
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        jsonObject.addProperty("nbt", nbtPath);
        separator.ifPresent(baseComponent -> jsonObject.add("separator", baseComponent.buildJson()));
        return jsonObject;
    }

    public String getNBTPath() {
        return nbtPath;
    }

    public void setNBTPath(String nbtPath) {
        this.nbtPath = nbtPath;
    }

    public Optional<BaseComponent> getSeparator() {
        return separator;
    }

    public void setSeparator(Optional<BaseComponent> separator) {
        this.separator = separator;
    }
}
