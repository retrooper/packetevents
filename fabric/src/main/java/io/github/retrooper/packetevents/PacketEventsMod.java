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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PacketEventsMod implements PreLaunchEntrypoint, ModInitializer {

    public static final String MOD_ID = "packetevents";

    @Override
    public void onPreLaunch() {
        FabricLoader loader = FabricLoader.getInstance();
        String entrypoint = switch (loader.getEnvironmentType()) {
            case CLIENT -> "pePreLaunchClient";
            case SERVER -> "pePreLaunchServer";
        };
        loader.invokeEntrypoints(entrypoint,
                PreLaunchEntrypoint.class,
                PreLaunchEntrypoint::onPreLaunch);
    }

    @Override
    public void onInitialize() {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        if (api != null) {
            api.init();
        }
    }
}
