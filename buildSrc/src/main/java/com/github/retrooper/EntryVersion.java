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

package com.github.retrooper;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EntryVersion implements Comparable<EntryVersion> {

    /**
     * Format: V_1_M_m
     * M = Major
     * m = Minor
     * Example: V_1_9_4
     *
     * @param version The version string
     * @return EntryVersion object
     */
    public static EntryVersion fromString(final String version) {
        final String[] split = version.substring(4).split("_");

        final int major = Integer.parseInt(split[0]);
        final int minor;
        if (split.length == 2) {
            minor = Integer.parseInt(split[1]);
        } else {
            minor = 0;
        }

        return new EntryVersion(major, minor);
    }

    private final int major;
    private final int minor;

    public EntryVersion(final int major, final int minor) {
        this.major = major;
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public boolean isNewerThan(final EntryVersion other) {
        return major > other.major || (major == other.major && minor > other.minor);
    }

    public boolean isOlderThan(final EntryVersion other) {
        return major < other.major || (major == other.major && minor < other.minor);
    }

    @Override
    public int compareTo(@NotNull EntryVersion o) {
        if (major > o.major) {
            return 1;
        } else if (major < o.major) {
            return -1;
        } else {
            return Integer.compare(minor, o.minor);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntryVersion)) return false;
        EntryVersion that = (EntryVersion) o;
        return major == that.major && minor == that.minor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor);
    }

    @Override
    public String toString() {
        return "V_1_" + major + (minor == 0 ? "" : "_" + minor);
    }
}
