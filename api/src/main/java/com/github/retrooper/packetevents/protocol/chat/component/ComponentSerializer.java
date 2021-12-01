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

import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class ComponentSerializer {
    public static Gson GSON = new GsonBuilder().create();

    public static List<BaseComponent> parseJSONString(String jsonMessageRaw) {
        JsonObject parentJsonObject = GSON.fromJson(jsonMessageRaw, JsonObject.class);
        List<BaseComponent> result = new ArrayList<>();
        if (parentJsonObject.isJsonPrimitive()) {
            result.add(TextComponent.builder().text(parentJsonObject.getAsString()).build());
            return result;
        } else {
            List<JsonObject> jsonObjects = new ArrayList<>();
            jsonObjects.add(parentJsonObject);
            if (parentJsonObject.has("extra")) {
                JsonArray jsonExtraComponents = parentJsonObject.getAsJsonArray("extra");
                for (JsonElement jsonElement : jsonExtraComponents) {
                    if (jsonElement.isJsonPrimitive()) {
                        JsonObject wrapperObject = new JsonObject();
                        wrapperObject.addProperty("text", jsonElement.getAsString());
                        jsonObjects.add(wrapperObject);
                    } else {
                        jsonObjects.add(jsonElement.getAsJsonObject());
                    }
                }
            }

            for (JsonObject jsonObject : jsonObjects) {
                TextComponent component = new TextComponent();
                component.parseJson(jsonObject);

                result.add(component);
            }
        }
        return result;
    }

    public static String buildJSONString(List<TextComponent> messageComponents) {
        JsonObject parentJsonObject = new JsonObject();
        boolean first = true;
        for (TextComponent component : messageComponents) {
            if (first) {
                parentJsonObject = component.buildJson();
                if (messageComponents.size() > 1) {
                    parentJsonObject.add("extra", new JsonArray());
                }
                first = false;
            } else {
                JsonObject extra = component.buildJson();
                JsonArray extraArray = parentJsonObject.getAsJsonArray("extra");
                extraArray.add(extra);
            }
        }
        return parentJsonObject.toString();
    }

    public static BaseComponent parseJsonComponent(String json) {
        JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
        return parseJsonComponent(jsonObject);
    }

    public static BaseComponent parseJsonComponent(JsonObject jsonObject) {
        BaseComponent component;
        if (jsonObject.has("text")) {
            component = new TextComponent();
        } else if (jsonObject.has("translate")) {
            component = null;
        } else if (jsonObject.has("score")) {
            component = null;
        } else if (jsonObject.has("selector")) {
            component = null;
        } else if (jsonObject.has("keybind")) {
            component = null;
        } else if (jsonObject.has("nbt")) {
            component = null;
        } else {
            component = null;
        }

        if (component != null) {
            component.parseJson(jsonObject);
            return component;
        } else {
            //TODO Maybe handle this differently
            return null;
        }
    }
}
