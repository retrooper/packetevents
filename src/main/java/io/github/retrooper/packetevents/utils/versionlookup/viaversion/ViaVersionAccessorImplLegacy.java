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

import io.github.retrooper.packetevents.utils.reflection.Reflection;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViaVersionAccessorImplLegacy implements ViaVersionAccessor {
    private static Class<?> viaClass;
    private static Method apiAccessor;
    private static Method getPlayerVersionMethod;
    @Override
    public int getProtocolVersion(Player player) {
        if (viaClass == null) {
            try {
                viaClass = Class.forName("us.myles.ViaVersion.api.Via");
                Class<?> viaAPIClass = Class.forName("us.myles.ViaVersion.api.ViaAPI");
                apiAccessor = viaClass.getMethod("getAPI");
                getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
        try {
            Object viaAPI = apiAccessor.invoke(null);
            return (int) getPlayerVersionMethod.invoke(viaAPI, player);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
