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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MojangAPIUtil {
    public static List<TextureProperty> requestPlayerTextureProperties(final @NotNull UUID uuid) {
        return createUserProfile(uuid).getTextureProperties();
    }

    /**
     * Here you can get a full User Profile with Texture in only one HTTP Request
     */
    public static @Nullable UserProfile createUserProfile(final @NotNull UUID uuid) {
        List<TextureProperty> textureProperties = new ArrayList<>();
        JsonObject responseObject = parseMojangURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDUtil.toStringWithoutDashes(uuid) + "?unsigned=false");
        if (responseObject == null) return null;

        JsonArray jsonProperties = responseObject.get("properties").getAsJsonArray();
        for (JsonElement element : jsonProperties) {
            JsonObject property = element.getAsJsonObject();

            String name = property.get("name").getAsString();
            String value = property.get("value").getAsString();
            String signature = null;
            if (property.has("signature")) {
                signature = property.get("signature").getAsString();
            }

            textureProperties.add(new TextureProperty(name, value, signature));
        }
        return new UserProfile(uuid, responseObject.get("name").getAsString(), textureProperties);
    }

    public static String requestPlayerName(final @NotNull UUID uuid) {
        //Remove the "-"s from the UUID
        JsonObject responseObject = parseMojangURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDUtil.toStringWithoutDashes(uuid));
        if (responseObject == null) return null;

        return responseObject.get("name").getAsString();
    }

    public static UUID requestPlayerUUID(final @NotNull String name) {
        JsonObject responseObject = parseMojangURL("https://api.mojang.com/users/profiles/minecraft/" + name);
        if (responseObject == null) return null;

        String uuidStr = responseObject.get("id").getAsString();
        //Now we must add the "-"s to the UUID
        return UUIDUtil.fromStringWithoutDashes(uuidStr);
    }

    public static JsonObject parseMojangURL(final @NotNull String purl) {
        try {
            URL url = new URL(purl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // GET Request are by Default
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IllegalStateException("Failed to do a HTTP request to: " + purl + " Response code: " + connection.getResponseCode());
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.US_ASCII))) {
                String inputLine;
                // We know the output from here MUST be a string (assuming
                // - they don't change their API) so we can use StringBuilder not buffer.
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                return AdventureSerializer.getGsonSerializer().serializer().fromJson(sb.toString(), JsonObject.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
