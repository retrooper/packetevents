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

import com.retrooper.packetevents.util.updatechecker.LowLevelUpdateChecker;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;

import java.io.IOException;

public class LowLevelUpdateCheckerLegacy implements LowLevelUpdateChecker {
    @Override
    public String getLatestRelease() {
        String jsonResponse;
        try {
            jsonResponse = getLatestReleaseJson();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to parse packetevents version!");
        }

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();
        String versionName = null;
        if (jsonObject != null) {
            versionName = jsonObject.get("tag_name").getAsString();
        }
        return versionName;
    }
}
