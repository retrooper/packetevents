package me.purplex.packetevents.utils.tps;

import net.minecraft.server.v1_9_R1.MinecraftServer;

public class TPS_1_9 {
    public static double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }
}