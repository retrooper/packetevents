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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoverEvent {
    private HoverType type;
    //Can also be a component in string form
    private List<BaseComponent> values;

    public HoverEvent(HoverType type, List<BaseComponent> values) {
        this.type = type;
        this.values = values;
    }

    public HoverEvent(HoverType type, BaseComponent... values) {
        this.type = type;
        this.values = new ArrayList<>();
        this.values.addAll(Arrays.asList(values));
    }

    public HoverEvent(HoverType type) {
        this(type, new ArrayList<>());
    }

    public HoverType getType() {
        return type;
    }

    public void setType(HoverType type) {
        this.type = type;
    }

    public List<BaseComponent> getValues() {
        return values;
    }

    public void setValues(List<BaseComponent> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof HoverEvent) {
            HoverEvent hoverEvent = (HoverEvent) obj;
            if (type == hoverEvent.type && values.size() == hoverEvent.values.size()) {
                for (int i = 0; i < values.size(); i++) {
                    if (!values.get(i).equals(hoverEvent.values.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public enum HoverType {
        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY,
        @Deprecated
        SHOW_ACHIEVEMENT,
        EMPTY("");

        private final String name;

        HoverType() {
            this.name = name().toLowerCase();
        }

        HoverType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static HoverType getByName(String name) {
            for (HoverType type : values()) {
                if (type.getName().equals(name)) return type;
            }
            return null;
        }
    }
}
