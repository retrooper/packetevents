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

package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.component.ComponentPredicate;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class MerchantItemCost {

    private ItemType item;
    private int count;
    private ComponentPredicate predicate;

    public MerchantItemCost(ItemType item) {
        this(item, 1);
    }

    public MerchantItemCost(ItemType item, int count) {
        this(item, count, ComponentPredicate.emptyPredicate());
    }

    public MerchantItemCost(ItemType item, int count, ComponentPredicate predicate) {
        this.item = item;
        this.count = count;
        this.predicate = predicate;
    }

    @Contract("null -> null; !null -> !null")
    public static @Nullable MerchantItemCost ofItem(@Nullable ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (stack.isEmpty()) {
            return MerchantItemCost.emptyCost();
        }
        ComponentPredicate predicate = ComponentPredicate.fromPatches(stack.getComponents());
        return new MerchantItemCost(stack.getType(), stack.getAmount(), predicate);
    }

    public static ItemStack readItem(PacketWrapper<?> wrapper) {
        return wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_5)
                ? wrapper.readItemStack() : read(wrapper).asItem();
    }

    public static MerchantItemCost read(PacketWrapper<?> wrapper) {
        ItemType item = wrapper.readMappedEntity(ItemTypes::getById);
        int count = wrapper.readVarInt();
        ComponentPredicate predicate = ComponentPredicate.read(wrapper);
        return new MerchantItemCost(item, count, predicate);
    }

    public static void writeItem(PacketWrapper<?> wrapper, ItemStack costItem) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_5)) {
            wrapper.writeItemStack(costItem);
        } else {
            write(wrapper, ofItem(costItem));
        }
    }

    public static void write(PacketWrapper<?> wrapper, MerchantItemCost cost) {
        wrapper.writeMappedEntity(cost.item);
        wrapper.writeVarInt(cost.count);
        ComponentPredicate.write(wrapper, cost.predicate);
    }

    public static MerchantItemCost emptyCost() {
        return new MerchantItemCost(ItemTypes.AIR, 0);
    }

    public ItemStack asItem() {
        return ItemStack.builder().type(this.item).amount(this.count)
                .components(this.predicate.asPatches(this.item.getComponents()))
                .build();
    }

    public ItemType getItem() {
        return this.item;
    }

    public void setItem(ItemType item) {
        this.item = item;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ComponentPredicate getPredicate() {
        return this.predicate;
    }

    public void setPredicate(ComponentPredicate predicate) {
        this.predicate = predicate;
    }
}
