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

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerEntityEquipment extends PacketWrapper<WrapperPlayServerEntityEquipment> {
    private static final IndexOutOfBoundsException OUT_OF_BOUNDS_EXCEPTION;

    static {
        OUT_OF_BOUNDS_EXCEPTION = new IndexOutOfBoundsException("Index of EquipmentSlot is out of bounds");
    }

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

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            entityId = readInt();
        } else {
            entityId = readVarInt();
        }
        equipment = new ArrayList<>();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            byte value;
            do {
                value = readByte();
                Optional<EquipmentSlot> equipmentSlot = EquipmentSlot.byId(value & Byte.MAX_VALUE);
                if (!equipmentSlot.isPresent()) {
                    throw OUT_OF_BOUNDS_EXCEPTION;
                }
                ItemStack itemStack = readItemStack();
                equipment.add(new Equipment(equipmentSlot.get(), itemStack));
            } while ((value & Byte.MIN_VALUE) != 0);
        } else {
            Optional<EquipmentSlot> equipmentSlot;
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                equipmentSlot = EquipmentSlot.byId(readVarInt());
            } else {
                equipmentSlot = EquipmentSlot.byId(readShort());
            }

            if (!equipmentSlot.isPresent()) {
                throw OUT_OF_BOUNDS_EXCEPTION;
            }
            equipment.add(new Equipment(equipmentSlot.get(), readItemStack()));
        }
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityId);
        } else {
            writeVarInt(entityId);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            for (int i = 0; i < this.equipment.size(); i++) {
                Equipment equipment = this.equipment.get(i);
                boolean last = i == (this.equipment.size() - 1);
                writeByte(last ? equipment.getSlot().getId(serverVersion) : (equipment.getSlot().getId(serverVersion) | Byte.MIN_VALUE));
                writeItemStack(equipment.getItem());
            }
        } else {
            Equipment equipment = this.equipment.get(0);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                writeVarInt(equipment.getSlot().getId(serverVersion));
            } else {
                writeShort(equipment.getSlot().getId(serverVersion));
            }
            writeItemStack(equipment.getItem());
        }
    }

    @Override
    public void copy(WrapperPlayServerEntityEquipment wrapper) {
        entityId = wrapper.entityId;
        equipment = wrapper.equipment;
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
}
