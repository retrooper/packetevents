package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.event.manager.EventManager;
import org.bukkit.scheduler.BukkitTask;

public final class PacketEventsAPI {
    private final EventManager eventManager = new EventManager();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final ServerUtils serverUtils = new ServerUtils();

    private BukkitTask serverTickTask;

    /**
     * Not thread safe
     * Preciser than #currentMillis()
     * Expensive
     * Returns crazy values when you change system time
     * @return {@link System#nanoTime()} / 1000000(1 million)
     */
    public long currentPreciseMillis() {
        return System.nanoTime() / 1000000;
    }

    /**
     * Thread safe
     * Less precise compared to #currentPreciseMillis()
     * Cheap
     * Doesn't break when system time is changed
     *
     * @return {@link System#currentTimeMillis()}
     */
    public long currentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Get all utilities to do with the player
     * @return Player Utilities
     */
    public PlayerUtils getPlayerUtilities() {
        return playerUtils;
    }

    /**
     * Get all utilities to do with the server
     * @return Server Utilities
     */
    public ServerUtils getServerUtilities() {
        return serverUtils;
    }

    /**
     * Get the event manager
     * Used to call events, register and unregister listeners
     * @return PacketEvents' event manager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Get the server tick task, called **asynchronously** every tick
     * @return server tick task
     */
    public BukkitTask getServerTickTask() {
        return serverTickTask;
    }

    /**
     * Modify the server tick task object
     * @param task
     */
    public void setServerTickTask(final BukkitTask task) {
        this.serverTickTask = task;
    }


}
