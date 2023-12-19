/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerUpdateAttributes extends PacketWrapper<WrapperPlayServerUpdateAttributes> {
    private int entityID;
    private List<Property> properties;

    public WrapperPlayServerUpdateAttributes(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateAttributes(int entityID, List<Property> properties) {
        super(PacketType.Play.Server.UPDATE_ATTRIBUTES);
        this.entityID = entityID;
        this.properties = properties;
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            entityID = readInt();
        } else {
            entityID = readVarInt();
        }

        int propertyCount;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            propertyCount = readVarInt();
        } else {
            propertyCount = readInt();
        }
        properties = new ArrayList<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            Attribute attribute;
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                attribute = Attributes.getById(this.serverVersion.toClientVersion(), this.readVarInt());
            } else {
                //NOTE: Some people report errors that this limit check breaks for them, lets try removing it
                //int maxKeyLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) ? 32767 : 64;
                int maxKeyLength = 32767;
                String key = this.readString(maxKeyLength);
                attribute = Attributes.getByName(key);
            }

            double value = readDouble();
            int modifiersLength;
            if (serverVersion == ServerVersion.V_1_7_10) {
                modifiersLength = readShort();
            } else {
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
            this.properties.add(new Property(attribute, value, modifiers));
        }
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeInt(entityID);
        } else {
            writeVarInt(entityID);
        }

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeVarInt(properties.size());
        } else {
            writeInt(properties.size());
        }
        for (Property property : properties) {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                this.writeVarInt(property.getAttribute().getId(this.serverVersion.toClientVersion()));
            } else {
                int maxKeyLength = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) ? 32767 : 64;
                this.writeString(property.getKey(), maxKeyLength);
            }

            writeDouble(property.value);
            if (serverVersion == ServerVersion.V_1_7_10) {
                writeShort(property.modifiers.size());
            } else {
                writeVarInt(property.modifiers.size());
            }
            for (PropertyModifier modifier : property.modifiers) {
                writeUUID(modifier.uuid);
                writeDouble(modifier.amount);
                writeByte(modifier.operation.ordinal());
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerUpdateAttributes wrapper) {
        entityID = wrapper.entityID;
        properties = wrapper.properties;
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

        private Attribute attribute;
        private double value;
        private List<PropertyModifier> modifiers;

        @Deprecated
        public Property(String key, double value, List<PropertyModifier> modifiers) {
            this(Attributes.getByName(key), value, modifiers);
        }

        public Property(Attribute attribute, double value, List<PropertyModifier> modifiers) {
            this.attribute = attribute;
            this.value = value;
            this.modifiers = modifiers;
        }

        public Attribute getAttribute() {
            return this.attribute;
        }

        public void setAttribute(Attribute attribute) {
            this.attribute = attribute;
        }

        @Deprecated
        public String getKey() {
            return this.getAttribute().getName().toString();
        }

        @Deprecated
        public void setKey(String key) {
            this.setAttribute(Attributes.getByName(key));
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
