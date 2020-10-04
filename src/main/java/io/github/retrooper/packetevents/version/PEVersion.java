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

package io.github.retrooper.packetevents.version;

import java.util.ArrayList;
import java.util.List;



public class PEVersion {
    private final int[] version = new int[4];

    public PEVersion(int... version) throws IllegalArgumentException {
        if (version.length > 4) {
            throw new IllegalArgumentException("You are not allowed to have more than 4 arguments!");
        } else if (version.length < 2) {
            throw new IllegalArgumentException("You need atleast two arguments.");
        }
        System.arraycopy(version, 0, this.version, 0, version.length);
    }

    public PEVersion(String text) {
        this(stringConstructor(text));
    }

    private static int[] stringConstructor(String text) {
        //1.2.3.4
        text += ".";

        int dotCount = 0;
        for (char c : text.toCharArray()) {
            if (c == '.') {
                dotCount++;
            }
        }

        //1.2.3.4.
        int arrayIndex = 0;
        int[] version = new int[4];

        // String t = "1.2.3.4";
        for (int i = 0; i < text.toCharArray().length; i++) {
            char c = text.toCharArray()[i];

            if (c == '.') {
                version[arrayIndex++] =
                        Integer.parseInt(Character.toString(text.toCharArray()[i - 1]));
            }
        }
        return version;
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
        List<Integer> versionBoxed = new ArrayList<>();
        if (version[3] == 0) {
            versionBoxed.add(version[0]);
            versionBoxed.add(version[1]);
            versionBoxed.add(version[2]);

            if (version[2] == 0) {
                versionBoxed.clear();
                versionBoxed.add(version[0]);
                versionBoxed.add(version[1]);
            }
            return convertIntListToIntArray(versionBoxed);
        } else {
            return version;
        }
    }

    private int[] convertIntListToIntArray(List<Integer> list) {
        int[] versionArray = new int[list.size()];
        for (int i = 0; i < versionArray.length; i++) {
            versionArray[i] = list.get(i);
        }
        return versionArray;
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
