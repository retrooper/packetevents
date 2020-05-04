package me.purplex.packetevents.injector.channels._1_12;

import io.netty.channel.Channel;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.NetworkManager;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Channel_1_12 {
    public Channel getChannel(Player p) {
        EntityPlayer entityPlayer = ((CraftPlayer)p).getHandle();
        NetworkManager networkManager = entityPlayer.playerConnection.networkManager;
        Channel channel = networkManager.channel;
        return channel;
    }
}
