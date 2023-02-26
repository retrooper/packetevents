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

package io.github.retrooper.packetevents.updatechecker;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.version.PEVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * PacketEvents update checker.
 *
 * @author retrooper
 * @since 1.6.9
 */
public class UpdateChecker {
    private final UpdaterCheck lowLevelUpdateChecker;

    public UpdateChecker() {
        lowLevelUpdateChecker = new UpdaterCheck();
    }

    public String checkLatestReleasedVersion() {
        return lowLevelUpdateChecker.getLatestRelease();
    }

    /**
     * Check for an update and log in the console (ALL DONE ON THE CURRENT THREAD).
     */
    public UpdateCheckerStatus checkForUpdate() {
        PEVersion localVersion = PacketEvents.get().getVersion();

        String version = checkLatestReleasedVersion();

        if (version == null) {
            return UpdateCheckerStatus.FAILED;
        }

        PEVersion newVersion = new PEVersion(checkLatestReleasedVersion());

        if (localVersion.isOlderThan(newVersion)) {
            inform("There is an update available for the PacketEvents API! Your build: (" + localVersion + ") | Latest released build: (" + newVersion + ")");
            return UpdateCheckerStatus.OUTDATED;
        } else if (localVersion.isNewerThan(newVersion)) {
            inform("You are on a dev or pre released build of PacketEvents. Your build: (" + localVersion + ") | Latest released build: (" + newVersion + ")");
            return UpdateCheckerStatus.PRE_RELEASE;
        } else if (localVersion.equals(newVersion)) {
            inform("You are on the latest released version of PacketEvents. (" + newVersion + ")");
            return UpdateCheckerStatus.UP_TO_DATE;
        } else {
            report("Something went wrong while checking for an update. Your build: (" + localVersion + ") | Latest released build: (" + newVersion + ")");
            return UpdateCheckerStatus.FAILED;
        }
    }

    /**
     * Log a positive message in the console.
     *
     * @param message Message
     */
    private void inform(String message) {
        Bukkit.getLogger().info("[packetevents] " + message);
    }

    /**
     * Log a negative message in the console.
     *
     * @param message Message
     */
    private void report(String message) {
        Bukkit.getLogger().warning(ChatColor.DARK_RED + "[packetevents] " + message);
    }

    /**
     * Result of an update check.
     *
     * @author retrooper
     * @since 1.8
     */
    public enum UpdateCheckerStatus {
        /**
         * Your build is outdated, an update is available.
         */
        OUTDATED,
        /**
         * You are on a dev or pre-released build. Not on the latest release.
         */
        PRE_RELEASE,
        /**
         * Your build is up to date.
         */
        UP_TO_DATE,
        /**
         * Failed to check for an update. There might be an issue with your connection, if your connection seems to be fine make sure to contact me.
         * It could have been a mistake from my end when naming a release.
         */
        FAILED
    }
}