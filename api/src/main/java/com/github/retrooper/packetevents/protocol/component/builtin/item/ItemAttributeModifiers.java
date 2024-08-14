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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import com.github.retrooper.packetevents.protocol.attribute.AttributeOperation;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes.PropertyModifier;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemAttributeModifiers {

    public static final ItemAttributeModifiers EMPTY = new ItemAttributeModifiers(
            Collections.emptyList(), true) {
        @Override
        public void setShowInTooltip(boolean showInTooltip) {
            throw new UnsupportedOperationException();
        }
    };

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemAttributeModifiers)) return false;
        ItemAttributeModifiers that = (ItemAttributeModifiers) obj;
        if (this.showInTooltip != that.showInTooltip) return false;
        return this.modifiers.equals(that.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.modifiers, this.showInTooltip);
    }

    @Override
    public String toString() {
        return "ItemAttributeModifiers{modifiers=" + this.modifiers + ", showInTooltip=" + this.showInTooltip + '}';
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ModifierEntry)) return false;
            ModifierEntry that = (ModifierEntry) obj;
            if (!this.attribute.equals(that.attribute)) return false;
            if (!this.modifier.equals(that.modifier)) return false;
            return this.slotGroup == that.slotGroup;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.attribute, this.modifier, this.slotGroup);
        }

        @Override
        public String toString() {
            return "ModifierEntry{attribute=" + this.attribute + ", modifier=" + this.modifier + ", slotGroup=" + this.slotGroup + '}';
        }
    }

    public static class Modifier {

        private UUID id; // removed in 1.21
        private String name; // ResourceLocation since 1.21
        private double value;
        private AttributeOperation operation;

        public Modifier(ResourceLocation name, double value, AttributeOperation operation) {
            this(PropertyModifier.generateSemiUniqueId(name), name.toString(), value, operation);
        }

        @ApiStatus.Obsolete
        public Modifier(UUID id, String name, double value, AttributeOperation operation) {
            this.id = id;
            this.name = name;
            this.value = value;
            this.operation = operation;
        }

        public static Modifier read(PacketWrapper<?> wrapper) {
            UUID id;
            String name;
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
                ResourceLocation nameKey = wrapper.readIdentifier();
                name = nameKey.toString();
                id = PropertyModifier.generateSemiUniqueId(nameKey);
            } else {
                id = wrapper.readUUID();
                name = wrapper.readString();
            }
            double value = wrapper.readDouble();
            AttributeOperation operation = wrapper.readEnum(AttributeOperation.values());
            return new Modifier(id, name, value, operation);
        }

        public static void write(PacketWrapper<?> wrapper, Modifier modifier) {
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
                wrapper.writeIdentifier(new ResourceLocation(modifier.name));
            } else {
                wrapper.writeUUID(modifier.id);
                wrapper.writeString(modifier.name);
            }
            wrapper.writeDouble(modifier.value);
            wrapper.writeEnum(modifier.operation);
        }

        public ResourceLocation getNameKey() {
            return new ResourceLocation(this.name);
        }

        public void setNameKey(ResourceLocation nameKey) {
            this.name = nameKey.toString();
        }

        @ApiStatus.Obsolete
        public UUID getId() {
            return this.id;
        }

        @ApiStatus.Obsolete
        public void setId(UUID id) {
            this.id = id;
        }

        @ApiStatus.Obsolete
        public String getName() {
            return this.name;
        }

        @ApiStatus.Obsolete
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Modifier)) return false;
            Modifier modifier = (Modifier) obj;
            if (Double.compare(modifier.value, this.value) != 0) return false;
            if (!this.id.equals(modifier.id)) return false;
            if (!this.name.equals(modifier.name)) return false;
            return this.operation == modifier.operation;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.id, this.name, this.value, this.operation);
        }

        @Override
        public String toString() {
            return "Modifier{id=" + this.id + ", name='" + this.name + '\'' + ", value=" + this.value + ", operation=" + this.operation + '}';
        }
    }

    public enum EquipmentSlotGroup {

        ANY("any"),
        MAINHAND("mainhand"),
        OFFHAND("offhand"),
        HAND("hand"),
        FEET("feet"),
        LEGS("legs"),
        CHEST("chest"),
        HEAD("head"),
        ARMOR("armor"),
        BODY("body");

        public static final Index<String, EquipmentSlotGroup> ID_INDEX = Index.create(
                EquipmentSlotGroup.class, EquipmentSlotGroup::getId);

        private final String id;

        EquipmentSlotGroup(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }
}
