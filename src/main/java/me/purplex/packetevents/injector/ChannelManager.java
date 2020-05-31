package me.purplex.packetevents.injector;

import me.purplex.packetevents.enums.ServerVersion;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public class ChannelManager {
    private static final ServerVersion version = ServerVersion.getVersion();
    private static final String nmsDir = ServerVersion.getNMSDirectory();
    private static final Class<?> networkManager;
    private static final Field channelField;

    private static final Class<?> craftPlayer;
    private static final Method handleMethod;

    private static final Class<?> entityPlayer;

    private static final Class<?> playerConnection;
    private static final Field playerConnectionField;
    private static final Field networkManagerField;


    private static HashMap<UUID, Object> playerChannels = new HashMap<>();

    static {
        try {
            networkManager = Class.forName(nmsDir + ".NetworkManager");
            craftPlayer = Class.forName("org.bukkit.craftbukkit." + ServerVersion.getNmsSuffix() + ".entity.CraftPlayer");
            entityPlayer = Class.forName(nmsDir + ".EntityPlayer");
            playerConnection = Class.forName(nmsDir + ".PlayerConnection");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to find a NMS class! You are probably using an unsupported version of spigot.");
        }


        try {
            channelField = networkManager.getDeclaredField(getChannelFieldName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to find the channel field in the NetworkManager! You are probably using an unsupported version of spigot.");
        }
        if (!channelField.isAccessible()) {
            channelField.setAccessible(true);
        }

        try {
            handleMethod = craftPlayer.getMethod("getHandle");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to find the getHandle function in the CraftPlayer class! You are probably using an unsupported version of spigot.");
        }

        try {
            playerConnectionField = entityPlayer.getDeclaredField("playerConnection");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to find the playerConnection field in the EntityPlayer class! You are probably using an unsupported version of spigot.");
        }
        if (!playerConnectionField.isAccessible()) {
            playerConnectionField.setAccessible(true);
        }

        try {
            networkManagerField = playerConnection.getDeclaredField("networkManager");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to find the networkManager field in the PlayerConnection class! You are probably using an unsupported version of spigot.");
        }

        if (!networkManagerField.isAccessible()) {
            networkManagerField.setAccessible(true);
        }

    }

    private static Object getChannelObject(final Player player) throws IllegalAccessException, InvocationTargetException {
        final Object craftPlayerObj = craftPlayer.cast(player);
        final Object entityPlayerObj = handleMethod.invoke(craftPlayerObj);
        final Object playerConnectionObj = playerConnectionField.get(entityPlayerObj);
        final Object networkManagerObj = networkManagerField.get(playerConnectionObj);
        return channelField.get(networkManagerObj);
    }

    public static void removeChannel(final UUID uuid) {
        playerChannels.remove(uuid);
    }

    public static boolean contains(final Player player) {
        return playerChannels.containsKey(player.getUniqueId());
    }

    public static Object getChannel(final Player player) {
        final UUID uuid = player.getUniqueId();
        if (contains(player)) {
            return playerChannels.get(uuid);
        }

        Object channel = null;
        try {
            channel = getChannelObject(player);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (channel != null) {
            playerChannels.put(uuid, channel);
        } else {
            System.out.println("ITS NULL");
        }
        return channel;
    }

    private static String getChannelFieldName() {
        if (version == ServerVersion.v_1_7_10) {
            return "m";
        } else if (version == ServerVersion.v_1_8) {
            return "i";
        } else if (version == ServerVersion.v_1_8_3) {
            return "k";
        } else { //all 1.8.8 ->1.15
            return "channel";
        }
    }
}

