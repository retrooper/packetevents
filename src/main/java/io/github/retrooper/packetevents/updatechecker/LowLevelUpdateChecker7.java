package io.github.retrooper.packetevents.updatechecker;

import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;

import java.io.IOException;

public class LowLevelUpdateChecker7 {
    public static String getLatestRelease() {
        String jsonResponse;
        try {
            jsonResponse = UpdateChecker.getLatestReleaseJson();
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
