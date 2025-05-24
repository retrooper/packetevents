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

import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class ItemDeathProtection {

    private List<ConsumeEffect<?>> deathEffects;

    public ItemDeathProtection(List<ConsumeEffect<?>> deathEffects) {
        this.deathEffects = deathEffects;
    }

    public static ItemDeathProtection read(PacketWrapper<?> wrapper) {
        List<ConsumeEffect<?>> deathEffects = wrapper.readList(ConsumeEffect::readFull);
        return new ItemDeathProtection(deathEffects);
    }

    public static void write(PacketWrapper<?> wrapper, ItemDeathProtection deathProtection) {
        wrapper.writeList(deathProtection.deathEffects, ConsumeEffect::writeFull);
    }

    public List<ConsumeEffect<?>> getDeathEffects() {
        return this.deathEffects;
    }

    public void setDeathEffects(List<ConsumeEffect<?>> deathEffects) {
        this.deathEffects = deathEffects;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemDeathProtection)) return false;
        ItemDeathProtection that = (ItemDeathProtection) obj;
        return this.deathEffects.equals(that.deathEffects);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.deathEffects);
    }

    @Override
    public String toString() {
        return "ItemDeathProtection{deathEffects=" + this.deathEffects + '}';
    }
}
