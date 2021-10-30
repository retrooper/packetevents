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


import com.github.retrooper.packetevents.protocol.data.chat.ClickEvent;
import com.github.retrooper.packetevents.protocol.data.chat.ClickEvent.ClickType;
import com.github.retrooper.packetevents.protocol.data.chat.Color;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseComponent {
    private Color color = Color.WHITE;
    private String insertion = "";
    private ClickEvent openURLClickEvent = new ClickEvent(ClickEvent.ClickType.OPEN_URL);
    private ClickEvent openFileClickEvent = new ClickEvent(ClickEvent.ClickType.OPEN_FILE);
    private ClickEvent runCommandClickEvent = new ClickEvent(ClickEvent.ClickType.RUN_COMMAND);
    private ClickEvent suggestCommandClickEvent = new ClickEvent(ClickEvent.ClickType.SUGGEST_COMMAND);
    private ClickEvent changePageClickEvent = new ClickEvent(ClickEvent.ClickType.CHANGE_PAGE);
    private ClickEvent copyToClipboardClickEvent = new ClickEvent(ClickEvent.ClickType.COPY_TO_CLIPBOARD);
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

    public ClickEvent getOpenURLClickEvent() {
        return openURLClickEvent;
    }

    public void setOpenURLClickEvent(ClickEvent openURLClickEvent) {
        this.openURLClickEvent = openURLClickEvent;
    }

    public ClickEvent getOpenFileClickEvent() {
        return openFileClickEvent;
    }

    public void setOpenFileClickEvent(ClickEvent openFileClickEvent) {
        this.openFileClickEvent = openFileClickEvent;
    }

    public ClickEvent getRunCommandClickEvent() {
        return runCommandClickEvent;
    }

    public void setRunCommandClickEvent(ClickEvent runCommandClickEvent) {
        this.runCommandClickEvent = runCommandClickEvent;
    }

    public ClickEvent getSuggestCommandClickEvent() {
        return suggestCommandClickEvent;
    }

    public void setSuggestCommandClickEvent(ClickEvent suggestCommandClickEvent) {
        this.suggestCommandClickEvent = suggestCommandClickEvent;
    }

    public ClickEvent getChangePageClickEvent() {
        return changePageClickEvent;
    }

    public void setChangePageClickEvent(ClickEvent changePageClickEvent) {
        this.changePageClickEvent = changePageClickEvent;
    }

    public ClickEvent getCopyToClipboardClickEvent() {
        return copyToClipboardClickEvent;
    }

    public void setCopyToClipboardClickEvent(ClickEvent copyToClipboardClickEvent) {
        this.copyToClipboardClickEvent = copyToClipboardClickEvent;
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

        if (!colorStr.isEmpty()) {
            this.color = Color.getByName(colorStr);
        } else {
            this.color = Color.WHITE;
        }

        this.insertion = (String) jsonObject.getOrDefault("insertion", "");
        this.bold = (boolean) jsonObject.getOrDefault("bold", false);
        this.italic = (boolean) jsonObject.getOrDefault("italic", false);
        this.underlined = (boolean) jsonObject.getOrDefault("underlined", false);
        this.strikeThrough = (boolean) jsonObject.getOrDefault("strikethrough", false);
        this.obfuscated = (boolean) jsonObject.getOrDefault("obfuscated", false);

        //Read click events if it has been specified
        JSONObject clickEvents = (JSONObject) jsonObject.get("clickEvent");
        if (clickEvents != null) {
            String openURLValue = (String) clickEvents.getOrDefault(ClickEvent.ClickType.OPEN_URL.getName(), "");
            String openFileValue = (String) clickEvents.getOrDefault(ClickEvent.ClickType.OPEN_FILE.getName(), "");
            String runCommandValue = (String) clickEvents.getOrDefault(ClickEvent.ClickType.RUN_COMMAND.getName(), "");
            String suggestCommandValue = (String) clickEvents.getOrDefault(ClickEvent.ClickType.SUGGEST_COMMAND.getName(), "");
            String changePageValue = (String) clickEvents.getOrDefault(ClickEvent.ClickType.CHANGE_PAGE.getName(), "");
            String copyToClipboardValue = (String) clickEvents.getOrDefault(ClickEvent.ClickType.COPY_TO_CLIPBOARD.getName(), "");

            this.openURLClickEvent = new ClickEvent(ClickType.OPEN_URL, openURLValue);
            this.openFileClickEvent = new ClickEvent(ClickType.OPEN_FILE, openFileValue);
            this.runCommandClickEvent = new ClickEvent(ClickType.RUN_COMMAND, runCommandValue);
            this.suggestCommandClickEvent = new ClickEvent(ClickType.SUGGEST_COMMAND, suggestCommandValue);
            this.changePageClickEvent = new ClickEvent(ClickType.CHANGE_PAGE, changePageValue);
            this.copyToClipboardClickEvent = new ClickEvent(ClickType.COPY_TO_CLIPBOARD, copyToClipboardValue);
        }
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

        List<ClickEvent> clickEvents = new ArrayList<>();
        clickEvents.add(openURLClickEvent);
        clickEvents.add(openFileClickEvent);
        clickEvents.add(runCommandClickEvent);
        clickEvents.add(suggestCommandClickEvent);
        clickEvents.add(changePageClickEvent);
        clickEvents.add(copyToClipboardClickEvent);
        boolean allClickEventsEmpty = true;
        JSONObject jsonClickEvents = new JSONObject();
        for (ClickEvent clickEvent : clickEvents) {
            if (!clickEvent.getValue().isEmpty()) {
                jsonClickEvents.put(clickEvent.getType().getName(), clickEvent.getValue());
                allClickEventsEmpty = false;
            }
        }
        if (!allClickEventsEmpty) {
            jsonObject.put("clickEvent", jsonClickEvents);
        }
        return jsonObject;
    }

    public static class Builder {
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

        public Builder openURLClickEvent(String value) {
            this.component.setOpenURLClickEvent(new ClickEvent(ClickEvent.ClickType.OPEN_URL, value));
            return this;
        }

        public Builder openFileClickEvent(String value) {
            this.component.setOpenFileClickEvent(new ClickEvent(ClickEvent.ClickType.OPEN_FILE, value));
            return this;
        }

        public Builder runCommandClickEvent(String value) {
            this.component.setRunCommandClickEvent(new ClickEvent(ClickEvent.ClickType.RUN_COMMAND, value));
            return this;
        }

        public Builder suggestCommandClickEvent(String value) {
            this.component.setSuggestCommandClickEvent(new ClickEvent(ClickEvent.ClickType.SUGGEST_COMMAND, value));
            return this;
        }

        public Builder changePageClickEvent(String value) {
            this.component.setChangePageClickEvent(new ClickEvent(ClickEvent.ClickType.CHANGE_PAGE, value));
            return this;
        }

        public Builder copyToClipboardClickEvent(String value) {
            this.component.setCopyToClipboardClickEvent(new ClickEvent(ClickEvent.ClickType.COPY_TO_CLIPBOARD, value));
            return this;
        }

        public BaseComponent build() {
            return component;
        }
    }
}
