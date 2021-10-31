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

package com.github.retrooper.packetevents.protocol.data.chat.component;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ComponentParser {
    public static final JSONParser PARSER = new JSONParser();
    public static List<TextComponent> parseTextComponents(String jsonMessageRaw) {
        List<TextComponent> messageComponents = new ArrayList<>();
        JSONObject fullJsonObject = null;
        try {
            fullJsonObject = (JSONObject) PARSER.parse(jsonMessageRaw);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (fullJsonObject == null) {
            return messageComponents;
        }
        List<JSONObject> jsonObjects = new ArrayList<>();
        //We add the whole JSON object as it contains the data for the parent component
        jsonObjects.add(fullJsonObject);
        //Extra components, I'm not sure why minecraft designed their component system like this (parent and extra components)
        //Everything could have just been one array of components, no parent required
        Object jsonArrayObj = fullJsonObject.get("extra");
        if (jsonArrayObj != null) {
            for (Object o : (JSONArray) jsonArrayObj) {
                jsonObjects.add((JSONObject) o);
            }
        }


        for (JSONObject jsonObject : jsonObjects) {
            TextComponent component = new TextComponent();
            component.parseJSON(jsonObject);
            messageComponents.add(component);
        }
        return messageComponents;
    }

    public static String buildJSONString(List<TextComponent> messageComponents) {
        JSONObject fullJSONObject = new JSONObject();
        boolean firstComponent = true;
        for (TextComponent component : messageComponents) {
            if (firstComponent) {
                fullJSONObject = component.buildJSON();
                firstComponent = false;
                if (messageComponents.size() > 1) {
                    fullJSONObject.put("extra", new JSONArray());
                }
            } else {
                JSONObject output = component.buildJSON();
                JSONArray extraComponents = (JSONArray) fullJSONObject.get("extra");
                extraComponents.add(output);
            }
        }
        return fullJSONObject.toJSONString();
    }
}
