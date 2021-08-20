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

package io.github.retrooper.packetevents.utils.gameprofile;

import io.github.retrooper.packetevents.utils.nms.MinecraftReflection;
import io.github.retrooper.packetevents.manager.player.Skin;

import java.util.UUID;

public class GameProfileUtil {

    public static Object getGameProfile(UUID uuid, String username) {
        if (MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return GameProfileUtil_8.getGameProfile(uuid, username);
        } else {
            return GameProfileUtil_7.getGameProfile(uuid, username);
        }
    }


    public static WrappedGameProfile getWrappedGameProfile(Object gameProfile) {
        if (MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return GameProfileUtil_8.getWrappedGameProfile(gameProfile);
        } else {
            return GameProfileUtil_7.getWrappedGameProfile(gameProfile);
        }
    }

    public static void setGameProfileSkin(Object gameProfile, Skin skin) {
        if (MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            GameProfileUtil_8.setGameProfileSkin(gameProfile, skin);
        } else {
            GameProfileUtil_7.setGameProfileSkin(gameProfile, skin);
        }
    }

    public static Skin getGameProfileSkin(Object gameProfile) {
        if (MinecraftReflection.USE_MODERN_NETTY_PACKAGE) {
            return GameProfileUtil_8.getGameProfileSkin(gameProfile);
        } else {
            return GameProfileUtil_7.getGameProfileSkin(gameProfile);
        }
    }
}
