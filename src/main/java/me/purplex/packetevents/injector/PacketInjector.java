package me.purplex.packetevents.injector;


import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;
import org.bukkit.entity.Player;


public class PacketInjector {

    private final static ServerVersion version = PacketEvents.getServerVersion();

    private static final String nettyImport = "io.netty.channel";

    public static final String nettyPrefixImport = version == ServerVersion.v_1_7_10 ? "net.minecraft.util." + nettyImport : nettyImport;

    public void injectPlayer(final Player player) {
        if(version == ServerVersion.v_1_7_10) {
            PlayerInject_1_7_10.injectPlayer(player);
        }
        else {
            PlayerInject_1_8.injectPlayer(player);
        }
    }

    public void uninjectPlayer(final Player player) {
        if(version == ServerVersion.v_1_7_10) {
            PlayerInject_1_7_10.uninjectPlayer(player);
        }
        else {
            PlayerInject_1_8.uninjectPlayer(player);
        }
    }

}