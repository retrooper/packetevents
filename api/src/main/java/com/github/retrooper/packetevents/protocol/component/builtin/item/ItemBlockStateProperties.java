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

import com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class ItemBlockStateProperties {

    private Map<String, String> properties;

    public ItemBlockStateProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public static ItemBlockStateProperties read(PacketWrapper<?> wrapper) {
        Map<String, String> properties = wrapper.readMap(
                PacketWrapper::readString, PacketWrapper::readString);
        return new ItemBlockStateProperties(properties);
    }

    public static void write(PacketWrapper<?> wrapper, ItemBlockStateProperties props) {
        wrapper.writeMap(props.properties,
                PacketWrapper::writeString,
                PacketWrapper::writeString);
    }

    public @Nullable Object getProperty(StateValue stateValue) {
        String value = this.getProperty(stateValue.getName());
        if (value != null) {
            return stateValue.getParser().apply(value);
        }
        return null;
    }

    public @Nullable String getProperty(String key) {
        return this.properties.get(key);
    }

    public void setProperty(StateValue stateValue, @Nullable Object value) {
        this.setProperty(stateValue.getName(), value == null ? null : value.toString());
    }

    public void setProperty(String key, @Nullable String value) {
        if (value == null) {
            this.properties.remove(key);
        } else {
            this.properties.put(key, value);
        }
    }

    public void unsetProperty(StateValue stateValue) {
        this.unsetProperty(stateValue.getName());
    }

    public void unsetProperty(String key) {
        this.setProperty(key, null);
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemBlockStateProperties)) return false;
        ItemBlockStateProperties that = (ItemBlockStateProperties) obj;
        return this.properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.properties);
    }
}
