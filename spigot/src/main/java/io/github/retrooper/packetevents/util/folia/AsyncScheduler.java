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

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Represents a scheduler for executing tasks asynchronously.
 */
public class AsyncScheduler {

    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.AsyncScheduler asyncScheduler;

    protected AsyncScheduler() {
        if (FoliaScheduler.isFolia) {
            asyncScheduler = Bukkit.getAsyncScheduler();
        } else {
            bukkitScheduler = Bukkit.getScheduler();
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
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(bukkitScheduler.runTaskAsynchronously(plugin, () -> task.accept(null)));
        }

        return new TaskWrapper(asyncScheduler.runNow(plugin, (o) -> task.accept(null)));
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
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(bukkitScheduler.runTaskLaterAsynchronously(plugin, () -> task.accept(null), convertTimeToTicks(delay, timeUnit)));
        }

        return new TaskWrapper(asyncScheduler.runDelayed(plugin, (o) -> task.accept(null), delay, timeUnit));
    }

    /**
     * Schedules the specified task to be executed asynchronously after the initial delay has passed, and then periodically executed with the specified period.
     *
     * @param plugin   Plugin which owns the specified task.
     * @param task     Specified task.
     * @param delay    The time delay to pass before the task should be executed.
     * @param period   The time period between each task execution. Any value less-than 1 is treated as 1.
     * @param timeUnit The time unit for the initial delay and period.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, long period, @NotNull TimeUnit timeUnit) {
        if (period < 1) period = 1;

        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(bukkitScheduler.runTaskTimerAsynchronously(plugin, () -> task.accept(null), convertTimeToTicks(delay, timeUnit), convertTimeToTicks(period, timeUnit)));
        }

        return new TaskWrapper(asyncScheduler.runAtFixedRate(plugin, (o) -> task.accept(null), delay, period, timeUnit));
    }

    /**
     * Schedules the specified task to be executed asynchronously after the initial delay has passed, and then periodically executed.
     *
     * @param plugin            Plugin which owns the specified task.
     * @param task              Specified task.
     * @param initialDelayTicks The time delay in ticks to pass before the task should be executed.
     * @param periodTicks       The time period in ticks between each task execution. Any value less-than 1 is treated as 1.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (periodTicks < 1) periodTicks = 1;

        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(bukkitScheduler.runTaskTimerAsynchronously(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        return new TaskWrapper(asyncScheduler.runAtFixedRate(plugin, (o) -> task.accept(null), initialDelayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS));
    }

    /**
     * Attempts to cancel all tasks scheduled by the specified plugin.
     *
     * @param plugin Specified plugin.
     */
    public void cancel(@NotNull Plugin plugin) {
        if (!FoliaScheduler.isFolia) {
            bukkitScheduler.cancelTasks(plugin);
            return;
        }

        asyncScheduler.cancelTasks(plugin);
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