package me.purplex.packetevents;

import me.purplex.packetevents.customlistener.injector.PacketInjector;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.example.TestExample;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private static PacketInjector packetInjector;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        packetInjector = new PacketInjector();
        PacketEvents.getPacketManager().registerListener(new TestExample());

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                PacketEvents.getPacketManager().callEvent(new ServerTickEvent(now));
            }
        }, 0L, 1L);

    }

    @Override
    public void onDisable() {
        /**
         * You don't need to unregister all events when the server shutsdown,
         * This is just an example
         */

        PacketEvents.getPacketManager().unregisterListener(new TestExample());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                packetInjector.injectPlayer(e.getPlayer());
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        packetInjector.uninjectPlayer(e.getPlayer());
    }

    public static Main getInstance() {
        return instance;
    }


}
