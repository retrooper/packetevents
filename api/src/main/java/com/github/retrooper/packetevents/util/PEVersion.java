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

package com.github.retrooper.packetevents.util;

import java.util.Arrays;

/**
 * PacketEvents version.
 * This class represents a PacketEvents version.
 *
 * @author retrooper
 * @since 1.8
 */
public class PEVersion {
    /**
     * Array containing the digits in the version.
     * For example, "1.8.9" will be stored as {1, 8, 9} in an array.
     */
    private final int[] versionIntArray;

    /**
     * Specify your version using an array.
     *
     * @param version Array version.
     */
    public PEVersion(final int... version) {
        this.versionIntArray = version;
    }

    /**
     * Specify your version using a string, for example: "1.8.9".
     *
     * @param version String version.
     */
    public PEVersion(final String version) {
        String[] versionIntegers = version.split("\\.");
        int length = versionIntegers.length;
        this.versionIntArray = new int[length];
        for (int i = 0; i < length; i++) {
            versionIntArray[i] = Integer.parseInt(versionIntegers[i]);
        }
    }

    /**
     * Compare to another PEVersion.
     * If we are newer than the compared version,
     * this method will return 1.
     * If we are older than the compared version,
     * this method will return -1.
     * If we are equal to the compared version,
     * this method will return 0.
     * Similar to {@link Integer#compareTo(Integer)}.
     *
     * @param version Compared version
     * @return Comparing to another Version.
     */
    public int compareTo(PEVersion version) {
        int localLength = versionIntArray.length;
        int oppositeLength = version.versionIntArray.length;
        int length = Math.max(localLength, oppositeLength);
        for (int i = 0; i < length; i++) {
            int localInteger = i < localLength ? versionIntArray[i] : 0;
            int oppositeInteger = i < oppositeLength ? version.versionIntArray[i] : 0;
            if (localInteger > oppositeInteger) {
                return 1;
            } else if (localInteger < oppositeInteger) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Does the {@link #compareTo(PEVersion)} return 1?
     *
     * @param version Compared version.
     * @return Is this newer than the compared version.
     */
    public boolean isNewerThan(PEVersion version) {
        return compareTo(version) == 1;
    }

    /**
     * Does the {@link #compareTo(PEVersion)} return -1?
     *
     * @param version Compared version.
     * @return Is this older than the compared version.
     */
    public boolean isOlderThan(PEVersion version) {
        return compareTo(version) == -1;
    }

    /**
     * Represented as an array.
     *
     * @return Array version.
     */
    public int[] asArray() {
        return versionIntArray;
    }

    /**
     * Is this version equal to the compared object.
     * The object must be a PEVersion and the array values must be equal.
     *
     * @param obj Compared object.
     * @return Are they equal?
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof PEVersion) {
            return Arrays.equals(versionIntArray, ((PEVersion) obj).versionIntArray);
        }
        return false;
    }

    /**
     * Clone the PEVersion.
     *
     * @return A clone.
     */
    @Override
    public PEVersion clone() {
        return new PEVersion(versionIntArray);
    }

    /**
     * Represent the version as a string.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(versionIntArray.length * 2 - 1).append(versionIntArray[0]);
        for (int i = 1; i < versionIntArray.length; i++) {
            sb.append(".").append(versionIntArray[i]);
        }
        return sb.toString();
    }
}
