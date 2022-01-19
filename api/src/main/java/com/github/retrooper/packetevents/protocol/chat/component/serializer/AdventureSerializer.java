package com.github.retrooper.packetevents.protocol.chat.component.serializer;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class AdventureSerializer {

    public static final GsonComponentSerializer GSON = GsonComponentSerializer.gson();

    public static Component parseComponent(String json) {
        return GSON.deserialize(json);
    }

    public static Component parseJsonTree(JsonElement json) {
        return GSON.deserializeFromTree(json);
    }

    public static String toJson(Component component) {
        return GSON.serialize(component);
    }

    public static JsonElement toJsonTree(Component component) {
        return GSON.serializeToTree(component);
    }

}
