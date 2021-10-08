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

package io.github.retrooper.packetevents.utils.dependencies.gameprofile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.github.retrooper.packetevents.protocol.data.player.WrappedGameProfile;
import com.github.retrooper.packetevents.util.MinecraftReflectionUtil;
import com.github.retrooper.packetevents.util.dependencies.google.WrappedPropertyMap;
import com.github.retrooper.packetevents.util.dependencies.google.WrappedPropertyMapModern;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * 1.8 (and above) Mojang Game Profile util using the 1.8 (and above) Mojang API import location.
 *
 * @author retrooper
 * @since 1.6.8.2
 */
public class GameProfileUtil_8 {

    public static Object getGameProfile(UUID uuid, String username) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Object entityHuman = MinecraftReflectionUtil.ENTITY_HUMAN_CLASS.cast(MinecraftReflectionUtil.getEntityPlayer(player));
            ReflectionObject wrappedEntityPlayer = new ReflectionObject(entityHuman, MinecraftReflectionUtil.ENTITY_HUMAN_CLASS);
            return wrappedEntityPlayer.readObject(0, GameProfile.class);
        } else {
            return new GameProfile(uuid, username);
        }
    }

    public static WrappedGameProfile getWrappedGameProfile(Object gameProfile) {
        GameProfile gp = (GameProfile) gameProfile;
        WrappedPropertyMap wrappedPropertyMap = new WrappedPropertyMapModern(gp.getProperties());
        return new WrappedGameProfile(gp.getId(), gp.getName(), wrappedPropertyMap);
    }

    public static void setGameProfileSkin(Object gameProfile, WrappedProperty skin) {
        GameProfile gp = (GameProfile) gameProfile;
        gp.getProperties().put("textures", new Property(skin.getName(), skin.getValue(), skin.getSignature()));
    }

    public static WrappedProperty getGameProfileSkin(Object gameProfile) {
        Property property = ((GameProfile) gameProfile).getProperties().get("textures").iterator().next();
        return new WrappedProperty(property.getName(), property.getValue(), property.getSignature());
    }
}
