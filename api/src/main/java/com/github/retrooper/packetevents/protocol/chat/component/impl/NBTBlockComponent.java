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
import com.google.gson.JsonObject;

public class NBTBlockComponent extends NBTComponent {
    //TODO Parse this pattern
    private String positionPattern;

    @Override
    public void parseJson(JsonObject jsonObject, Color defaultColor) {
        super.parseJson(jsonObject, defaultColor);
        if (jsonObject.has("block")) {
            positionPattern = jsonObject.get("block").getAsString();
        }
        else {
            positionPattern = "";
        }
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        jsonObject.addProperty("block", positionPattern);
        return jsonObject;
    }

    public String getPositionPattern() {
        return positionPattern;
    }

    public void setPositionPattern(String positionPattern) {
        this.positionPattern = positionPattern;
    }
}
