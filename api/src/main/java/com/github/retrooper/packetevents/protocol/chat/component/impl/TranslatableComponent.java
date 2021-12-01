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
import com.github.retrooper.packetevents.protocol.chat.component.serializer.ComponentSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TranslatableComponent extends BaseComponent {
    private String translate;
    private List<BaseComponent> with = new ArrayList<>();

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public List<BaseComponent> getWith() {
        return with;
    }

    public void setWith(List<BaseComponent> with) {
        this.with = with;
    }

    @Override
    public void parseJson(JsonObject jsonObject) {
        if (jsonObject.has("translate")) {
            translate = jsonObject.get("translate").getAsString();
        }
        else {
            translate = "";
        }

       //System.out.println("pre!");
        if (jsonObject.has("with")) {
            JsonArray withArray = jsonObject.get("with").getAsJsonArray();
            //System.out.println("parsing: " + withArray.toString());
            for (JsonElement withElement : withArray) {
                JsonObject withObject = withElement.getAsJsonObject();
                BaseComponent child = ComponentSerializer.parseJsonComponent(withObject);
                with.add(child);
            }
        }
        super.parseJson(jsonObject);
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        jsonObject.addProperty("translate", translate);
        if (!with.isEmpty()) {
            JsonArray withArray = new JsonArray();
            for (BaseComponent child : with) {
                withArray.add(child.buildJson());
            }
            jsonObject.add("with", withArray);
        }
        return jsonObject;
    }
}
