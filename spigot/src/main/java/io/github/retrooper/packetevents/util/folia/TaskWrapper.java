package io.github.retrooper.packetevents.util.folia;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * Represents a wrapper around {@code BukkitTask} and Folia's {@code ScheduledTask}.
 * This class provides a unified interface for interacting with both Bukkit's task scheduler
 * and Folia's task scheduler.
 * <p>
 * If Folia is used, reflection is employed to interact with its {@code ScheduledTask} objects.
 * <p>
 * The wrapped tasks can be canceled, and their owning plugin and cancellation status can be queried.
 *
 */
public class TaskWrapper {
    private BukkitTask bukkitTask;
    private Object scheduledTask;

    private static Method getOwningPluginMethod;
    private static Method isCancelledMethod;
    private static Method cancelMethod;

    static {
        if (FoliaCompatUtil.isFolia()) {
            Class<?> scheduledTaskClass = Reflection.getClassByNameWithoutException("io.papermc.paper.threadedregions.scheduler.ScheduledTask");

            getOwningPluginMethod = Reflection.getMethod(scheduledTaskClass, "getOwningPlugin");
            isCancelledMethod = Reflection.getMethod(scheduledTaskClass, "isCancelled");
            cancelMethod = Reflection.getMethod(scheduledTaskClass, "cancel");
        }
    }

    /**
     * Constructs a new TaskWrapper around a BukkitTask.
     *
     * @param bukkitTask the BukkitTask to wrap
     */
    public TaskWrapper(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    /**
     * Constructs a new TaskWrapper around a Folia's ScheduledTask.
     *
     * @param scheduledTask the Folia's ScheduledTask to wrap
     */
    public TaskWrapper(Object scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    /**
     * Retrieves the Plugin that owns this task.
     *
     * @return the owning Plugin
     */
    public Plugin getOwner() {
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
        return getIfBukkitTaskOrElse(
                () -> bukkitTask.isCancelled(),
                () -> invokeReflectedMethod(isCancelledMethod));
    }

    /**
     * Cancels the task. If the task is running, it will be interrupted.
     */
    public void cancel() {
        if (bukkitTask != null) {
            bukkitTask.cancel();
        } else {
            invokeReflectedMethod(cancelMethod);
        }
    }

    /**
     * Private helper method to encapsulate the logic of conditional fetching data
     * depending on the type of the task (Bukkit or Folia).
     *
     * @param bukkitTaskSupplier function that should be called for a Bukkit task
     * @param scheduledTaskSupplier function that should be called for a Folia task
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
    private <T> T invokeReflectedMethod(Method method) {
        try {
            return (T) method.invoke(scheduledTask);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}