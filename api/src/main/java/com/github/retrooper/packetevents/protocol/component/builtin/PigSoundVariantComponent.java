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

import com.github.retrooper.packetevents.protocol.entity.pig.PigSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.pig.PigSoundVariants;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class PigSoundVariantComponent {

    private final PigSoundVariant soundVariant;

    public PigSoundVariantComponent(PigSoundVariant soundVariant) {
        this.soundVariant = soundVariant;
    }

    public static PigSoundVariantComponent read(PacketWrapper<?> wrapper) {
        PigSoundVariant type = wrapper.readMappedEntity(PigSoundVariants.getRegistry());
        return new PigSoundVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, PigSoundVariantComponent component) {
        wrapper.writeMappedEntity(component.soundVariant);
    }

    public PigSoundVariant getSoundVariant() {
        return this.soundVariant;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PigSoundVariantComponent)) return false;
        PigSoundVariantComponent that = (PigSoundVariantComponent) obj;
        return this.soundVariant.equals(that.soundVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.soundVariant);
    }
}
