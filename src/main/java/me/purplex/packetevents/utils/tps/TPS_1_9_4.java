package me.purplex.packetevents.utils.tps;

import net.minecraft.server.v1_9_R2.MinecraftServer;

public class TPS_1_9_4 {
    public static double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }
}
