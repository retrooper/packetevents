/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.inventory;

import com.github.retrooper.packetevents.protocol.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ItemTypes {
    public static final Map<ResourceLocation, ItemType> ITEM_TYPE_MAP = new HashMap<>();
    public static final Map<Integer, ItemType> ITEM_TYPE_ID_MAP = new HashMap<>();
    //TODO get id
    public static ItemType DIAMOND_SWORD = define(-1, 1, ResourceLocation.minecraft("diamond_sword"));


    public static ItemType define(int id, int maxAmount, ResourceLocation identifier) {
        ItemType type = new ItemType() {
            @Override
            public int getMaxAmount() {
                return maxAmount;
            }

            @Override
            public ResourceLocation getIdentifier() {
                return identifier;
            }

            @Override
            public int getId() {
                return id;
            }
        };
        ITEM_TYPE_MAP.put(type.getIdentifier(), type);
        ITEM_TYPE_ID_MAP.put(type.getId(), type);
        return type;
    }

    public static ItemType getByIdentifier(ResourceLocation identifier) {
        return ITEM_TYPE_MAP.get(identifier);
    }

    public static ItemType getById(int id) {
        return ITEM_TYPE_ID_MAP.get(id);
    }
}
