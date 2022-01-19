package com.github.retrooper.packetevents.protocol.chat.component.serializer;

import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class AdventureSerializer {

    public static final GsonComponentSerializer GSON = GsonComponentSerializer.gson();

    public static Component asAdventure(BaseComponent component) {
        if (component == null) return null;
        String json = ComponentSerializer.buildJsonObject(component).toString();
        return GSON.deserialize(json);
    }

    public static BaseComponent asBaseComponent(Component component) {
        if (component == null) return null;
        String json = GSON.serialize(component);
        return ComponentSerializer.parseJsonComponent(json);
    }

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
