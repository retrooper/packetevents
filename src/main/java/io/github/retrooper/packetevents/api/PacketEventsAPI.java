package io.github.retrooper.packetevents.api;

import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.utils.api.PlayerUtils;
import io.github.retrooper.packetevents.utils.api.ServerUtils;

public final class PacketEventsAPI {
    private final EventManager eventManager = new EventManager();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final ServerUtils serverUtils = new ServerUtils();

    /**
     * Not thread safe
     * Preciser than #currentMillis()
     * Expensive
     * Returns crazy values when you change system time
     *
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
     *
     * @return Player Utilities
     */
    public PlayerUtils getPlayerUtils() {
        return playerUtils;
    }

    /**
     * Use {@link #getPlayerUtils()}
     *
     * @return
     */
    @Deprecated
    public PlayerUtils getPlayerUtilities() {
        return playerUtils;
    }

    /**
     * Get all utilities to do with the server
     *
     * @return Server Utilities
     */
    public ServerUtils getServerUtils() {
        return serverUtils;
    }

    /**
     * Please use {@link #getServerUtils()}
     *
     * @return
     */
    @Deprecated
    public ServerUtils getServerUtilities() {
        return serverUtils;
    }

    /**
     * Get the event manager
     * Used to call events, register and unregister listeners
     *
     * @return PacketEvents' event manager
     */
    public EventManager getEventManager() {
        return eventManager;
    }


}
