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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class ItemBees {

    private List<BeeEntry> bees;

    public ItemBees(List<BeeEntry> bees) {
        this.bees = bees;
    }

    public static ItemBees read(PacketWrapper<?> wrapper) {
        List<BeeEntry> bees = wrapper.readList(BeeEntry::read);
        return new ItemBees(bees);
    }

    public static void write(PacketWrapper<?> wrapper, ItemBees bees) {
        wrapper.writeList(bees.bees, BeeEntry::write);
    }

    public void addBee(BeeEntry bee) {
        this.bees.add(bee);
    }

    public List<BeeEntry> getBees() {
        return this.bees;
    }

    public void setBees(List<BeeEntry> bees) {
        this.bees = bees;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemBees)) return false;
        ItemBees itemBees = (ItemBees) obj;
        return this.bees.equals(itemBees.bees);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.bees);
    }

    public static class BeeEntry {

        private NBTCompound entityData;
        private int ticksInHive;
        private int minTicksInHive;

        public BeeEntry(NBTCompound entityData, int ticksInHive, int minTicksInHive) {
            this.entityData = entityData;
            this.ticksInHive = ticksInHive;
            this.minTicksInHive = minTicksInHive;
        }

        public static BeeEntry read(PacketWrapper<?> wrapper) {
            NBTCompound entityData = wrapper.readNBT();
            int ticksInHive = wrapper.readVarInt();
            int minTicksInHive = wrapper.readVarInt();
            return new BeeEntry(entityData, ticksInHive, minTicksInHive);
        }

        public static void write(PacketWrapper<?> wrapper, BeeEntry bee) {
            wrapper.writeNBT(bee.entityData);
            wrapper.writeVarInt(bee.ticksInHive);
            wrapper.writeVarInt(bee.minTicksInHive);
        }

        public NBTCompound getEntityData() {
            return this.entityData;
        }

        public void setEntityData(NBTCompound entityData) {
            this.entityData = entityData;
        }

        public int getTicksInHive() {
            return this.ticksInHive;
        }

        public void setTicksInHive(int ticksInHive) {
            this.ticksInHive = ticksInHive;
        }

        public int getMinTicksInHive() {
            return this.minTicksInHive;
        }

        public void setMinTicksInHive(int minTicksInHive) {
            this.minTicksInHive = minTicksInHive;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BeeEntry)) return false;
            BeeEntry beeEntry = (BeeEntry) obj;
            if (this.ticksInHive != beeEntry.ticksInHive) return false;
            if (this.minTicksInHive != beeEntry.minTicksInHive) return false;
            return this.entityData.equals(beeEntry.entityData);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.entityData, this.ticksInHive, this.minTicksInHive);
        }
    }
}
