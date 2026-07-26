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

/**
 * Single entry point for running PacketEvents on a Minestom server: sets the API, loads
 * and initializes it, and registers the connection tracker + packet feeder. This is what
 * the monorepo glue (and any {@code :game-*}/{@code :lobby} bootstrap) calls; after it
 * returns, {@code PacketEvents.getAPI()} is live and inbound/outbound packets flow into
 * PacketEvents (and thus Grim).
 */
public final class MinestomPacketEvents {

    private MinestomPacketEvents() {
    }

    /**
     * Boots PacketEvents for Minestom.
     *
     * @param mcVersion the native Minecraft version string (e.g. {@code "26.2"})
     * @param settings  PacketEvents settings (e.g. {@code checkForUpdates(false)} for tests)
     * @return the initialized API (also retrievable via {@link PacketEvents#getAPI()})
     */
    public static MinestomPacketEventsAPI init(String mcVersion, PacketEventsSettings settings) {
        MinestomPacketEventsAPI api = new MinestomPacketEventsAPI(mcVersion, settings);
        PacketEvents.setAPI(api);
        api.load();
        api.init();

        MinestomUserTracker.register();
        MinestomPacketFeeder.register();
        return api;
    }
}
