package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @deprecated This class has been moved to {@link io.github.retrooper.packetevents.util.folia.FoliaCompatUtil}.
 */
@Deprecated()
public class FoliaCompatUtil {
    private static boolean folia;

    private static Object globalRegionScheduler;

    private static Method runAtFixedRateMethod;
    private static Method runNowMethod;
    private static Method getEntitySchedulerMethod;

    private static Class<? extends Event> serverInitEventClass = null;

    static {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;

            Method getGlobalRegionSchedulerMethod = Reflection.getMethod(Server.class, "getGlobalRegionScheduler", 0);
            globalRegionScheduler = getGlobalRegionSchedulerMethod.invoke(Bukkit.getServer());

            Class<?> globalRegionSchedulerClass = globalRegionScheduler.getClass();
            runAtFixedRateMethod = globalRegionSchedulerClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);
            runNowMethod = globalRegionSchedulerClass.getMethod("run", Plugin.class, Consumer.class);

            serverInitEventClass = (Class<? extends Event>) Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
            getEntitySchedulerMethod = Reflection.getMethod(Entity.class, "getScheduler", 0);

        } catch (ClassNotFoundException e) {
            folia = false;
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Whether the server is running Folia
     * @deprecated This method has been moved to {@link io.github.retrooper.packetevents.util.folia.FoliaCompatUtil#isFolia()}.
     */
    public static boolean isFolia() {
        return folia;
    }

    /**
     * Run a task async, either with Bukkit scheduler or using Java
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    Runnable to run
     * @deprecated This method is deprecated, and it's recommended to use
     * {@link io.github.retrooper.packetevents.util.folia.FoliaCompatUtil#getAsyncScheduler()} instead.
     */
    public static void runTaskAsync(Plugin plugin, Runnable run) {
        if (!folia) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
            return;
        }
        try {
            Executors.defaultThreadFactory().newThread(run).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run a task every global tick
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    Consumer that accepts an object or null, for Folia or Paper/Spigot respectively
     * @param delay  Delay in ticks
     * @param period Period in ticks
     * @deprecated This method is deprecated, and it's recommended to use
     * {@link io.github.retrooper.packetevents.util.folia.FoliaCompatUtil#getAsyncScheduler()} instead.
     */
    public static void runTaskTimerAsync(Plugin plugin, Consumer<Object> run, long delay, long period) {
        if (!folia) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> run.accept(null), delay, period);
            return;
        }
        try {
            runAtFixedRateMethod.invoke(globalRegionScheduler, plugin, run, delay, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run a task on the next global tick
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    Consumer that accepts an object or null, for Folia or Paper/Spigot respectively
     * @deprecated This method is deprecated, and it's recommended to use
     * {@link io.github.retrooper.packetevents.util.folia.FoliaCompatUtil#getGlobalRegionScheduler()} or another suitable scheduler instead.
     */
    public static void runTask(Plugin plugin, Consumer<Object> run) {
        if (!folia) {
            Bukkit.getScheduler().runTask(plugin, () -> run.accept(null));
            return;
        }
        try {
            runNowMethod.invoke(globalRegionScheduler, plugin, run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Run a task after the server has finished initializing.
     * Undefined behavior if called after the server has finished initializing.
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    The task to run
     * @deprecated This method has been moved to {@link io.github.retrooper.packetevents.util.folia.FoliaCompatUtil#runTaskOnInit(Plugin, Runnable)}.
     */
    public static void runTaskOnInit(Plugin plugin, Runnable run) {
        if (!folia) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        }

        Bukkit.getServer().getPluginManager().registerEvent(serverInitEventClass, new Listener() {
        }, EventPriority.HIGHEST, (listener, event) -> run.run(), plugin);
    }

    /**
     * Run a task for an entity on whatever thread the entity is on
     *
     * @param entity  The entity to run the task for
     * @param plugin  Your plugin or PacketEvents
     * @param run     The task to run
     * @param retired The task to run if entity is retired before the task is run
     * @param delay   Delay in ticks
     * @deprecated This method is deprecated, and it's recommended to use {@link io.github.retrooper.packetevents.util.folia.FoliaCompatUtil#getEntityScheduler()}.
     */
    public static void runTaskForEntity(Entity entity, Plugin plugin, Runnable run, Runnable retired, long delay) {
        if (!folia) {
            Bukkit.getScheduler().runTaskLater(plugin, run, delay);
        }

        // Gradle doesn't allow us to use java 17 APIs, so we have to use reflection instead...
        try {
            Object entityScheduler = getEntitySchedulerMethod.invoke(entity);

            Class<?> schedulerClass = entityScheduler.getClass();
            Method executeMethod = schedulerClass.getMethod("execute", Plugin.class, Runnable.class, Runnable.class, long.class);

            executeMethod.invoke(entityScheduler, plugin, run, retired, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}