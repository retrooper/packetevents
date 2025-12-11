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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class ItemAttackRange {

    private float minRange;
    private float maxRange;
    private float minCreativeRange;
    private float maxCreativeRange;
    private float hitboxMargin;
    private float mobFactor;

    public ItemAttackRange(
            float minRange, float maxRange,
            float minCreativeRange, float maxCreativeRange,
            float hitboxMargin, float mobFactor
    ) {
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.minCreativeRange = minCreativeRange;
        this.maxCreativeRange = maxCreativeRange;
        this.hitboxMargin = hitboxMargin;
        this.mobFactor = mobFactor;
    }

    public static ItemAttackRange read(PacketWrapper<?> wrapper) {
        float minRange = wrapper.readFloat();
        float maxRange = wrapper.readFloat();
        float minCreativeRange = wrapper.readFloat();
        float maxCreativeRange = wrapper.readFloat();
        float hitboxMargin = wrapper.readFloat();
        float mobFactor = wrapper.readFloat();
        return new ItemAttackRange(minRange, maxRange, minCreativeRange, maxCreativeRange, hitboxMargin, mobFactor);
    }

    public static void write(PacketWrapper<?> wrapper, ItemAttackRange component) {
        wrapper.writeFloat(component.minRange);
        wrapper.writeFloat(component.maxRange);
        wrapper.writeFloat(component.minCreativeRange);
        wrapper.writeFloat(component.maxCreativeRange);
        wrapper.writeFloat(component.hitboxMargin);
        wrapper.writeFloat(component.mobFactor);
    }

    public float getMinRange() {
        return this.minRange;
    }

    public void setMinRange(float minRange) {
        this.minRange = minRange;
    }

    public float getMaxRange() {
        return this.maxRange;
    }

    public void setMaxRange(float maxRange) {
        this.maxRange = maxRange;
    }

    public float getMinCreativeRange() {
        return this.minCreativeRange;
    }

    public void setMinCreativeRange(float minCreativeRange) {
        this.minCreativeRange = minCreativeRange;
    }

    public float getMaxCreativeRange() {
        return this.maxCreativeRange;
    }

    public void setMaxCreativeRange(float maxCreativeRange) {
        this.maxCreativeRange = maxCreativeRange;
    }

    public float getHitboxMargin() {
        return this.hitboxMargin;
    }

    public void setHitboxMargin(float hitboxMargin) {
        this.hitboxMargin = hitboxMargin;
    }

    public float getMobFactor() {
        return this.mobFactor;
    }

    public void setMobFactor(float mobFactor) {
        this.mobFactor = mobFactor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemAttackRange that = (ItemAttackRange) obj;
        if (Float.compare(that.minRange, this.minRange) != 0) return false;
        if (Float.compare(that.maxRange, this.maxRange) != 0) return false;
        if (Float.compare(that.minCreativeRange, this.minCreativeRange) != 0) return false;
        if (Float.compare(that.maxCreativeRange, this.maxCreativeRange) != 0) return false;
        if (Float.compare(that.hitboxMargin, this.hitboxMargin) != 0) return false;
        return Float.compare(that.mobFactor, this.mobFactor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minRange, this.maxRange, this.minCreativeRange, this.maxCreativeRange, this.hitboxMargin, this.mobFactor);
    }
}
