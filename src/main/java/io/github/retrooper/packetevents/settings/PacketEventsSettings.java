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

    /**
     * This boolean stores if PacketEvents should check for updates,
     * and give you a notice in the console.
     */
    private boolean checkForUpdates = true;

    /**
     * This boolean stores if PacketEvents should inject a player earlier using the {@code LateInjector}.
     * We also call it the "compatibility injector", because it should actually be compatible with everything.
     * Using this injector prevents us from listening to packets during the early packet-states. (such as LOGIN and STATUS state)
     * PacketEvents requires these early states to be able to detect client versions of players without the help of any dependencies!
     */
    private boolean compatInjector = false;

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
     * @param compatInjector Value
     * @return Settings instance.
     */
    public PacketEventsSettings compatInjector(boolean compatInjector) {
        if (!locked) {
            this.compatInjector = compatInjector;
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
     * Should we check for updates?
     *
     * @return Getter for {@link #checkForUpdates}
     */
    public boolean shouldCheckForUpdates() {
        return checkForUpdates;
    }

    /**
     * Should we use the {@code LateInjector}(aka. "Compatibility Injector") with the sacrifice of a few features.
     *
     * @return Getter for {@link #compatInjector}
     */
    public boolean shouldUseCompatibilityInjector() {
        return compatInjector;
    }
}
