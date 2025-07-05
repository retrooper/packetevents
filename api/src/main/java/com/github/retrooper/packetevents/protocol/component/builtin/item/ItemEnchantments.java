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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public class ItemEnchantments implements Iterable<Map.Entry<EnchantmentType, Integer>> {

    public static final ItemEnchantments EMPTY = new ItemEnchantments(
            Collections.emptyMap(), true) {
        @Override
        public void setEnchantments(Map<EnchantmentType, Integer> enchantments) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setShowInTooltip(boolean showInTooltip) {
            throw new UnsupportedOperationException();
        }
    };

    private Map<EnchantmentType, Integer> enchantments;
    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    private boolean showInTooltip;

    public ItemEnchantments(Map<EnchantmentType, Integer> enchantments) {
        this(enchantments, true);
    }

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public ItemEnchantments(Map<EnchantmentType, Integer> enchantments, boolean showInTooltip) {
        this.enchantments = Collections.unmodifiableMap(enchantments);
        this.showInTooltip = showInTooltip;
    }

    public static ItemEnchantments read(PacketWrapper<?> wrapper) {
        Map<EnchantmentType, Integer> enchantments = wrapper.readMap(
                ew -> wrapper.readMappedEntity(EnchantmentTypes.getRegistry()),
                PacketWrapper::readVarInt
        );
        boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
        return new ItemEnchantments(enchantments, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemEnchantments enchantments) {
        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        wrapper.writeMap(enchantments.getEnchantments(),
                (ew, enchantment) -> ew.writeVarInt(enchantment.getId(version)),
                PacketWrapper::writeVarInt
        );
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeBoolean(enchantments.isShowInTooltip());
        }
    }

    public static ItemEnchantments decode(NBT nbt, ClientVersion version) {
        NBTCompound compound = (NBTCompound) nbt;

        NBTCompound levelsCompound;
        boolean showInTooltip;
        if (version.isOlderThan(ClientVersion.V_1_21_5)) {
            levelsCompound = compound.getCompoundTagOrNull("levels");
            showInTooltip = compound.getBoolean("show_in_tooltip");
        } else {
            levelsCompound = compound;
            showInTooltip = false;
        }

        Map<EnchantmentType, Integer> enchantments = new HashMap<>();
        if (levelsCompound != null) {
            for (Map.Entry<String, NBT> entry : levelsCompound.getTags().entrySet()) {
                EnchantmentType enchantment = EnchantmentTypes.getByName(entry.getKey());
                if (enchantment != null && entry.getValue() instanceof NBTNumber) {
                    enchantments.put(enchantment, ((NBTNumber) entry.getValue()).getAsInt());
                }
            }
        }

        return new ItemEnchantments(enchantments, showInTooltip);
    }

    public static NBT encode(ItemEnchantments enchantments, ClientVersion version) {
        NBTCompound levelsCompound = new NBTCompound();

        for (Map.Entry<EnchantmentType, Integer> entry : enchantments.getEnchantments().entrySet()) {
            levelsCompound.setTag(entry.getKey().getName().toString(), new NBTInt(entry.getValue()));
        }

        if (version.isOlderThan(ClientVersion.V_1_21_5)) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("levels", levelsCompound);
            compound.setTag("show_in_tooltip", new NBTByte(enchantments.isShowInTooltip()));
            return compound;
        } else {
            return levelsCompound;
        }
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

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
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
