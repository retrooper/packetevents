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

    private final List<ModifierEntry> modifiers;
    private final boolean showInTooltip;

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

    public static class ModifierEntry {

        private final Attribute attribute;
        private final Modifier modifier;
        private final EquipmentSlotGroup slotGroup;

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
    }

    public static class Modifier {

        private final UUID id;
        private final String name;
        private final double value;
        private final AttributeOperation operation;

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
