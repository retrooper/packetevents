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

package com.github.retrooper.packetevents.manager.server;

/**
 * Server Version.
 * This is a nice wrapper over minecraft's protocol versions.
 * You won't have to memorize the protocol version, just memorize the server version you see in the launcher.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
 * @since 1.6.9
 */
public enum ServerVersion {
    //TODO Rename to PlatformVersion or LocalVersion or Version?
    V_1_7_10(5),
    V_1_8(47), V_1_8_3(47), V_1_8_8(47),
    V_1_9(107), V_1_9_2(109), V_1_9_4(110),
    //1.10 and 1.10.1 are redundant
    V_1_10(210), V_1_10_1(210), V_1_10_2(210),
    V_1_11(315), V_1_11_2(316),
    V_1_12(335), V_1_12_1(338), V_1_12_2(340),
    V_1_13(393), V_1_13_1(401), V_1_13_2(404),
    V_1_14(477), V_1_14_1(480), V_1_14_2(485), V_1_14_3(490), V_1_14_4(498),
    V_1_15(573), V_1_15_1(575), V_1_15_2(578),
    V_1_16(735), V_1_16_1(736), V_1_16_2(751), V_1_16_3(753), V_1_16_4(754), V_1_16_5(754),
    V_1_17(755), V_1_17_1(756),
    V_1_18(757), V_1_18_1(757), V_1_18_2(758),
    //TODO UPDATE Add server version constant
    ERROR(-1, true);

    private static final ServerVersion[] VALUES = values();
    private static final ServerVersion[] REVERSED_VALUES;

    static {
        REVERSED_VALUES = values();
        int i = 0;
        int j = REVERSED_VALUES.length - 1;
        ServerVersion tmp;
        while (j > i) {
            tmp = REVERSED_VALUES[j];
            REVERSED_VALUES[j--] = REVERSED_VALUES[i];
            REVERSED_VALUES[i++] = tmp;
        }
    }

    private final int protocolVersion;
    private final String name;

    ServerVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.name = name().substring(2).replace("_", ".");
    }

    ServerVersion(int protocolVersion, boolean isNotRelease) {
        this.protocolVersion = protocolVersion;
        if (isNotRelease) {
            this.name = name();
        } else {
            this.name = name().substring(2).replace("_", ".");
        }
    }

    public static ServerVersion[] reversedValues() {
        return REVERSED_VALUES;
    }

    public static ServerVersion getLatest() {
        return reversedValues()[1];
    }

    public static ServerVersion getOldest() {
        return values()[0];
    }

    /**
     * Get the release name of this server version.
     * For example, for the V_1_18 enum constant, it would return "1.18".
     * @return Release name
     */
    public String getReleaseName() {
        return name;
    }

    /**
     * Get this server version's protocol version.
     *
     * @return Protocol version.
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Is this server version newer than the compared server version?
     * This method simply checks if this server version's protocol version is greater than
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version newer than the compared server version.
     */
    public boolean isNewerThan(ServerVersion target) {
        /*
         * Some server versions have the same protocol version in the minecraft protocol.
         * We still need this method to work in such cases.
         * We first check if this is the case, if the protocol versions aren't the same, we can just use the protocol versions
         * to compare the server versions.
         */
        if (target.protocolVersion != protocolVersion || this == target) {
            return protocolVersion > target.protocolVersion;
        }

        /*
         * The server versions unfortunately have the same protocol version.
         * We need to look at this "reversedValues" variable.
         * The reversed values variable is an array containing all enum constants in this enum but in a reversed order.
         * I already made this variable a while ago for a different usage, you can check that out.
         * The first one we find in the array is the newer version.
         */
        for (ServerVersion version : reversedValues()) {
            if (version == target) {
                return false;
            }
            if (version == this) return true;
        }

        return false;
    }

    /**
     * Is this server version older than the compared server version?
     * This method simply checks if this server version's protocol version is less than
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version older than the compared server version.
     */
    public boolean isOlderThan(ServerVersion target) {
        /*
         * Some server versions have the same protocol version in the minecraft protocol.
         * We still need this method to work in such cases.
         * We first check if this is the case, if the protocol versions aren't the same, we can just use the protocol versions
         * to compare the server versions.
         */
        if (target.protocolVersion != protocolVersion || this == target) {
            return protocolVersion < target.protocolVersion;
        }
        /*
         * The server versions unfortunately have the same protocol version.
         * We look at all enum constants in the ServerVersion enum in the order they have been defined in.
         * The first one we find in the array is the newer version.
         */
        for (ServerVersion version : VALUES) {
            if (version == this) {
                return true;
            } else if (version == target) {
                return false;
            }
        }
        return false;
    }

    /**
     * Is this server version newer than or equal to the compared server version?
     * This method simply checks if this server version's protocol version is greater than or equal to
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version newer than or equal to the compared server version.
     */
    public boolean isNewerThanOrEquals(ServerVersion target) {
        return this == target || isNewerThan(target);
    }

    /**
     * Is this server version older than or equal to the compared server version?
     * This method simply checks if this server version's protocol version is older than or equal to
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version older than or equal to the compared server version.
     */
    public boolean isOlderThanOrEquals(ServerVersion target) {
        return this == target || isOlderThan(target);
    }
}
