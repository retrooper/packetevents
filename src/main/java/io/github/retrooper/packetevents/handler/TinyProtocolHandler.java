package io.github.retrooper.packetevents.handler;


import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.tinyprotocol.Reflection;
import io.github.retrooper.packetevents.tinyprotocol.Reflection.FieldAccessor;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TinyProtocolHandler {
    private static final ServerVersion version = PacketEvents.getServerVersion();
    private final Plugin plugin;

    public TinyProtocolHandler(final Plugin plugin) {
        this.plugin = plugin;
    }
    public void initTinyProtocol() {
        if(version.isLowerThan(ServerVersion.v_1_8)) {
            new TinyProtocolHandler_1_7(getPlugin()).initTinyProtocol();
        }
        else {
            new TinyProtocolHandler_1_8(getPlugin()).initTinyProtocol();
        }
    }

    public static Object getPlayerChannel(final Player player) {
        final Object entityPlayer = NMSUtils.getEntityPlayer(player);
        final Object playerConnection = playerConnectionAccessor.get(entityPlayer);
        final Object networkManager = networkManagerAccessor.get(playerConnection);
        return nettyChannelAccessor.get(networkManager);
    }

    public final Plugin getPlugin() {
        return plugin;
    }

    private static Class<?> networkManagerClass;

    private static final FieldAccessor<?> playerConnectionAccessor, networkManagerAccessor, nettyChannelAccessor;

    private static Class<?> channelClass;

    static {
        try {
            networkManagerClass = NMSUtils.getNMSClass("NetworkManager");
            channelClass = NMSUtils.getNettyClass("channel.Channel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        playerConnectionAccessor = Reflection.getField(NMSUtils.entityPlayerClass, NMSUtils.playerConnectionClass, 0);
        networkManagerAccessor = Reflection.getField(NMSUtils.playerConnectionClass, networkManagerClass, 0);
        nettyChannelAccessor = Reflection.getField(networkManagerClass, channelClass, 0);
    }
}