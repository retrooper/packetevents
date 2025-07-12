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

package io.github.retrooper.packetevents.manager.server;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PEVersion;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ServerManagerImpl implements ServerManager {
    private ServerVersion serverVersion;

    private ServerVersion resolveVersionNoCache() {
        Plugin plugin = (Plugin) PacketEvents.getAPI().getPlugin();
        String bukkitVersion = Bukkit.getBukkitVersion();
        ServerVersion fallbackVersion = ServerVersion.V_1_8_8;

        String failureToDetectVersionMsg = "Your server software is preventing us from checking the Minecraft Server version. This is what we found: " + Bukkit.getBukkitVersion() + ". We will assume the Server version is " + fallbackVersion.name() + "...\n If you need assistance, join our Discord server: https://discord.gg/DVHxPPxHZc";

        if (bukkitVersion.contains("Unknown")) {
            plugin.getLogger().warning(failureToDetectVersionMsg);
            return fallbackVersion;
        }

        for (final ServerVersion val : ServerVersion.reversedValues()) {
            //For example "V_1_18" -> "1.18"
            if (bukkitVersion.contains(val.getReleaseName())) {
                return val;
            }
        }

        plugin.getLogger().warning(failureToDetectVersionMsg);
        return fallbackVersion;
    }

    @Override
    public ServerVersion getVersion() {
        if (serverVersion == null) {
            serverVersion = resolveVersionNoCache();
        }
        return serverVersion;
    }

    @Override
    public Object getRegistryCacheKey(User user, ClientVersion version) {
        return GlobalRegistryHolder.getGlobalRegistryCacheKey(user, version);
    }
}
