/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Contains legacy text and an adventure chat {@link Component}.
 * This is used to represent legacy text without loosing information by serializing/deserializing using adventure.
 */
@NullMarked
public final class LegacyComponent {

    private static final LegacyComponent EMPTY = new LegacyComponent(Component.empty(), "");

    private final Component component;
    private final String legacy;

    public LegacyComponent(Component component) {
        this(component, LegacyComponentSerializer.legacySection().serialize(component));
    }

    public LegacyComponent(String legacy) {
        this(LegacyComponentSerializer.legacySection().deserialize(legacy), legacy);
    }

    private LegacyComponent(Component component, String legacy) {
        this.component = component;
        this.legacy = legacy;
    }

    public static LegacyComponent wrapOrEmpty(@Nullable Component component) {
        if (component != null && component != Component.empty()) {
            return new LegacyComponent(component);
        }
        return EMPTY;
    }

    public static LegacyComponent empty() {
        return EMPTY;
    }

    public Component getComponent() {
        return this.component;
    }

    public String getLegacy() {
        return this.legacy;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        LegacyComponent that = (LegacyComponent) obj;
        if (!this.component.equals(that.component)) return false;
        return this.legacy.equals(that.legacy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.component, this.legacy);
    }
}
