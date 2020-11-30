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
import java.util.List;


public class PEVersion {
    private final int[] versionIntArray;

    public PEVersion(final int... version) {
        this.versionIntArray = version;
    }

    public PEVersion(final String version) {
        String[] versionIntegers = version.split("\\.");
        int length = versionIntegers.length;
        this.versionIntArray = new int[length];
        for (int i = 0; i < length; i++) {
            versionIntArray[i] = Integer.parseInt(versionIntegers[i]);
        }
    }

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

    public boolean equals(PEVersion version) {
        return Arrays.equals(versionIntArray, version.versionIntArray);
    }

    public boolean isNewerThan(PEVersion version) {
        return compareTo(version) == 1;
    }

    public boolean isOlderThan(PEVersion version) {
        return compareTo(version) == -1;
    }

    public int[] asArray() {
        return versionIntArray;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PEVersion) {
            return equals((PEVersion) obj);
        }
        return false;
    }

    @Override
    public PEVersion clone() {
        return new PEVersion(this.versionIntArray);
    }

    @Override
    public String toString() {
        StringBuilder asString = new StringBuilder(Integer.toString(versionIntArray[0]));
        for (int i = 1; i < versionIntArray.length; i++) {
            asString.append(".").append(versionIntArray[i]);
        }
        return asString.toString();
    }
}
