/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.attribute;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AttributeDisplayTypes {

    private static final VersionedRegistry<AttributeDisplayType<?>> REGISTRY = new VersionedRegistry<>("attribute_display_type");

    private AttributeDisplayTypes() {
    }

    @ApiStatus.Internal
    public static <T extends AttributeDisplay> AttributeDisplayType<T> define(
            String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer
    ) {
        return REGISTRY.define(name, data ->
                new StaticAttributeDisplayType<>(data, reader, writer));
    }

    public static VersionedRegistry<AttributeDisplayType<?>> getRegistry() {
        return REGISTRY;
    }

    public static final AttributeDisplayType<DefaultAttributeDisplay> DEFAULT = define("default",
            DefaultAttributeDisplay::read, DefaultAttributeDisplay::write);
    public static final AttributeDisplayType<HiddenAttributeDisplay> HIDDEN = define("hidden",
            HiddenAttributeDisplay::read, HiddenAttributeDisplay::write);
    public static final AttributeDisplayType<OverrideAttributeDisplay> OVERRIDE = define("override",
            OverrideAttributeDisplay::read, OverrideAttributeDisplay::write);

    static {
        REGISTRY.unloadMappings();
    }
}
