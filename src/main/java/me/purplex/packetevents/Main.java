package me.purplex.packetevents;

import com.comphenix.tinyprotocol.*;
import io.netty.channel.Channel;
import me.purplex.packetevents.events.PacketReceiveEvent;
import me.retrooper.packetevents.events.*;
import me.purplex.packetevents.example.TestExample;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public TinyProtocol protocol;
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        PacketEvents.getPacketManager().registerPacketListener(new TestExample());
        protocol = new TinyProtocol(this) {
            @Override
            public Object onPacketInAsync(Player p, Channel channel, Object packet) {
                if(packet == null)  return super.onPacketInAsync(p, channel, null);
                if(p == null)  return super.onPacketInAsync(null, channel, packet);
                if(channel == null)  return super.onPacketInAsync(p, null, packet);
                String packetName = packet.getClass().getSimpleName();
                PacketEvents.getPacketManager().callPacketReceiveEvent(new PacketReceiveEvent(p, packetName, packet));
                return super.onPacketInAsync(p, channel, packet);
            }

            @Override
            public Object onPacketOutAsync(Player p, Channel channel, Object packet) {
                if(packet == null)  return super.onPacketInAsync(p, channel, null);
                if(p == null)  return super.onPacketInAsync(null, channel, packet);
                if(channel == null)  return super.onPacketInAsync(p, null, packet);
                String packetName = packet.getClass().getSimpleName();
                PacketEvents.getPacketManager().callPacketReceiveEvent(new PacketReceiveEvent(p, packetName, packet));
                return super.onPacketOutAsync(p, channel, packet);
            }
        };

    }
    @Override
    public void onDisable() {

    }


}
