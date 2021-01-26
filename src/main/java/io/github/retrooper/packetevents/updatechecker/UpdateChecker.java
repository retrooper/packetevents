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

package io.github.retrooper.packetevents.updatechecker;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.version.PEVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * PacketEvents update checker.
 *
 * @author retrooper
 * @since 1.6.9
 */
public class UpdateChecker {

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
        FAILED;
    }

    protected static String getLatestReleaseJson() throws IOException {
        URLConnection connection = new URL("https://api.github.com/repos/retrooper/packetevents/releases/latest").openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = reader.readLine();
        reader.close();
        return line;
    }

    public String getLatestReleasedVersion() {
        if (PacketEvents.get().getServerUtils().getVersion().isLowerThan(ServerVersion.v_1_8)) {
            return LowLevelUpdateChecker7.getLatestRelease();
        } else {
            return LowLevelUpdateChecker8.getLatestRelease();
        }
    }

    /**
     * Check for an update and log in the console.
     */
    public UpdateCheckerStatus checkForUpdate() {
        PEVersion localVersion = PacketEvents.get().getVersion();
        inform("Checking for an update, please wait...");
        String versionStr;
        PEVersion newVersion;
        try {
            versionStr = getLatestReleasedVersion();
            newVersion = new PEVersion(versionStr);
        } catch (Exception ex) {
            newVersion = null;

        }
        if (newVersion != null && localVersion.isOlderThan(newVersion)) {
            inform("There is an update available for the PacketEvents API! Your build: (" + localVersion.toString() + ") | Latest released build: (" + newVersion.toString() + ")");
            return UpdateCheckerStatus.OUTDATED;
        } else if (newVersion != null && localVersion.isNewerThan(newVersion)) {
            inform("You are on a dev or pre released build of PacketEvents. Your build: (" + localVersion.toString() + ") | Latest released build: (" + newVersion.toString() + ")");
            return UpdateCheckerStatus.PRE_RELEASE;
        } else if (localVersion.equals(newVersion)) { //No NPE will occur, don't worry :D
            inform("You are on the latest released version of PacketEvents. (" + newVersion.toString() + ")");
            return UpdateCheckerStatus.UP_TO_DATE;
        } else {
            report("Something went wrong while checking for an update. Your build: (" + localVersion.toString() + ") | Latest released build: (" + newVersion.toString() + ")");
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
}