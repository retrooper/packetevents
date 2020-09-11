/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.event.manager.EventManager;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.player.PlayerUtils;
import io.github.retrooper.packetevents.utils.server.ServerUtils;

public final class PacketEventsAPI {
    private final EventManager eventManager = new EventManager();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final ServerUtils serverUtils = new ServerUtils();
    private final PacketEventsSettings settings = new PacketEventsSettings();

    /**
     * Not thread safe
     * Preciser than #currentMillis()
     * Expensive
     * Returns crazy values when you change system time
     *
     * @return {@link System#nanoTime()} / 1000000(1 million)
     */
    @Deprecated
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
    @Deprecated
    public long currentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Get all utilities to do with the player
     * @return Player Utilities
     */
    public PlayerUtils getPlayerUtils() {
        return playerUtils;
    }

    /**
     * Get all utilities to do with the server
     * @return Server Utilities
     */
    public ServerUtils getServerUtils() {
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
     * Get the PacketEvents settings.
     * @return Configure some settings for the API
     */
    public PacketEventsSettings getSettings() {
        return settings;
    }
}