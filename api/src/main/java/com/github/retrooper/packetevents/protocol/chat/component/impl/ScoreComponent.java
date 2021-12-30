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

public class ScoreComponent extends BaseComponent {
    private String name;
    private String objective;
    //Apparently deprecated?
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void parseJson(JsonObject jsonObject, Color defaultColor) {
        if (jsonObject.has("score")) {
            JsonObject score = jsonObject.getAsJsonObject("score");
            if (score.has("name")) {
                name = score.get("name").getAsString();
            }
            else {
                name = "";
            }
            if (score.has("objective")) {
                objective = score.get("objective").getAsString();
            }
            else {
                objective = "";
            }

            if (score.has("value") && !score.get("value").getAsString().isEmpty()) {
                value = score.get("value").getAsString();
            }
            else {
                value = "";
            }
        }
        super.parseJson(jsonObject, defaultColor);
    }

    @Override
    public JsonObject buildJson() {
        JsonObject jsonObject = super.buildJson();
        JsonObject score = new JsonObject();
        score.addProperty("name", name);
        score.addProperty("objective", objective);
        if (!value.isEmpty()) {
            score.addProperty("value", value);
        }
        jsonObject.add("score", score);
        return jsonObject;
    }

    public static ScoreComponent.Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseComponent.Builder<Builder> {
        public Builder() {
            super(new ScoreComponent());
        }

        public Builder name(String name) {
            ((ScoreComponent)component).setName(name);
            return this;
        }

        public Builder objective(String objective) {
            ((ScoreComponent)component).setObjective(objective);
            return this;
        }

        public Builder value(String value) {
            ((ScoreComponent)component).setValue(value);
            return this;
        }

        public ScoreComponent build() {
            return (ScoreComponent) component;
        }
    }
}
