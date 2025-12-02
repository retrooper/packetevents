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

package com.github.retrooper.packetevents.protocol.item.enchantment.type;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import com.github.retrooper.packetevents.protocol.item.enchantment.EnchantmentDefinition;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.mapper.ResolvableEntity;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class StaticEnchantmentType extends AbstractMappedEntity implements EnchantmentType, ResolvableEntity {

    private final Component description;
    private final EnchantmentDefinition definition;
    private final MappedEntityRefSet<EnchantmentType> exclusiveSetRef;
    private @Nullable MappedEntitySet<EnchantmentType> exclusiveSet;
    private final StaticComponentMap effects;

    public StaticEnchantmentType(
            Component description,
            EnchantmentDefinition definition,
            MappedEntitySet<EnchantmentType> exclusiveSet,
            StaticComponentMap effects
    ) {
        this(null, description, definition, exclusiveSet, effects);
    }

    @ApiStatus.Internal
    public StaticEnchantmentType(
            @Nullable TypesBuilderData data,
            Component description,
            EnchantmentDefinition definition,
            MappedEntityRefSet<EnchantmentType> exclusiveSet,
            StaticComponentMap effects
    ) {
        super(data);
        this.description = description;
        this.definition = definition;
        this.exclusiveSetRef = exclusiveSet;
        this.effects = effects;
    }

    @Override
    public void doResolve(PacketWrapper<?> wrapper) {
        this.exclusiveSet = this.exclusiveSetRef.resolve(wrapper, EnchantmentTypes.getRegistry());
    }

    @Override
    public EnchantmentType copy(@Nullable TypesBuilderData newData) {
        StaticEnchantmentType type = new StaticEnchantmentType(newData, this.description,
                this.definition, this.exclusiveSetRef, this.effects);
        type.exclusiveSet = this.exclusiveSet;
        return type;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public EnchantmentDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public MappedEntitySet<EnchantmentType> getExclusiveSet() {
        if (this.exclusiveSet == null) {
            // this shouldn't need to be called normally
            this.exclusiveSet = this.exclusiveSetRef.resolve(
                    PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(),
                    GlobalRegistryHolder.INSTANCE,
                    EnchantmentTypes.getRegistry()
            );
        }
        return this.exclusiveSet;
    }

    @Override
    public MappedEntityRefSet<EnchantmentType> getExclusiveRefSet() {
        return this.exclusiveSetRef;
    }

    @Override
    public StaticComponentMap getEffects() {
        return this.effects;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticEnchantmentType)) return false;
        if (!super.equals(obj)) return false;
        StaticEnchantmentType that = (StaticEnchantmentType) obj;
        if (!this.description.equals(that.description)) return false;
        if (!this.definition.equals(that.definition)) return false;
        if (!this.exclusiveSetRef.equals(that.exclusiveSetRef)) return false;
        return this.effects.equals(that.effects);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.description, this.definition, this.exclusiveSetRef, this.effects);
    }

    @Override
    public String toString() {
        return "StaticEnchantmentType{description=" + this.description + ", definition=" + this.definition + ", exclusiveSetRef=" + this.exclusiveSetRef + ", effects=" + this.effects + "}";
    }
}
