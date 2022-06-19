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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.util.adventure.GsonComponentSerializerExtended;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class AdventureSerializer {

    private static GsonComponentSerializer GSON;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder().build();

    public static String asVanilla(Component component) {
        return LEGACY.serialize(component);
    }

    public static Component fromLegacyFormat(String legacyMessage) {
        return LEGACY.deserialize(legacyMessage);
    }

    public static GsonComponentSerializer getGsonSerializer() {
        if (GSON == null) {
            ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
            GSON = new GsonComponentSerializerExtended(version.isOlderThan(ServerVersion.V_1_16),
                    version.isOlderThanOrEquals(ServerVersion.V_1_12_2));
        }
        return GSON;
    }

    public static LegacyComponentSerializer getLegacyGsonSerializer() {
        return LEGACY;
    }

    public static Component parseComponent(String json) {
        return getGsonSerializer().deserialize(json);
    }

    public static Component parseJsonTree(JsonElement json) {
        return getGsonSerializer().deserializeFromTree(json);
    }

    public static String toJson(Component component) {
        return getGsonSerializer().serialize(component);
    }

    public static JsonElement toJsonTree(Component component) {
        return getGsonSerializer().serializeToTree(component);
    }

}