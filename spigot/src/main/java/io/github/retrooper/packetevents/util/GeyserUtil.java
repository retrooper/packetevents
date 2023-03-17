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

package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.util.reflection.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class GeyserUtil {
    private static boolean CHECKED_FOR_GEYSER = false;
    private static boolean GEYSER_PRESENT = false;
    private static Class<?> GEYSER_CLASS;
    private static Class<?> GEYSER_API_CLASS;
    private static Method GEYSER_API_METHOD;
    private static Method CONNECTION_BY_UUID_METHOD;

    public static boolean isGeyserPlayer(UUID uuid) {
        if (!CHECKED_FOR_GEYSER) {
            try {
                GEYSER_CLASS = Class.forName("org.geysermc.api.Geyser");
                GEYSER_PRESENT = true;
            } catch (ClassNotFoundException e) {
                GEYSER_PRESENT = false;
            }
            CHECKED_FOR_GEYSER = true;
        }

        if (GEYSER_PRESENT) {
            if (GEYSER_API_CLASS == null) {
                try {
                    GEYSER_API_CLASS = Class.forName("org.geysermc.api.GeyserApiBase");
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (GEYSER_API_METHOD == null) {
                GEYSER_API_METHOD = Reflection.getMethodExact(GEYSER_CLASS, "api", null);
            }
            if (CONNECTION_BY_UUID_METHOD == null) {
                CONNECTION_BY_UUID_METHOD = Reflection.getMethod(GEYSER_API_CLASS, "connectionByUuid", 0);
            }
            Object apiInstance = null;
            try {
                apiInstance = GEYSER_API_METHOD.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            Object connection = null;
            try {
                if (apiInstance != null) {
                    connection = CONNECTION_BY_UUID_METHOD.invoke(apiInstance, uuid);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return connection != null;
        }
        return false;
    }
}
