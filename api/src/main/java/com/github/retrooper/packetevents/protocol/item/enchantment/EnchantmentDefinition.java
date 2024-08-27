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

package com.github.retrooper.packetevents.protocol.item.enchantment;

import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers.EquipmentSlotGroup;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil.indexValueOrThrow;

public final class EnchantmentDefinition {

    private final MappedEntitySet<ItemType> supportedItems;
    private final Optional<MappedEntitySet<ItemType>> primaryItems;
    private final int weight;
    private final int maxLevel;
    private final EnchantmentCost minCost;
    private final EnchantmentCost maxCost;
    private final int anvilCost;
    private final List<EquipmentSlotGroup> slots;

    public EnchantmentDefinition(
            MappedEntitySet<ItemType> supportedItems,
            Optional<MappedEntitySet<ItemType>> primaryItems,
            int weight, int maxLevel,
            EnchantmentCost minCost, EnchantmentCost maxCost,
            int anvilCost, List<EquipmentSlotGroup> slots
    ) {
        this.supportedItems = supportedItems;
        this.primaryItems = primaryItems;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.anvilCost = anvilCost;
        this.slots = slots;
    }

    public static EnchantmentDefinition decode(NBT nbt, ClientVersion version) {
        NBTCompound compound = (NBTCompound) nbt;
        MappedEntitySet<ItemType> supportedItems = MappedEntitySet.decode(
                compound.getTagOrThrow("supported_items"), version, ItemTypes.getRegistry());
        Optional<MappedEntitySet<ItemType>> primaryItems = Optional.ofNullable(compound.getTagOrNull("primary_items"))
                .map(items -> MappedEntitySet.decode(items, version, ItemTypes.getRegistry()));
        int weight = compound.getNumberTagOrThrow("weight").getAsInt();
        int maxLevel = compound.getNumberTagOrThrow("max_level").getAsInt();
        EnchantmentCost minCost = EnchantmentCost.decode(compound.getTagOrThrow("min_cost"), version);
        EnchantmentCost maxCost = EnchantmentCost.decode(compound.getTagOrThrow("max_cost"), version);
        int anvilCost = compound.getNumberTagOrThrow("anvil_cost").getAsInt();

        NBT slotsTag = compound.getTagOrThrow("slots");
        List<EquipmentSlotGroup> slots;
        if (slotsTag instanceof NBTList<?>) {
            NBTList<?> slotsTagList = (NBTList<?>) slotsTag;
            slots = new ArrayList<>(slotsTagList.size());
            for (NBT tag : slotsTagList.getTags()) {
                String slotGroupId = ((NBTString) tag).getValue();
                slots.add(indexValueOrThrow(EquipmentSlotGroup.ID_INDEX, slotGroupId));
            }
        } else {
            String slotGroupId = ((NBTString) slotsTag).getValue();
            EquipmentSlotGroup slotGroup = indexValueOrThrow(EquipmentSlotGroup.ID_INDEX, slotGroupId);
            slots = Collections.singletonList(slotGroup);
        }

        return new EnchantmentDefinition(supportedItems, primaryItems, weight,
                maxLevel, minCost, maxCost, anvilCost, slots);
    }

    public static NBT encode(EnchantmentDefinition definition, ClientVersion version) {
        NBTList<NBTString> slotsTag = NBTList.createStringList();
        for (EquipmentSlotGroup slot : definition.slots) {
            slotsTag.addTag(new NBTString(slot.getId()));
        }

        NBTCompound compound = new NBTCompound();
        compound.setTag("supported_items", MappedEntitySet.encode(
                definition.supportedItems, version));
        definition.primaryItems.ifPresent(set -> compound.setTag(
                "primary_items", MappedEntitySet.encode(set, version)));
        compound.setTag("weight", new NBTInt(definition.weight));
        compound.setTag("max_level", new NBTInt(definition.maxLevel));
        compound.setTag("min_cost", EnchantmentCost.encode(definition.minCost, version));
        compound.setTag("max_cost", EnchantmentCost.encode(definition.maxCost, version));
        compound.setTag("anvil_cost", new NBTInt(definition.anvilCost));
        compound.setTag("slots", slotsTag);
        return compound;
    }

    public MappedEntitySet<ItemType> getSupportedItems() {
        return this.supportedItems;
    }

    public Optional<MappedEntitySet<ItemType>> getPrimaryItems() {
        return this.primaryItems;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public EnchantmentCost getMinCost() {
        return this.minCost;
    }

    public EnchantmentCost getMaxCost() {
        return this.maxCost;
    }

    public int getAnvilCost() {
        return this.anvilCost;
    }

    public List<EquipmentSlotGroup> getSlots() {
        return this.slots;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EnchantmentDefinition)) return false;
        EnchantmentDefinition that = (EnchantmentDefinition) obj;
        if (this.weight != that.weight) return false;
        if (this.maxLevel != that.maxLevel) return false;
        if (this.anvilCost != that.anvilCost) return false;
        if (!this.supportedItems.equals(that.supportedItems)) return false;
        if (!this.primaryItems.equals(that.primaryItems)) return false;
        if (!this.minCost.equals(that.minCost)) return false;
        if (!this.maxCost.equals(that.maxCost)) return false;
        return this.slots.equals(that.slots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.supportedItems, this.primaryItems, this.weight, this.maxLevel, this.minCost, this.maxCost, this.anvilCost, this.slots);
    }

    @Override
    public String toString() {
        return "EnchantmentDefinition{supportedItems=" + this.supportedItems + ", primaryItems=" + this.primaryItems + ", weight=" + this.weight + ", maxLevel=" + this.maxLevel + ", minCost=" + this.minCost + ", maxCost=" + this.maxCost + ", anvilCost=" + this.anvilCost + ", slots=" + this.slots + '}';
    }
}
