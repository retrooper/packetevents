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

package io.github.retrooper.packetevents.utils.v1_7;

import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SpigotVersionLookup_1_7 {
    private static Method getPlayerVersionMethod = null;

    public static int getProtocolVersion(Player player) {
        if (getPlayerVersionMethod == null) {
            try {
                getPlayerVersionMethod = SpigotReflectionUtil.NETWORK_MANAGER_CLASS.getMethod("getVersion");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        Object networkManager = SpigotReflectionUtil.getNetworkManager(player);
        try {
            return (int) getPlayerVersionMethod.invoke(networkManager);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
