package me.purplex.packetevents.utils.tps;

import net.minecraft.server.v1_8_R2.MinecraftServer;

public class TPS_1_8_3 {
    public static double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }
}