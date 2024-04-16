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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ItemProfile {

    private final @Nullable String name;
    private final @Nullable UUID id;
    private final List<Property> properties;

    public ItemProfile(@Nullable String name, @Nullable UUID id, List<Property> properties) {
        this.name = name;
        this.id = id;
        this.properties = properties;
    }

    public static ItemProfile read(PacketWrapper<?> wrapper) {
        String name = wrapper.readOptional(ew -> ew.readString(16));
        UUID id = wrapper.readOptional(PacketWrapper::readUUID);
        List<Property> properties = wrapper.readList(Property::read);
        return new ItemProfile(name, id, properties);
    }

    public static void write(PacketWrapper<?> wrapper, ItemProfile profile) {
        wrapper.writeOptional(profile.name, (ew, name) -> ew.writeString(name, 16));
        wrapper.writeOptional(profile.id, PacketWrapper::writeUUID);
        wrapper.writeList(profile.properties, Property::write);
    }

    public static class Property {

        private final String name;
        private final String value;
        private final @Nullable String signature;

        public Property(String name, String value, @Nullable String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public static Property read(PacketWrapper<?> wrapper) {
            String name = wrapper.readString(64);
            String value = wrapper.readString(32767);
            String signature = wrapper.readOptional(ew -> ew.readString(1024));
            return new Property(name, value, signature);
        }

        public static void write(PacketWrapper<?> wrapper, Property property) {
            wrapper.writeString(property.name, 64);
            wrapper.writeString(property.value, 32767);
            wrapper.writeOptional(property.signature,
                    (ew, signature) -> ew.writeString(signature, 1024));
        }
    }
}
