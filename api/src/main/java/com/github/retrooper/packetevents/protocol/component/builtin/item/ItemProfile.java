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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemProfile {

    private @Nullable String name;
    private @Nullable UUID id;
    private List<Property> properties;

    public ItemProfile(
            @Nullable String name,
            @Nullable UUID id,
            List<Property> properties
    ) {
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

    public @Nullable String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public @Nullable UUID getId() {
        return this.id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemProfile)) return false;
        ItemProfile that = (ItemProfile) obj;
        if (!Objects.equals(this.name, that.name)) return false;
        if (!Objects.equals(this.id, that.id)) return false;
        return this.properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.id, this.properties);
    }

    public static class Property {

        private String name;
        private String value;
        private @Nullable String signature;

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

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public @Nullable String getSignature() {
            return this.signature;
        }

        public void setSignature(@Nullable String signature) {
            this.signature = signature;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Property)) return false;
            Property property = (Property) obj;
            if (!this.name.equals(property.name)) return false;
            if (!this.value.equals(property.value)) return false;
            return Objects.equals(this.signature, property.signature);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.value, this.signature);
        }
    }
}
