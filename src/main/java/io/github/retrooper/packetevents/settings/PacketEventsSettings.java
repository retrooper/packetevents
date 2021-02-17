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

package io.github.retrooper.packetevents.settings;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Packet Events' settings.
 *
 * @author retrooper
 * @since 1.5.8
 */
public class PacketEventsSettings {
    private boolean locked;
    private ServerVersion backupServerVersion = ServerVersion.v_1_7_10;
    private boolean injectAsync = false;

    /**
     * This boolean stores if PacketEvents should eject a player asynchronously.
     */
    private boolean ejectAsync = false;

    /**
     * This boolean stores if PacketEvents should check for updates,
     * and give you a notice in the console.
     */
    private boolean checkForUpdates = true;

    /**
     * This boolean stores if PacketEvents should inject a player earlier using the {@code EarlyInjector}.
     * Allowing us to listen to the LOGIN and STATUS packets and detect client version independently.
     */
    private boolean injectEarly = true;

    /**
     * This int stores how many threads should use to inject and eject a player.
     */
    private int injectEjectThreadCount = 1;

    /**
     * This is the reaction that happens when we fail to inject a player. By default we will kick.
     */
    private Consumer<Player> injectionFailureReaction = new Consumer<Player>() {
        @Override
        public void accept(Player player) {
            Bukkit.getScheduler().runTask(PacketEvents.get().getPlugin(), new Runnable() {
                @Override
                public void run() {
                    player.kickPlayer("Failed to inject you. Please try rejoining!");
                }
            });
        }
    };

    /**
     * This method locks the settings.
     * If the settings are locked, you won't be able to modify any settings using the setters.
     */
    public PacketEventsSettings lock() {
        this.locked = true;
        return this;
    }

    /**
     * This is the server version PacketEvents should assume the server is when detecting
     * the server version fails using the Bukkit API.
     * This seems to be most common on 1.7.10 paper forks.
     * They probably mess up somewhere.
     *
     * @param serverVersion ServerVersion
     * @return Settings instance.
     */
    public PacketEventsSettings backupServerVersion(ServerVersion serverVersion) {
        if (!locked) {
            this.backupServerVersion = serverVersion;
        }
        return this;
    }

    /**
     * This is decides if PacketEvents should inject users on internal executor or on the main thread.
     *
     * @param injectAsync Value
     * @return Settings instance.
     */
    public PacketEventsSettings injectAsync(boolean injectAsync) {
        if (!locked) {
            this.injectAsync = injectAsync;
        }
        return this;
    }

    /**
     * This is decides if PacketEvents should eject users on internal executor or on the main thread.
     *
     * @param ejectAsync Value
     * @return Settings instance.
     */
    public PacketEventsSettings ejectAsync(boolean ejectAsync) {
        if (!locked) {
            this.ejectAsync = ejectAsync;
        }
        return this;
    }

    /**
     * This decides if PacketEvents should check for updates and notify when your server starts.
     *
     * @param checkForUpdates Value
     * @return Settings instance.
     */
    public PacketEventsSettings checkForUpdates(boolean checkForUpdates) {
        if (!locked) {
            this.checkForUpdates = checkForUpdates;
        }
        return this;
    }

    /**
     * This decides if PacketEvents should inject users earlier than usual,
     * resulting in us being able to resolve client versions without the need of any dependencies.
     * We end up using a different injection method which isn't supported on a few spigot forks.
     *
     * @param injectEarly Value
     * @return Settings instance.
     */
    public PacketEventsSettings injectEarly(boolean injectEarly) {
        if (!locked) {
            this.injectEarly = injectEarly;
        }
        return this;
    }

    /**
     * Thread count for the injection/ejection executor service.
     * Basically how many threads should we use for injection and ejection.
     *
     * @param threadCount Value
     * @return This instance.
     */
    public PacketEventsSettings injectAndEjectThreadCount(int threadCount) {
        if (!locked) {
            this.injectEjectThreadCount = threadCount;
        }
        return this;
    }

    /**
     * When PacketEvents fails to inject a user, it schedules a re-injection and if that fails, we will execute your function.
     * By default we will kick the user. You can modify the action. Make sure you note that this action will be called off the main thread.
     *
     * @param consumer Value
     * @return Settings instance.
     */
    public PacketEventsSettings injectionFailureReaction(Consumer<Player> consumer) {
        if (!locked) {
            this.injectionFailureReaction = consumer;
        }
        return this;
    }

    /**
     * Are the settings locked?
     *
     * @return Is locked.
     * @see #lock()
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Backup server version.
     *
     * @return Getter for {@link #backupServerVersion}
     */
    public ServerVersion getBackupServerVersion() {
        return backupServerVersion;
    }

    /**
     * Should we inject a player async?
     *
     * @return Getter for {@link #injectAsync}
     */
    public boolean shouldInjectAsync() {
        return injectAsync;
    }

    /**
     * Should we eject a player async?
     *
     * @return Getter for {@link #ejectAsync}
     */
    public boolean shouldEjectAsync() {
        return ejectAsync;
    }

    /**
     * Should we check for updates?
     *
     * @return Getter for {@link #checkForUpdates}
     */
    public boolean shouldCheckForUpdates() {
        return checkForUpdates;
    }

    /**
     * Should we inject a player earlier with the early injection method?
     *
     * @return Getter for {@link #injectEarly}
     */
    public boolean shouldInjectEarly() {
        return injectEarly;
    }

    /**
     * Injection and ejection executor service thread count(if async).
     *
     * @return Getter for {@link #injectEjectThreadCount}
     */
    public int getInjectAndEjectThreadCount() {
        return injectEjectThreadCount;
    }
    /**
     * Injection failure reaction.
     *
     * @return Getter for {@link #injectionFailureReaction}
     */
    public Consumer<Player> getInjectionFailureAction() {
        return injectionFailureReaction;
    }
}
