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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerEntityProperties extends PacketWrapper<WrapperPlayServerEntityProperties> {
    private int entityID;
    private List<Property> properties;

    public WrapperPlayServerEntityProperties(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityProperties(int entityID, List<Property> properties) {
        super(PacketType.Play.Server.ENTITY_PROPERTIES);
        this.entityID = entityID;
        this.properties = properties;
    }

    @Override
    public void readData() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            entityID = readInt();
        }
        else {
            entityID = readVarInt();
        }

        int propertyCount = readInt();
        properties = new ArrayList<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            String key = readString(64);
            double value = readDouble();
            int modifiersLength;
            if (serverVersion == ServerVersion.V_1_7_10) {
                 modifiersLength = readShort();
            }
            else {
                modifiersLength = readVarInt();
            }
            List<PropertyModifier> modifiers = new ArrayList<>(modifiersLength);
            for (int j = 0; j < modifiersLength; j++) {
                UUID uuid = readUUID();
                //Name always had this value, and was never part of the protocol, so why include it?
                //String name = "Unknown synced attribute modifier";
                double amount = readDouble();
                byte operationIndex = readByte();
                PropertyModifier.Operation operation = PropertyModifier.Operation.VALUES[operationIndex];
                modifiers.add(new PropertyModifier(uuid, amount, operation));
            }
            properties.add(new Property(key, value, modifiers));
        }
    }

    @Override
    public void readData(WrapperPlayServerEntityProperties wrapper) {
        entityID = wrapper.entityID;
        properties = wrapper.properties;
    }

    @Override
    public void writeData() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
        }
        else {
            writeVarInt(entityID);
        }

        writeInt(properties.size());
        for (Property property : properties) {
            writeString(property.key, 64);
            writeDouble(property.value);
            if (serverVersion == ServerVersion.V_1_7_10) {
                writeShort(property.modifiers.size());
            }
            else {
                writeVarInt(property.modifiers.size());
            }
            for (PropertyModifier modifier : property.modifiers) {
                writeUUID(modifier.uuid);
                writeDouble(modifier.amount);
                writeByte(modifier.operation.ordinal());
            }
        }
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
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

            public static final Operation[] VALUES = values();
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
