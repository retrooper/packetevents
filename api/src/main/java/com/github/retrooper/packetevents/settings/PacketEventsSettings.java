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
import org.jetbrains.annotations.ApiStatus;

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
    private boolean defaultReencode = true; // true for backwards compat and more idiot-proof
    private boolean checkForUpdates = true;
    private boolean downsampleColors = false;
    private boolean debugEnabled = false;
    private boolean fullStackTraceEnabled = false;
    private boolean kickOnPacketExceptionEnabled = true;
    private boolean kickIfTerminated = true;
    private Function<String, InputStream> resourceProvider = path -> PacketEventsSettings.class
            .getClassLoader()
            .getResourceAsStream(path);

    /**
     * Time stamp mode. How precise should the timestamps in the events be.
     *
     * @param timeStampMode Time Stamp mode
     * @return Settings instance
     */
    @ApiStatus.Internal
    public PacketEventsSettings timeStampMode(TimeStampMode timeStampMode) {
        this.timestampMode = timeStampMode;
        return this;
    }

    /**
     * Do we re-encode all packets by default?
     *
     * @param reEncodeByDefault Value
     * @return Settings instance
     */
    @ApiStatus.Internal
    public PacketEventsSettings reEncodeByDefault(boolean reEncodeByDefault) {
        this.defaultReencode = reEncodeByDefault;
        return this;
    }

    /**
     * This decides if PacketEvents should check for updates and notify when your server starts.
     *
     * @param checkForUpdates Value
     * @return Settings instance.
     */
    @ApiStatus.Internal
    public PacketEventsSettings checkForUpdates(boolean checkForUpdates) {
        this.checkForUpdates = checkForUpdates;
        return this;
    }

    /**
     * This decides if PacketEvents should downsample RGB colors on pre-1.16 servers.
     *
     * @param downsampleColors Value
     * @return Settings instance.
     */
    @ApiStatus.Internal
    public PacketEventsSettings downsampleColors(boolean downsampleColors) {
        this.downsampleColors = downsampleColors;
        return this;
    }

    /**
     * This used to decide if PacketEvents should collect data anonymously and report to bStats.
     *
     * @param bStatsEnabled Value
     * @return Settings instance.
     * @deprecated This method has been deprecated. To disable bStats use the config file found in the bStats folder.
     */
    @Deprecated()
    public PacketEventsSettings bStats(boolean bStatsEnabled) {
        return this;
    }

    /**
     * This decides if PacketEvents should spam debug messages
     *
     * @param debugEnabled Value
     * @return Settings instance.
     */
    @ApiStatus.Internal
    public PacketEventsSettings debug(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    /**
     * This decides if PacketEvents should print packets error stacktrace
     *
     * @param fullStackTraceEnabled Value
     * @return Settings instance.
     */
    @ApiStatus.Internal
    public PacketEventsSettings fullStackTrace(boolean fullStackTraceEnabled) {
        this.fullStackTraceEnabled = fullStackTraceEnabled;
        return this;
    }

    /**
     * This decides if PacketEvents should kick the player in case a packet exception occurs.
     *
     * @param kickOnPacketExceptionEnabled Value
     * @return Settings instance.
     */
    @ApiStatus.Internal
    public PacketEventsSettings kickOnPacketException(boolean kickOnPacketExceptionEnabled) {
        this.kickOnPacketExceptionEnabled = kickOnPacketExceptionEnabled;
        return this;
    }

    /**
     * This decides if PacketEvents should kick the player on join if PacketEvents is terminated.
     *
     * @param kickIfTerminated Value
     * @return Settings instance.
     */
    @ApiStatus.Internal
    public PacketEventsSettings kickIfTerminated(boolean kickIfTerminated) {
        this.kickIfTerminated = kickIfTerminated;
        return this;
    }

    /**
     * Some projects may want to implement a CDN with resources like asset mappings
     * By default, all resources are retrieved from the ClassLoader
     *
     * @param resourceProvider Function
     * @return Settings instance.
     */
    @ApiStatus.Internal
    public PacketEventsSettings customResourceProvider(Function<String, InputStream> resourceProvider) {
        this.resourceProvider = resourceProvider;
        return this;
    }

    /**
     * Should the packet listeners be read only?
     *
     * @return Getter for {@link #defaultReencode}
     */
    public boolean reEncodeByDefault() {
        return defaultReencode;
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
     * Should we downsample RGB colors on pre-1.16 servers?
     *
     * @return Getter for {@link #downsampleColors}
     */
    public boolean shouldDownsampleColors() {
        return downsampleColors;
    }


    /**
     * Should we collect server data anonymously and report to bStats?
     *
     * @return Getter for bStatsEnabled
     * @deprecated This method has been deprecated, because the setter has been removed.
     * Disabling bStats is now done through the bStats config file.
     */
    @Deprecated
    public boolean isbStatsEnabled() {
        return true;
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
     * Should packetevents send packet exception Stacktraces to the console?
     *
     * @return Getter for {@link #fullStackTrace}
     */
    public boolean isFullStackTraceEnabled() {
        return fullStackTraceEnabled;
    }

    /**
     * Should packetevents kick the player due to an packet exception?
     *
     * @return Getter for {@link #kickOnPacketException}
     */
    public boolean isKickOnPacketExceptionEnabled() {
        return kickOnPacketExceptionEnabled;
    }

    /**
     * Should packetevents kick the player on join if PacketEvents is terminated?
     *
     * @return Getter for {@link #kickIfTerminated}
     */
    public boolean isKickIfTerminated() {
        return kickIfTerminated;
    }

    /**
     * As described above, this method retrieves the function that acquires the InputStream
     * of a desired resource by its path.
     *
     * @return Getter for {@link #resourceProvider}
     */
    public Function<String, InputStream> getResourceProvider() {
        return resourceProvider;
    }

    /**
     * Get the timestamp mode
     *
     * @return Time Stamp Mode
     */
    public TimeStampMode getTimeStampMode() {
        return timestampMode;
    }
}
