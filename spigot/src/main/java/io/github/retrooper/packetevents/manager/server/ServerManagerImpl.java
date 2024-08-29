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
        if (bukkitVersion.contains("Unknown")) {
            return fallbackVersion;
        }
        //Our PEVersion class can parse this version and detect if it is a newer version than what is currently supported
        //and account for that properly
        PEVersion version = PEVersion.fromString(bukkitVersion.substring(0, bukkitVersion.indexOf("-")));
        PEVersion latestVersion = PEVersion.fromString(ServerVersion.getLatest().getReleaseName());
        if (version.isNewerThan(latestVersion)) {
            //We do not support this version yet, so let us warn the user
            plugin.getLogger().warning("[packetevents] We currently do not support the minecraft version "
                    + version + ", so things might break. PacketEvents will behave as if the minecraft version were "
                    + latestVersion + "!");
            return ServerVersion.getLatest();
        }
        for (final ServerVersion val : ServerVersion.reversedValues()) {
            //For example "V_1_18" -> "1.18"
            if (bukkitVersion.contains(val.getReleaseName())) {
                return val;
            }
        }

        plugin.getLogger().warning("[packetevents] Your server software is preventing us from checking the server version. This is what we found: " + Bukkit.getBukkitVersion() + ". We will assume the server version is " + fallbackVersion.name() + "...");
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
