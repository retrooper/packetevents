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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DebugStickState {

    private Map<StateType, String> properties;

    public DebugStickState(Map<StateType, String> properties) {
        this.properties = properties;
    }

    public static DebugStickState read(PacketWrapper<?> wrapper) {
        NBTCompound compound = wrapper.readNBT();
        Map<StateType, String> properties = new HashMap<>(compound.size());
        for (Map.Entry<String, NBT> tag : compound.getTags().entrySet()) {
            StateType stateType = StateTypes.getByName(tag.getKey());
            String property = ((NBTString) tag.getValue()).getValue();
            properties.put(stateType, property);
        }
        return new DebugStickState(properties);
    }

    public static void write(PacketWrapper<?> wrapper, DebugStickState state) {
        NBTCompound compound = new NBTCompound();
        for (Map.Entry<StateType, String> property : state.properties.entrySet()) {
            compound.setTag(property.getKey().getName(),
                    new NBTString(property.getValue()));
        }
        wrapper.writeNBT(compound);
    }

    public @Nullable String getProperty(StateType stateType) {
        return this.properties.get(stateType);
    }

    public void setProperty(StateType stateType, @Nullable String property) {
        if (property != null) {
            this.properties.put(stateType, property);
        } else {
            this.properties.remove(stateType);
        }
    }

    public Map<StateType, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<StateType, String> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DebugStickState)) return false;
        DebugStickState that = (DebugStickState) o;
        return this.properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.properties);
    }
}
