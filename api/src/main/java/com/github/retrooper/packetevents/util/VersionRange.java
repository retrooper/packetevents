/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Version range with inclusive minimum and inclusive maximum.
 */
@ApiStatus.Internal
@NullMarked
public final class VersionRange {

    public static final VersionRange ALL_VERSIONS = new VersionRange(null);

    private static final ClientVersion[] VERSIONS = ClientVersion.values();

    private final ClientVersion minimum;
    private final ClientVersion maximum;

    public VersionRange(@Nullable ClientVersion version) {
        this(version, version);
    }

    public VersionRange(@Nullable ClientVersion minimum, @Nullable ClientVersion maximum) {
        this.minimum = minimum != null ? minimum : ClientVersion.getOldest();
        this.maximum = maximum != null ? maximum : ClientVersion.getLatest();
        if (this.minimum.compareTo(this.maximum) > 0) {
            throw new IllegalArgumentException("Minimum version is newer than maximum version: "
                    + this.minimum + " > " + this.maximum);
        }
    }

    public void iterate(Consumer<ClientVersion> consumer) {
        if (this.minimum == this.maximum) {
            consumer.accept(this.maximum);
        } else {
            consumer.accept(this.minimum);
            for (int i = this.minimum.ordinal() + 1; i < this.maximum.ordinal(); i++) {
                consumer.accept(VERSIONS[i]);
            }
            consumer.accept(this.maximum);
        }
    }

    public ClientVersion[] getAll() {
        return Arrays.copyOfRange(VERSIONS, this.minimum.ordinal(), this.maximum.ordinal() + 1);
    }

    public boolean contains(ClientVersion version) {
        return version.compareTo(this.minimum) >= 0
                && version.compareTo(this.maximum) <= 0;
    }

    public ClientVersion getMinimum() {
        return this.minimum;
    }

    public ClientVersion getMaximum() {
        return this.maximum;
    }

    @Override
    public String toString() {
        return "(" + this.minimum + " to " + this.maximum + ")";
    }
}
