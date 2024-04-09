/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Represents a scheduler for executing region tasks
 */
public class RegionScheduler {
    private final boolean isFolia = FoliaCompatUtil.isFolia();

    private Object regionScheduler;

    private Method regionExecuteWorldMethod;
    private Method regionExecuteLocationMethod;
    private Method regionRunWorldMethod;
    private Method regionRunLocationMethod;
    private Method regionRunDelayedWorldMethod;
    private Method regionRunDelayedLocationMethod;
    private Method regionRunAtFixedRateWorldMethod;
    private Method regionRunAtFixedRateLocationMethod;

    protected RegionScheduler() {
        try {
            if (isFolia) {
                Method getRegionSchedulerMethod = Reflection.getMethod(Server.class, "getRegionScheduler", 0);
                regionScheduler = getRegionSchedulerMethod.invoke(Bukkit.getServer());
                Class<?> regionSchedulerClass = regionScheduler.getClass();

                regionExecuteWorldMethod = regionSchedulerClass.getMethod("execute", Plugin.class, World.class, int.class, int.class, Runnable.class);
                regionExecuteLocationMethod = regionSchedulerClass.getMethod("execute", Plugin.class, Location.class, Runnable.class);
                regionRunWorldMethod = regionSchedulerClass.getMethod("run", Plugin.class, World.class, int.class, int.class, Consumer.class);
                regionRunLocationMethod = regionSchedulerClass.getMethod("run", Plugin.class, Location.class, Consumer.class);
                regionRunDelayedWorldMethod = regionSchedulerClass.getMethod("runDelayed", Plugin.class, World.class, int.class, int.class, Consumer.class, long.class);
                regionRunDelayedLocationMethod = regionSchedulerClass.getMethod("runDelayed", Plugin.class, Location.class, Consumer.class, long.class);
                regionRunAtFixedRateWorldMethod = regionSchedulerClass.getMethod("runAtFixedRate", Plugin.class, World.class, int.class, int.class, Consumer.class, long.class, long.class);
                regionRunAtFixedRateLocationMethod = regionSchedulerClass.getMethod("runAtFixedRate", Plugin.class, Location.class, Consumer.class, long.class, long.class);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Schedules a task to be executed on the region which owns the location.
     *
     * @param plugin The plugin that owns the task
     * @param world  The world of the region that owns the task
     * @param chunkX The chunk X coordinate of the region that owns the task
     * @param chunkZ The chunk Z coordinate of the region that owns the task
     * @param run    The task to execute
     */
    public void execute(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Runnable run) {
        if (!isFolia) {
            Bukkit.getScheduler().runTask(plugin, run);
        }

        try {
            regionExecuteWorldMethod.invoke(regionScheduler, plugin, world, chunkX, chunkZ, run);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Schedules a task to be executed on the region which owns the location.
     *
     * @param plugin   The plugin that owns the task
     * @param location The location at which the region executing should own
     * @param run      The task to execute
     */
    public void execute(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable run) {
        if (!isFolia) {
            Bukkit.getScheduler().runTask(plugin, run);
        }

        try {
            regionExecuteLocationMethod.invoke(regionScheduler, plugin, location, run);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Schedules a task to be executed on the region which owns the location on the next tick.
     *
     * @param plugin The plugin that owns the task
     * @param world  The world of the region that owns the task
     * @param chunkX The chunk X coordinate of the region that owns the task
     * @param chunkZ The chunk Z coordinate of the region that owns the task
     * @param task   The task to execute
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper run(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, () -> task.accept(null)));
        }

        try {
            return new TaskWrapper(regionRunWorldMethod.invoke(regionScheduler, plugin, world, chunkX, chunkZ, task));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules a task to be executed on the region which owns the location on the next tick.
     *
     * @param plugin   The plugin that owns the task
     * @param location The location at which the region executing should own
     * @param task     The task to execute
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper run(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, () -> task.accept(null)));
        }

        try {
            return new TaskWrapper(regionRunLocationMethod.invoke(regionScheduler, plugin, location, task));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules a task to be executed on the region which owns the location after the specified delay in ticks.
     *
     * @param plugin     The plugin that owns the task
     * @param world      The world of the region that owns the task
     * @param chunkX The chunk X coordinate of the region that owns the task
     * @param chunkZ The chunk Z coordinate of the region that owns the task
     * @param task       The task to execute
     * @param delayTicks The delay, in ticks.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task, long delayTicks) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }

        try {
            return new TaskWrapper(regionRunDelayedWorldMethod.invoke(regionScheduler, plugin, world, chunkX, chunkZ, task, delayTicks));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules a task to be executed on the region which owns the location after the specified delay in ticks.
     *
     * @param plugin     The plugin that owns the task
     * @param location   The location at which the region executing should own
     * @param task       The task to execute
     * @param delayTicks The delay, in ticks.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task, long delayTicks) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }

        try {
            return new TaskWrapper(regionRunDelayedLocationMethod.invoke(regionScheduler, plugin, location, task, delayTicks));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules a repeating task to be executed on the region which owns the location after the initial delay with the specified period.
     *
     * @param plugin            The plugin that owns the task
     * @param world             The world of the region that owns the task
     * @param chunkX The chunk X coordinate of the region that owns the task
     * @param chunkZ The chunk Z coordinate of the region that owns the task
     * @param task              The task to execute
     * @param initialDelayTicks The initial delay, in ticks.
     * @param periodTicks       The period, in ticks.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        try {
            return new TaskWrapper(regionRunAtFixedRateWorldMethod.invoke(regionScheduler, plugin, world, chunkX, chunkZ, task, initialDelayTicks, periodTicks));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Schedules a repeating task to be executed on the region which owns the location after the initial delay with the specified period.
     *
     * @param plugin            The plugin that owns the task
     * @param location          The location at which the region executing should own
     * @param task              The task to execute
     * @param initialDelayTicks The initial delay, in ticks.
     * @param periodTicks       The period, in ticks.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (!isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        try {
            return new TaskWrapper(regionRunAtFixedRateLocationMethod.invoke(regionScheduler, plugin, location, task, initialDelayTicks, periodTicks));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}