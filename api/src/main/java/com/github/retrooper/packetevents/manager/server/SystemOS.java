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

package com.github.retrooper.packetevents.manager.server;

/**
 * System Operating system.
 *
 * @author retrooper
 * @since 1.7
 */
public enum SystemOS {

    WINDOWS, MACOS, LINUX, OTHER;
    private static SystemOS CACHE;

    /**
     * Get the server's operating system.
     * This method will NOT cache.
     *
     * @return Operating System.
     */
    public static SystemOS getOSNoCache() {
        final String os = System.getProperty("os.name").toLowerCase();
        for (SystemOS sysos : values()) {
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
        if (CACHE == null) {
            CACHE = getOSNoCache();
        }
        return CACHE;
    }

}
