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

package io.github.retrooper.packetevents.enums;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 * @author retrooper
 */
public enum ServerVersion {

    v_1_7_10, v_1_8, v_1_8_3, v_1_8_4, v_1_8_5, v_1_8_6, v_1_8_7, v_1_8_8,
    v_1_9, v_1_9_2, v_1_9_4, v_1_10, v_1_10_2, v_1_11, v_1_11_1, v_1_11_2,
    v_1_12, v_1_12_1, v_1_12_2, v_1_13, v_1_13_1, v_1_13_2, v_1_14, v_1_14_1,
    v_1_14_2, v_1_14_3, v_1_14_4, v_1_15, v_1_15_1, v_1_15_2, v_1_16, v_1_16_1,
    v_1_16_2, ERROR, EMPTY;

    private static final String nmsVersionSuffix = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    public static ServerVersion[] reversedValues = new ServerVersion[values().length];
    private static ServerVersion cachedVersion;

    private static ServerVersion getVersionNoCache() {
        if(reversedValues[0] == null) {
            reversedValues = ServerVersion.reverse(values());
        }
        for (final ServerVersion val : reversedValues) {
            String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            }
        }
        if(PacketEvents.getSettings().getDefaultServerVersion() != null) {
            return PacketEvents.getSettings().getDefaultServerVersion();
        }
        return ERROR;
    }

    public static ServerVersion getVersion() {
        if (cachedVersion == null) {
            cachedVersion = getVersionNoCache();
        }
        //WARNING TODO debug
        System.out.println(cachedVersion.name());
        return cachedVersion;
    }


    public static ServerVersion[] reverse(final ServerVersion[] arr) {
        ServerVersion[] array = arr.clone();
        if (array == null) {
            return null;
        }
        int i = 0;
        int j = array.length - 1;
        ServerVersion tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static String getNmsSuffix() {
        return nmsVersionSuffix;
    }

    public static String getNMSDirectory() {
        return "net.minecraft.server." + getNmsSuffix();
    }

    public static String getOBCDirectory() {
        return "org.bukkit.craftbukkit." + getNmsSuffix();
    }

    /**
     * Returns if the current version is more up to date than the argument passed version
     *
     * @param version The version to compare to the server's value.
     * @return True if the supplied version is lower, false if it is equal or higher than the server's version.
     */
    public boolean isHigherThan(final ServerVersion version) {
        if (this == version) return false;
        return !isLowerThan(version);
    }

    /**
     * Returns if the current version is more outdated than the argument passed version
     *
     * @param version The version to compare to the server's value.
     * @return True if the supplied version is higher, false if it is equal or lower than the server's version.
     */
    public boolean isLowerThan(final ServerVersion version) {
        if (this == version) return false;
        final int len = ServerVersion.values().length;
        for (byte i = 0; i < len; i++) {
            final ServerVersion v = ServerVersion.values()[i];
            if (v == this) {
                return true;
            } else if (v == version) {
                return false;
            }
        }
        return false;
    }

    /**
     * Converts the server version to a protocol version.
     * Recommended to cache.
     * @return protocol version
     */
    public short toProtocolVersion() {
        switch (this) {
            case v_1_7_10:
                return 5;
            case v_1_8:
            case v_1_8_3:
            case v_1_8_4:
            case v_1_8_5:
            case v_1_8_6:
            case v_1_8_7:
            case v_1_8_8:
                return 47;
            case v_1_9:
                return 107;
            case v_1_9_2:
                return 109;
            case v_1_9_4:
                return 110;
            case v_1_10:
            case v_1_10_2:
                return 210;
            case v_1_11:
                return 315;
            case v_1_11_1:
            case v_1_11_2:
                return 316;
            case v_1_12:
                return 335;
            case v_1_12_1:
                return 338;
            case v_1_12_2:
                return 340;
            case v_1_13:
                return 393;
            case v_1_13_1:
                return 401;
            case v_1_13_2:
                return 404;
            case v_1_14:
                return 477;
            case v_1_14_1:
                return 480;
            case v_1_14_2:
                return 485;
            case v_1_14_3:
                return 490;
            case v_1_14_4:
                return 498;
            case v_1_15:
                return 573;
            case v_1_15_1:
                return 575;
            case v_1_15_2:
                return 578;
            case v_1_16:
                return 735;
            case v_1_16_1:
                return 736;
            case v_1_16_2:
                return 751;
            default:
                return -1;
        }
    }
}