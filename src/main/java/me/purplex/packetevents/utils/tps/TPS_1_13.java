package me.purplex.packetevents.utils.tps;
import net.minecraft.server.v1_13_R1.MinecraftServer;

public class TPS_1_13 {
    public static double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }
}