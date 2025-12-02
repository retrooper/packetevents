package io.github.retrooper.packetevents.util;

import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.function.Consumer;

/**
 * @deprecated This class has been moved to {@link FoliaScheduler}.
 */
@Deprecated
public class FoliaCompatUtil {
    private static boolean folia;

    private static BukkitScheduler bukkitScheduler;
    private static AsyncScheduler asyncScheduler;
    private static GlobalRegionScheduler globalRegionScheduler;

    private static Class<? extends Event> regionizedServerInitEventClass;

    static {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;

            asyncScheduler = Bukkit.getAsyncScheduler();
            globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
            regionizedServerInitEventClass = (Class<? extends Event>) Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
        } catch (ClassNotFoundException e) {
            folia = false;
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    /**
     * @return Whether the server is running Folia
     * @deprecated This method has been moved to {@link FoliaScheduler#isFolia()}.
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
     * {@link FoliaScheduler#getAsyncScheduler()} instead.
     */
    public static void runTaskAsync(Plugin plugin, Runnable run) {
        if (!folia) {
            bukkitScheduler.runTaskAsynchronously(plugin, run);
            return;
        }

        asyncScheduler.runNow(plugin, (o) -> run.run());
    }

    /**
     * Run a task every global tick
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    Consumer that accepts an object or null, for Folia or Paper/Spigot respectively
     * @param delay  Delay in ticks
     * @param period Period in ticks
     * @deprecated This method is deprecated, and it's recommended to use
     * {@link FoliaScheduler#getAsyncScheduler()} instead.
     */
    public static void runTaskTimerAsync(Plugin plugin, Consumer<Object> run, long delay, long period) {
        if (!folia) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> run.accept(null), delay, period);
            return;
        }

        globalRegionScheduler.runAtFixedRate(plugin, (o) -> run.accept(null), delay, period);
    }

    /**
     * Run a task on the next global tick
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    Consumer that accepts an object or null, for Folia or Paper/Spigot respectively
     * @deprecated This method is deprecated, and it's recommended to use
     * {@link FoliaScheduler#getGlobalRegionScheduler()} or another suitable scheduler instead.
     */
    public static void runTask(Plugin plugin, Consumer<Object> run) {
        if (!folia) {
            Bukkit.getScheduler().runTask(plugin, () -> run.accept(null));
            return;
        }

        globalRegionScheduler.run(plugin, (o) -> run.accept(null));
    }


    /**
     * Run a task after the server has finished initializing.
     * Undefined behavior if called after the server has finished initializing.
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    The task to run
     * @deprecated This method has been moved to {@link FoliaScheduler#runTaskOnInit(Plugin, Runnable)}.
     */
    public static void runTaskOnInit(Plugin plugin, Runnable run) {
        if (!folia) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        }

        Bukkit.getServer().getPluginManager().registerEvent(regionizedServerInitEventClass, new Listener() {
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
     * @deprecated This method is deprecated, and it's recommended to use {@link FoliaScheduler#getEntityScheduler()}.
     */
    public static void runTaskForEntity(Entity entity, Plugin plugin, Runnable run, Runnable retired, long delay) {
        if (!folia) {
            Bukkit.getScheduler().runTaskLater(plugin, run, delay);
            return;
        }

        entity.getScheduler().execute(plugin, run, retired, delay);
    }
}