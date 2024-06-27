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

package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.stats.Statistics;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.text.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class AdventureSerializer {

    private static GsonComponentSerializer GSON;
    private static LegacyComponentSerializer LEGACY;
    private static AdventureNBTSerializer NBT;

    public static GsonComponentSerializer getGsonSerializer() {
        if (GSON == null) {
            ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
            GSON = GsonComponentSerializer.builder().editOptions(builder -> {
                boolean is_1_20_3_or_new = version.isNewerThanOrEquals(ServerVersion.V_1_20_3);
                builder.value(JSONOptions.EMIT_RGB, version.isNewerThanOrEquals(ServerVersion.V_1_16) && !PacketEvents.getAPI().getSettings().shouldDownsampleColors());
                builder.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.BOTH);
                builder.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, is_1_20_3_or_new);
                builder.value(JSONOptions.VALIDATE_STRICT_EVENTS, is_1_20_3_or_new);
                builder.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, JSONOptions.ShowItemHoverDataMode.EMIT_EITHER);
            })
                    .legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get())
                    .showAchievementToComponent(input -> Statistics.getById(input).display())
                    .build();
        }
        return GSON;
    }

    @Deprecated
    public static LegacyComponentSerializer getLegacyGsonSerializer() {
        return getLegacySerializer();
    }

    public static LegacyComponentSerializer getLegacySerializer() {
        if (LEGACY == null) {
            LegacyComponentSerializer.Builder builder = LegacyComponentSerializer.builder();
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16))
                builder = builder.hexColors();
            LEGACY = builder.build();
        }
        return LEGACY;
    }

    public static AdventureNBTSerializer getNBTSerializer() {
        if (NBT == null) {
            NBT = new AdventureNBTSerializer(
                    PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_16) ||
                            PacketEvents.getAPI().getSettings().shouldDownsampleColors()
            );
        }
        return NBT;
    }

    public static String asVanilla(Component component) {
        return getLegacySerializer().serialize(component);
    }

    public static Component fromLegacyFormat(String legacyMessage) {
        return getLegacySerializer().deserializeOrNull(legacyMessage);
    }

    public static String toLegacyFormat(Component component) {
        return getLegacySerializer().serializeOrNull(component);
    }

    public static Component parseComponent(String json) {
        return getGsonSerializer().deserializeOrNull(json);
    }

    public static Component parseJsonTree(JsonElement json) {
        return getGsonSerializer().deserializeFromTree(json);
    }

    public static String toJson(Component component) {
        return getGsonSerializer().serializeOrNull(component);
    }

    public static JsonElement toJsonTree(Component component) {
        return getGsonSerializer().serializeToTree(component);
    }

    public static Component fromNbt(NBT nbt) {
        return getNBTSerializer().deserialize(nbt);
    }

    public static NBT toNbt(Component component) {
        return getNBTSerializer().serialize(component);
    }

}
