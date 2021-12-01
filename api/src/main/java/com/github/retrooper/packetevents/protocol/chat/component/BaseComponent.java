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


import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.ClickEvent.ClickType;
import com.github.retrooper.packetevents.protocol.chat.component.HoverEvent.HoverType;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class BaseComponent {
    private Color color = Color.WHITE;
    private String insertion = "";
    private ClickEvent clickEvent = new ClickEvent(ClickType.EMPTY);
    private HoverEvent hoverEvent = new HoverEvent(HoverType.EMPTY);
    private boolean bold = false;
    private boolean italic = false;
    private boolean underlined = false;
    private boolean strikeThrough = false;
    private boolean obfuscated = false;

    public BaseComponent() {
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getInsertion() {
        return insertion;
    }

    public void setInsertion(String insertion) {
        this.insertion = insertion;
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }

    public boolean isStrikeThrough() {
        return strikeThrough;
    }

    public void setStrikeThrough(boolean strikeThrough) {
        this.strikeThrough = strikeThrough;
    }

    public boolean isObfuscated() {
        return obfuscated;
    }

    public void setObfuscated(boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void parseJson(JsonObject jsonObject) {
        if (jsonObject.has("color")) {
            this.color = Color.getByName(jsonObject.get("color").getAsString());
        } else {
            this.color = Color.WHITE;
        }
        if (jsonObject.has("insertion")) {
            this.insertion = jsonObject.get("insertion").getAsString();
        } else {
            this.insertion = "";
        }
        if (jsonObject.has("bold")) {
            this.bold = jsonObject.get("bold").getAsBoolean();
        } else {
            this.bold = false;
        }
        if (jsonObject.has("italic")) {
            this.italic = jsonObject.get("italic").getAsBoolean();
        } else {
            this.italic = false;
        }
        if (jsonObject.has("underlined")) {
            this.underlined = jsonObject.get("underlined").getAsBoolean();
        } else {
            this.underlined = false;
        }
        if (jsonObject.has("strikethrough")) {
            this.strikeThrough = jsonObject.get("strikethrough").getAsBoolean();
        } else {
            this.strikeThrough = false;
        }

        if (jsonObject.has("obfuscated")) {
            this.obfuscated = jsonObject.get("obfuscated").getAsBoolean();
        } else {
            this.obfuscated = false;
        }

        //Read click events if it has been specified
        JsonElement clickEventElement = jsonObject.get("clickEvent");
        if (clickEventElement != null) {
            JsonObject clickEventObject = clickEventElement.getAsJsonObject();
            String action;
            if (clickEventObject.has("action")) {
                action = clickEventObject.get("action").getAsString();
            } else {
                action = "";
            }
            String value;
            if (clickEventObject.has("value")) {
                value = clickEventObject.get("value").getAsString();
            } else {
                value = "";
            }
            this.clickEvent = new ClickEvent(ClickType.getByName(action), value);
        } else {
            this.clickEvent = new ClickEvent(ClickType.EMPTY);
        }

        JsonElement hoverEventElement = jsonObject.get("hoverEvent");
        if (hoverEventElement != null) {
            JsonObject hoverEventObject = hoverEventElement.getAsJsonObject();
            String action;
            if (hoverEventObject.has("action")) {
                action = hoverEventObject.get("action").getAsString();
            } else {
                action = "";
            }
            List<BaseComponent> values = new ArrayList<>();

            JsonElement jsonHoverEventValueElement = hoverEventObject.get("value");
            if (jsonHoverEventValueElement == null) {
                jsonHoverEventValueElement = hoverEventObject.get("contents");
            }
            if (jsonHoverEventValueElement != null) {
                if (jsonHoverEventValueElement.isJsonArray()) {
                    for (JsonElement jsonElement : jsonHoverEventValueElement.getAsJsonArray()) {
                        JsonObject jsonObj = jsonElement.getAsJsonObject();
                        BaseComponent baseComponent;
                        if (jsonObj.has("text")) {
                            baseComponent = new TextComponent();
                        }
                        else {
                            baseComponent = new BaseComponent();
                        }
                        baseComponent.parseJson(jsonElement.getAsJsonObject());
                        values.add(baseComponent);
                    }
                }
                else if (jsonHoverEventValueElement.isJsonObject()) {
                    JsonObject jsonObj = jsonHoverEventValueElement.getAsJsonObject();
                    BaseComponent baseComponent;
                    if (jsonObj.has("text")) {
                        baseComponent = new TextComponent();
                    }
                    else {
                        baseComponent = new BaseComponent();
                    }
                    baseComponent.parseJson(jsonHoverEventValueElement.getAsJsonObject());
                    values.add(baseComponent);
                }
                else {
                }
            }
            this.hoverEvent = new HoverEvent(values.isEmpty() ? HoverType.EMPTY : HoverType.getByName(action), values);
        } else {
            this.hoverEvent = new HoverEvent(HoverType.EMPTY);
        }

        //TODO Other components such as the translation component, and the score component, and the selector component, and the keybind component

    }

    public JsonObject buildJson() {
        JsonObject jsonObject = new JsonObject();
        if (color != Color.WHITE && color != null) {
            jsonObject.addProperty("color", color.getName());
        }
        if (insertion != null && !insertion.isEmpty()) {
            jsonObject.addProperty("insertion", insertion);
        }
        if (bold) {
            jsonObject.addProperty("bold", true);
        }
        if (italic) {
            jsonObject.addProperty("italic", true);
        }
        if (underlined) {
            jsonObject.addProperty("underlined", true);
        }
        if (strikeThrough) {
            jsonObject.addProperty("strikethrough", true);
        }
        if (obfuscated) {
            jsonObject.addProperty("obfuscated", true);
        }

        if (clickEvent != null && clickEvent.getType() != ClickType.EMPTY) {
            JsonObject clickEventObject = new JsonObject();
            clickEventObject.addProperty("action", clickEvent.getType().getName());
            clickEventObject.addProperty("value", clickEvent.getValue());
            jsonObject.add("clickEvent", clickEventObject);
        }

        if (hoverEvent != null && hoverEvent.getType() != HoverType.EMPTY) {
            JsonObject hoverEventObject = new JsonObject();
            hoverEventObject.addProperty("action", hoverEvent.getType().getName());
            JsonArray hoverEventValueArray = new JsonArray();
            for (BaseComponent value : hoverEvent.getValues()) {
                hoverEventValueArray.add(value.buildJson());
            }

            //We use "value" instead of "contents" because we can be sure it will work on every Spigot version.
            if (hoverEventValueArray.size() == 1) {
                hoverEventObject.add("value", hoverEventValueArray.get(0));
            }
            else {
                hoverEventObject.add("value", hoverEventValueArray);
            }
            jsonObject.add("hoverEvent", hoverEventObject);
        }
        return jsonObject;
    }

    public static class Builder<T extends BaseComponent> {
        private final BaseComponent component = new BaseComponent();

        public Builder color(Color color) {
            this.component.setColor(color);
            return this;
        }

        public Builder bold(boolean bold) {
            this.component.setBold(bold);
            return this;
        }

        public Builder italic(boolean italic) {
            this.component.setItalic(italic);
            return this;
        }

        public Builder underlined(boolean underlined) {
            this.component.setUnderlined(underlined);
            return this;
        }

        public Builder strikeThrough(boolean strikeThrough) {
            this.component.setStrikeThrough(strikeThrough);
            return this;
        }

        public Builder obfuscated(boolean obfuscated) {
            this.component.setObfuscated(obfuscated);
            return this;
        }

        public Builder insertion(String insertion) {
            this.component.setInsertion(insertion);
            return this;
        }

        public Builder clickEvent(ClickEvent event) {
            this.component.setClickEvent(event);
            return this;
        }

        public Builder hoverEvent(HoverEvent event) {
            this.component.setHoverEvent(event);
            return this;
        }

        public T build() {
            return (T) component;
        }
    }
}
