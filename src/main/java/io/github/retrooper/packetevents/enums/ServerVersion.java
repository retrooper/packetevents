package io.github.retrooper.packetevents.enums;

import org.bukkit.Bukkit;

/**
 * @author retrooper
 */
public enum ServerVersion {
    v_1_7_10, v_1_8, v_1_8_3, v_1_8_8, v_1_9, v_1_9_4, v_1_10, v_1_11, v_1_12, v_1_13, v_1_13_2, v_1_14, v_1_15, v_1_16_1,
    ERROR, EMPTY;

    private static final ServerVersion[] reversedValues;

    static {
        reversedValues = reverse(values());
    }

    private static final String nmsVersionSuffix = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    private static ServerVersion cachedVersion = ServerVersion.EMPTY;

    public static ServerVersion getVersion() {
        if(cachedVersion != ServerVersion.EMPTY) {
            return cachedVersion;
        }
        for (final ServerVersion val : reversedValues) {
            String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                cachedVersion = val;
                return val;
            }
        }
        return ERROR;
    }

    private static ServerVersion[] reverse(final ServerVersion[] arr) {
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
