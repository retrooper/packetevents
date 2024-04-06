package io.github.retrooper.packetevents.util.folia.schedulers;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import io.github.retrooper.packetevents.util.folia.FoliaCompatUtil;
import io.github.retrooper.packetevents.util.folia.TaskWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Represents a scheduler for executing tasks asynchronously.
 */
public class AsyncScheduler {
    private static final boolean isFolia = FoliaCompatUtil.isFolia();

    private static Object asyncScheduler;

    private static Method asyncRunNowMethod;
    private static Method asyncRunDelayedMethod;
    private static Method asyncRunAtFixedRateMethod;
    private static Method asyncCancelMethod;

    static {
        try {
            if (isFolia) {
                Method getAsyncSchedulerMethod = Reflection.getMethod(Server.class, "getAsyncScheduler", 0);
                asyncScheduler = getAsyncSchedulerMethod.invoke(Bukkit.getServer());
                Class<?> asyncSchedulerClass = asyncScheduler.getClass();

                asyncRunNowMethod = asyncSchedulerClass.getMethod("runNow", Plugin.class, Consumer.class);
                asyncRunDelayedMethod = asyncSchedulerClass.getMethod("runDelayed", Plugin.class, Consumer.class, long.class, TimeUnit.class);
                asyncRunAtFixedRateMethod = asyncSchedulerClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class);
                asyncCancelMethod = asyncSchedulerClass.getMethod("cancel", Plugin.class);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Schedules the specified task to be executed asynchronously immediately.
     *
     * @param plugin Plugin which owns the specified task.
     * @param task   Specified task.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public static TaskWrapper runNow(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> task.accept(null)));
        }

        try {
            return new TaskWrapper(asyncRunNowMethod.invoke(asyncScheduler, plugin, task));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Schedules the specified task to be executed asynchronously after the specified delay.
     *
     * @param plugin   Plugin which owns the specified task.
     * @param task     Specified task.
     * @param delay    The time delay to pass before the task should be executed.
     * @param timeUnit The time unit for the time delay.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public static TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, @NotNull TimeUnit timeUnit) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> task.accept(null), timeUnit.ordinal()));
        }

        try {
            return new TaskWrapper(asyncRunDelayedMethod.invoke(asyncScheduler, plugin, task, delay, timeUnit));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Schedules the specified task to be executed asynchronously after the initial delay has passed, and then periodically executed with the specified period.
     *
     * @param plugin   Plugin which owns the specified task.
     * @param task     Specified task.
     * @param delay    The time delay to pass before the task should be executed.
     * @param period   The time period between each task execution.
     * @param timeUnit The time unit for the initial delay and period.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public static TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, long period, @NotNull TimeUnit timeUnit) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> task.accept(null), delay, period));
        }

        try {
            return new TaskWrapper(asyncRunAtFixedRateMethod.invoke(asyncScheduler, plugin, task, delay, period, timeUnit));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Attempts to cancel all tasks scheduled by the specified plugin.
     *
     * @param plugin Specified plugin.
     */
    public static void cancel(@NotNull Plugin plugin) {
        if (!isFolia) {
            Bukkit.getScheduler().cancelTasks(plugin);
            return;
        }

        try {
            asyncCancelMethod.invoke(asyncScheduler, plugin);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}