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

package com.github.retrooper.packetevents.protocol.component.builtin;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariant;
import com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariants;
import com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

/**
 * @versions 1.21.11+
 */
public class ZombieNautilusVariantComponent {

    private MaybeMappedEntity<ZombieNautilusVariant> variant;

    public ZombieNautilusVariantComponent(ZombieNautilusVariant variant) {
        this(new MaybeMappedEntity<>(variant));
    }

    /**
     * @versions 1.21.11
     */
    public ZombieNautilusVariantComponent(MaybeMappedEntity<ZombieNautilusVariant> variant) {
        this.variant = variant;
    }

    public static ZombieNautilusVariantComponent read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            return new ZombieNautilusVariantComponent(ZombieNautilusVariant.read(wrapper));
        }
        MaybeMappedEntity<ZombieNautilusVariant> variant = MaybeMappedEntity.read(wrapper,
                ZombieNautilusVariants.getRegistry(), ZombieNautilusVariant::read);
        return new ZombieNautilusVariantComponent(variant);
    }

    public static void write(PacketWrapper<?> wrapper, ZombieNautilusVariantComponent component) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            ZombieNautilusVariant.write(wrapper, component.variant.getValueOrThrow());
        } else {
            MaybeMappedEntity.write(wrapper, component.variant, ZombieNautilusVariant::write);
        }
    }

    public MaybeMappedEntity<ZombieNautilusVariant> getVariant() {
        return this.variant;
    }

    public void setVariant(MaybeMappedEntity<ZombieNautilusVariant> variant) {
        this.variant = variant;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ZombieNautilusVariantComponent)) return false;
        ZombieNautilusVariantComponent that = (ZombieNautilusVariantComponent) obj;
        return this.variant.equals(that.variant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.variant);
    }
}
