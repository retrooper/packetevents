/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.attribute;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.MathUtil;

public interface Attribute extends MappedEntity {

    @Override
    default ResourceLocation getName() {
        return this.getName(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    ResourceLocation getName(ClientVersion version);

    default double sanitizeValue(double value) {
        return this.sanitizeValue(value, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    default double sanitizeValue(double value, ClientVersion version) {
        if (!Double.isNaN(value)) {
            return MathUtil.clamp(value, this.getMinValue(), this.getMaxValue());
        }
        return this.getMinValue();
    }

    double getDefaultValue();

    double getMinValue();

    double getMaxValue();
}
