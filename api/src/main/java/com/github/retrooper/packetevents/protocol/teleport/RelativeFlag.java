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

package com.github.retrooper.packetevents.protocol.teleport;

// This is an immutable container for a masking byte
public final class RelativeFlag {

    public static final RelativeFlag X = new RelativeFlag(1 << 0);
    public static final RelativeFlag Y = new RelativeFlag(1 << 1);
    public static final RelativeFlag Z = new RelativeFlag(1 << 2);
    public static final RelativeFlag YAW = new RelativeFlag(1 << 3);
    public static final RelativeFlag PITCH = new RelativeFlag(1 << 4);

    private final byte mask;

    public RelativeFlag(int mask) {
        this.mask = (byte) mask;
    }

    public RelativeFlag combine(RelativeFlag relativeFlag) { // FIXME: Should this be called append?
        return new RelativeFlag(this.mask | relativeFlag.mask);
    }

    public byte getMask() {
        return mask;
    }

    public boolean isSet(byte flags) {
        return (flags & mask) != 0;
    }

    public byte set(byte flags, boolean relative) {
        if (relative) {
            return (byte) (flags | mask);
        }
        return (byte) (flags & ~mask);
    }
}