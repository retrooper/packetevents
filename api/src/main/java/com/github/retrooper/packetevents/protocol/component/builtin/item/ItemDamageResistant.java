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
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 1.21.2+
 */
@NullMarked
public class ItemDamageResistant {

    /**
     * @versions 26.1+
     */
    private MappedEntitySet<DamageType> types;

    public ItemDamageResistant(ResourceLocation types) {
        this(new MappedEntitySet<>(types));
    }

    public ItemDamageResistant(MappedEntitySet<DamageType> types) {
        this.types = types;
    }

    public static ItemDamageResistant read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            return new ItemDamageResistant(MappedEntitySet.read(wrapper, DamageTypes.getRegistry()));
        }
        return new ItemDamageResistant(wrapper.readIdentifier());
    }

    public static void write(PacketWrapper<?> wrapper, ItemDamageResistant resistant) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            MappedEntitySet.write(wrapper, resistant.types);
        } else {
            wrapper.writeIdentifier(resistant.getTypesTagKey());
        }
    }

    /**
     * @versions 26.1+
     */
    public MappedEntitySet<DamageType> getTypes() {
        return this.types;
    }

    /**
     * @versions 26.1+
     */
    public void setTypes(MappedEntitySet<DamageType> types) {
        this.types = types;
    }

    public ResourceLocation getTypesTagKey() {
        ResourceLocation tagKey = this.types.getTagKey();
        if (tagKey == null) {
            throw new IllegalStateException("No tag key present");
        }
        return tagKey;
    }

    public void setTypesTagKey(ResourceLocation types) {
        this.types = new MappedEntitySet<>(types);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemDamageResistant)) return false;
        ItemDamageResistant that = (ItemDamageResistant) obj;
        return this.types.equals(that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.types);
    }

    @Override
    public String toString() {
        return "ItemDamageResistant{types=" + this.types + '}';
    }
}
