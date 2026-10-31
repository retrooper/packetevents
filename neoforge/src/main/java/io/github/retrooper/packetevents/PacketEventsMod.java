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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import io.github.retrooper.packetevents.factory.neoforge.NeoForgeClientPlayerManager;
import io.github.retrooper.packetevents.factory.neoforge.NeoForgePacketEventsAPI;
import io.github.retrooper.packetevents.factory.neoforge.NeoForgePlayerManager;
import io.github.retrooper.packetevents.factory.neoforge.NeoForgeServerManager;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import net.minecraft.SharedConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Mod(PacketEventsMod.MOD_ID)
public class PacketEventsMod {

    public static final String MOD_ID = "packetevents";

    public PacketEventsMod(IEventBus modBus) {
        Dist dist = FMLLoader.getCurrent().getDist();

        NeoForgePacketEventsAPI api = new NeoForgePacketEventsAPI(MOD_ID, dist) {
            @Override
            protected ServerManager constructServerManager() {
                SharedConstants.tryDetectVersion();
                String mcVersion = SharedConstants.getCurrentVersion().id();
                return new NeoForgeServerManager(mcVersion);
            }

            @Override
            protected PlayerManagerAbstract constructPlayerManager() {
                // use client player manager for client, server player manager for server
                if (dist.isClient()) {
                    return new NeoForgeClientPlayerManager();
                }

                return new NeoForgePlayerManager();
            }
        };
        
        PacketEvents.setAPI(api);
        PacketEvents.getAPI().load();
    }
}
