/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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
 * @versions 26.1+
 */
@NullMarked
public class ItemAdditionalTradeCost {

    private final int cost;

    public ItemAdditionalTradeCost(int cost) {
        this.cost = cost;
    }

    public static ItemAdditionalTradeCost read(PacketWrapper<?> wrapper) {
        return new ItemAdditionalTradeCost(wrapper.readVarInt());
    }

    public static void write(PacketWrapper<?> wrapper, ItemAdditionalTradeCost cost) {
        wrapper.writeVarInt(cost.cost);
    }

    public ItemAdditionalTradeCost withCost(int cost) {
        return new ItemAdditionalTradeCost(cost);
    }

    public int getCost() {
        return this.cost;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemAdditionalTradeCost)) return false;
        ItemAdditionalTradeCost that = (ItemAdditionalTradeCost) obj;
        return this.cost == that.cost;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.cost);
    }
}
