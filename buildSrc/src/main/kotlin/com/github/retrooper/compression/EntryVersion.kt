/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
package com.github.retrooper.compression

data class EntryVersion(val major: Int, val minor: Int) : Comparable<EntryVersion> {

    companion object {
        /**
         * Format: V_1_M_m
         * M = Major
         * m = Minor
         * Example: V_1_9_4
         *
         * @param version The version string
         * @return EntryVersion object
         */
        fun fromString(version: String): EntryVersion {
            val split = version.substring(4).split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val major = split[0].toInt()
            val minor = if (split.size == 2) {
                split[1].toInt()
            } else {
                0
            }

            return EntryVersion(major, minor)
        }
    }

    fun isNewerThan(other: EntryVersion): Boolean {
        return major > other.major || (major == other.major && minor > other.minor)
    }

    fun isOlderThan(other: EntryVersion): Boolean {
        return major < other.major || (major == other.major && minor < other.minor)
    }

    override fun compareTo(other: EntryVersion): Int {
        return if (major > other.major) {
            1
        } else if (major < other.major) {
            -1
        } else {
            minor.compareTo(other.minor)
        }
    }

    override fun toString(): String {
        return "V_1_" + major + (if (minor == 0) "" else "_$minor")
    }
}
