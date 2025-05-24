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
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.stats.Statistics;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.text.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public final class AdventureSerializer {

    private static final Map<ClientVersion, AdventureSerializer> SERIALIZERS = new EnumMap<>(ClientVersion.class);

    private final ClientVersion version;
    private @Nullable GsonComponentSerializer gson;
    private @Nullable LegacyComponentSerializer legacy;
    private @Nullable AdventureNBTSerializer nbt;

    private AdventureSerializer(ClientVersion version) {
        this.version = version;
    }

    public static AdventureSerializer serializer(ClientVersion version) {
        AdventureSerializer holder = SERIALIZERS.get(version);
        if (holder != null) {
            return holder;
        }
        // synchronize serializer construction
        synchronized (SERIALIZERS) {
            return SERIALIZERS.computeIfAbsent(version, AdventureSerializer::new);
        }
    }

    /**
     * Please use {@link #serializer(ClientVersion)} instead of this if possible
     */
    public static AdventureSerializer serializer() {
        return serializer(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    /**
     * @deprecated use {@link #gson()} instead
     */
    @Deprecated
    public static GsonComponentSerializer getGsonSerializer() {
        return serializer().gson();
    }

    /**
     * @deprecated use {@link #legacy()} instead
     */
    @Deprecated
    public static LegacyComponentSerializer getLegacyGsonSerializer() {
        return serializer().legacy();
    }

    /**
     * @deprecated use {@link #legacy()} instead
     */
    @Deprecated
    public static LegacyComponentSerializer getLegacySerializer() {
        return serializer().legacy();
    }

    /**
     * @deprecated use {@link #nbt()} instead
     */
    @Deprecated
    public static AdventureNBTSerializer getNBTSerializer() {
        return serializer().nbt();
    }

    /**
     * @deprecated use {@link #asLegacy(Component)} instead
     */
    @Deprecated
    public static String asVanilla(Component component) {
        return serializer().asLegacy(component);
    }

    /**
     * @deprecated use {@link #fromLegacy(String)} instead
     */
    @Deprecated
    public static Component fromLegacyFormat(String legacyMessage) {
        return serializer().fromLegacy(legacyMessage);
    }

    /**
     * @deprecated use {@link #asLegacy(Component)} instead
     */
    @Deprecated
    public static String toLegacyFormat(Component component) {
        return serializer().asLegacy(component);
    }

    /**
     * @deprecated use {@link #fromJson(String)} instead
     */
    @Deprecated
    public static Component parseComponent(String json) {
        return serializer().fromJson(json);
    }

    /**
     * @deprecated use {@link #fromJsonTree(JsonElement)} instead
     */
    @Deprecated
    public static Component parseJsonTree(JsonElement json) {
        return serializer().fromJsonTree(json);
    }

    /**
     * @deprecated use {@link #asJson(Component)} instead
     */
    @Deprecated
    public static String toJson(Component component) {
        return serializer().asJson(component);
    }

    /**
     * @deprecated use {@link #asJsonTree(Component)} instead
     */
    @Deprecated
    public static JsonElement toJsonTree(Component component) {
        return serializer().asJsonTree(component);
    }

    /**
     * @deprecated use {@link #fromNbtTag(NBT)} instead
     */
    @Deprecated
    public static Component fromNbt(NBT tag) {
        return serializer().fromNbtTag(tag);
    }

    /**
     * @deprecated use {@link #asNbtTag(Component)} instead
     */
    @Deprecated
    public static NBT toNbt(Component component) {
        return serializer().asNbtTag(component);
    }

    public Component fromLegacy(String legacy) {
        return this.legacy().deserializeOrNull(legacy);
    }

    public String asLegacy(Component component) {
        return this.legacy().serializeOrNull(component);
    }

    public Component fromJson(String json) {
        return this.gson().deserializeOrNull(json);
    }

    public String asJson(Component component) {
        return this.gson().serializeOrNull(component);
    }

    public Component fromJsonTree(JsonElement json) {
        return json != null ? this.gson().deserializeFromTree(json) : null;
    }

    public JsonElement asJsonTree(Component component) {
        return component != null ? this.gson().serializeToTree(component) : null;
    }

    public Component fromNbtTag(NBT tag) {
        return this.nbt().deserializeOrNull(tag);
    }

    public NBT asNbtTag(Component component) {
        return this.nbt().serializeOrNull(component);
    }

    public GsonComponentSerializer gson() {
        if (this.gson == null) {
            this.gson = GsonComponentSerializer.builder()
                    .editOptions(builder -> {
                        builder
                                .values(JSONOptions.byDataVersion().at(0))
                                .value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.BOTH)
                                .value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, JSONOptions.ShowItemHoverDataMode.EMIT_EITHER);
                        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_16)
                                && !PacketEvents.getAPI().getSettings().shouldDownsampleColors()) {
                            builder.value(JSONOptions.EMIT_RGB, true);
                        }
                        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_3)) {
                            builder.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, true);
                            builder.value(JSONOptions.VALIDATE_STRICT_EVENTS, true);
                        }
                        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
                            builder.value(JSONOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY, true);
                        }
                        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
                            builder.value(JSONOptions.SHADOW_COLOR_MODE, JSONOptions.ShadowColorEmitMode.EMIT_INTEGER);
                        }
                    })
                    .legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get())
                    .showAchievementToComponent(input -> Statistics.getById(input).display())
                    .build();
        }
        return this.gson;
    }

    public LegacyComponentSerializer legacy() {
        if (this.legacy == null) {
            LegacyComponentSerializer.Builder builder = LegacyComponentSerializer.builder();
            if (this.version.isNewerThanOrEquals(ClientVersion.V_1_16)) {
                builder.hexColors();
            }
            this.legacy = builder.build();
        }
        return this.legacy;
    }

    public AdventureNBTSerializer nbt() {
        if (this.nbt == null) {
            boolean downsample = this.version.isOlderThan(ClientVersion.V_1_16)
                    || PacketEvents.getAPI().getSettings().shouldDownsampleColors();
            this.nbt = new AdventureNBTSerializer(this.version, downsample);
        }
        return this.nbt;
    }
}
