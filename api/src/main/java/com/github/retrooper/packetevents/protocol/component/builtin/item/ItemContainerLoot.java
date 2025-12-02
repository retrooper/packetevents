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

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemContainerLoot {

    private ResourceLocation lootTable;
    private long seed;

    public ItemContainerLoot(ResourceLocation lootTable, long seed) {
        this.lootTable = lootTable;
        this.seed = seed;
    }

    public static ItemContainerLoot read(PacketWrapper<?> wrapper) {
        NBTCompound compound = wrapper.readNBT();
        ResourceLocation lootTable = new ResourceLocation(compound.getStringTagValueOrThrow("loot_table"));
        NBTNumber seedTag = compound.getNumberTagOrNull("seed");
        long seed = seedTag == null ? 0L : seedTag.getAsLong();
        return new ItemContainerLoot(lootTable, seed);
    }

    public static void write(PacketWrapper<?> wrapper, ItemContainerLoot loot) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("loot_table", new NBTString(loot.lootTable.toString()));
        if (loot.seed != 0L) {
            compound.setTag("seed", new NBTLong(loot.seed));
        }
        wrapper.writeNBT(compound);
    }

    public ResourceLocation getLootTable() {
        return this.lootTable;
    }

    public void setLootTable(ResourceLocation lootTable) {
        this.lootTable = lootTable;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemContainerLoot)) return false;
        ItemContainerLoot that = (ItemContainerLoot) obj;
        if (this.seed != that.seed) return false;
        return this.lootTable.equals(that.lootTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lootTable, this.seed);
    }
}
