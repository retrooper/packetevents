/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.utils.player;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Client Version.
 * This is a nice tool for minecraft's client protocol versions.
 * You won't have to memorize the protocol version, just memorize the client version
 * as the version you see in the minecraft launcher.
 * Some enum constants may represent two or more versions as there have been cases where some versions have the same protocol version due to no protocol changes.
 * We added a comment over those enum constants so check it out.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
 * @since 1.6.9
 */
public enum ClientVersion {
    v_1_7_10(5),

    v_1_8(47),
    v_1_9(107),

    v_1_9_1(108),
    v_1_9_2(109),
    /**
     * 1.9.3 or 1.9.4 as they have the same protocol version.
     */
    v_1_9_3(110),

    v_1_10(210),
    v_1_11(315),
    /**
     * 1.11.1 or 1.11.2 as they have the same protocol version.
     */
    v_1_11_1(316),

    v_1_12(335),
    v_1_12_1(338),
    v_1_12_2(340),

    v_1_13(393),
    v_1_13_1(401),
    v_1_13_2(404),

    v_1_14(477),
    v_1_14_1(480),
    v_1_14_2(485),
    v_1_14_3(490),
    v_1_14_4(498),

    v_1_15(573),
    v_1_15_1(575),
    v_1_15_2(578),

    v_1_16(735),
    v_1_16_1(736),
    v_1_16_2(751),
    v_1_16_3(753),
    /**
     * 1.16.4 or 1.16.5 as they have the same protocol version.
     */
    v_1_16_4(754),

    v_1_17(755),

    v_1_17_1(756),

    /**
     * 1.18 or 1.18.1 as they have the same protocol version.
     */
    v_1_18(757),

    v_1_18_2(758),

    v_1_19(759), v_1_19_1(760), v_1_19_3(761), v_1_19_4(762),
    //TODO Update(checkpoint for things to look out for when updating)

    LOWER_THAN_SUPPORTED_VERSIONS(v_1_7_10.protocolVersion - 1),
    //TODO Update(checkpoint for things to look out for when updating)
    HIGHER_THAN_SUPPORTED_VERSIONS(v_1_19_4.protocolVersion + 1),
    /**
     * Pre releases just aren't supported, we would end up with so many enum constants.
     * This constant assures you they are on a pre-release.
     */
    ANY_PRE_RELEASE_VERSION(0),

    TEMP_UNRESOLVED(-1),

    @Deprecated
    UNRESOLVED(-1),

    UNKNOWN(-1);

    private static final int LOWEST_SUPPORTED_PROTOCOL_VERSION = LOWER_THAN_SUPPORTED_VERSIONS.protocolVersion + 1;
    private static final int HIGHEST_SUPPORTED_PROTOCOL_VERSION = HIGHER_THAN_SUPPORTED_VERSIONS.protocolVersion - 1;

    private static final Map<Integer, ClientVersion> CLIENT_VERSION_CACHE = new IdentityHashMap<>();
    //TODO Update(checkpoint for things to look out for when updating)
    private static final int[] CLIENT_VERSIONS = new int[]{5, 47, 107, 108, 109, 110, 210, 315, 316, 335, 338,
            340, 393, 401, 404, 477, 480, 485, 490, 498, 573,
            575, 578, 735, 736, 751, 753, 754, 755, 756, 757, 758, 759, 760, 761, 762};
    private int protocolVersion;

    ClientVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Get a ClientVersion enum by protocol version.
     *
     * @param protocolVersion Protocol version.
     * @return ClientVersion
     */
    @NotNull
    public static ClientVersion getClientVersion(int protocolVersion) {
        if (protocolVersion == -1) {
            return ClientVersion.UNRESOLVED;
        } else if (protocolVersion < LOWEST_SUPPORTED_PROTOCOL_VERSION) {
            return LOWER_THAN_SUPPORTED_VERSIONS;
        } else if (protocolVersion > HIGHEST_SUPPORTED_PROTOCOL_VERSION) {
            return HIGHER_THAN_SUPPORTED_VERSIONS;
        } else {
            ClientVersion cached = CLIENT_VERSION_CACHE.get(protocolVersion);
            if (cached == null) {
                for (ClientVersion version : values()) {
                    if (version.protocolVersion > protocolVersion) {
                        break;
                    } else if (version.protocolVersion == protocolVersion) {
                        //Cache for next time
                        CLIENT_VERSION_CACHE.put(protocolVersion, version);
                        return version;
                    }
                }
                cached = UNKNOWN;
                cached.protocolVersion = protocolVersion;
            }
            return cached;
        }
    }

