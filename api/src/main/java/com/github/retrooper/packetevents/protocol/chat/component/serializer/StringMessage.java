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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringMessage {
    //TODO Work on parsing links
    private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))|(\\n)", Pattern.CASE_INSENSITIVE);
    private static final Pattern INCREMENTAL_PATTERN_KEEP_NEWLINES = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " ]|$))))", Pattern.CASE_INSENSITIVE);
    private static final BaseComponent RESET = BaseComponent.builder().buildBase();

    private final List<BaseComponent> list = new ArrayList<>();

    private BaseComponent currentChatComponent = TextComponent.builder().text("").build();

    private BaseComponent modifier = RESET;

    private final BaseComponent[] output;

    private int currentIndex;

    private StringBuilder hex;

    private final String message;

    public StringMessage(String message, boolean keepNewlines, boolean plain) {
        this.message = message;
        if (message == null) {
            this.output = new BaseComponent[]{this.currentChatComponent};
            return;
        }
        this.list.add(this.currentChatComponent);
        Matcher matcher = (keepNewlines ? INCREMENTAL_PATTERN_KEEP_NEWLINES : INCREMENTAL_PATTERN).matcher(message);
        String match = null;
        boolean needsAdd = false;
        while (matcher.find()) {
            char c;
            Color format;
            int groupId = 0;
            do {

            } while ((match = matcher.group(++groupId)) == null);
            int index = matcher.start(groupId);
            if (index > this.currentIndex) {
                needsAdd = false;
                appendNewComponent(index);
            }
            switch (groupId) {
                case 1:
                    c = match.toLowerCase(Locale.ENGLISH).charAt(1);
                    format = Color.getByCode(c);
                    if (c == 'x') {
                        this.hex = new StringBuilder("#");
                    } else if (this.hex != null) {
                        /*this.hex.append(c);
                        if (this.hex.length() == 7) {
                            this.modifier = RESET.setColor(ChatHexColor.a(this.hex.toString()));
                            this.hex = null;
                        }*/
                    } else if (!format.isColor() && format != Color.RESET) {
                        switch (format) {
                            case BOLD:
                                this.modifier.setBold(true);
                                break;
                            case ITALIC:
                                this.modifier.setItalic(true);
                                break;
                            case STRIKETHROUGH:
                                this.modifier.setStrikeThrough(true);
                                break;
                            case UNDERLINE:
                                this.modifier.setUnderlined(true);
                                break;
                            case OBFUSCATED:
                                this.modifier.setObfuscated(true);
                                break;
                            default:
                                throw new AssertionError("Unexpected message format");
                        }
                    } else {
                        this.modifier.setColor(format);
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
        if (this.currentIndex < message.length() || needsAdd)
            appendNewComponent(message.length());
        this.output = this.list.toArray(new BaseComponent[this.list.size()]);
    }

    private void appendNewComponent(int index) {
        BaseComponent childComponent = (TextComponent.builder().text(this.message.substring(this.currentIndex, index)).build());
        childComponent.setModifier(modifier);
        this.currentIndex = index;
        if (this.currentChatComponent == null) {
            this.currentChatComponent = TextComponent.builder().text("").build();
            this.list.add(this.currentChatComponent);
        }
        this.currentChatComponent.getChildren().add(childComponent);
    }

    public BaseComponent[] getOutput() {
        return this.output;
    }
}