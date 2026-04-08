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

import com.github.retrooper.packetevents.protocol.entity.cow.CowSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.cow.CowSoundVariants;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class CowSoundVariantComponent {

    private final CowSoundVariant soundVariant;

    public CowSoundVariantComponent(CowSoundVariant soundVariant) {
        this.soundVariant = soundVariant;
    }

    public static CowSoundVariantComponent read(PacketWrapper<?> wrapper) {
        CowSoundVariant type = wrapper.readMappedEntity(CowSoundVariants.getRegistry());
        return new CowSoundVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, CowSoundVariantComponent component) {
        wrapper.writeMappedEntity(component.soundVariant);
    }

    public CowSoundVariant getSoundVariant() {
        return this.soundVariant;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CowSoundVariantComponent)) return false;
        CowSoundVariantComponent that = (CowSoundVariantComponent) obj;
        return this.soundVariant.equals(that.soundVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.soundVariant);
    }
}
