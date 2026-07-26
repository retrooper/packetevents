/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.minestom;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import net.minestom.server.MinecraftServer;

/**
 * Shared, idempotent boot for the loopback tests. {@link MinecraftServer#init()} and
 * {@link PacketEvents#setAPI} may each run only once per JVM, so every test class routes
 * through {@link #ensureBooted()} instead of booting independently.
 */
final class PeMinestomTestBootstrap {

    private static boolean booted;

    private PeMinestomTestBootstrap() {
    }

    static synchronized void ensureBooted() {
        if (booted) {
            return;
        }
        // Load Minestom's registries + packet parsers; init() does not bind a socket.
        MinecraftServer.init();

        PacketEventsSettings settings = new PacketEventsSettings()
                .checkForUpdates(false) // no network call in tests
                .reEncodeByDefault(false);
        MinestomPacketEventsAPI api = new MinestomPacketEventsAPI("26.2", settings);
        PacketEvents.setAPI(api);
        api.load();
        api.init();
        booted = true;
    }
}
