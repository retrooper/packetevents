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

package io.github.retrooper.packetevents;

import java.util.ArrayList;
import java.util.List;

public class PEVersion {
    private final int[] version;


    public PEVersion(int... version) {
        this.version = version;
    }

    public PEVersion(String text) {
        //1.2.3.4
        text += ".";
        //1.2.3.4.
        int arrayIndex = 0;
        int[] version = new int[4];
        // String t = "1.2.3.4";
        for(int i = 0; i < text.toCharArray().length; i++) {
            char c = text.toCharArray()[i];
            if(c == '.') {
                version[arrayIndex++] =
                        Integer.parseInt(String.valueOf(text.toCharArray()[i - 1]));
            }
        }

        this.version = version;
    }

    public boolean isNewer(PEVersion other) {
        //The version length if guaranteed to be 4.
        for (int i = 0; i < version.length; i++) {
            if (version[i] > other.getVersionAsByteArray()[i]) {
                return true;
            } else if (version[i] < other.getVersionAsByteArray()[i]) {
                return false;
            }
        }
        //If they are equal, they aren't newer nor older.
        return false;
    }

    public boolean isOlder(PEVersion other) {
        //The version length if guaranteed to be 4.
        for (int i = 0; i < version.length; i++) {
            if (version[i] < other.getVersionAsByteArray()[i]) {
                return true;
            } else if (version[i] > other.getVersionAsByteArray()[i]) {
                return false;
            }
        }
        //If they are equal, they aren't newer nor older.
        return false;
    }

    public final int[] getVersionAsByteArray() {
        return version;
    }

    public final int[] getVersionAsByteArrayShortened() {
        int endingIndex = 0;
        for (int i = version.length - 1; i >= 0; i--) {
            if (version[i] == 0) {
                endingIndex = i;
            }
        }

        List<Integer> versionListBoxed = new ArrayList<>();
        for (int i = 0; i < version.length; i++) {
            if (i == endingIndex) {
                break;
            }
            versionListBoxed.add(version[i]);
        }

        int[] version = new int[versionListBoxed.size()];
        for (int i = 0; i < version.length; i++) {
            version[i] = versionListBoxed.get(i);
        }
        return version;
    }

    @Override
    public String toString() {
        int[] shortenedVersion = getVersionAsByteArrayShortened();
        StringBuilder text = new StringBuilder();
        for (int v : shortenedVersion) {
            text.append(".").append(v);
        }
        return text.substring(1);
    }
}
