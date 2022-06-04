/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.player;

// This is an immutable container for a masking byte
public final class SkinSection {

    public static final SkinSection CAPE = new SkinSection(1 << 0);
    public static final SkinSection JACKET = new SkinSection(1 << 1);
    public static final SkinSection LEFT_SLEEVE = new SkinSection(1 << 2);
    public static final SkinSection RIGHT_SLEEVE = new SkinSection(1 << 3);
    public static final SkinSection LEFT_PANTS = new SkinSection(1 << 4);
    public static final SkinSection RIGHT_PANTS = new SkinSection(1 << 5);
    public static final SkinSection HAT = new SkinSection(1 << 6);

    public static final SkinSection ALL = CAPE.combine(JACKET).combine(LEFT_SLEEVE).combine(RIGHT_SLEEVE).combine(LEFT_PANTS).combine(RIGHT_PANTS).combine(HAT);

    private final byte mask;

    public SkinSection(int mask) {
        this.mask = (byte) mask;
    }

    public SkinSection combine(SkinSection skinSection) { // FIXME: Should this be called append?
        return new SkinSection(this.mask | skinSection.mask);
    }

    public byte getMask() {
        return mask;
    }

    public boolean isSet(byte skinParts) {
        return (skinParts & this.mask) != 0;
    }

    public byte set(byte skinParts, boolean present) {
        if (present) {
            skinParts |= mask;
        } else {
            skinParts &= ~mask;
        }
        return skinParts;
    }
}
