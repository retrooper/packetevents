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

    private static Object tinyProtocol;

    public static void initTinyProtocol(final Plugin plugin) {
        if (version.isLowerThan(ServerVersion.v_1_8)) {
            TinyProtocolHandler_1_7 tiny = new TinyProtocolHandler_1_7(plugin);
            tiny.initTinyProtocol();
            tinyProtocol = tiny;
        } else {
            TinyProtocolHandler_1_8 tiny = new TinyProtocolHandler_1_8(plugin);
            tiny.initTinyProtocol();
            tinyProtocol = tiny;
        }
    }

    public static boolean hasInjected(final Player player) {
        if (version.isLowerThan(ServerVersion.v_1_8)) {
            TinyProtocolHandler_1_7 tiny = (TinyProtocolHandler_1_7) tinyProtocol;
            assert tiny.tinyProtocol != null;
            return tiny.tinyProtocol.hasInjected(player);
        } else {
            TinyProtocolHandler_1_8 tiny = (TinyProtocolHandler_1_8) tinyProtocol;
            assert tiny.tinyProtocol != null;
            return tiny.tinyProtocol.hasInjected(player);
        }
    }

    public static void inject(final Player player) {
        if (version.isLowerThan(ServerVersion.v_1_8)) {
            TinyProtocolHandler_1_7 tiny = (TinyProtocolHandler_1_7) tinyProtocol;
            assert tiny.tinyProtocol != null;
            tiny.tinyProtocol.injectPlayer(player);
        } else {
            TinyProtocolHandler_1_8 tiny = (TinyProtocolHandler_1_8) tinyProtocol;
            assert tiny.tinyProtocol != null;
            tiny.tinyProtocol.injectPlayer(player);
        }
    }

    public static void uninject(final Player player) {
        if (version.isLowerThan(ServerVersion.v_1_8)) {
            TinyProtocolHandler_1_7 tiny = (TinyProtocolHandler_1_7) tinyProtocol;
            assert tiny.tinyProtocol != null;
            tiny.tinyProtocol.uninjectPlayer(player);
        } else {
            TinyProtocolHandler_1_8 tiny = (TinyProtocolHandler_1_8) tinyProtocol;
            assert tiny.tinyProtocol != null;
            tiny.tinyProtocol.uninjectPlayer(player);
        }
    }


    public static Object getPlayerChannel(final Player player) {
        final Object entityPlayer = NMSUtils.getEntityPlayer(player);
        final Object playerConnection = playerConnectionAccessor.get(entityPlayer);
        final Object networkManager = networkManagerAccessor.get(playerConnection);
        return nettyChannelAccessor.get(networkManager);
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