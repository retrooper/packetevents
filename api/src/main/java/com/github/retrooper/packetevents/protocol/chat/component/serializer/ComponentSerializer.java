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

package com.github.retrooper.packetevents.protocol.chat.component.serializer;

import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.*;
import com.google.gson.*;

public class ComponentSerializer {
    public static Gson GSON = new GsonBuilder().create();

    public static JsonObject buildJsonObject(BaseComponent component) {
        JsonObject parentJsonObject = component.buildJson();
        //LogManager.debug("Built json object: " + parentJsonObject.toString());
        return parentJsonObject;
    }

    public static BaseComponent parseJsonComponent(String json) {
        return parseJsonComponent(json, Color.WHITE);
    }

    public static BaseComponent parseJsonComponent(JsonElement jsonObject) {
        return parseJsonComponent(jsonObject, Color.WHITE);
    }

    public static BaseComponent parseJsonComponent(String json, Color defaultColor) {
        JsonElement jsonObject = GSON.fromJson(json, JsonElement.class);
        return parseJsonComponent(jsonObject, defaultColor);
    }

    public static BaseComponent parseJsonComponent(JsonElement jsonElement, Color defaultColor) {
        BaseComponent component = null;
        if (jsonElement.isJsonPrimitive()) {
            //Convert to a text component
            component = TextComponent.builder().text(jsonElement.getAsString()).color(defaultColor).build();
        } else if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("text")) {
                component = new TextComponent();
            } else if (jsonObject.has("translate")) {
                component = new TranslatableComponent();
            } else if (jsonObject.has("score")) {
                component = new ScoreComponent();
            } else if (jsonObject.has("selector")) {
                component = new SelectorComponent();
            } else if (jsonObject.has("keybind")) {
                component = new KeybindComponent();
            } else if (jsonObject.has("nbt")) {
                if (jsonObject.has("block")) {
                    component = new NBTBlockComponent();
                } else if (jsonObject.has("entity")) {
                    component = new NBTEntityComponent();
                } else if (jsonObject.has("storage")) {
                    component = new NBTStorageComponent();
                } else {
                    throw new IllegalStateException("Failed to parse an NBT chat component. It might be invalid!");
                }
            } else {
                throw new IllegalStateException("Failed to parse a chat component! It might be invalid!");
            }
            component.parseJson(jsonObject, defaultColor);
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            for (JsonElement element : array) {
                BaseComponent child = ComponentSerializer.parseJsonComponent(element);
                if (component == null) {
                    component = child;
                    continue;
                }
                component.getChildren().add(child);
            }
        }
        return component;
    }
}
