/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.utils.dependencies.protocolsupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

import java.net.SocketAddress;

public class ProtocolSupportUtil {
    private static byte available = -1;

    public static boolean isAvailable() {
        if (available == -1) {
            boolean present = Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null;
            available = (byte) (present ? 1 : 0);
        }
        return available == 1;
    }

    public static int getProtocolVersion(SocketAddress address) {
        return ProtocolSupportAPI.getProtocolVersion(address).getId();
    }

    public static int getProtocolVersion(Player player) {
        return ProtocolSupportAPI.getProtocolVersion(player).getId();
    }
}
