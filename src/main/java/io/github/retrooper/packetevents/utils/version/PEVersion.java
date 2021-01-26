/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.utils.version;

import java.util.Arrays;

/**
 * PacketEvents version.
 * This class represents a PacketEvents version, but you can use it for your own projects as a software version util if you wish.
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
        return new PEVersion(this.versionIntArray);
    }

    /**
     * Represent the version as a string.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        StringBuilder asString = new StringBuilder(Integer.toString(versionIntArray[0]));
        for (int i = 1; i < versionIntArray.length; i++) {
            asString.append(".").append(versionIntArray[i]);
        }
        return asString.toString();
    }
}
