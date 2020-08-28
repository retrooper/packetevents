/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.enums;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;

/**
 * @author retrooper
 */
public enum ServerVersion {
    v_1_7_10, v_1_8, v_1_8_3, v_1_8_8, v_1_9, v_1_9_4, v_1_10, v_1_11, v_1_12, v_1_13, v_1_13_2, v_1_14, v_1_15, v_1_16, v_1_16_1, v_1_16_2,
    ERROR, EMPTY;

    private static final String nmsVersionSuffix = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    public static ServerVersion[] reversedValues;
    private static ServerVersion cachedVersion;

    private static ServerVersion getVers() {
        for (final ServerVersion val : reversedValues) {
            String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            } else if (PacketEvents.getSettings().getDefaultServerVersion() != null) {
                return PacketEvents.getSettings().getDefaultServerVersion();
            }
        }
        return ERROR;
    }

    public static ServerVersion getVersion() {
        if (cachedVersion == null) {
            cachedVersion = getVers();
        }
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

    public int toProtocolVersion() {
        if (this.equals(ServerVersion.v_1_7_10)) {
            return 5;
        }
        if (this.name().startsWith("v_1_8")) {
            return 47;
        }
        if (this.equals(ServerVersion.v_1_9)) {
            return 107;
        }
        if (this.equals(ServerVersion.v_1_9_4)) {
            return 110;
        }
        if (this.equals(ServerVersion.v_1_10)) {
            return 210;
        }
        if (this.equals(ServerVersion.v_1_11)) {
            return 315;
        }
        if (this.equals(ServerVersion.v_1_12)) {
            return 335;
        }
        if (this.equals(ServerVersion.v_1_13)) {
            return 393;
        }
        if (this.equals(ServerVersion.v_1_13_2)) {
            return 404;
        }
        if (this.equals(ServerVersion.v_1_14)) {
            return 477;
        }
        if (this.equals(ServerVersion.v_1_15)) {
            return 573;
        }
        if (this.equals(ServerVersion.v_1_16)) {
            return 735;
        }
        if (this.equals(ServerVersion.v_1_16_1)) {
            return 736;
        }
        if (this.equals(ServerVersion.v_1_16_2)) {
            return 737;
        }
        return -1;
    }
}