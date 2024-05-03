/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.util.folia;

import com.github.retrooper.packetevents.util.reflection.Reflection;
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
    private final boolean isFolia = FoliaScheduler.isFolia();

    private Object asyncScheduler;

    private Method asyncRunNowMethod;
    private Method asyncRunDelayedMethod;
    private Method asyncRunAtFixedRateMethod;
    private Method asyncCancelMethod;

    protected AsyncScheduler() {
        try {
            if (isFolia) {
                Method getAsyncSchedulerMethod = Reflection.getMethod(Server.class, "getAsyncScheduler", 0);
                asyncScheduler = getAsyncSchedulerMethod.invoke(Bukkit.getServer());
                Class<?> asyncSchedulerClass = asyncScheduler.getClass();

                asyncRunNowMethod = asyncSchedulerClass.getMethod("runNow", Plugin.class, Consumer.class);
                asyncRunDelayedMethod = asyncSchedulerClass.getMethod("runDelayed", Plugin.class, Consumer.class, long.class, TimeUnit.class);
                asyncRunAtFixedRateMethod = asyncSchedulerClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class);
                asyncCancelMethod = asyncSchedulerClass.getMethod("cancelTasks", Plugin.class);
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
    public TaskWrapper runNow(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
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
    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, @NotNull TimeUnit timeUnit) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> task.accept(null), convertTimeToTicks(delay, timeUnit)));
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
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, long period, @NotNull TimeUnit timeUnit) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> task.accept(null), convertTimeToTicks(delay, timeUnit), convertTimeToTicks(period, timeUnit)));
        }

        try {
            return new TaskWrapper(asyncRunAtFixedRateMethod.invoke(asyncScheduler, plugin, task, delay, period, timeUnit));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules the specified task to be executed asynchronously after the initial delay has passed, and then periodically executed.
     *
     * @param plugin   Plugin which owns the specified task.
     * @param task     Specified task.
     * @param initialDelayTicks    The time delay in ticks to pass before the task should be executed.
     * @param periodTicks   The time period in ticks between each task execution.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {

        if (initialDelayTicks < 1) initialDelayTicks = 1;

        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        try {
            return new TaskWrapper(asyncRunAtFixedRateMethod.invoke(asyncScheduler, plugin, task,
                    initialDelayTicks * 50,
                    periodTicks * 50,
                    TimeUnit.MILLISECONDS));
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
            asyncCancelMethod.invoke(asyncScheduler, plugin);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the specified time to ticks.
     *
     * @param time     The time to convert.
     * @param timeUnit The time unit of the time.
     * @return The time converted to ticks.
     */
    private long convertTimeToTicks(long time, TimeUnit timeUnit) {
        return timeUnit.toMillis(time) / 50;
    }
}