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

package com.github.retrooper.packetevents.util.updatechecker;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.component.ComponentSerializer;
import com.github.retrooper.packetevents.util.PEVersion;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * PacketEvents update checker.
 *
 * @author retrooper
 * @since 1.6.9
 */
public class UpdateChecker {
    public String checkLatestReleasedVersion() {
        try {
            URLConnection connection = new URL("https://api.github.com/repos/retrooper/packetevents/releases/latest").openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonResponse = reader.readLine();
            reader.close();
            JsonObject jsonObject = ComponentSerializer.GSON.fromJson(jsonResponse, JsonObject.class);
            return jsonObject.get("tag_name").getAsString();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse packetevents version!", e);
        }
    }

    /**
     * Check for an update and log in the console (ALL DONE ON THE CURRENT THREAD).
     */
    public UpdateCheckerStatus checkForUpdate() {
        PEVersion localVersion = PacketEvents.getAPI().getVersion();
        PEVersion newVersion;
        try {
            newVersion = new PEVersion(checkLatestReleasedVersion());
        } catch (Exception ex) {
            ex.printStackTrace();
            newVersion = null;
        }
        if (newVersion != null && localVersion.isOlderThan(newVersion)) {
            inform("There is an update available for packetevents! Your build: (" + localVersion + ") | Latest released build: (" + newVersion + ")");
            return UpdateCheckerStatus.OUTDATED;
        } else if (newVersion != null && localVersion.isNewerThan(newVersion)) {
            inform("You are on a dev or pre released build of packetevents. Your build: (" + localVersion + ") | Latest released build: (" + newVersion + ")");
            return UpdateCheckerStatus.PRE_RELEASE;
        } else if (localVersion.equals(newVersion)) {
            inform("You are on the latest released version of packetevents. (" + newVersion + ")");
            return UpdateCheckerStatus.UP_TO_DATE;
        } else {
            report("Something went wrong while checking for an update. Your build: (" + localVersion + ")");
            return UpdateCheckerStatus.FAILED;
        }
    }

    public void handleUpdateCheck() {
        Thread thread = new Thread(() -> {
            PacketEvents.getAPI().getLogger().info("[packetevents] Checking for an update, please wait...");
            UpdateChecker.UpdateCheckerStatus status = checkForUpdate();
            int waitTimeInSeconds = 5;
            int maxRetryCount = 5;
            int retries = 0;
            while (retries < maxRetryCount) {
                if (status != UpdateChecker.UpdateCheckerStatus.FAILED) {
                    break;
                }
                PacketEvents.getAPI().getLogger().severe("[packetevents] Checking for an update again in " + waitTimeInSeconds + " seconds...");
                try {
                    Thread.sleep(waitTimeInSeconds * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitTimeInSeconds *= 2;

                status = checkForUpdate();

                if (retries == (maxRetryCount - 1)) {
                    PacketEvents.getAPI().getLogger().severe("[packetevents] packetevents failed to check for an update. No longer retrying.");
                    break;
                }

                retries++;
            }

        }, "packetevents-update-check-thread");
        thread.start();
    }

    /**
     * Log a positive message in the console.
     *
     * @param message Message
     */
    private void inform(String message) {
        PacketEvents.getAPI().getLogger().info("[packetevents] " + message);
    }

    /**
     * Log a negative message in the console.
     *
     * @param message Message
     */
    private void report(String message) {
        //TODO Maybe find some universal way to make it red
        PacketEvents.getAPI().getLogger().warning("[packetevents] " + message);
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
         * Your build is up-to-date.
         */
        UP_TO_DATE,
        /**
         * Failed to check for an update. There might be an issue with your connection, if your connection seems to be fine make sure to contact me.
         * It could have been a mistake from my end when naming a release.
         */
        FAILED
    }
}