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

import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenSoundVariants;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ChickenSoundVariantComponent {

    private final ChickenSoundVariant soundVariant;

    public ChickenSoundVariantComponent(ChickenSoundVariant soundVariant) {
        this.soundVariant = soundVariant;
    }

    public static ChickenSoundVariantComponent read(PacketWrapper<?> wrapper) {
        ChickenSoundVariant type = wrapper.readMappedEntity(ChickenSoundVariants.getRegistry());
        return new ChickenSoundVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, ChickenSoundVariantComponent component) {
        wrapper.writeMappedEntity(component.soundVariant);
    }

    public ChickenSoundVariant getSoundVariant() {
        return this.soundVariant;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChickenSoundVariantComponent)) return false;
        ChickenSoundVariantComponent that = (ChickenSoundVariantComponent) obj;
        return this.soundVariant.equals(that.soundVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.soundVariant);
    }
}
