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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Collections;
import java.util.Map;

public class ItemEnchantments {

    private final Map<EnchantmentType, Integer> enchantments;
    private final boolean showInTooltip;

    public ItemEnchantments(Map<EnchantmentType, Integer> enchantments, boolean showInTooltip) {
        this.enchantments = Collections.unmodifiableMap(enchantments);
        this.showInTooltip = showInTooltip;
    }

    public static ItemEnchantments read(PacketWrapper<?> wrapper) {
        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        Map<EnchantmentType, Integer> enchantments = wrapper.readMap(
                ew -> EnchantmentTypes.getById(version, ew.readVarInt()),
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

    public Map<EnchantmentType, Integer> getEnchantments() {
        return this.enchantments;
    }

    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }
}
