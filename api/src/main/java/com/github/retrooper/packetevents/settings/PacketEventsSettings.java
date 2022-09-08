/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.settings;

import com.github.retrooper.packetevents.util.TimeStampMode;

import java.io.InputStream;
import java.util.function.Function;

/**
 * Packet Events' settings.
 *
 * @author retrooper
 * @since 1.5.8
 */
public class PacketEventsSettings {

    private TimeStampMode timestampMode = TimeStampMode.MILLIS;
    private boolean readOnlyListeners = false;
    private boolean checkForUpdates = true;
    private boolean bStatsEnabled = true;
    private boolean debugEnabled = false;
    private Function<String, InputStream> resourceProvider = path -> PacketEventsSettings.class
            .getClassLoader()
            .getResourceAsStream(path);

    /**
     * Time stamp mode. How precise should the timestamps in the events be.
     * @param timeStampMode Time Stamp mode
     * @return Settings instance
     */
    public PacketEventsSettings timeStampMode(TimeStampMode timeStampMode) {
        this.timestampMode = timeStampMode;
        return this;
    }

    /**
     * Get the timestamp mode
     * @return Time Stamp Mode
     */
    public TimeStampMode getTimeStampMode() {
        return timestampMode;
    }

    /**
     * Are the packet listeners all read only?
     * @param readOnlyListeners Value
     * @return Settings instance
     */
    public PacketEventsSettings readOnlyListeners(boolean readOnlyListeners) {
        this.readOnlyListeners = readOnlyListeners;
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
     * This decides if PacketEvents should spam debug messages
     *
     * @param debugEnabled Value
     * @return Settings instance.
     */
    public PacketEventsSettings debug(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    /**
     * Some projects may want to implement a CDN with resources like asset mappings
     * By default, all resources are retrieved from the ClassLoader
     *
     * @param resourceProvider Function
     * @return Settings instance.
     */
    public PacketEventsSettings customResourceProvider(Function<String, InputStream> resourceProvider) {
        this.resourceProvider = resourceProvider;
        return this;
    }

    /**
     * Should the packet listeners be read only?
     * @return Getter for {@link #readOnlyListeners}
     */
    public boolean shouldListenersReadOnly() {
        return readOnlyListeners;
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

    /**
     * Should packetevents send debug Messages to the console?
     *
     * @return Getter for {@link #debugEnabled}
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * As described above, this method retrieves the function that acquires the InputStream
     * of a desired resource by its path.
     * @return Getter for {@link #resourceProvider}
     */
    public Function<String, InputStream> getResourceProvider() {
        return resourceProvider;
    }
}