    /**
     * Protocol version of this client version.
     *
     * @return Protocol version.
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Is this client version newer than the compared client version?
     * This method simply checks if this client version's protocol version is greater than
     * the compared client version's protocol version.
     *
     * @param target Compared client version.
     * @return Is this client version newer than the compared client version.
     */
    public boolean isNewerThan(ClientVersion target) {
        return protocolVersion > target.protocolVersion &&
                (target != UNRESOLVED && this != UNRESOLVED && target != TEMP_UNRESOLVED && this != TEMP_UNRESOLVED);
    }

    /**
     * Is this client version newer than or equal to the compared client version?
     * This method simply checks if this client version's protocol version is newer than or equal to
     * the compared client version's protocol version.
     *
     * @param target Compared client version.
     * @return Is this client version newer than or equal to the compared client version.
     */
    public boolean isNewerThanOrEquals(ClientVersion target) {
        return this == target || isNewerThan(target);
    }

    /**
     * Is this client version older than the compared client version?
     * This method simply checks if this client version's protocol version is less than
     * the compared client version's protocol version.
     *
     * @param target Compared client version.
     * @return Is this client version older than the compared client version.
     */
    public boolean isOlderThan(ClientVersion target) {
        return protocolVersion < target.protocolVersion &&
                (target != UNRESOLVED && this != UNRESOLVED && target != TEMP_UNRESOLVED && this != TEMP_UNRESOLVED);
    }

    /**
     * Is this client version older than or equal to the compared client version?
     * This method simply checks if this client version's protocol version is older than or equal to
     * the compared client version's protocol version.
     *
     * @param target Compared client version.
     * @return Is this client version older than or equal to the compared client version.
     */
    public boolean isOlderThanOrEquals(ClientVersion target) {
        return this == target || isOlderThan(target);
    }

    /**
     * Deprecated, please use {@link #isNewerThan(ClientVersion)}
     *
     * @param target Compared client version.
     * @return Is this client version newer than the compared client version.
     * @deprecated Rename...
     */
    @Deprecated
    public boolean isHigherThan(ClientVersion target) {
        return isNewerThan(target);
    }

    /**
     * Deprecated, please use {@link #isNewerThanOrEquals(ClientVersion)}
     *
     * @param target Compared client version.
     * @return Is this client version newer than or equal to the compared client version.
     * @deprecated Rename...
     */
    @Deprecated
    public boolean isHigherThanOrEquals(ClientVersion target) {
        return isNewerThanOrEquals(target);
    }

    /**
     * Deprecated, please use {@link #isOlderThan(ClientVersion)}
     *
     * @param target Compared client version.
     * @return Is this client version older than the compared client version.
     * @deprecated Rename...
     */
    @Deprecated
    public boolean isLowerThan(ClientVersion target) {
        return isOlderThan(target);
    }

    /**
     * Deprecated, please use {@link #isOlderThanOrEquals(ClientVersion)}
     *
     * @param target Compared client version.
     * @return Is this client version older than or equal to the compared client version.
     * @deprecated Rename...
     */
    @Deprecated
    public boolean isLowerThanOrEquals(ClientVersion target) {
        return isOlderThanOrEquals(target);
    }


    /**
     * Is this client version a pre release?
     * This method checks if this version is a pre release.
     *
     * @return Is pre release
     */
    public boolean isPreRelease() {
        if (protocolVersion > LOWEST_SUPPORTED_PROTOCOL_VERSION && protocolVersion < HIGHEST_SUPPORTED_PROTOCOL_VERSION) {
            //We don't have to iterate through the LOWEST and the HIGHEST supported version anymore...
            return Arrays.binarySearch(CLIENT_VERSIONS, protocolVersion) < 0;
        }
        return true;
    }

    /**
     * Is this client version resolved?
     * This method checks if the version is not equal to TEMP_UNRESOLVED or UNRESOLVED.
     *
     * @return Is resolved
     */
    public boolean isResolved() {
        return this != TEMP_UNRESOLVED && this != UNRESOLVED;
    }
}