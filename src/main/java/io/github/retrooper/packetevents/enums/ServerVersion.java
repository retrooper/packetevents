package io.github.retrooper.packetevents.enums;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;


public enum ServerVersion {
    v_1_7_10, v_1_8, v_1_8_3, v_1_8_8, v_1_9, v_1_9_4, v_1_10, v_1_11, v_1_12, v_1_13, v_1_13_2, v_1_14, v_1_15, v_1_16,
    ERROR;


    private static final String nmsVersionSuffix = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public static ServerVersion getVersion() {
        ServerVersion[] vals = values();
        ArrayUtils.reverse(vals);
        for (ServerVersion val : vals) {
            String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            }
        }
        return ERROR;
    }

    public static String getNmsSuffix() {
        return nmsVersionSuffix;
    }

    public static String getNMSDirectory() {
        return "net.minecraft.server." + nmsVersionSuffix;
    }

    public static String getOBCDirectory() {
        return "org.bukkit.craftbukkit." + getNmsSuffix();
    }

    /**
     * Returns if the current version is more up to date than the argument passed version
     *
     * @param version
     * @return
     */
    public boolean isHigherThan(final ServerVersion version) {
        if (this == version) return false;
        return !isLowerThan(version);
    }

    /**
     * Returns if the current version is more outdated than the argument passed version
     *
     * @param version
     * @return
     */
    public boolean isLowerThan(final ServerVersion version) {
        if (version == this) return false;
        byte len = (byte) values().length;
        for (byte i = 0; i < len; i++) {
            final ServerVersion v = values()[i];
            if (v == this) {
                return true;
            } else if (v == version) {
                return false;
            }
        }
        return false;
    }

}

