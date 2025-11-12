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

import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariants;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class WolfSoundVariantComponent {

    private WolfSoundVariant soundVariant;

    public WolfSoundVariantComponent(WolfSoundVariant soundVariant) {
        this.soundVariant = soundVariant;
    }

    public static WolfSoundVariantComponent read(PacketWrapper<?> wrapper) {
        WolfSoundVariant type = wrapper.readMappedEntity(WolfSoundVariants.getRegistry());
        return new WolfSoundVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, WolfSoundVariantComponent component) {
        wrapper.writeMappedEntity(component.soundVariant);
    }

    public WolfSoundVariant getSoundVariant() {
        return this.soundVariant;
    }

    public void setSoundVariant(WolfSoundVariant soundVariant) {
        this.soundVariant = soundVariant;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WolfSoundVariantComponent)) return false;
        WolfSoundVariantComponent that = (WolfSoundVariantComponent) obj;
        return this.soundVariant.equals(that.soundVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.soundVariant);
    }
}
