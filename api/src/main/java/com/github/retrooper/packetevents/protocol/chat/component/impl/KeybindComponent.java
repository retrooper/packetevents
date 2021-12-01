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

import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.google.gson.JsonObject;

public class KeybindComponent extends BaseComponent {
    private String keybind;

    public String getKeybind() {
        return keybind;
    }

    public void setKeybind(String keybind) {
        this.keybind = keybind;
    }

    @Override
    public void parseJson(JsonObject jsonObject) {
        if (jsonObject.has("keybind")) {
            keybind = jsonObject.get("keybind").getAsString();
        }
        else {
            keybind = "";
        }
        super.parseJson(jsonObject);
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        jsonObject.addProperty("keybind", keybind);
        return jsonObject;
    }
}
