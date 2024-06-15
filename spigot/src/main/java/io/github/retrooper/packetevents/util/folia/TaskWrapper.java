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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * Represents a wrapper around {@code BukkitTask} and Paper's {@code ScheduledTask}.
 * This class provides a unified interface for interacting with both Bukkit's task scheduler
 * and Paper's task scheduler.
 */
public class TaskWrapper {
    private static Method getOwningPluginMethod;
    private static Method isCancelledMethod;
    private static Method cancelMethod;

    static {
        if (FoliaScheduler.isFolia()) {
            // When Folia is being used, we initialize the reflection methods for Paper's ScheduledTask on startup
            // to avoid reflection overhead during runtime.
            Class<?> scheduledTaskClass = Reflection.getClassByNameWithoutException("io.papermc.paper.threadedregions.scheduler.ScheduledTask");

            getOwningPluginMethod = Reflection.getMethod(scheduledTaskClass, "getOwningPlugin");
            isCancelledMethod = Reflection.getMethod(scheduledTaskClass, "isCancelled");
            cancelMethod = Reflection.getMethod(scheduledTaskClass, "cancel");
        }
    }

    private BukkitTask bukkitTask;
    private Object scheduledTask;

    /**
     * Constructs a new TaskWrapper around a BukkitTask.
     *
     * @param bukkitTask the BukkitTask to wrap
     */
    public TaskWrapper(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    /**
     * Constructs a new TaskWrapper around Paper's ScheduledTask.
     *
     * @param scheduledTask the ScheduledTask to wrap
     */
    public TaskWrapper(Object scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    /**
     * Retrieves the Plugin that owns this task.
     *
     * @return the owning {@link Plugin}
     */
    public Plugin getOwner() {
        if (bukkitTask == null && scheduledTask == null) {
            return null;
        }
        return getIfBukkitTaskOrElse(
                () -> bukkitTask.getOwner(),
                () -> invokeReflectedMethod(getOwningPluginMethod));
    }

    /**
     * Checks if the task is canceled.
     *
     * @return true if the task is canceled, false otherwise
     */
    public boolean isCancelled() {
        if (bukkitTask == null && scheduledTask == null) {
            return false;
        }
        return getIfBukkitTaskOrElse(
                () -> bukkitTask.isCancelled(),
                () -> invokeReflectedMethod(isCancelledMethod));
    }

    /**
     * Cancels the task. If the task is running, it will be canceled.
     */
    public void cancel() {
        if (bukkitTask == null && scheduledTask == null) {
            return;
        }
        if (bukkitTask != null) {
            bukkitTask.cancel();
        } else {
            invokeReflectedMethod(cancelMethod);
        }
    }

    /**
     * Private helper method to encapsulate the logic of conditional fetching data
     * depending on the type of the task (BukkitTask or ScheduledTask).
     *
     * @param bukkitTaskSupplier    function that should be called for a Bukkit task
     * @param scheduledTaskSupplier function that should be called for a Paper / Folia task
     * @return the result of calling the appropriate function based on the task type
     */
    private <T> T getIfBukkitTaskOrElse(Supplier<T> bukkitTaskSupplier, Supplier<T> scheduledTaskSupplier) {
        return bukkitTask != null ? bukkitTaskSupplier.get() : scheduledTaskSupplier.get();
    }

    /**
     * Invokes the given reflection method on the scheduled task.
     *
     * @param method method to be invoked
     * @return the result of the method invocation
     */
    @SuppressWarnings("unchecked")
    private <T> T invokeReflectedMethod(Method method) {
        try {
            return (T) method.invoke(scheduledTask);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}