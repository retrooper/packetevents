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

import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class ItemPiercingWeapon {

    private boolean dealsKnockback;
    private boolean dismounts;
    private @Nullable Sound sound;
    private @Nullable Sound hitSound;

    public ItemPiercingWeapon(
            boolean dealsKnockback, boolean dismounts,
            @Nullable Sound sound, @Nullable Sound hitSound
    ) {
        this.dealsKnockback = dealsKnockback;
        this.dismounts = dismounts;
        this.sound = sound;
        this.hitSound = hitSound;
    }

    public static ItemPiercingWeapon read(PacketWrapper<?> wrapper) {
        boolean dealsKnockback = wrapper.readBoolean();
        boolean dismounts = wrapper.readBoolean();
        Sound sound = wrapper.readOptional(Sound::read);
        Sound hitSound = wrapper.readOptional(Sound::read);
        return new ItemPiercingWeapon(dealsKnockback, dismounts, sound, hitSound);
    }

    public static void write(PacketWrapper<?> wrapper, ItemPiercingWeapon component) {
        wrapper.writeBoolean(component.dealsKnockback);
        wrapper.writeBoolean(component.dismounts);
        wrapper.writeOptional(component.sound, Sound::write);
        wrapper.writeOptional(component.hitSound, Sound::write);
    }

    public boolean isDealsKnockback() {
        return this.dealsKnockback;
    }

    public void setDealsKnockback(boolean dealsKnockback) {
        this.dealsKnockback = dealsKnockback;
    }

    public boolean isDismounts() {
        return this.dismounts;
    }

    public void setDismounts(boolean dismounts) {
        this.dismounts = dismounts;
    }

    public @Nullable Sound getSound() {
        return this.sound;
    }

    public void setSound(@Nullable Sound sound) {
        this.sound = sound;
    }

    public @Nullable Sound getHitSound() {
        return this.hitSound;
    }

    public void setHitSound(@Nullable Sound hitSound) {
        this.hitSound = hitSound;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemPiercingWeapon that = (ItemPiercingWeapon) obj;
        if (this.dealsKnockback != that.dealsKnockback) return false;
        if (this.dismounts != that.dismounts) return false;
        if (!Objects.equals(this.sound, that.sound)) return false;
        return Objects.equals(this.hitSound, that.hitSound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dealsKnockback, this.dismounts, this.sound, this.hitSound);
    }
}
