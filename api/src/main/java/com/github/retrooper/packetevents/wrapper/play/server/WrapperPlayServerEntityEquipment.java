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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Arrays;

public class WrapperPlayServerEntityEquipment extends PacketWrapper<WrapperPlayServerEntityEquipment> {
    private int entityId;
    private List<Equipment> equipment;
    public WrapperPlayServerEntityEquipment(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityEquipment(int entityId, List<Equipment> equipment) {
        super(PacketType.Play.Server.ENTITY_EQUIPMENT);
        this.entityId = entityId;
        this.equipment = equipment;
    }

    public WrapperPlayServerEntityEquipment(int entityId, Equipment... equipment) {
        this(entityId, Arrays.asList(equipment));
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }


    public enum EquipmentSlot {
        MAINHAND,
        OFFHAND,
        BOOTS,
        LEGGINGS,
        CHESTPLATE,
        HELMET
    }

    public static class Equipment {
        private EquipmentSlot slot;
        private ItemStack item;

        public Equipment(EquipmentSlot slot, ItemStack item) {
            this.slot = slot;
            this.item = item;
        }

        public EquipmentSlot getSlot() {
            return slot;
        }

        public void setSlot(EquipmentSlot slot) {
            this.slot = slot;
        }

        public ItemStack getItem() {
            return item;
        }

        public void setItem(ItemStack item) {
            this.item = item;
        }
    }
}
