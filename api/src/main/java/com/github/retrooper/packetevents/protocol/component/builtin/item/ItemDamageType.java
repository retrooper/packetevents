/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class ItemDamageType {

    private MaybeMappedEntity<DamageType> damageType;

    public ItemDamageType(DamageType damageType) {
        this(new MaybeMappedEntity<>(damageType));
    }

    /**
     * @versions 1.21.11
     */
    @ApiStatus.Obsolete
    public ItemDamageType(MaybeMappedEntity<DamageType> damageType) {
        this.damageType = damageType;
    }

    public static ItemDamageType read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            return new ItemDamageType(DamageType.read(wrapper));
        }
        MaybeMappedEntity<DamageType> damageType = MaybeMappedEntity.read(wrapper,
                DamageTypes.getRegistry(), DamageType::read);
        return new ItemDamageType(damageType);
    }

    public static void write(PacketWrapper<?> wrapper, ItemDamageType component) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            DamageType.write(wrapper, component.damageType.getValueOrThrow());
        } else {
            MaybeMappedEntity.write(wrapper, component.damageType, DamageType::write);
        }
    }

    public MaybeMappedEntity<DamageType> getDamageType() {
        return this.damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.setDamageType(new MaybeMappedEntity<>(damageType));
    }

    public void setDamageType(MaybeMappedEntity<DamageType> damageType) {
        this.damageType = damageType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemDamageType that = (ItemDamageType) obj;
        return this.damageType.equals(that.damageType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.damageType);
    }
}
