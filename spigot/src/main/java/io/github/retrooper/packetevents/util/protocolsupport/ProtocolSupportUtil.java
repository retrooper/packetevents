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

package io.github.retrooper.packetevents.util.protocolsupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

import java.net.SocketAddress;

public class ProtocolSupportUtil {
    private static ProtocolSupportState available = ProtocolSupportState.UNKNOWN;

    public static boolean isAvailable() {
        if (available == ProtocolSupportState.UNKNOWN) {
            try {
                Class.forName("protocolsupport.api.ProtocolSupportAPI");
                available = ProtocolSupportState.ENABLED;
                return true;
            } catch (Exception e) {
                available = ProtocolSupportState.DISABLED;
                return false;
            }
        } else {
            return available == ProtocolSupportState.ENABLED;
        }
    }

    public static void checkIfProtocolSupportIsPresent() {
        boolean present = Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport");
        available = present ? ProtocolSupportState.ENABLED : ProtocolSupportState.DISABLED;
    }

    public static int getProtocolVersion(SocketAddress address) {
        return ProtocolSupportAPI.getProtocolVersion(address).getId();
    }

    public static int getProtocolVersion(Player player) {
        return ProtocolSupportAPI.getProtocolVersion(player).getId();
    }
}

enum ProtocolSupportState {
    UNKNOWN,
    DISABLED,
    ENABLED
}
