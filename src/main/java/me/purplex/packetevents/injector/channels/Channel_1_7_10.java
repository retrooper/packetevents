package me.purplex.packetevents.injector.channels;

import io.netty.channel.Channel;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Channel_1_7_10 {

    private static Field field;
    static {
        try {
            field = NetworkManager.class.getDeclaredField("m");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
    }
    public Channel getChannel(Player p) {
        EntityPlayer entityPlayer = ((CraftPlayer)p).getHandle();
        NetworkManager networkManager = entityPlayer.playerConnection.networkManager;
        Channel channel = null;
        try {
            channel = (Channel) field.get(networkManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
