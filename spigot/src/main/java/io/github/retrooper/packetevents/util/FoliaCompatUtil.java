package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FoliaCompatUtil {
    private static boolean folia;

    static {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
    }

    /**
     * @return Whether the server is running Folia
     */
    public static boolean isFolia() {
        return folia;
    }

    /**
     * Run a task async, either with bukkit scheduler or using java
     * @param plugin Your plugin or PacketEvents
     * @param run Runnable to run
     */
    public static void runTaskAsync(Plugin plugin, Runnable run) {
        if (!folia) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
            return;
        }
        Executors.defaultThreadFactory().newThread(run).start();
    }

    /**
     * Run a task every global tick
     * @param plugin Your plugin or PacketEvents
     * @param run Consumer that accepts an object or null, for Folia or Paper/Spigot respectively
     * @param delay Delay in ticks
     * @param period Period in ticks
     */
    public static void runTaskTimerAsync(Plugin plugin, Consumer<Object> run, long delay, long period) {
        if (!folia) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> run.accept(null), delay, period);
            return;
        }
        try {
            Method getSchedulerMethod = Reflection.getMethod(Server.class, "getGlobalRegionScheduler", 0);
            Object globalRegionScheduler = getSchedulerMethod.invoke(Bukkit.getServer());

            Class<?> schedulerClass = globalRegionScheduler.getClass();
            Method executeMethod = schedulerClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);

            executeMethod.invoke(globalRegionScheduler, plugin, run, delay, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run a task on the next global tick
     * @param plugin Your plugin or PacketEvents
     * @param run Consumer that accepts an object or null, for Folia or Paper/Spigot respectively
     */
    public static void runTask(Plugin plugin, Consumer<Object> run) {
        if (!folia) {
            Bukkit.getScheduler().runTask(plugin, () -> run.accept(null));
            return;
        }
        try {
            Method getSchedulerMethod = Reflection.getMethod(Server.class, "getGlobalRegionScheduler", 0);
            Object globalRegionScheduler = getSchedulerMethod.invoke(Bukkit.getServer());

            Class<?> schedulerClass = globalRegionScheduler.getClass();
            Method executeMethod = schedulerClass.getMethod("run", Plugin.class, Consumer.class);

            executeMethod.invoke(globalRegionScheduler, plugin, run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Run a task after the server has finished initializing.
     * Undefined behavior if called after the server has finished initializing.
     * @param plugin Your plugin or PacketEvents
     * @param run The task to run
     */
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

    /**
     * Run a task for an entity on whatever thread the entity is on
     * @param entity The entity to run the task for
     * @param plugin Your plugin or PacketEvents
     * @param run The task to run
     * @param retired The task to run if entity is retired before the task is run
     * @param delay Delay in ticks
     */
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
