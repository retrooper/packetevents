/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.settings;

import io.github.retrooper.packetevents.utils.server.ServerVersion;

/**
 * Packet Events' settings.
 *
 * @author retrooper
 * @since 1.5.8
 */
public class PacketEventsSettings {
    private boolean locked;
    private ServerVersion fallbackServerVersion = ServerVersion.v_1_7_10;

    /**
     * This boolean stores if PacketEvents should check for updates,
     * and give you a notice in the console.
     */
    private boolean checkForUpdates = true;

    /**
     * This boolean stores if PacketEvents should inject a player earlier using the {@code LateInjector}.
     * We also call it the "compatibility injector", because it should actually be compatible with everything.
     * Using this injector prevents us from listening to packets during the early packet-states. (STATUS, HANDSHAKING, LOGIN)
     */
    private boolean compatInjector = false;

    /**
     * Can PacketEvents collect server data like player count, java version, plugins, etc... anonymously and report to bStats?
     */
    private boolean bStatsEnabled = true;

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
     * @deprecated Use {@link #getFallbackServerVersion()}
     */
    @Deprecated
    public PacketEventsSettings backupServerVersion(ServerVersion serverVersion) {
        if (!locked) {
            this.fallbackServerVersion = serverVersion;
        }
        return this;
    }

    /**
     * This is the server version PacketEvents should assume the server is when detecting
     * the server version fails using the Bukkit API.
     * This seems to be most common on 1.7.10 paper forks.
     *
     * @param version ServerVersion
     * @return Settings instance.
     */
    public PacketEventsSettings fallbackServerVersion(ServerVersion version) {
        if (!locked) {
            this.fallbackServerVersion = version;
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
     * This decides if PacketEvents should collect data anonymously and report to bStats.
     *
     * @param bStatsEnabled Value
     * @return Settings instance.
     */
    public PacketEventsSettings bStats(boolean bStatsEnabled) {
        if (!locked) {
            this.bStatsEnabled = bStatsEnabled;
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
     * @deprecated Use {@link #getFallbackServerVersion()}
     */
    @Deprecated
    public ServerVersion getBackupServerVersion() {
        return fallbackServerVersion;
    }

    /**
     * Fallback server version.
     *
     * @return Getter for {@link #backupServerVersion}
     */
    public ServerVersion getFallbackServerVersion() {
        return fallbackServerVersion;
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

    /**
     * Should we collect server data anonymously and report to bStats?
     *
     * @return Getter for {@link #bStatsEnabled}
     */
    public boolean isbStatsEnabled() {
        return bStatsEnabled;
    }
}
