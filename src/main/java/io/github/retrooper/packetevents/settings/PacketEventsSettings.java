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

import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.threadmode.PacketListenerThreadMode;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

/**
 * Packet Events' settings.
 *
 * @author retrooper
 * @since 1.5.8
 */
public class PacketEventsSettings {
    /**
     * This boolean stores whether the settings class is locked.
     * Is this boolean is set to true, any of the setters won't work.
     * This setting is locked when initializing PacketEvents.
     */
    private boolean locked = false;

    /**
     * This is the server version PacketEvents should use when detecting
     * the server version fails using the Bukkit API.
     * For some reason, this usually the case on 1.7.10 spigot forks.
     * They probably mess up somewhere.
     */
    private ServerVersion backupServerVersion = ServerVersion.v_1_7_10;

    /**
     * This boolean stores if PacketEvents should inject a player asynchronously.
     */
    private boolean injectAsync = true;

    /**
     * This boolean stores if PacketEvents should eject a player asynchronously.
     */
    private boolean ejectAsync = true;

    /**
     * This boolean stores if PacketEvents should check for updates,
     * and give you a notice in the console.
     */
    private boolean checkForUpdates = true;

    /**
     * This boolean stores if PacketEvents should inject a player earlier using the {@code EarlyChannelInjector}.
     * Allowing us to listen to the LOGIN and STATUS packets and detect client version independently.
     * @see io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector
     */
    private boolean injectEarly = true;

    /**
     * This int stores how many threads should use to inject and eject a player.
     */
    private int injectEjectThreadCount = 1;

    /**
     * How many threads should PacketEvents use to process your event listeners
     * if your listener's PacketListenerThread
     * {@link PacketListenerDynamic#getThreadMode()} is set to {@link PacketListenerThreadMode#PACKETEVENTS}.
     */
    private int packetProcessingThreadCount = -1;

    /**
     * What should the kick message be when PacketEvents fails to inject a player and kicks them.
     */
    private String injectionFailureMessage = "We were unable to inject you. Please try again!";

    /**
     * This method locks the settings.
     * Sets the {@link #locked} field to true.
     * @return This instance.
     */
    public PacketEventsSettings lock() {
        this.locked = true;
        return this;
    }

    /**
     * Setter for the {@link #backupServerVersion} field.
     * Only succeeds if the settings class isn't locked.
     * @param serverVersion Server Version
     * @return This instance.
     */
    public PacketEventsSettings backupServerVersion(ServerVersion serverVersion) {
        if (!locked) {
            this.backupServerVersion = serverVersion;
        }
        return this;
    }

    /**
     * Setter for the {@link #injectAsync} field.
     * Only succeeds if the settings class isn't locked.
     * @param injectAsync Player injection async?
     * @return This instance.
     */
    public PacketEventsSettings injectAsync(boolean injectAsync) {
        if (!locked) {
            this.injectAsync = injectAsync;
        }
        return this;
    }

    /**
     * Setter for the {@link #ejectAsync} field.
     * Only succeeds if the settings class isn't locked.
     * @param ejectAsync Player and netty channel ejection async?
     * @return This instance.
     */
    public PacketEventsSettings ejectAsync(boolean ejectAsync) {
        if (!locked) {
            this.ejectAsync = ejectAsync;
        }
        return this;
    }

    /**
     * Setter for the {@link #checkForUpdates} field.
     * Only succeeds if the settings class isn't locked.
     * @param checkForUpdates Should we check for updates?
     * @return This instance.
     */
    public PacketEventsSettings checkForUpdates(boolean checkForUpdates) {
        if (!locked) {
            this.checkForUpdates = checkForUpdates;
        }
        return this;
    }

    /**
     * Setter for the {@link #injectEarly} field.
     * Only succeeds if the settings class isn't locked.
     * @param injectEarly Use the {@link io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector}?
     * @return This instance.
     */
    public PacketEventsSettings injectEarly(boolean injectEarly) {
        if (!locked) {
            this.injectEarly = injectEarly;
        }
        return this;
    }

    /**
     * Setter for the {@link #injectEjectThreadCount} field.
     * Only succeeds if the settings class isn't locked.
     * @param threadCount How many threads?
     * @return This instance.
     */
    public PacketEventsSettings injectAndEjectThreadCount(int threadCount) {
        if(!locked) {
            this.injectEjectThreadCount = threadCount;
        }
        return this;
    }

    /**
     * Setter for the {@link #packetProcessingThreadCount} field.
     * Only succeeds if the setting class isn't locked.
     * @param threadCount How many threads?
     * @return This instance.
     */
    public PacketEventsSettings packetProcessingThreadCount(int threadCount) {
        if(!locked) {
            this.packetProcessingThreadCount = threadCount;
        }
        return this;
    }

    /**
     * Setter for the {@link #injectionFailureMessage} field.
     * Only succeeds if the settings class isn't locked.
     * @param message Kick message for an injection failure.
     * @return This instance.
     */
    public PacketEventsSettings injectionFailureMessage(String message) {
        if (!locked) {
            this.injectionFailureMessage = message;
        }
        return this;
    }

    /**
     * Is the settings class locked?
     * @return Getter for {@link #locked}
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Backup server version.
     * @return Getter for {@link #backupServerVersion}
     */
    public ServerVersion getBackupServerVersion() {
        return backupServerVersion;
    }

    /**
     * Should we inject a player async?
     * @return Getter for {@link #injectAsync}
     */
    public boolean shouldInjectAsync() {
        return injectAsync;
    }

    /**
     * Should we eject a player async?
     * @return Getter for {@link #ejectAsync}
     */
    public boolean shouldEjectAsync() {
        return ejectAsync;
    }

    /**
     * Should we check for updates?
     * @return Getter for {@link #checkForUpdates}
     */
    public boolean shouldCheckForUpdates() {
        return checkForUpdates;
    }

    /**
     * Should we inject a player earlier with the {@link io.github.retrooper.packetevents.injector.earlyinjector.EarlyChannelInjector}
     * @return Getter for {@link #injectEarly}
     */
    public boolean shouldInjectEarly() {
        return injectEarly;
    }

    /**
     * Injection and ejection executor service thread count(if async).
     * @return Getter for {@link #injectEjectThreadCount}
     */
    public int getInjectAndEjectThreadCount() {
        return injectEjectThreadCount;
    }

    /**
     * Packet processing executor service thread count(if not on the NETTY threads).
     * @return Getter for {@link #packetProcessingThreadCount}
     */
    public int getPacketProcessingThreadCount() {
        return packetProcessingThreadCount;
    }

    /**
     * Injection failure message.
     * @return Getter for {@link #injectionFailureMessage}
     */
    public String getInjectionFailureMessage() {
        return injectionFailureMessage;
    }
}
