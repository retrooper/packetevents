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
import com.github.retrooper.packetevents.manager.server.MultiVersion;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerEntityProperties extends PacketWrapper<WrapperPlayServerEntityProperties> {
    private int entityId;
    private List<Property> properties;

    public WrapperPlayServerEntityProperties(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityProperties(int entityId, List<Property> properties) {
        super(PacketType.Play.Server.ENTITY_PROPERTIES);
        this.entityId = entityId;
        this.properties = properties;
    }

    @Override
    public void read() {
        entityId = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8,
                PacketWrapper::readVarInt,
                PacketWrapper::readInt);
        int propertyCount = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_17,
                PacketWrapper::readVarInt,
                PacketWrapper::readInt);
        properties = new ArrayList<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            int maxKeyLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) ? 32767 : 64;
            String key = readString(maxKeyLength);
            double value = readDouble();
            int modifiersLength = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8,
                    PacketWrapper::readVarInt,
                    PacketWrapper::readShort);
            List<PropertyModifier> modifiers = new ArrayList<>(modifiersLength);
            for (int j = 0; j < modifiersLength; j++) {
                UUID uuid = readUUID();
                // Name always had this value, and was never part of the protocol, so why include it?
                // String name = "Unknown synced attribute modifier";
                double amount = readDouble();
                byte operationIndex = readByte();
                PropertyModifier.Operation operation = PropertyModifier.Operation.getById(operationIndex);
                modifiers.add(new PropertyModifier(uuid, amount, operation));
            }
            properties.add(new Property(key, value, modifiers));
        }
    }

    @Override
    public void write() {
        writeMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, entityId,
                PacketWrapper::writeVarInt,
                PacketWrapper::writeInt);
        writeMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_17, properties.size(),
                PacketWrapper::writeVarInt,
                PacketWrapper::writeInt);
        for (Property property : properties) {
            int maxKeyLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) ? 32767 : 64;
            writeString(property.key, maxKeyLength);
            writeDouble(property.value);
            writeMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, property.modifiers.size(),
                    PacketWrapper::writeVarInt,
                    PacketWrapper::writeShort);
            for (PropertyModifier modifier : property.modifiers) {
                writeUUID(modifier.uuid);
                writeDouble(modifier.amount);
                writeByte(modifier.operation.ordinal());
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerEntityProperties wrapper) {
        entityId = wrapper.entityId;
        properties = wrapper.properties;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityID) {
        this.entityId = entityID;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public static class PropertyModifier {
        public enum Operation {
            ADDITION,
            MULTIPLY_BASE,
            MULTIPLY_TOTAL;

            private static final Operation[] VALUES = values();

            public static Operation getById(int index) {
                return VALUES[index];
            }
        }

        private UUID uuid;
        private double amount;
        private Operation operation;

        public PropertyModifier(UUID uuid, double amount, Operation operation) {
            this.uuid = uuid;
            this.amount = amount;
            this.operation = operation;
        }

        public UUID getUUID() {
            return uuid;
        }

        public void setUUID(UUID uuid) {
            this.uuid = uuid;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Operation getOperation() {
            return operation;
        }

        public void setOperation(Operation operation) {
            this.operation = operation;
        }
    }

    public static class Property {
        private String key;
        private double value;
        private List<PropertyModifier> modifiers;

        public Property(String key, double value, List<PropertyModifier> modifiers) {
            this.key = key;
            this.value = value;
            this.modifiers = modifiers;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public List<PropertyModifier> getModifiers() {
            return modifiers;
        }

        public void setModifiers(List<PropertyModifier> modifiers) {
            this.modifiers = modifiers;
        }
    }
}
