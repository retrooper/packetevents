package me.purplex.packetevents.enums;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;


public enum ServerVersion
{
    v_1_7_10, v_1_8, v_1_8_3, v_1_8_8, v_1_9, v_1_9_4, v_1_10, v_1_11, v_1_12, v_1_13, v_1_13_2, v_1_14, v_1_15,
    ERROR;


    public static ServerVersion getVersion() {
        ServerVersion[] vals = values();
        ArrayUtils.reverse(vals);
        for(ServerVersion val : vals) {
            String valName = val.name().substring(2).replace("_", ".");
            if(Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            }
        }
        return ERROR;
    }
    private static final String nmsVersionSuffix = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public static String getNmsSuffix() {
        return nmsVersionSuffix;
    }

    public static String getNMSDirectory() {
        return "net.minecraft.server." + nmsVersionSuffix;
    }

}

