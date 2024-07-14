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
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class WrapperPlayServerUpdateAttributes extends PacketWrapper<WrapperPlayServerUpdateAttributes> {

    private static final List<Map.Entry<String, Attribute>> PRE_1_16_ATTRIBUTES = Collections.unmodifiableList(Arrays.asList(
            new SimpleEntry<>("generic.maxHealth", Attributes.GENERIC_MAX_HEALTH),
            new SimpleEntry<>("Max Health", Attributes.GENERIC_MAX_HEALTH),
            new SimpleEntry<>("zombie.spawnReinforcements", Attributes.ZOMBIE_SPAWN_REINFORCEMENTS),
            new SimpleEntry<>("Spawn Reinforcements Chance", Attributes.ZOMBIE_SPAWN_REINFORCEMENTS),
            new SimpleEntry<>("horse.jumpStrength", Attributes.HORSE_JUMP_STRENGTH),
            new SimpleEntry<>("Jump Strength", Attributes.HORSE_JUMP_STRENGTH),
            new SimpleEntry<>("generic.followRange", Attributes.GENERIC_FOLLOW_RANGE),
            new SimpleEntry<>("Follow Range", Attributes.GENERIC_FOLLOW_RANGE),
            new SimpleEntry<>("generic.knockbackResistance", Attributes.GENERIC_KNOCKBACK_RESISTANCE),
            new SimpleEntry<>("Knockback Resistance", Attributes.GENERIC_KNOCKBACK_RESISTANCE),
            new SimpleEntry<>("generic.movementSpeed", Attributes.GENERIC_MOVEMENT_SPEED),
            new SimpleEntry<>("Movement Speed", Attributes.GENERIC_MOVEMENT_SPEED),
            new SimpleEntry<>("generic.flyingSpeed", Attributes.GENERIC_FLYING_SPEED),
            new SimpleEntry<>("Flying Speed", Attributes.GENERIC_FLYING_SPEED),
            new SimpleEntry<>("generic.attackDamage", Attributes.GENERIC_ATTACK_DAMAGE),
            new SimpleEntry<>("generic.attackKnockback", Attributes.GENERIC_ATTACK_KNOCKBACK),
            new SimpleEntry<>("generic.attackSpeed", Attributes.GENERIC_ATTACK_SPEED),
            new SimpleEntry<>("generic.armorToughness", Attributes.GENERIC_ARMOR_TOUGHNESS),
            new SimpleEntry<>("generic.armor", Attributes.GENERIC_ARMOR),
            new SimpleEntry<>("generic.luck", Attributes.GENERIC_LUCK)
    ));
    private static final Map<String, Attribute> PRE_1_16_ATTRIBUTES_MAP = PRE_1_16_ATTRIBUTES.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    private static final Map<Attribute, String> PRE_1_16_ATTRIBUTES_RMAP = PRE_1_16_ATTRIBUTES.stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,
                    (s1, s2) -> s1));

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
                attribute = this.readMappedEntity(Attributes::getById);
            } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                attribute = Attributes.getByName(this.readIdentifier().toString());
            } else {
                String attributeName = this.readString(64);
                attribute = PRE_1_16_ATTRIBUTES_MAP.get(attributeName);
                if (attribute == null) {
                    throw new IllegalStateException("Can't find attribute for name " + attributeName
                            + " (version: " + this.serverVersion.name() + ")");
                }
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
                ResourceLocation name;
                UUID uuid;
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
                    name = this.readIdentifier();
                    uuid = PropertyModifier.generateSemiUniqueId(name);
                } else {
                    uuid = this.readUUID();
                    name = new ResourceLocation(uuid.toString());
                }

                double amount = readDouble();
                byte operationIndex = readByte();
                PropertyModifier.Operation operation = PropertyModifier.Operation.VALUES[operationIndex];
                modifiers.add(new PropertyModifier(name, uuid, amount, operation));
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
            } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                this.writeIdentifier(property.getAttribute().getName());
            } else {
                this.writeString(PRE_1_16_ATTRIBUTES_RMAP.get(property.getAttribute()));
            }

            writeDouble(property.value);
            if (serverVersion == ServerVersion.V_1_7_10) {
                writeShort(property.modifiers.size());
            } else {
                writeVarInt(property.modifiers.size());
            }
            for (PropertyModifier modifier : property.modifiers) {
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
                    this.writeIdentifier(modifier.name);
                } else {
                    this.writeUUID(modifier.uuid);
                }
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

        private ResourceLocation name; // added in 1.21
        private UUID uuid; // removed in 1.21
        private double amount;
        private Operation operation;

        public PropertyModifier(UUID uuid, double amount, Operation operation) {
            this(new ResourceLocation(uuid.toString()), uuid, amount, operation);
        }

        public PropertyModifier(ResourceLocation name, double amount, Operation operation) {
            this(name, generateSemiUniqueId(name), amount, operation);
        }

        public PropertyModifier(ResourceLocation name, UUID uuid, double amount, Operation operation) {
            this.name = name;
            this.uuid = uuid;
            this.amount = amount;
            this.operation = operation;
        }

        @ApiStatus.Internal
        public static UUID generateSemiUniqueId(ResourceLocation name) {
            String extendedName = "packetevents_" + name.toString();
            return UUID.nameUUIDFromBytes(extendedName.getBytes(StandardCharsets.UTF_8));
        }

        // added in 1.21
        public ResourceLocation getName() {
            return this.name;
        }

        // added in 1.21
        public void setName(ResourceLocation name) {
            this.name = name;
            this.uuid = generateSemiUniqueId(name);
        }

        @ApiStatus.Obsolete // unused since 1.21
        public UUID getUUID() {
            return uuid;
        }

        @ApiStatus.Obsolete // unused since 1.21
        public void setUUID(UUID uuid) {
            this.name = new ResourceLocation(uuid.toString());
            this.uuid = uuid;
        }

        public void setNameAndUUID(ResourceLocation name, UUID uuid) {
            this.name = name;
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
