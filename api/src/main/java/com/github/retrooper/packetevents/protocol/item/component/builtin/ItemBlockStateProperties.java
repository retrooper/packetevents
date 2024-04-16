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

import java.util.Map;

public class ItemBlockStateProperties {

    private final Map<String, String> properties;

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
}
