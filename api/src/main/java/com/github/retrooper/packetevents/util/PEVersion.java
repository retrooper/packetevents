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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * PacketEvents version.
 * This class represents a PacketEvents version using Semantic Versioning.
 * It supports comparison and cloning operations.
 */
public class PEVersion implements Comparable<PEVersion>, Cloneable {

    private final int major;
    private final int minor;
    private final int patch;
    private final boolean snapshot;

    /**
     * Specify your version using Semantic Versioning.
     *
     * @param major    the major version number.
     * @param minor    the minor version number
     * @param patch    the patch version number.
     * @param snapshot boolean flag indicating whether the version is a snapshot.
     */
    public PEVersion(int major, int minor, int patch, boolean snapshot) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.snapshot = snapshot;
    }

    /**
     * Specify your version using a string, for example: "1.8.9-SNAPSHOT".
     *
     * @param version String version.
     */
    public PEVersion(@NotNull String version) {
        this.snapshot = version.endsWith("-SNAPSHOT");
        String versionWithoutSnapshot = version.replace("-SNAPSHOT", "");
        String[] parts = versionWithoutSnapshot.split("\\.");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Version string must be in the format 'major.minor.patch[-SNAPSHOT]'");
        }

        this.major = Integer.parseInt(parts[0]);
        this.minor = Integer.parseInt(parts[1]);
        this.patch = Integer.parseInt(parts[2]);
    }

    /**
     * Creates a PEVersion instance from the package version.
     *
     * @return PEVersion instance with version derived from the package implementation version.
     */
    public static PEVersion createFromPackageVersion() {
        String version = Optional.ofNullable(PacketEvents.class.getPackage().getImplementationVersion()).orElse("0.0.0");
        return new PEVersion(version);
    }

    /**
     * Compares this PEVersion instance with another PEVersion.
     * Considers major, minor, patch versions, and snapshot state.
     *
     * @param other The PEVersion object to be compared.
     * @return A negative integer, zero, or a positive integer as this version is less than, equal to, or greater than the specified version.
     */
    @Override
    public int compareTo(@NotNull PEVersion other) {
        int majorCompare = Integer.compare(this.major, other.major);
        if (majorCompare != 0) return majorCompare;

        int minorCompare = Integer.compare(this.minor, other.minor);
        if (minorCompare != 0) return minorCompare;

        int patchCompare = Integer.compare(this.patch, other.patch);
        if (patchCompare != 0) return patchCompare;

        // Snapshot versions are considered older than non-snapshot versions
        return Boolean.compare(other.snapshot, this.snapshot);
    }

    /**
     * Determines whether the provided object is equal to the current PEVersion.
     * Checks for equality by comparing major, minor, patch versions, and the snapshot state.
     *
     * @param obj Object expected to be a PEVersion instance that is to be compared with the current PEVersion.
     * @return Boolean, true if the provided object is logically equal to the current PEVersion, false otherwise.
     */
    @Override
    public boolean equals(@NotNull Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PEVersion)) return false;
        PEVersion other = (PEVersion) obj;

        return this.major == other.major &&
                this.minor == other.minor &&
                this.patch == other.patch &&
                this.snapshot == other.snapshot;
    }

    /**
     * Checks if this version is newer than the provided version.
     *
     * @param otherVersion Other PEVersion.
     * @return boolean, true if this version is newer, false otherwise.
     */
    public boolean isNewerThan(@NotNull PEVersion otherVersion) {
        return this.compareTo(otherVersion) > 0;
    }

    /**
     * Checks if this version is older than the provided version.
     *
     * @param otherVersion Other PEVersion.
     * @return boolean, true if this version is older, false otherwise.
     */
    public boolean isOlderThan(@NotNull PEVersion otherVersion) {
        return this.compareTo(otherVersion) < 0;
    }

    /**
     * Returns a hash code value for the object based on its state.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, snapshot);
    }

    /**
     * Creates and returns a copy of this PEVersion instance.
     *
     * @return A clone of this instance.
     */
    @Override
    public PEVersion clone() {
        try {
            return (PEVersion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e); // Should never happen as we implement Cloneable
        }
    }

    /**
     * Converts the PEVersion instance to a string representation.
     * Constructed by concatenating major, minor, patch versions, and snapshot state.
     *
     * @return A string representation of the version in the pattern "Major.Minor.Patch-SnapshotStatus".
     */
    @Override
    public String toString() {
        return major + "." + minor + "." + patch + (snapshot ? "-SNAPSHOT" : "");
    }
}

