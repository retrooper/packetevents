/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.nettystom;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;

/**
 * Entry point for building a {@link PacketEventsAPI} instance on a nettystom server.
 *
 * <pre>{@code
 * PacketEvents.setAPI(NettystomPacketEventsBuilder.build("my-server"));
 * PacketEvents.getAPI().load();
 * // register listeners ...
 * PacketEvents.getAPI().init();
 * }</pre>
 */
public final class NettystomPacketEventsBuilder {

    private static PacketEventsAPI<Object> INSTANCE;

    private NettystomPacketEventsBuilder() {
    }

    public static void clearBuildCache() {
        INSTANCE = null;
    }

    public static PacketEventsAPI<Object> build(String id) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(id);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<Object> build(String id, PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(id, settings);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<Object> build(String id, ServerVersion version, PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(id, version, settings);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<Object> buildNoCache(String id) {
        return buildNoCache(id, new PacketEventsSettings());
    }

    public static PacketEventsAPI<Object> buildNoCache(String id, PacketEventsSettings settings) {
        return buildNoCache(id, ServerVersion.V_1_21_11, settings);
    }

    public static PacketEventsAPI<Object> buildNoCache(String id, ServerVersion version, PacketEventsSettings settings) {
        ServerManager serverManager = new NettystomServerManager(version);
        return new NettystomPacketEventsAPI(id, id, settings, serverManager);
    }
}
