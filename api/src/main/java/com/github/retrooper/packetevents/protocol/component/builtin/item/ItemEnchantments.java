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

import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ItemEnchantments implements Iterable<Map.Entry<EnchantmentType, Integer>> {

    public static final ItemEnchantments EMPTY = new ItemEnchantments(
            Collections.emptyMap(), true) {
        @Override
        public void setShowInTooltip(boolean showInTooltip) {
            throw new UnsupportedOperationException();
        }
    };

    private Map<EnchantmentType, Integer> enchantments;
    private boolean showInTooltip;

    public ItemEnchantments(Map<EnchantmentType, Integer> enchantments, boolean showInTooltip) {
        this.enchantments = Collections.unmodifiableMap(enchantments);
        this.showInTooltip = showInTooltip;
    }

    public static ItemEnchantments read(PacketWrapper<?> wrapper) {
        Map<EnchantmentType, Integer> enchantments = wrapper.readMap(
                ew -> wrapper.readMappedEntity(EnchantmentTypes.getRegistry()),
                PacketWrapper::readVarInt
        );
        boolean showInTooltip = wrapper.readBoolean();
        return new ItemEnchantments(enchantments, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemEnchantments enchantments) {
        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        wrapper.writeMap(enchantments.getEnchantments(),
                (ew, enchantment) -> ew.writeVarInt(enchantment.getId(version)),
                PacketWrapper::writeVarInt
        );
        wrapper.writeBoolean(enchantments.isShowInTooltip());
    }

    public int getEnchantmentLevel(EnchantmentType enchantment) {
        return this.enchantments.getOrDefault(enchantment, 0);
    }

    public void setEnchantmentLevel(EnchantmentType enchantment, int level) {
        if (level == 0) {
            this.enchantments.remove(enchantment);
        } else {
            this.enchantments.put(enchantment, level);
        }
    }

    public boolean isEmpty() {
        return this.getEnchantmentCount() < 1;
    }

    public int getEnchantmentCount() {
        return this.enchantments.size();
    }

    public Map<EnchantmentType, Integer> getEnchantments() {
        return this.enchantments;
    }

    public void setEnchantments(Map<EnchantmentType, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    @Override
    public Iterator<Map.Entry<EnchantmentType, Integer>> iterator() {
        return this.enchantments.entrySet().iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemEnchantments)) return false;
        ItemEnchantments that = (ItemEnchantments) obj;
        if (this.showInTooltip != that.showInTooltip) return false;
        return this.enchantments.equals(that.enchantments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enchantments, this.showInTooltip);
    }

    @Override
    public String toString() {
        return "ItemEnchantments{enchantments=" + this.enchantments + ", showInTooltip=" + this.showInTooltip + '}';
    }
}
