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

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    V_1_7_10(5),

    V_1_8(47),

    V_1_9(107), V_1_9_1(108), V_1_9_2(109),
    /**
     * 1.9.3 or 1.9.4 as they have the same protocol version.
     */
    V_1_9_3(110),
    V_1_10(210),
    V_1_11(315),
    /**
     * 1.11.1 or 1.11.2 as they have the same protocol version.
     */
    V_1_11_1(316),
    V_1_12(335), V_1_12_1(338), V_1_12_2(340),

    V_1_13(393), V_1_13_1(401), V_1_13_2(404),

    V_1_14(477), V_1_14_1(480), V_1_14_2(485),
    V_1_14_3(490), V_1_14_4(498),

    V_1_15(573), V_1_15_1(575), V_1_15_2(578),

    V_1_16(735), V_1_16_1(736), V_1_16_2(751),
    V_1_16_3(753),
    /**
     * 1.16.4 or 1.16.5 as they have the same protocol version.
     */
    V_1_16_4(754),

    V_1_17(755), V_1_17_1(756),

    /**
     * 1.18 or 1.18.1 as they have the same protocol version.
     */
    V_1_18(757),
    V_1_18_2(758),

    V_1_19(759),
    V_1_19_1(760),
    V_1_19_3(761),
    V_1_19_4(762),
    /**
     * 1.20 and 1.20.1 have the same protocol version.
     */
    V_1_20(763),
    V_1_20_2(764),
    /**
     * 1.20.3 and 1.20.4 have the same protocol version.
     */
    V_1_20_3(765),
    /**
     * 1.20.5 and 1.20.6 have the same protocol version.
     */
    V_1_20_5(766),

    /**
     * 1.21 and 1.21.1 have the same protocol version.
     */
    V_1_21(767),
    V_1_21_2(768),
    //TODO UPDATE Add new protocol version field

    @Deprecated
    LOWER_THAN_SUPPORTED_VERSIONS(V_1_7_10.protocolVersion - 1, true),
    //TODO UPDATE Update HIGHER_THAN_SUPPORTED_VERSIONS field
    @Deprecated
    HIGHER_THAN_SUPPORTED_VERSIONS(V_1_21_2.protocolVersion + 1, true),

    UNKNOWN(-1, true);

    private static final ClientVersion[] VALUES = values();
    private static final ClientVersion[] REVERSED_VALUES;

    static {
        List<ClientVersion> valuesAsList = Arrays.asList(values());
        Collections.reverse(valuesAsList);
        REVERSED_VALUES = valuesAsList.toArray(new ClientVersion[0]);
    }

    private static final int LOWEST_SUPPORTED_PROTOCOL_VERSION = LOWER_THAN_SUPPORTED_VERSIONS.protocolVersion + 1;
    private static final int HIGHEST_SUPPORTED_PROTOCOL_VERSION = HIGHER_THAN_SUPPORTED_VERSIONS.protocolVersion - 1;

    private final int protocolVersion;
    private final String name;
    private ServerVersion serverVersion;

    ClientVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.name = name().substring(2).replace("_", ".");
    }

    ClientVersion(int protocolVersion, boolean isNotRelease) {
        this.protocolVersion = protocolVersion;
        if (isNotRelease) {
            this.name = name();
        } else {
            this.name = name().substring(2).replace("_", ".");
        }
    }

    public static boolean isPreRelease(int protocolVersion) {
        return getLatest().protocolVersion <= protocolVersion
                || getOldest().protocolVersion >= protocolVersion;
    }

    public static boolean isRelease(int protocolVersion) {
        return protocolVersion <= getLatest().protocolVersion
                && protocolVersion >= getOldest().protocolVersion;
    }

    public boolean isPreRelease() {
        return isPreRelease(protocolVersion);
    }

    public boolean isRelease() {
        return isRelease(protocolVersion);
    }

    /**
     * Get the release name of this client version.
     * For example, for the V_1_18 enum constant, it would return "1.18".
     *
     * @return Release name
     */
    public String getReleaseName() {
        return name;
    }

    /**
     * Get a ClientVersion enum by protocol version.
     *
     * @param protocolVersion Protocol version.
     * @return ClientVersion
     */
    @NotNull
    public static ClientVersion getById(int protocolVersion) {
        if (protocolVersion < LOWEST_SUPPORTED_PROTOCOL_VERSION) {
            return getOldest();
        } else if (protocolVersion > HIGHEST_SUPPORTED_PROTOCOL_VERSION) {
            return getLatest();
        } else {
            for (ClientVersion version : VALUES) {
                if (version.protocolVersion > protocolVersion) {
                    break;
                } else if (version.protocolVersion == protocolVersion) {
                    return version;
                }
            }
            return UNKNOWN;
        }
    }

    public static ClientVersion getLatest() {
        return REVERSED_VALUES[3];
    }

    public static ClientVersion getOldest() {
        return VALUES[0];
    }

    @Deprecated
    public ServerVersion toServerVersion() {
        if (serverVersion == null) {
            serverVersion = ServerVersion.getById(protocolVersion);
        }
        return serverVersion;
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
        return protocolVersion > target.protocolVersion;
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
        return this.protocolVersion >= target.protocolVersion;
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
        return protocolVersion < target.protocolVersion;
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
        return this.protocolVersion <= target.protocolVersion;
    }

    /**
     * Is this client version newer than, older than or equal to the compared client version?
     * This method simply checks if this client version's protocol version is greater than, less than or equal to
     * the compared client version's protocol version.
     *
     * @param comparison    Comparison type.
     * @param targetVersion Compared client version.
     * @return true or false, based on the comparison type.
     * @see #isNewerThan(ClientVersion)
     * @see #isNewerThanOrEquals(ClientVersion)
     * @see #isOlderThan(ClientVersion)
     * @see #isOlderThanOrEquals(ClientVersion)
     */
    public boolean is(@NotNull VersionComparison comparison, @NotNull ClientVersion targetVersion) {
        switch (comparison) {
            case EQUALS:
                return protocolVersion == targetVersion.protocolVersion;
            case NEWER_THAN:
                return isNewerThan(targetVersion);
            case NEWER_THAN_OR_EQUALS:
                return isNewerThanOrEquals(targetVersion);
            case OLDER_THAN:
                return isOlderThan(targetVersion);
            case OLDER_THAN_OR_EQUALS:
                return isOlderThanOrEquals(targetVersion);
            default:
                return false;
        }
    }
}
