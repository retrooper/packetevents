/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.utils.server;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;

/**
 * Server Version.
 * This is a nice tool for minecraft's protocol versions.
 * You won't have to memorize the protocol version, just memorize the server version
 * as the version you see on the release.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
 * @since 1.6.9
 */
public enum ServerVersion {
    v_1_7_10((short) 5), v_1_8((short) 47), v_1_8_3((short) 47), v_1_8_4((short) 47), v_1_8_5((short) 47),
    v_1_8_6((short) 47), v_1_8_7((short) 47), v_1_8_8((short) 47), v_1_9((short) 107), v_1_9_2((short) 109),
    v_1_9_4((short) 110), v_1_10((short) 210), v_1_10_2((short) 210), v_1_11((short) 315), v_1_11_1((short) 316),
    v_1_11_2((short) 316), v_1_12((short) 335), v_1_12_1((short) 338), v_1_12_2((short) 340), v_1_13((short) 393),
    v_1_13_1((short) 401), v_1_13_2((short) 404), v_1_14((short) 477), v_1_14_1((short) 480), v_1_14_2((short) 485),
    v_1_14_3((short) 490), v_1_14_4((short) 498), v_1_15((short) 573), v_1_15_1((short) 575), v_1_15_2((short) 578),
    v_1_16((short) 735), v_1_16_1((short) 736), v_1_16_2((short) 751), v_1_16_3((short) 753), v_1_16_4((short) 754),
    v_1_16_5((short) 754), ERROR((short) -1);

    private static final String NMS_VERSION_SUFFIX = Bukkit.getServer().getClass().getPackage().getName()
            .replace(".", ",").split(",")[3];
    private static final ServerVersion[] VALUES = values();
    public static ServerVersion[] reversedValues = new ServerVersion[VALUES.length];
    private static ServerVersion cachedVersion;
    private final short protocolVersion;

    ServerVersion(short protocolId) {
        this.protocolVersion = protocolId;
    }

    private static ServerVersion getVersionNoCache() {
        if (reversedValues[0] == null) {
            reversedValues = ServerVersion.reverse();
        }
        if (reversedValues == null) {
            throw new IllegalStateException("Failed to reverse the ServerVersion enum constant values.");
        }
        for (final ServerVersion val : reversedValues) {
            String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            }
        }
        if (PacketEvents.get().getSettings().getBackupServerVersion() != null) {
            return PacketEvents.get().getSettings().getBackupServerVersion();
        }
        return ERROR;
    }

    /**
     * Get the server version.
     * If PacketEvents has already attempted resolving, return the cached version.
     * If PacketEvents hasn't already attempted resolving, it will resolve it, cache it and return the version.
     *
     * @return Server Version. (possibly cached)
     */
    public static ServerVersion getVersion() {
        if (cachedVersion == null) {
            cachedVersion = getVersionNoCache();
        }
        return cachedVersion;
    }

    /**
     * The values in this enum in reverse.
     *
     * @return Reversed server version enum values.
     */
    private static ServerVersion[] reverse() {
        ServerVersion[] array = values();
        int i = 0;
        int j = array.length - 1;
        ServerVersion tmp;
        while (j > i) {
            tmp = array[j];
            array[j--] = array[i];
            array[i++] = tmp;
        }
        return array;
    }

    public static String getNMSSuffix() {
        return NMS_VERSION_SUFFIX;
    }

    public static String getNMSDirectory() {
        return "net.minecraft.server." + getNMSSuffix();
    }

    public static String getOBCDirectory() {
        return "org.bukkit.craftbukkit." + getNMSSuffix();
    }

    public static ServerVersion getLatest() {
        return reversedValues[0];
    }

    public static ServerVersion getOldest() {
        return values()[0];
    }

    /**
     * Get this server version's protocol version.
     *
     * @return Protocol version.
     */
    public short getProtocolVersion() {
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
        for (ServerVersion version : reversedValues) {
            if (version == target) {
                return false;
            }
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

    /**
     * Deprecated, please use {@link #isNewerThan(ServerVersion)}
     *
     * @param target Compared version.
     * @return Is this server version newer than the compared server version.
     * @deprecated Rename...
     **/
    @Deprecated
    public boolean isHigherThan(final ServerVersion target) {
        return isNewerThan(target);
    }

    /**
     * Deprecated, Please use {@link #isNewerThanOrEquals(ServerVersion)}
     *
     * @param target Compared server version.
     * @return Is this server version newer than or equal to the compared server version.
     * @deprecated Rename...
     */
    @Deprecated
    public boolean isHigherThanOrEquals(final ServerVersion target) {
        return isNewerThanOrEquals(target);
    }

    /**
     * Deprecated... Please use {@link #isOlderThan(ServerVersion)}
     *
     * @param target Compared server version.
     * @return Is this server version older than the compared server version.
     * @deprecated Rename....
     */
    @Deprecated
    public boolean isLowerThan(final ServerVersion target) {
        return isOlderThan(target);
    }

    /**
     * Deprecated, please use {@link #isOlderThanOrEquals(ServerVersion)}
     *
     * @param target Compared server version.
     * @return Is this server version older than or equal to the compared server version.
     * @deprecated Rename...
     */
    @Deprecated
    public boolean isLowerThanOrEquals(final ServerVersion target) {
        return isOlderThanOrEquals(target);
    }
}
