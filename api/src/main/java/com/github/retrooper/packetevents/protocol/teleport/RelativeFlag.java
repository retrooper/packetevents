/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

/**
 * An immutable container for a single relative teleport flag or
 * multiple relative teleport flags.
 */
public final class RelativeFlag {

    public static final RelativeFlag NONE = new RelativeFlag(0);
    public static final RelativeFlag X = new RelativeFlag(1 << 0);
    public static final RelativeFlag Y = new RelativeFlag(1 << 1);
    public static final RelativeFlag Z = new RelativeFlag(1 << 2);
    public static final RelativeFlag YAW = new RelativeFlag(1 << 3);
    public static final RelativeFlag PITCH = new RelativeFlag(1 << 4);
    /**
     * Added with 1.21.2
     */
    public static final RelativeFlag DELTA_X = new RelativeFlag(1 << 5);
    /**
     * Added with 1.21.2
     */
    public static final RelativeFlag DELTA_Y = new RelativeFlag(1 << 6);
    /**
     * Added with 1.21.2
     */
    public static final RelativeFlag DELTA_Z = new RelativeFlag(1 << 7);
    /**
     * Added with 1.21.2
     */
    public static final RelativeFlag ROTATE_DELTA = new RelativeFlag(1 << 8);

    private final int mask;

    public RelativeFlag(int mask) {
        this.mask = mask;
    }

    public RelativeFlag and(RelativeFlag other) {
        return new RelativeFlag(this.mask & other.mask);
    }

    public RelativeFlag or(RelativeFlag other) {
        return new RelativeFlag(this.mask | other.mask);
    }

    public boolean has(RelativeFlag flag) {
        return this.has(flag.mask);
    }

    public boolean has(int flags) {
        return (flags & this.mask) != 0;
    }

    public RelativeFlag set(RelativeFlag flag, boolean relative) {
        return this.set(flag.mask, relative);
    }

    public RelativeFlag set(int flags, boolean relative) {
        int ret = relative
                ? (this.mask | flags)      // turn singular bit on
                : (this.mask & ~flags);    // turn singular bit off
        return new RelativeFlag(ret);
    }

    @Deprecated
    public RelativeFlag combine(RelativeFlag relativeFlag) {
        return this.or(relativeFlag);
    }

    @Deprecated
    public boolean isSet(byte flags) {
        return this.has(flags);
    }

    @Deprecated
    public byte set(byte flags, boolean relative) {
        return (byte) this.set((int) flags, relative).mask;
    }

    public byte getMask() {
        return (byte) this.mask;
    }

    public int getFullMask() {
        return this.mask;
    }
}
