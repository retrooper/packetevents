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

package io.github.retrooper.packetevents.utils.dependencies.google;

import com.github.retrooper.packetevents.protocol.gameprofile.WrappedProperty;
import com.github.retrooper.packetevents.protocol.gameprofile.WrappedPropertyMap;
import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class WrappedPropertyMapModern extends ForwardingMultimap<String, WrappedProperty> implements WrappedPropertyMap {
    private final Multimap<String, WrappedProperty> properties = LinkedHashMultimap.create();

    public WrappedPropertyMapModern() {

    }

    public WrappedPropertyMapModern(PropertyMap propertyMap) {
        for (String key : propertyMap.keys()) {
            Collection<Property> properties = propertyMap.get(key);
            Collection<WrappedProperty> wrappedProperties = new ArrayList<>();
            for (Property property : properties) {
                wrappedProperties.add(new WrappedProperty(property.getName(), property.getValue(), property.getSignature()));
            }
            //TODO Confirm if correct
            putAll(key, wrappedProperties);
        }

    }

    protected Multimap<String, WrappedProperty> delegate() {
        return this.properties;
    }

    public static class Serializer implements JsonSerializer<WrappedPropertyMapModern>, JsonDeserializer<WrappedPropertyMapModern> {
        public Serializer() {
        }

        public WrappedPropertyMapModern deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            WrappedPropertyMapModern result = new WrappedPropertyMapModern();
            if (json instanceof JsonObject) {
                JsonObject object = (JsonObject) json;
                Iterator var6 = object.entrySet().iterator();

                while (true) {
                    Map.Entry entry;
                    do {
                        if (!var6.hasNext()) {
                            return result;
                        }

                        entry = (Map.Entry) var6.next();
                    } while (!(entry.getValue() instanceof JsonArray));

                    for (JsonElement element : (JsonArray) entry.getValue()) {
                        String name = (String) entry.getKey();
                        String value = element.getAsString();
                        result.put(name, new WrappedProperty(name, value));
                    }
                }
            } else if (json instanceof JsonArray) {
                Iterator var10 = ((JsonArray) json).iterator();

                while (var10.hasNext()) {
                    JsonElement element = (JsonElement) var10.next();
                    if (element instanceof JsonObject) {
                        JsonObject object = (JsonObject) element;
                        String name = object.getAsJsonPrimitive("name").getAsString();
                        String value = object.getAsJsonPrimitive("value").getAsString();
                        if (object.has("signature")) {
                            result.put(name, new WrappedProperty(name, value, object.getAsJsonPrimitive("signature").getAsString()));
                        } else {
                            result.put(name, new WrappedProperty(name, value));
                        }
                    }
                }
            }

            return result;
        }

        public JsonElement serialize(WrappedPropertyMapModern src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray result = new JsonArray();

            JsonObject object;
            for (Iterator var5 = src.values().iterator(); var5.hasNext(); result.add(object)) {
                Property property = (Property) var5.next();
                object = new JsonObject();
                object.addProperty("name", property.getName());
                object.addProperty("value", property.getValue());
                if (property.hasSignature()) {
                    object.addProperty("signature", property.getSignature());
                }
            }

            return result;
        }
    }
}
