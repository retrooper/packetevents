package me.purplex.packetevents.injector.channels._1_8;

import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.NetworkManager;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Channel_1_8 {

    private static Field field;
    static {
        try {
            field = NetworkManager.class.getDeclaredField("i");
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
