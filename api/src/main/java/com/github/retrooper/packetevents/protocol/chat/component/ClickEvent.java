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

public class ClickEvent {
    private ClickType type;
    private String value;

    public ClickEvent(ClickType type, String value) {
        this.type = type;
        this.value = value;
    }

    public ClickEvent(ClickType type) {
        this(type, "");
    }

    public ClickType getType() {
        return type;
    }

    public void setType(ClickType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ClickEvent) {
            ClickEvent clickEvent = (ClickEvent) obj;
            return clickEvent.type == type && clickEvent.value.equals(value);
        }
        return false;
    }

    public enum ClickType {
        OPEN_URL,
        OPEN_FILE,
        RUN_COMMAND,
        SUGGEST_COMMAND,
        CHANGE_PAGE,
        COPY_TO_CLIPBOARD,
        EMPTY("");

        private final String name;

        ClickType() {
            this.name = name().toLowerCase();
        }

        ClickType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ClickType getByName(String name) {
            for (ClickType type : ClickType.values()) {
                if (type.getName().equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}
