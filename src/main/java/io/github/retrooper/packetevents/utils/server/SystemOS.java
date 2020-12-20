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

/**
 * System Operating system.
 * @author retrooper
 * @since 1.7
 */
public enum SystemOS {
    WINDOWS, MACOS, LINUX, OTHER;

    private static SystemOS value;

    /**
     * Get the server's operating system.
     * This method will NOT cache.
     * @return Operating System.
     */
    public static SystemOS getOSNoCache() {
        final String os = System.getProperty("os.name");
        for (final String osName : getOperatingSystemNames()) {
            if (os.toLowerCase().contains(osName.toLowerCase())) {
                return SystemOS.valueOf(osName);
            }
        }
        return OTHER;
    }

    /**
     * Get the server's operating system.
     * This method will CACHE for you.
     * @return Operating System.
     */
    public static SystemOS getOS() {
        if(value == null) {
            value = getOSNoCache();
        }
        return value;
    }

    /**
     * Get the server's operating system.
     * This method will CACHE for you.
     * @deprecated Use {@link #getOS()}, method renamed.
     * @return Operating System.
     */
    @Deprecated
    public static SystemOS getOperatingSystem() {
        return getOS();
    }

    /**
     * Internally used method to get the names of all operating systems.
     * @return Operating system names array.
     */
    private static String[] getOperatingSystemNames() {
        final SystemOS[] values = values();
        final int valuesLength = values.length - 1;
        final String[] arr = new String[valuesLength];
        for (int i = 0; i < valuesLength; i++) {
            arr[i] = values[i].name();
        }
        return arr;
    }
}
