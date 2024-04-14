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
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MojangAPIUtil {
    public static List<TextureProperty> requestPlayerTextureProperties(UUID uuid) {
        //Remove the "-" from the UUID
        String uuidStr = UUIDUtil.toStringWithoutDashes(uuid);
        try {
            List<TextureProperty> textureProperties = new ArrayList<>();
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidStr + "?unsigned=false");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //Bad request, this UUID is not valid
            if (connection.getResponseCode() != 200) {
                throw new IllegalStateException("Failed to request texture properties with their UUID " + uuidStr + "! Response code: " + connection.getResponseCode());
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            // We know the output from here MUST be a string (assuming
            // - they don't change their API) so we can use StringBuilder not buffer.
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
            JsonObject responseObject = AdventureSerializer.getGsonSerializer().serializer().fromJson(sb.toString(), JsonObject.class);
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
            return textureProperties;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String requestPlayerName(UUID uuid) {
        //Remove the "-"s from the UUID
        String uuidStr = UUIDUtil.toStringWithoutDashes(uuid);
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //Bad request, this UUID is not valid
            if (connection.getResponseCode() != 200) {
                throw new IllegalStateException("Failed to request player name with their UUID " + uuidStr + "! Response code: " + connection.getResponseCode());
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            // We know the output from here MUST be a string (assuming
            // - they don't change their API) so we can use StringBuilder not buffer.
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
            JsonObject responseObject = AdventureSerializer.getGsonSerializer().serializer().fromJson(sb.toString(), JsonObject.class);
            return responseObject.get("name").getAsString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static UUID requestPlayerUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //Bad request, this name is not valid
            if (connection.getResponseCode() != 200) {
                throw new IllegalStateException("Failed to request player UUID with their name: " + name + "! Response code: " + connection.getResponseCode());
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            // We know the output from here MUST be a string (assuming
            // - they don't change their API) so we can use StringBuilder not buffer.
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
            JsonObject responseObject = AdventureSerializer.getGsonSerializer().serializer().fromJson(sb.toString(), JsonObject.class);
            String uuidStr = responseObject.get("id").getAsString();
            //Now we must add the "-"s to the UUID
            return UUIDUtil.fromStringWithoutDashes(uuidStr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
