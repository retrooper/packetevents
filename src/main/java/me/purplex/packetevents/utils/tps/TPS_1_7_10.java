package me.purplex.packetevents.utils.tps;

import net.minecraft.server.v1_7_R4.MinecraftServer;

public class TPS_1_7_10 {
    public static double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }
}
