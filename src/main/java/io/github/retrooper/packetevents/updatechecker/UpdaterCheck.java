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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdaterCheck {
    public String getLatestRelease() {
        try {
            String jsonResponse = getLatestReleaseJson();

            String targetPart = "\"name\":";
            int nameIndex = jsonResponse.indexOf(targetPart);

            int stringStart = nameIndex + targetPart.length();
            int stringEnd = jsonResponse.indexOf(",", stringStart);
            return jsonResponse.substring(stringStart + 1, stringEnd - 1);
        } catch (Exception e) {
            return null;
        }
    }

    private String getLatestReleaseJson() throws IOException {
        URLConnection connection = new URL("https://api.github.com/repos/retrooper/packetevents/releases/latest").openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = reader.readLine();
        reader.close();
        return line;
    }
}
