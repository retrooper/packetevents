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

import java.util.HashSet;
import java.util.Set;

public enum RelativeFlag {
    X(0x01),
    Y(0x02),
    Z(0x04),
    YAW(0x08),
    PITCH(0x10);

    private final byte bit;

    RelativeFlag(int bit) {
        this.bit = (byte)bit;
    }

    public byte getBit() {
        return bit;
    }

    public boolean isSet(byte mask) {
        return (mask & bit) != 0;
    }

    public byte set(byte mask, boolean relative) {
        if (relative) {
            return (byte) (mask | bit);
        } else {
            return (byte) (mask & ~bit);
        }
    }

    public static Set<RelativeFlag> getRelativeFlagsByMask(byte mask) {
        Set<RelativeFlag> relativeFlags = new HashSet<>();
        for (RelativeFlag relativeFlag : values()) {
            if (relativeFlag.isSet(mask)) {
                relativeFlags.add(relativeFlag);
            }
        }
        return relativeFlags;
    }

    public static byte getMaskByRelativeFlags(Set<RelativeFlag> sections) {
        byte mask = 0;
        for (RelativeFlag relativeFlag : sections) {
            mask |= relativeFlag.bit;
        }
        return mask;
    }
}