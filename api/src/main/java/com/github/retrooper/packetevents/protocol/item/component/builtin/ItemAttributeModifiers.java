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

import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import com.github.retrooper.packetevents.protocol.attribute.AttributeOperation;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.UUID;

public class ItemAttributeModifiers {

    private List<ModifierEntry> modifiers;
    private boolean showInTooltip;

    public ItemAttributeModifiers(List<ModifierEntry> modifiers, boolean showInTooltip) {
        this.modifiers = modifiers;
        this.showInTooltip = showInTooltip;
    }

    public static ItemAttributeModifiers read(PacketWrapper<?> wrapper) {
        List<ModifierEntry> modifiers = wrapper.readList(ModifierEntry::read);
        boolean showInTooltip = wrapper.readBoolean();
        return new ItemAttributeModifiers(modifiers, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemAttributeModifiers modifiers) {
        wrapper.writeList(modifiers.modifiers, ModifierEntry::write);
        wrapper.writeBoolean(modifiers.showInTooltip);
    }

    public void addModifier(ModifierEntry modifier) {
        this.modifiers.add(modifier);
    }

    public List<ModifierEntry> getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(List<ModifierEntry> modifiers) {
        this.modifiers = modifiers;
    }

    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    public static class ModifierEntry {

        private Attribute attribute;
        private Modifier modifier;
        private EquipmentSlotGroup slotGroup;

        public ModifierEntry(Attribute attribute, Modifier modifier, EquipmentSlotGroup slotGroup) {
            this.attribute = attribute;
            this.modifier = modifier;
            this.slotGroup = slotGroup;
        }

        public static ModifierEntry read(PacketWrapper<?> wrapper) {
            Attribute attribute = wrapper.readMappedEntity(Attributes::getById);
            Modifier modifier = Modifier.read(wrapper);
            EquipmentSlotGroup slot = wrapper.readEnum(EquipmentSlotGroup.values());
            return new ModifierEntry(attribute, modifier, slot);
        }

        public static void write(PacketWrapper<?> wrapper, ModifierEntry entry) {
            wrapper.writeMappedEntity(entry.attribute);
            Modifier.write(wrapper, entry.modifier);
            wrapper.writeEnum(entry.slotGroup);
        }

        public Attribute getAttribute() {
            return this.attribute;
        }

        public void setAttribute(Attribute attribute) {
            this.attribute = attribute;
        }

        public Modifier getModifier() {
            return this.modifier;
        }

        public void setModifier(Modifier modifier) {
            this.modifier = modifier;
        }

        public EquipmentSlotGroup getSlotGroup() {
            return this.slotGroup;
        }

        public void setSlotGroup(EquipmentSlotGroup slotGroup) {
            this.slotGroup = slotGroup;
        }
    }

    public static class Modifier {

        private UUID id;
        private String name;
        private double value;
        private AttributeOperation operation;

        public Modifier(UUID id, String name, double value, AttributeOperation operation) {
            this.id = id;
            this.name = name;
            this.value = value;
            this.operation = operation;
        }

        public static Modifier read(PacketWrapper<?> wrapper) {
            UUID id = wrapper.readUUID();
            String name = wrapper.readString();
            double value = wrapper.readDouble();
            AttributeOperation operation = wrapper.readEnum(AttributeOperation.values());
            return new Modifier(id, name, value, operation);
        }

        public static void write(PacketWrapper<?> wrapper, Modifier modifier) {
            wrapper.writeUUID(modifier.id);
            wrapper.writeString(modifier.name);
            wrapper.writeDouble(modifier.value);
            wrapper.writeEnum(modifier.operation);
        }

        public UUID getId() {
            return this.id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getValue() {
            return this.value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public AttributeOperation getOperation() {
            return this.operation;
        }

        public void setOperation(AttributeOperation operation) {
            this.operation = operation;
        }
    }

    public enum EquipmentSlotGroup {
        ANY,
        MAINHAND,
        OFFHAND,
        HAND,
        FEET,
        LEGS,
        CHEST,
        HEAD,
        ARMOR,
        BODY,
    }
}
