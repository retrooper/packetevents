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

public class HoverEvent {
    private HoverType type;
    private String value;

    public HoverEvent(HoverType type, String value) {
        this.type = type;
        this.value = value;
    }

    public HoverEvent(HoverType type) {
        this(type, "");
    }

    public HoverType getType() {
        return type;
    }

    public void setType(HoverType type) {
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
        if (obj instanceof HoverEvent) {
            HoverEvent hoverEvent = (HoverEvent) obj;
            return type == hoverEvent.type && value.equals(hoverEvent.value);
        }
        return false;
    }

    public enum HoverType {
        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY,
        @Deprecated
        SHOW_ACHIEVEMENT;

        private final String name;

        HoverType() {
            this.name = name().toLowerCase();
        }

        public String getName() {
            return name;
        }
    }
}
