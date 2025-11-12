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

import com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerType;
import com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class VillagerVariantComponent {

    private VillagerType villagerType;

    public VillagerVariantComponent(VillagerType villagerType) {
        this.villagerType = villagerType;
    }

    public static VillagerVariantComponent read(PacketWrapper<?> wrapper) {
        VillagerType type = wrapper.readMappedEntity(VillagerTypes.getRegistry());
        return new VillagerVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, VillagerVariantComponent component) {
        wrapper.writeMappedEntity(component.villagerType);
    }

    public VillagerType getVillagerType() {
        return this.villagerType;
    }

    public void setVillagerType(VillagerType villagerType) {
        this.villagerType = villagerType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VillagerVariantComponent)) return false;
        VillagerVariantComponent that = (VillagerVariantComponent) obj;
        return this.villagerType.equals(that.villagerType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.villagerType);
    }
}
