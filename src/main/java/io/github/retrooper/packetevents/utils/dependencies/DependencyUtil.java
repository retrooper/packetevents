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

package io.github.retrooper.packetevents.utils.dependencies;

import io.github.retrooper.packetevents.protocol.data.player.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;
import io.github.retrooper.packetevents.utils.dependencies.gameprofile.GameProfileUtil_7;
import io.github.retrooper.packetevents.utils.dependencies.gameprofile.GameProfileUtil_8;
import io.github.retrooper.packetevents.utils.dependencies.gameprofile.WrappedProperty;
import io.github.retrooper.packetevents.utils.dependencies.google.GuavaUtils_7;
import io.github.retrooper.packetevents.utils.dependencies.google.GuavaUtils_8;
import io.github.retrooper.packetevents.utils.dependencies.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionUtil;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class DependencyUtil {
    public static boolean isProtocolTranslationDependencyAvailable() {
        return ViaVersionUtil.isAvailable()
                || ProtocolSupportUtil.isAvailable();
    }

    public static int getProtocolVersion(Player player) {
        if (ViaVersionUtil.isAvailable()) {
            return ViaVersionUtil.getProtocolVersion(player);
        } else if (ProtocolSupportUtil.isAvailable()) {
            return ProtocolSupportUtil.getProtocolVersion(player);
        }
        return -1;
    }

    public static <T, K> ConcurrentMap<T, K> makeMap() {
        if (MinecraftReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return GuavaUtils_8.makeMap();
        } else {
            return GuavaUtils_7.makeMap();
        }
    }

    public static Object getGameProfile(UUID uuid, String username) {
        if (MinecraftReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return GameProfileUtil_8.getGameProfile(uuid, username);
        } else {
            return GameProfileUtil_7.getGameProfile(uuid, username);
        }
    }


    public static WrappedGameProfile getWrappedGameProfile(Object gameProfile) {
        if (MinecraftReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return GameProfileUtil_8.getWrappedGameProfile(gameProfile);
        } else {
            return GameProfileUtil_7.getWrappedGameProfile(gameProfile);
        }
    }

    public static void setGameProfileSkin(Object gameProfile, WrappedProperty skin) {
        if (MinecraftReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            GameProfileUtil_8.setGameProfileSkin(gameProfile, skin);
        } else {
            GameProfileUtil_7.setGameProfileSkin(gameProfile, skin);
        }
    }

    public static WrappedProperty getGameProfileSkin(Object gameProfile) {
        if (MinecraftReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
            return GameProfileUtil_8.getGameProfileSkin(gameProfile);
        } else {
            return GameProfileUtil_7.getGameProfileSkin(gameProfile);
        }
    }
}
