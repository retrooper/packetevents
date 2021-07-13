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

package io.github.retrooper.packetevents.utils.versionlookup.viaversion;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;

public class ViaVersionLookupUtils {
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("ViaVersion") != null;
    }

    public static int getProtocolVersion(Player player) {
        //TODO I think 4.0.0 repackaged the Via class, account for that
        return Via.getAPI().getPlayerVersion(player.getUniqueId());
    }
}
