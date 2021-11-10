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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class BaseComponent {
    private static final JSONParser PARSER = new JSONParser();
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

    public void parseJSON(JSONObject jsonObject) {
        String colorStr = (String) jsonObject.getOrDefault("color", "");
        this.color = Color.getByName(colorStr);
        this.insertion = (String) jsonObject.getOrDefault("insertion", "");
        this.bold = (boolean) jsonObject.getOrDefault("bold", false);
        this.italic = (boolean) jsonObject.getOrDefault("italic", false);
        this.underlined = (boolean) jsonObject.getOrDefault("underlined", false);
        this.strikeThrough = (boolean) jsonObject.getOrDefault("strikethrough", false);
        this.obfuscated = (boolean) jsonObject.getOrDefault("obfuscated", false);

        //Read click events if it has been specified
        JSONObject clickEvent = (JSONObject) jsonObject.get("clickEvent");
        if (clickEvent != null) {
            String action = (String) clickEvent.get("action");
            String value = (String) clickEvent.get("value");
            this.clickEvent = new ClickEvent(ClickType.getByName(action), value);
        } else {
            this.clickEvent = new ClickEvent(ClickType.EMPTY);
        }

        JSONObject hoverEvent = (JSONObject) jsonObject.get("hoverEvent");
        if (hoverEvent != null) {
            String action = (String) hoverEvent.get("action");
            //Parse value as JSON array or TODO : JSON object, String or primitive
            JSONArray s = (JSONArray) hoverEvent.get("value");
            List<String> values = new ArrayList<>();
            for (Object o : s) {
                JSONObject jsonObj = (JSONObject) o;
                values.add((jsonObj.toJSONString()));
            }
            this.hoverEvent = new HoverEvent(HoverType.getByName(action), values);
        } else {
            this.hoverEvent = new HoverEvent(HoverType.EMPTY);
        }

        //TODO Other components such as the translation component, and the score component, and the selector component, and the keybind component

    }

    public JSONObject buildJSON() {
        JSONObject jsonObject = new JSONObject();
        if (color != Color.WHITE && color != null) {
            jsonObject.put("color", color.getName());
        }
        if (insertion != null && !insertion.isEmpty()) {
            jsonObject.put("insertion", insertion);
        }
        if (bold) {
            jsonObject.put("bold", true);
        }
        if (italic) {
            jsonObject.put("italic", true);
        }
        if (underlined) {
            jsonObject.put("underlined", true);
        }
        if (strikeThrough) {
            jsonObject.put("strikethrough", true);
        }
        if (obfuscated) {
            jsonObject.put("obfuscated", true);
        }

        if (clickEvent != null && clickEvent.getType() != ClickType.EMPTY) {
            JSONObject clickEventJSON = new JSONObject();
            clickEventJSON.put("action", clickEvent.getType().getName());
            clickEventJSON.put("value", clickEvent.getValue());
            jsonObject.put("clickEvent", clickEventJSON);
        }

        if (hoverEvent != null && hoverEvent.getType() != HoverType.EMPTY) {
            JSONObject hoverEventJSON = new JSONObject();
            hoverEventJSON.put("action", hoverEvent.getType().getName());
            JSONArray jsonArray = new JSONArray();
            for (String s : hoverEvent.getValues()) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = (JSONObject) PARSER.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                jsonArray.add(jsonObj);
            }
            hoverEventJSON.put("value", jsonArray);
            jsonObject.put("hoverEvent", hoverEventJSON);
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
