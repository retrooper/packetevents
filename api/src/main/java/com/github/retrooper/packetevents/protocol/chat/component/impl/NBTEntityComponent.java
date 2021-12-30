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
import com.google.gson.JsonObject;

import java.util.Optional;

public class NBTEntityComponent extends NBTComponent {
    private String selectorPattern;

    @Override
    public void parseJson(JsonObject jsonObject, Color defaultColor) {
        super.parseJson(jsonObject, defaultColor);
        if (jsonObject.has("entity")) {
            this.selectorPattern = jsonObject.get("entity").getAsString();
        }
        else {
            this.selectorPattern = "";
        }
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        jsonObject.addProperty("entity", this.selectorPattern);
        return jsonObject;
    }

    public String getSelectorPattern() {
        return this.selectorPattern;
    }

    public void setSelectorPattern(String selectorPattern) {
        this.selectorPattern = selectorPattern;
    }

    public static NBTEntityComponent.Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseComponent.Builder<Builder> {
        public Builder() {
            super(new NBTEntityComponent());
        }

        public Builder nbtPath(String nbtPath) {
            ((NBTEntityComponent)component).setNBTPath(nbtPath);
            return this;
        }

        public Builder separator(Optional<BaseComponent> separator) {
            ((NBTEntityComponent)component).setSeparator(separator);
            return this;
        }

        public Builder interpreting(boolean interpreting) {
            ((NBTEntityComponent)component).setInterpreting(interpreting);
            return this;
        }

        public Builder selectorPattern(String selectorPattern) {
            ((NBTEntityComponent)component).setSelectorPattern(selectorPattern);
            return this;
        }

        public NBTEntityComponent build() {
            return (NBTEntityComponent) component;
        }
    }
}
