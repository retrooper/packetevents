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

package io.github.retrooper.packetevents.utils.server;

public enum SystemOS {
    WINDOWS, MACOS, LINUX, OTHER;

    private static SystemOS value;

    private static SystemOS getOS() {
        final String os = System.getProperty("os.name");
        for (final String osName : getOperatingSystemNames()) {
            if (os.toLowerCase().contains(osName.toLowerCase())) {
                return SystemOS.valueOf(osName);
            }
        }
        return OTHER;
    }

    public static SystemOS getOperatingSystem() {
        if (value == null) {
            value = getOS();
        }
        return value;
    }

    public static String[] getOperatingSystemNames() {
        final SystemOS[] values = values();
        final int length = values.length - 1;
        final String[] arr = new String[length];
        for (int i = 0; i < length; i++) {
            arr[i] = values[i].name();
        }
        return arr;
    }
}
