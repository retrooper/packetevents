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

package com.github.retrooper.packetevents.protocol.data.nbt;

import java.util.Objects;

public class NBTString extends NBT {

    protected final String string;

    public NBTString(String string) {
        this.string = string;
    }

    @Override
    public NBTType<NBTString> getType() {
        return NBTType.STRING;
    }

    public String getValue() {
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NBTString other = (NBTString) obj;
        return Objects.equals(string, other.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

}
