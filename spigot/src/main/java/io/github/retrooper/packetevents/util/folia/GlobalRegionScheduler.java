package io.github.retrooper.packetevents.util.folia;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Represents a scheduler for executing global region tasks.
 */
public class GlobalRegionScheduler {
    private final boolean isFolia = FoliaCompatUtil.isFolia();

    private Object globalRegionScheduler;

    private Method globalExecuteMethod;
    private Method globalRunMethod;
    private Method globalRunDelayedMethod;
    private Method globalRunAtFixedRateMethod;
    private Method globalCancelMethod;

    protected GlobalRegionScheduler() {
        try {
            if (isFolia) {
                Method getGlobalRegionSchedulerMethod = Reflection.getMethod(Server.class, "getGlobalRegionScheduler", 0);
                globalRegionScheduler = getGlobalRegionSchedulerMethod.invoke(Bukkit.getServer());
                Class<?> globalRegionSchedulerClass = globalRegionScheduler.getClass();

                globalExecuteMethod = globalRegionSchedulerClass.getMethod("execute", Plugin.class, Runnable.class);
                globalRunMethod = globalRegionSchedulerClass.getMethod("run", Plugin.class, Consumer.class);
                globalRunDelayedMethod = globalRegionSchedulerClass.getMethod("runDelayed", Plugin.class, Consumer.class, long.class);
                globalRunAtFixedRateMethod = globalRegionSchedulerClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);
                globalCancelMethod = globalRegionSchedulerClass.getMethod("cancelTasks", Plugin.class);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Schedules a task to be executed on the global region.
     *
     * @param plugin The plugin that owns the task
     * @param run    The task to execute
     */
    private void execute(@NotNull Plugin plugin, @NotNull Runnable run) {
        if (!isFolia) {
            Bukkit.getScheduler().runTask(plugin, run);
        }

        try {
            globalExecuteMethod.invoke(globalRegionScheduler, plugin, run);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Schedules a task to be executed on the global region.
     *
     * @param plugin The plugin that owns the task
     * @param task   The task to execute
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper run(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, () -> task.accept(null)));
        }

        try {
            return new TaskWrapper(globalRunMethod.invoke(globalRegionScheduler, plugin, task));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules a task to be executed on the global region after the specified delay in ticks.
     *
     * @param plugin The plugin that owns the task
     * @param task   The task to execute
     * @param delay  The delay, in ticks.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(null), delay));
        }

        try {
            return new TaskWrapper(globalRunDelayedMethod.invoke(globalRegionScheduler, plugin, task, delay));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules a repeating task to be executed on the global region after the initial delay with the specified period.
     *
     * @param plugin            The plugin that owns the task
     * @param task              The task to execute
     * @param initialDelayTicks The initial delay, in ticks.
     * @param periodTicks       The period, in ticks.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        try {
            return new TaskWrapper(globalRunAtFixedRateMethod.invoke(globalRegionScheduler, plugin, task, initialDelayTicks, periodTicks));
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
    public void cancel(@NotNull Plugin plugin) {
        if (!isFolia) {
            Bukkit.getScheduler().cancelTasks(plugin);
            return;
        }

        try {
            globalCancelMethod.invoke(globalRegionScheduler, plugin);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}