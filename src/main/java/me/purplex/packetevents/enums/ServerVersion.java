package me.purplex.packetevents.enums;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;


public enum ServerVersion
{
    v_1_8, v_1_8_3, v_1_8_8, v_1_9, v_1_9_4, v_1_10_2, v_1_11_1, v_1_11_2, v_1_12_1, v_1_12_2, v_1_13, v_1_13_2, v_1_14, v_1_14_1, v_1_15_1,
    ERROR;

    public static ServerVersion getVersion() {
        ServerVersion[] vals = values();
        ArrayUtils.reverse(vals);
        for(ServerVersion val : vals) {
            String valName = val.name().substring(2).replace("_", ".");
            if(Bukkit.getBukkitVersion().contains(valName)) {
                System.out.println(val + " we got");
                return val;
            }
        }
        return ERROR;
    }
}

