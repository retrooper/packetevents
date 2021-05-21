/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.exceptions;

import io.github.retrooper.packetevents.utils.reflection.ClassUtil;

/**
 * An exception thrown by PacketEvents when a wrapper fails
 * to find a field.
 *
 * @author retrooper
 * @see io.github.retrooper.packetevents.packetwrappers.WrappedPacket#read(int, Class)
 * @see io.github.retrooper.packetevents.packetwrappers.WrappedPacket#write(Class, int, Object)
 * @since 1.6.9
 */
public class WrapperFieldNotFoundException extends RuntimeException {
    public WrapperFieldNotFoundException(String message) {
        super(message);
    }

    public WrapperFieldNotFoundException(Class<?> packetClass, Class<?> type, int index) {
        this("PacketEvents failed to find a " + ClassUtil.getClassSimpleName(type) + " indexed " + index + " by its type in the " + packetClass.getName() + " class!");
    }
}
