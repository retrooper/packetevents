/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.debug;

import com.github.retrooper.packetevents.protocol.debug.struct.DebugBeeInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugBrainDump;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugBreezeInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugEntityBlockIntersection;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugGameEventInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugGameEventListenerInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugGoalInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugHiveInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugNeighborUpdateInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugPathInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugPoiInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugRaidsInfo;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugStructureInfos;
import com.github.retrooper.packetevents.protocol.debug.struct.DebugVillageSections;
import com.github.retrooper.packetevents.protocol.debug.struct.RedstoneOrientation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugSubscriptions {

    private static final VersionedRegistry<DebugSubscription<?>> REGISTRY = new VersionedRegistry<>("debug_subscription");

    private DebugSubscriptions() {
    }

    public static VersionedRegistry<DebugSubscription<?>> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static <T> DebugSubscription<T> define(String name) {
        return define(name, null, null);
    }

    @ApiStatus.Internal
    public static <T> DebugSubscription<T> define(
            String name,
            PacketWrapper.@Nullable Reader<T> reader,
            PacketWrapper.@Nullable Writer<T> writer
    ) {
        return REGISTRY.define(name, data -> new StaticDebugSubscription<>(data, reader, writer));
    }

    public static final DebugSubscription<?> DEDICATED_SERVER_TICK_TIME = define("dedicated_server_tick_time");
    public static final DebugSubscription<DebugBeeInfo> BEES = define("bees", DebugBeeInfo::read, DebugBeeInfo::write);
    public static final DebugSubscription<DebugBrainDump> BRAINS = define("brains", DebugBrainDump::read, DebugBrainDump::write);
    public static final DebugSubscription<DebugBreezeInfo> BREEZES = define("breezes", DebugBreezeInfo::read, DebugBreezeInfo::write);
    public static final DebugSubscription<DebugGoalInfo> GOAL_SELECTORS = define("goal_selectors", DebugGoalInfo::read, DebugGoalInfo::write);
    public static final DebugSubscription<DebugPathInfo> ENTITY_PATHS = define("entity_paths", DebugPathInfo::read, DebugPathInfo::write);
    public static final DebugSubscription<DebugEntityBlockIntersection> ENTITY_BLOCK_INTERSECTIONS = define("entity_block_intersections", DebugEntityBlockIntersection::read, DebugEntityBlockIntersection::write);
    public static final DebugSubscription<DebugHiveInfo> BEE_HIVES = define("bee_hives", DebugHiveInfo::read, DebugHiveInfo::write);
    public static final DebugSubscription<DebugPoiInfo> POIS = define("pois", DebugPoiInfo::read, DebugPoiInfo::write);
    public static final DebugSubscription<RedstoneOrientation> REDSTONE_WIRE_ORIENTATIONS = define("redstone_wire_orientations", RedstoneOrientation::read, RedstoneOrientation::write);
    public static final DebugSubscription<DebugVillageSections> VILLAGE_SECTIONS = define("village_sections", DebugVillageSections::read, DebugVillageSections::write);
    public static final DebugSubscription<DebugRaidsInfo> RAIDS = define("raids", DebugRaidsInfo::read, DebugRaidsInfo::write);
    public static final DebugSubscription<DebugStructureInfos> STRUCTURES = define("structures", DebugStructureInfos::read, DebugStructureInfos::write);
    public static final DebugSubscription<DebugGameEventListenerInfo> GAME_EVENT_LISTENERS = define("game_event_listeners", DebugGameEventListenerInfo::read, DebugGameEventListenerInfo::write);
    public static final DebugSubscription<DebugNeighborUpdateInfo> NEIGHBOR_UPDATES = define("neighbor_updates", DebugNeighborUpdateInfo::read, DebugNeighborUpdateInfo::write);
    public static final DebugSubscription<DebugGameEventInfo> GAME_EVENTS = define("game_events", DebugGameEventInfo::read, DebugGameEventInfo::write);

    static {
        REGISTRY.unloadMappings();
    }
}
