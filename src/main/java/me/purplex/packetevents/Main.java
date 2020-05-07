package me.purplex.packetevents;

import me.purplex.packetevents.injector.PacketInjector;
import me.purplex.packetevents.packetevent.impl.ServerTickEvent;
import me.purplex.packetevents.example.TestExample;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private static PacketInjector packetInjector;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);

        packetInjector = new PacketInjector();
        //PacketEvents.getPacketManager().registerListener(new TestExample());
        //Bukkit.getPluginManager().registerEvents(new TestExample(),this);
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
         * You don't need to unregister all events when the plugin disables,
         * This is just an example
         */

        //PacketEvents.getPacketManager().unregisterListener(new TestExample());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
       Callable<Void> callable = new Callable<Void>() {
           @Override
           public Void call() throws Exception {
               packetInjector.injectPlayer(player);
               return null;
           }
       };
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Future<Void> future = threadPool.submit(callable);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        packetInjector.uninjectPlayer(e.getPlayer());
    }

    public static Main getInstance() {
        return instance;
    }

}
