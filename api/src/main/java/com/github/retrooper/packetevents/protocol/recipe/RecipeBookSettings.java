/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class RecipeBookSettings {

    private final Map<RecipeBookType, TypeState> states;

    public RecipeBookSettings(Map<RecipeBookType, TypeState> states) {
        this.states = states;
    }

    public static RecipeBookSettings read(PacketWrapper<?> wrapper) {
        Map<RecipeBookType, TypeState> state = new EnumMap<>(RecipeBookType.class);
        for (RecipeBookType bookType : RecipeBookType.values()) {
            state.put(bookType, TypeState.read(wrapper));
        }
        return new RecipeBookSettings(state);
    }

    public static void write(PacketWrapper<?> wrapper, RecipeBookSettings settings) {
        for (RecipeBookType bookType : RecipeBookType.values()) {
            TypeState.write(wrapper, settings.getState(bookType));
        }
    }

    public TypeState getState(RecipeBookType type) {
        return this.states.computeIfAbsent(type, $ -> new TypeState());
    }

    public void setState(RecipeBookType type, TypeState state) {
        this.states.put(type, state);
    }

    public Map<RecipeBookType, TypeState> getStates() {
        return this.states;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RecipeBookSettings)) return false;
        RecipeBookSettings that = (RecipeBookSettings) obj;
        return this.states.equals(that.states);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.states);
    }

    @Override
    public String toString() {
        return "RecipeBookSettings{states=" + this.states + '}';
    }

    public static final class TypeState {

        private static final boolean DEFAULT_OPEN = false;
        private static final boolean DEFAULT_FILTERING = false;

        private boolean open;
        private boolean filtering;

        public TypeState() { // default
            this(DEFAULT_OPEN, DEFAULT_FILTERING);
        }

        public TypeState(boolean open, boolean filtering) {
            this.open = open;
            this.filtering = filtering;
        }

        public static TypeState read(PacketWrapper<?> wrapper) {
            boolean open = wrapper.readBoolean();
            boolean filtering = wrapper.readBoolean();
            return new TypeState(open, filtering);
        }

        public static void write(PacketWrapper<?> wrapper, TypeState state) {
            wrapper.writeBoolean(state.open);
            wrapper.writeBoolean(state.filtering);
        }

        public boolean isOpen() {
            return this.open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public boolean isFiltering() {
            return this.filtering;
        }

        public void setFiltering(boolean filtering) {
            this.filtering = filtering;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TypeState)) return false;
            TypeState typeState = (TypeState) obj;
            if (this.open != typeState.open) return false;
            return this.filtering == typeState.filtering;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.open, this.filtering);
        }

        @Override
        public String toString() {
            return "TypeState{open=" + this.open + ", filtering=" + this.filtering + '}';
        }
    }
}
