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

data class EntryVersion(val major: Int, val minor: Int, val patch: Int) : Comparable<EntryVersion> {

    companion object {
        /**
         * Format: V_Major_Minor(_Patch) (e.g. V_1_9_4, V_26_1, V_26_1_3)
         *
         * @param version The version string
         * @return EntryVersion object
         */
        fun fromString(version: String): EntryVersion {
            val split = version.substring(2).split('_').dropLastWhile { it.isEmpty() }.toTypedArray()

            val major = split[0].toInt()
            val minor = split[1].toInt()
            val patch = if (split.size > 2) split[2].toInt() else 0

            return EntryVersion(major, minor, patch)
        }
    }

    fun isNewerThan(other: EntryVersion): Boolean {
        return compareTo(other) > 0
    }

    fun isOlderThan(other: EntryVersion): Boolean {
        return compareTo(other) < 0
    }

    override fun compareTo(other: EntryVersion): Int {
        val cmajor = major.compareTo(other.major)
        if (cmajor != 0) return cmajor
        val cminor = minor.compareTo(other.minor)
        if (cminor != 0) return cminor
        return patch.compareTo(other.patch)
    }

    override fun toString(): String {
        return "V_${major}_$minor" + (if (patch == 0) "" else "_$patch")
    }
}
