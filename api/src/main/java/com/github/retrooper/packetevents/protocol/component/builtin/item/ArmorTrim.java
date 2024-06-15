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

import com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterial;
import com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ArmorTrim {

    private TrimMaterial material;
    private TrimPattern pattern;
    private boolean showInTooltip;

    public ArmorTrim(TrimMaterial material, TrimPattern pattern, boolean showInTooltip) {
        this.material = material;
        this.pattern = pattern;
        this.showInTooltip = showInTooltip;
    }

    public static ArmorTrim read(PacketWrapper<?> wrapper) {
        TrimMaterial material = TrimMaterial.read(wrapper);
        TrimPattern pattern = TrimPattern.read(wrapper);
        boolean showInTooltip = wrapper.readBoolean();
        return new ArmorTrim(material, pattern, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ArmorTrim trim) {
        TrimMaterial.write(wrapper, trim.material);
        TrimPattern.write(wrapper, trim.pattern);
        wrapper.writeBoolean(trim.showInTooltip);
    }

    public TrimMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(TrimMaterial material) {
        this.material = material;
    }

    public TrimPattern getPattern() {
        return this.pattern;
    }

    public void setPattern(TrimPattern pattern) {
        this.pattern = pattern;
    }

    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ArmorTrim)) return false;
        ArmorTrim armorTrim = (ArmorTrim) obj;
        if (this.showInTooltip != armorTrim.showInTooltip) return false;
        if (!this.material.equals(armorTrim.material)) return false;
        return this.pattern.equals(armorTrim.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.material, this.pattern, this.showInTooltip);
    }

    @Override
    public String toString() {
        return "ArmorTrim{material=" + this.material + ", pattern=" + this.pattern + ", showInTooltip=" + this.showInTooltip + '}';
    }
}
