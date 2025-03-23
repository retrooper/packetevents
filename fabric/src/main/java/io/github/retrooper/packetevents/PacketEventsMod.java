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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPIManagerFactory;
import io.github.retrooper.packetevents.loader.ChainLoadData;
import io.github.retrooper.packetevents.loader.ChainLoadEntryPoint;
import io.github.retrooper.packetevents.manager.registry.FabricItemRegistry;
import io.github.retrooper.packetevents.manager.registry.FabricRegistryManager;
import io.github.retrooper.packetevents.util.LazyHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PacketEventsMod implements PreLaunchEntrypoint, ModInitializer {

    public static final String MOD_ID = "packetevents";

    @Override
    public void onPreLaunch() {
        FabricLoader loader = FabricLoader.getInstance();

        String peEntryPointName = null;
        String chainLoadEntryPointName = null;
        String clientChainLoadEntryPointName = null; // For client-specific entrypoints
        switch (loader.getEnvironmentType()) {
            case CLIENT -> {
                peEntryPointName = "pePreLaunchClient";
                chainLoadEntryPointName = "mainChainLoad";
                clientChainLoadEntryPointName = "clientChainLoad";
            }
            case SERVER -> {
                peEntryPointName = "pePreLaunchServer";
                chainLoadEntryPointName = "mainChainLoad";
            }
        }

        // Collect mainChainLoad entrypoints (always present)
        List<ChainLoadEntryPoint> mainChainLoadEntryPoints = loader.getEntrypoints(chainLoadEntryPointName, ChainLoadEntryPoint.class);

        // Collect clientChainLoad entrypoints (only on client, might be empty)
        List<ChainLoadEntryPoint> clientChainLoadEntryPoints = loader.getEnvironmentType() == EnvType.CLIENT
                ? loader.getEntrypoints(clientChainLoadEntryPointName, ChainLoadEntryPoint.class)
                : Collections.emptyList();

        // If on client, interleave the entrypoints; otherwise, use only mainChainLoad
        List<ChainLoadEntryPoint> allEntryPoints;
        if (loader.getEnvironmentType() == EnvType.SERVER) {
            allEntryPoints = interleaveEntryPoints(mainChainLoadEntryPoints, clientChainLoadEntryPoints);
        } else {
            allEntryPoints = new ArrayList<>(mainChainLoadEntryPoints);
            // Sort mainChainLoad entrypoints by version (newest first)
            allEntryPoints.sort((a, b) -> b.getNativeVersion().getProtocolVersion() - a.getNativeVersion().getProtocolVersion());
        }

        // Initialize single chainload data instance
        ChainLoadData chainLoadData = new ChainLoadData();

        // Execute all entrypoints in the sorted, interleaved order using the same ChainLoadData instance
        for (ChainLoadEntryPoint chainLoadEntryPoint : allEntryPoints) {
            try {
                chainLoadEntryPoint.initialize(chainLoadData);
            } catch (Exception e) {
                // Log error but continue with next entrypoint
                System.err.println("Error processing entrypoint for version " +
                        chainLoadEntryPoint.getNativeVersion() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Set default registry manager if not already set by any entrypoint
        chainLoadData.setRegistryManagerIfNull(LazyHolder.simple(() -> new FabricRegistryManager(
                new FabricItemRegistry()
        )));

        // Ordinarily I wouldn't be using a static here but since we need to maintain compile-time backwards compatibility
        // We need to preserve the ABI of FactoryPacketEventsAPI and do this static awfulness
        FabricPacketEventsAPIManagerFactory.init(chainLoadData);

        // Invoke pre-launch entrypoints
        loader.invokeEntrypoints(peEntryPointName,
                PreLaunchEntrypoint.class,
                PreLaunchEntrypoint::onPreLaunch);
    }

    /**
     * Interleaves mainChainLoad and clientChainLoad entrypoints, sorting by version (newest first).
     * If a clientChainLoad entrypoint is missing for a version, only the mainChainLoad entrypoint is included.
     * @param mainEntryPoints List of mainChainLoad entrypoints
     * @param clientEntryPoints List of clientChainLoad entrypoints
     * @return Interleaved and sorted list of entrypoints
     */
    private List<ChainLoadEntryPoint> interleaveEntryPoints(
            List<ChainLoadEntryPoint> mainEntryPoints,
            List<ChainLoadEntryPoint> clientEntryPoints
    ) {
        // Use a TreeMap to group entrypoints by version, sorted newest first
        Map<ServerVersion, List<ChainLoadEntryPoint>> versionToEntryPoints = new TreeMap<>(
                (v1, v2) -> v2.getProtocolVersion() - v1.getProtocolVersion()
        );

        // Populate the map with mainChainLoad entrypoints
        for (ChainLoadEntryPoint mainEntry : mainEntryPoints) {
            versionToEntryPoints.computeIfAbsent(mainEntry.getNativeVersion(), k -> new ArrayList<>(2))
                    .add(mainEntry);
        }

        // Add clientChainLoad entrypoints to the same version buckets
        for (ChainLoadEntryPoint clientEntry : clientEntryPoints) {
            versionToEntryPoints.computeIfAbsent(clientEntry.getNativeVersion(), k -> new ArrayList<>(2))
                    .add(0, clientEntry); // Insert client entrypoint before main entrypoint
        }

        // Flatten the map into a single list, preserving version order and interleaving
        List<ChainLoadEntryPoint> interleaved = new ArrayList<>(mainEntryPoints.size() + clientEntryPoints.size());
        for (List<ChainLoadEntryPoint> entryPoints : versionToEntryPoints.values()) {
            interleaved.addAll(entryPoints);
        }

        return interleaved;
    }

    @Override
    public void onInitialize() {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        if (api != null) {
            api.init();
        }
    }
}
