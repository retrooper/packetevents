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
 *
 * @author retrooper
 * @since 1.7
 */
public enum SystemOS {

    WINDOWS, MACOS, LINUX, OTHER;

    private static final SystemOS[] VALUES = values();
    private static SystemOS value;

    /**
     * Get the server's operating system.
     * This method will NOT cache.
     *
     * @return Operating System.
     */
    public static SystemOS getOSNoCache() {
        final String os = System.getProperty("os.name").toLowerCase();
        for (SystemOS sysos : VALUES) {
            if (os.contains(sysos.name().toLowerCase())) {
                return sysos;
            }
        }
        return OTHER;
    }

    /**
     * Get the server's operating system.
     * This method will CACHE for you.
     *
     * @return Operating System.
     */
    public static SystemOS getOS() {
        if (value == null) {
            value = getOSNoCache();
        }
        return value;
    }

    /**
     * Get the server's operating system.
     * This method will CACHE for you.
     *
     * @return Operating System.
     * @deprecated Use {@link #getOS()}, method renamed.
     */
    @Deprecated
    public static SystemOS getOperatingSystem() {
        return getOS();
    }

}
