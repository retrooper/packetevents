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

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class ComponentSerializer {
    public static Gson GSON = new GsonBuilder().create();

    //TODO Support translate components
    public static List<TextComponent> parseJSONString(String jsonMessageRaw) {
        JsonObject parentJsonObject = GSON.fromJson(jsonMessageRaw, JsonObject.class);
        List<TextComponent> result = new ArrayList<>();
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
                    }
                    else {
                        jsonObjects.add(jsonElement.getAsJsonObject());
                    }
                }
            }

            for (JsonObject jsonObject : jsonObjects) {
                TextComponent component = new TextComponent();
                component.parseJSON(jsonObject);
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
                parentJsonObject = component.buildJSON();
                if (messageComponents.size() > 1) {
                    parentJsonObject.add("extra", new JsonArray());
                }
                first = false;
            }
            else {
                JsonObject extra = component.buildJSON();
                JsonArray extraArray = parentJsonObject.getAsJsonArray("extra");
                extraArray.add(extra);
            }
        }
        return parentJsonObject.toString();
    }
}
