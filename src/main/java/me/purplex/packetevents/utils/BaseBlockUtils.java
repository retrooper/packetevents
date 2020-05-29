package me.purplex.packetevents.utils;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.utils.math.Vector3i;

public class BaseBlockUtils {
    private static final ServerVersion version = PacketEvents.getServerVersion();

    public static String getPosXFieldName() {
        return "a";
    }

    public static String getPosYFieldName() {
        if (version == ServerVersion.v_1_8) {
            return "b";
        } else if (version == ServerVersion.v_1_8_3
                || version == ServerVersion.v_1_8_8
                || version == ServerVersion.v_1_9) {
            return "c";
        }
        //1.9.4 and higher
        return "b";
    }

    public static String getPosZFieldName() {
        if (version == ServerVersion.v_1_8) {
            return "c";
        } else if (version == ServerVersion.v_1_8_3 || version == ServerVersion.v_1_8_8 || version == ServerVersion.v_1_9) {
            return "d";
        }
        return "c";
    }
}
