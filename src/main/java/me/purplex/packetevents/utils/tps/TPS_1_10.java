package me.purplex.packetevents.utils.tps;

import net.minecraft.server.v1_10_R1.MinecraftServer;

public class TPS_1_10 {
    public static double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }
}