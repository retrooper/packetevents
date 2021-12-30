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
import com.github.retrooper.packetevents.protocol.chat.component.serializer.ComponentSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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

    private List<BaseComponent> children = new ArrayList<>();

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

    public boolean hasStyling() {
        return (color != Color.WHITE) || (!insertion.isEmpty()) || (clickEvent.getType() != ClickType.EMPTY) || (hoverEvent.getType() != HoverType.EMPTY)
                || bold || italic || underlined || strikeThrough || obfuscated;
    }

    public List<BaseComponent> getChildren() {
        return children;
    }

    public void setChildren(List<BaseComponent> children) {
        this.children = children;
    }

    public void setModifier(BaseComponent modifier) {
        this.color = modifier.color;
        this.insertion = modifier.insertion;
        this.clickEvent = modifier.clickEvent;
        this.hoverEvent = modifier.hoverEvent;
        this.bold = modifier.bold;
        this.italic = modifier.italic;
        this.underlined = modifier.underlined;
        this.strikeThrough = modifier.strikeThrough;
        this.obfuscated = modifier.obfuscated;
    }

    public static Builder builder() {
        return new Builder(new BaseComponent());
    }

    public void parseJson(JsonObject jsonObject, Color defaultColor) {
        if (jsonObject.has("color")) {
            this.color = Color.getByName(jsonObject.get("color").getAsString());
        } else {
            if (defaultColor == null) {
                defaultColor = Color.WHITE;
            }
            this.color = defaultColor;
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
            List<Object> values = new ArrayList<>();

            JsonElement jsonHoverEventValueElement = hoverEventObject.get("value");
            if (jsonHoverEventValueElement == null) {
                jsonHoverEventValueElement = hoverEventObject.get("contents");
            }
            if (jsonHoverEventValueElement != null) {
                if (jsonHoverEventValueElement.isJsonPrimitive()) {
                    //Content can also be a string
                    values.add(jsonHoverEventValueElement.getAsString());
                }
                else {
                    //Content can also be a component, but here we make an exception
                    if (action.equals("show_entity")) {
                        String valueString = jsonHoverEventValueElement.toString();
                        values.add(valueString);
                    }
                    else {
                        values.add(ComponentSerializer.parseJsonComponent(jsonHoverEventValueElement));
                    }
                }
            }
            this.hoverEvent = new HoverEvent(values.isEmpty() ? HoverType.EMPTY : HoverType.getByName(action), values);
        } else {
            this.hoverEvent = new HoverEvent(HoverType.EMPTY);
        }

        if (jsonObject.has("extra")) {
            JsonArray jsonExtraComponents = jsonObject.getAsJsonArray("extra");
            for (JsonElement jsonElement : jsonExtraComponents) {
                BaseComponent child = ComponentSerializer.parseJsonComponent(jsonElement.getAsJsonObject(), this.color);
                children.add(child);
            }
        }
    }

    public JsonObject buildJson() {
        JsonObject jsonObject = new JsonObject();
        if (color != null) {
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
            for (Object value : hoverEvent.getValues()) {
                JsonElement output = (value instanceof BaseComponent) ? ((BaseComponent) value).buildJson() : new JsonPrimitive(value.toString());
                hoverEventValueArray.add(output);
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

        JsonArray extraArray = new JsonArray();
        for (BaseComponent child : children) {
            JsonObject childJsonObject = child.buildJson();
            extraArray.add(childJsonObject);
        }
        if (extraArray.size() > 0) {
            jsonObject.add("extra", extraArray);
        }
        return jsonObject;
    }

    public static class Builder<T extends Builder> {
        protected final BaseComponent component;

        public Builder(BaseComponent component) {
            this.component = component;
        }

        public T color(Color color) {
            component.setColor(color);
            return (T) this;
        }

        public T bold(boolean bold) {
            component.setBold(bold);
            return (T) this;
        }

        public T italic(boolean italic) {
            component.setItalic(italic);
            return (T) this;
        }

        public T underlined(boolean underlined) {
            component.setUnderlined(underlined);
            return (T) this;
        }

        public T strikeThrough(boolean strikeThrough) {
            component.setStrikeThrough(strikeThrough);
            return (T) this;
        }

        public T obfuscated(boolean obfuscated) {
            component.setObfuscated(obfuscated);
            return (T) this;
        }

        public T insertion(String insertion) {
            component.setInsertion(insertion);
            return (T) this;
        }

        public T clickEvent(ClickEvent event) {
            component.setClickEvent(event);
            return (T) this;
        }

        public T hoverEvent(HoverEvent event) {
            component.setHoverEvent(event);
            return (T) this;
        }

        public T append(BaseComponent component) {
            this.component.children.add(component);
            return (T) this;
        }

        public BaseComponent buildBase() {
            return component;
        }
    }
}
