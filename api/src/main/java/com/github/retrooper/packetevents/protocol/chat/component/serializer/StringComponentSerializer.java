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

package com.github.retrooper.packetevents.protocol.chat.component.serializer;

import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.ClickEvent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringComponentSerializer {
    private static final Pattern PATTERN = Pattern.compile("(" + '§' + "[0-9a-fk-orx])|((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + '§' + " \\n]|$))))|(\\n)", Pattern.CASE_INSENSITIVE);
    private static final BaseComponent RESET = BaseComponent.builder().buildBase();
    private final List<BaseComponent> list = new ArrayList<>();
    private BaseComponent currentModifier = RESET;

    public static BaseComponent[] parseMessage(String message, boolean keepNewLines, boolean plain) {
        BaseComponent currentComponent = TextComponent.builder().text("").build();
        BaseComponent modifier = BaseComponent.builder().buildBase();
        if (message == null) {
            return new BaseComponent[]{currentComponent};
        }
        List<BaseComponent> output = new ArrayList<>();
        output.add(currentComponent);
        int currentIndex = 0;
        Matcher matcher = PATTERN.matcher(message);
        String match;
        boolean add;
        while (matcher.find()) {
            char c;
            Color format;
            int group = 0;
            do {

            } while ((match = matcher.group(++group)) == null);
            int index = matcher.start(group);
            if (index > currentIndex) {
                add = false;
                appendNewComponent(index);
            }
            switch (group) {
                case 1:
                    c = match.toLowerCase().charAt(1);
                    format = Color.getByCode(c);
                    if (!format.isColor() && format != Color.RESET) {
                        switch (format) {
                            case BOLD:
                                modifier.setBold(true);
                                break;
                            case ITALIC:
                                modifier.setItalic(true);
                                break;
                            case STRIKETHROUGH:
                                modifier.setStrikeThrough(true);
                                break;
                            case UNDERLINE:
                                modifier.setUnderlined(true);
                                break;
                            case OBFUSCATED:
                                modifier.setObfuscated(true);
                                break;
                            default:
                                throw new AssertionError("Unexpected message format");
                        }
                    } else {
                        modifier.setColor(format);
                    }
                    needsAdd = true;
                    break;
                case 2:
                    if (plain) {
                        appendNewComponent(matcher.end(groupId));
                        break;
                    }
                    if (!match.startsWith("http://") && !match.startsWith("https://")) {
                        match = "http://" + match;
                    }
                    //TODO this.modifier.setClickEvent(null);
                    appendNewComponent(matcher.end(groupId));
                    this.modifier.setClickEvent(new ClickEvent(ClickEvent.ClickType.EMPTY));
                    break;
                case 3:
                    if (needsAdd)
                        appendNewComponent(index);
                    this.currentChatComponent = null;
                    break;
            }
            this.currentIndex = matcher.end(groupId);
        }
    }
}
