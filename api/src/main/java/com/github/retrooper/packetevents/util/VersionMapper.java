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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public class VersionMapper {

    private final ClientVersion[] versions;
    private final ClientVersion[] reversedVersions;

    public VersionMapper(ClientVersion... versions) {
        this.versions = versions.clone();
        Arrays.sort(this.versions);

        this.reversedVersions = new ClientVersion[this.versions.length];
        for (int i = this.versions.length - 1, j = 0; i >= 0; i--, j++) {
            this.reversedVersions[j] = this.versions[i];
        }
    }

    public VersionMapper withExtra(ClientVersion extraStep) {
        if (Arrays.binarySearch(this.versions, extraStep) >= 0) {
            return this; // already contained
        }
        ClientVersion[] clonedVersions = Arrays.copyOf(this.versions, this.versions.length + 1);
        clonedVersions[clonedVersions.length - 1] = extraStep; // insert extra mapping step, sorting will be handled by the ctor
        return new VersionMapper(clonedVersions);
    }

    public ClientVersion[] getVersions() {
        return versions;
    }

    public ClientVersion[] getReversedVersions() {
        return reversedVersions;
    }

    public int getIndex(ClientVersion version) {
        int index = reversedVersions.length - 1;
        for (ClientVersion v : reversedVersions) {
            if (version.isNewerThanOrEquals(v)) {
                return index;
            }
            index--;
        }
        //Give them the oldest version
        return 0;
    }

    public int size() {
        return this.versions.length;
    }
}
