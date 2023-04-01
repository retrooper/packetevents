package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class FoliaCompatUtil {
    // Thanks powercas_gamer in #folia-dev
    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    private static final boolean folia = isFolia();

    public static void runTaskAsync(Plugin plugin, Runnable run) {
        if (!folia) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
            return;
        }
        CompletableFuture.runAsync(run);
    }

    public static void runTaskOnInit(Plugin plugin, Runnable run) {
        if (!folia) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        }

        // Thanks for this code ViaVersion
        final Class<? extends Event> serverInitEventClass;
        try {
            //noinspection unchecked
            serverInitEventClass = (Class<? extends Event>) Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getServer().getPluginManager().registerEvent(serverInitEventClass, new Listener() {
        }, EventPriority.HIGHEST, (listener, event) -> run.run(), plugin);
    }

    public static void runTaskForEntity(Entity entity, Plugin plugin, Runnable run, Runnable retired, long delay) {
        if (!folia) {
            Bukkit.getScheduler().runTaskLater(plugin, run, delay);
            return;
        }

        // Gradle doesn't allow us to use java 17 APIs, so we have to use reflection instead...
        try {
            Method getSchedulerMethod = Reflection.getMethod(Entity.class, "getScheduler", 0);
            Object entityScheduler = getSchedulerMethod.invoke(entity);

            Class<?> schedulerClass = entityScheduler.getClass();
            Method executeMethod = schedulerClass.getMethod("execute", Plugin.class, Runnable.class, Runnable.class, long.class);

            executeMethod.invoke(entityScheduler, plugin, run, retired, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
