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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public class TranslatableComponent extends BaseComponent {
    private String translate;
    private List<Object> with = new ArrayList<>();

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public List<Object> getWith() {
        return with;
    }

    public void setWith(List<Object> with) {
        this.with = with;
    }

    @Override
    public void parseJson(JsonObject jsonObject, Color defaultColor) {
        super.parseJson(jsonObject, defaultColor);
        if (jsonObject.has("translate")) {
            translate = jsonObject.get("translate").getAsString();
        } else {
            translate = "";
        }


        if (jsonObject.has("with")) {
            JsonArray withArray = jsonObject.get("with").getAsJsonArray();
            for (JsonElement withElement : withArray) {
                BaseComponent child = ComponentSerializer.parseJsonComponent(withElement, getColor(), false);
                if (child instanceof TextComponent) {
                    TextComponent childComp = (TextComponent) child;
                    if (!childComp.hasStyling() && childComp.getChildren().isEmpty()) {
                        with.add(((TextComponent) child).getText());
                        continue;
                    }
                }
                with.add(child);
            }
        }
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        jsonObject.addProperty("translate", translate);
        if (!with.isEmpty()) {
            JsonArray withArray = new JsonArray();
            for (Object child : with) {
                if (child instanceof BaseComponent) {
                    withArray.add(((BaseComponent) child).buildJson());
                } else {
                    withArray.add(new JsonPrimitive(String.valueOf(child)));
                }
            }
            jsonObject.add("with", withArray);
        }
        return jsonObject;
    }

    public static TranslatableComponent.Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseComponent.Builder<Builder> {
        public Builder() {
            super(new TranslatableComponent());
        }

        public Builder translate(String translate) {
            ((TranslatableComponent)component).setTranslate(translate);
            return this;
        }

        public Builder with(List<Object> with) {
            ((TranslatableComponent)component).setWith(with);
            return this;
        }

        public Builder appendWith(Object obj) {
            if (obj instanceof BaseComponent) {
                //TODO Do same for the rest of the styling i think
                //((TranslatableComponent)obj).setColor(component.getColor());
            }
            ((TranslatableComponent)component).getWith().add(obj);
            return this;
        }

        public TranslatableComponent build() {
            return (TranslatableComponent) component;
        }
    }
}
