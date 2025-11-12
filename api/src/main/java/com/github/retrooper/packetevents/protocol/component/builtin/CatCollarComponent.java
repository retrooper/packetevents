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

package com.github.retrooper.packetevents.protocol.component.builtin;

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class CatCollarComponent {

    private DyeColor color;

    public CatCollarComponent(DyeColor color) {
        this.color = color;
    }

    public static CatCollarComponent read(PacketWrapper<?> wrapper) {
        DyeColor type = DyeColor.read(wrapper);
        return new CatCollarComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, CatCollarComponent component) {
        DyeColor.write(wrapper, component.color);
    }

    public DyeColor getDyeColor() {
        return this.color;
    }

    public void setDyeColor(DyeColor color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CatCollarComponent)) return false;
        CatCollarComponent that = (CatCollarComponent) obj;
        return this.color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.color);
    }
}
