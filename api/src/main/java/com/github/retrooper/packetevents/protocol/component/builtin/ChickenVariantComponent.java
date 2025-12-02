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

import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariant;
import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariants;
import com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ChickenVariantComponent {

    private MaybeMappedEntity<ChickenVariant> variant;

    public ChickenVariantComponent(MaybeMappedEntity<ChickenVariant> variant) {
        this.variant = variant;
    }

    public static ChickenVariantComponent read(PacketWrapper<?> wrapper) {
        MaybeMappedEntity<ChickenVariant> variant = MaybeMappedEntity.read(wrapper,
                ChickenVariants.getRegistry(), ChickenVariant::read);
        return new ChickenVariantComponent(variant);
    }

    public static void write(PacketWrapper<?> wrapper, ChickenVariantComponent component) {
        MaybeMappedEntity.write(wrapper, component.variant, ChickenVariant::write);
    }

    public MaybeMappedEntity<ChickenVariant> getVariant() {
        return this.variant;
    }

    public void setVariant(MaybeMappedEntity<ChickenVariant> variant) {
        this.variant = variant;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChickenVariantComponent)) return false;
        ChickenVariantComponent that = (ChickenVariantComponent) obj;
        return this.variant.equals(that.variant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.variant);
    }
}
