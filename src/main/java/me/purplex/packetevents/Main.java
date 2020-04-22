package me.purplex.packetevents;

import com.comphenix.tinyprotocol.*;
import io.netty.channel.Channel;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.example.TestExample;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public TinyProtocol protocol;
    @Override
    public void onEnable() {
        instance = this;
        //Bukkit.getPluginManager().registerEvents(this, this);
        PacketEvents.getPacketManager().registerPacketListener(new TestExample());
        protocol = new TinyProtocol(this) {
            @Override
            public Object onPacketInAsync(Player p, Channel channel, Object packet) {
                if(packet == null)  return super.onPacketInAsync(p, channel, null);
                if(p == null)  return super.onPacketInAsync(null, channel, packet);
                if(channel == null)  return super.onPacketInAsync(p, null, packet);
                String packetName = packet.getClass().getSimpleName();
                PacketReceiveEvent e = new PacketReceiveEvent(p, packetName, packet);
                PacketEvents.getPacketManager().callPacketReceiveEvent(e);
                if(e.isCancelled()) {
                    return super.onPacketInAsync(null, null, null);
                }
                return super.onPacketInAsync(p, channel, packet);
            }

            @Override
            public Object onPacketOutAsync(Player p, Channel channel, Object packet) {
                if(packet == null)  return super.onPacketInAsync(p, channel, null);
                if(p == null)  return super.onPacketInAsync(null, channel, packet);
                if(channel == null)  return super.onPacketInAsync(p, null, packet);
                String packetName = packet.getClass().getSimpleName();
                PacketSendEvent e = new PacketSendEvent(p, packetName, packet);
                PacketEvents.getPacketManager().callPacketSendEvent(e);
                if(e.isCancelled()) {
                    return super.onPacketOutAsync(null, null, null);
                }
                return super.onPacketOutAsync(p, channel, packet);
            }
        };

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                PacketEvents.getPacketManager().callServerTickEvent(new ServerTickEvent(now));
            }
        },  0L, 1L);

    }
    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return instance;
    }


}
