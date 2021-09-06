/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

import io.github.retrooper.packetevents.manager.server.ServerVersion;

/**
 * Packet Events' settings.
 *
 * @author retrooper
 * @since 1.5.8
 */
public class PacketEventsSettings {
    private ServerVersion fallbackServerVersion = ServerVersion.v_1_7_10;

    /**
     * This boolean stores if PacketEvents should check for updates,
     * and give you a notice in the console.
     */
    private boolean checkForUpdates = true;


    /**
     * Can PacketEvents collect server data like player count, java version, plugins, etc... anonymously and report to bStats?
     */
    private boolean bStatsEnabled = true;

    /**
     * This is the server version PacketEvents should assume the server is when detecting
     * the server version fails using the Bukkit API.
     * This seems to be most common on 1.7.10 paper forks.
     *
     * @param version ServerVersion
     * @return Settings instance.
     */
    public PacketEventsSettings fallbackServerVersion(ServerVersion version) {
        this.fallbackServerVersion = version;
        return this;
    }

    /**
     * This decides if PacketEvents should check for updates and notify when your server starts.
     *
     * @param checkForUpdates Value
     * @return Settings instance.
     */
    public PacketEventsSettings checkForUpdates(boolean checkForUpdates) {
        this.checkForUpdates = checkForUpdates;
        return this;
    }

    /**
     * This decides if PacketEvents should collect data anonymously and report to bStats.
     *
     * @param bStatsEnabled Value
     * @return Settings instance.
     */
    public PacketEventsSettings bStats(boolean bStatsEnabled) {
        this.bStatsEnabled = bStatsEnabled;
        return this;
    }

    /**
     * Fallback server version.
     *
     * @return Getter for {@link #fallbackServerVersion}
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
     * Should we collect server data anonymously and report to bStats?
     *
     * @return Getter for {@link #bStatsEnabled}
     */
    public boolean isbStatsEnabled() {
        return bStatsEnabled;
    }
}
