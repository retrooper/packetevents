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

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class ItemBees {

    private final List<BeeEntry> bees;

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

    public static class BeeEntry {

        private final NBTCompound entityData;
        private final int ticksInHive;
        private final int minTicksInHive;

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
    }
}
